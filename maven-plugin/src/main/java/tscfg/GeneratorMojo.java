package tscfg;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import tscfg.generators.GenOpts;
import tscfg.generators.GenResult;
import tscfg.generators.Generator;
import tscfg.generators.java.JavaGen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Mojo(name = "generate-config-class", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES, goal = "generate-config-class")
public class GeneratorMojo extends AbstractMojo {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Parameter(required = true)
  private File templateFile;

  @Parameter(required = true)
  private String packageName;

  @Parameter(required = true)
  private String className;

  @Parameter(defaultValue = "target/generated-sources/tscfg/")
  private String outputDirectory;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {
    String template = readTemplateFile();

    Generator generator = new JavaGen(new GenOpts(packageName, className, false));
    GenResult result = generator.generate(ModelBuilder.apply(template).objectType());

    writeResultToJavaFile(result);
  }

  private String readTemplateFile() throws MojoExecutionException {
    try {
      return new String(Files.readAllBytes(templateFile.toPath()), UTF_8);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read template file: " + e.getMessage());
    }
  }

  private void writeResultToJavaFile(GenResult result) throws MojoExecutionException {
    try {
      String outputFilePath = outputDirectory + (outputDirectory.endsWith(File.separator) ? "" : File.separator) +
          packageName.replace(".", File.separator) + File.separator + className + ".java";
      Path outputFile = new File(project.getModel().getProjectDirectory(), outputFilePath).toPath();

      getLog().debug("Generating config class to " + outputFile);

      mkDirs(outputFile);

      Files.write(outputFile, result.code().getBytes(UTF_8),
          StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to write file: " + e.getClass() + ": " + e.getMessage());
    }
  }

  private void mkDirs(Path outputFile) throws IOException {
    Path parentDir = outputFile.getParent();
    if (!Files.exists(parentDir)) {
      Files.createDirectories(parentDir);
    }
  }
}
