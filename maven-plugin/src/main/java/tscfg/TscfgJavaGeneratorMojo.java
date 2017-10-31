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
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

@Mojo(name = "generate-config-class", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES, goal = "generate-config-class")
public class TscfgJavaGeneratorMojo extends AbstractMojo {

  public static final Charset UTF_8 = Charset.forName("UTF-8");
  public static final String PACKAGE_SEPARATOR = ".";
  public static final String JAVA_FILE_EXTENSION = ".java";

  @Parameter(required = true)
  private File templateFile;

  @Parameter(required = true)
  private String packageName;

  @Parameter(required = true)
  private String className;

  @Parameter(defaultValue = "${project.build.directory}/generated-sources/tscfg/")
  private String outputDirectory;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {
    GenResult generatorResult = generateJavaCodeForTemplate(templateFile);
    writeGeneratedCodeToJavaFile(generatorResult.code());

    getLog().debug("Adding " + outputDirectory + " as source root in the maven project.");
    project.addCompileSourceRoot(outputDirectory);
  }

  private GenResult generateJavaCodeForTemplate(File templateFile) throws MojoExecutionException {
    Generator tscfgGenerator = new JavaGen(new GenOpts(packageName, className, false));
    return tscfgGenerator.generate(ModelBuilder.apply(readTscfgTemplate(templateFile)).objectType());
  }

  private String readTscfgTemplate(File templateFile) throws MojoExecutionException {
    try {
      return new String(Files.readAllBytes(templateFile.toPath()), UTF_8);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read template file (" + templateFile + "): " + e.getClass() + ": " + e.getMessage());
    }
  }

  private void writeGeneratedCodeToJavaFile(String javaClassCode) throws MojoExecutionException {
    Path javaClassFile = assembleJavaFilePath();
    try {
      createParentDirsIfNecessary(javaClassFile);
      Files.write(javaClassFile, javaClassCode.getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
      getLog().debug("Wrote generated java config file to " + javaClassFile);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to write file (" + javaClassFile + "). " +
          e.getClass() + ": " + e.getMessage());
    }
  }

  private Path assembleJavaFilePath() {
    String packageDirectoryPath = packageName.replace(PACKAGE_SEPARATOR, File.separator);
    String javaFileName = className + JAVA_FILE_EXTENSION;
    return Paths.get(outputDirectory, packageDirectoryPath, javaFileName);
  }

  private void createParentDirsIfNecessary(Path outputFile) throws IOException {
    Path parentDir = outputFile.getParent();
    if (!Files.exists(parentDir)) {
      Files.createDirectories(parentDir);
    }
  }

  void setTemplateFile(File templateFile) {
    this.templateFile = templateFile;
  }

  void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  void setClassName(String className) {
    this.className = className;
  }

  void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  void setProject(MavenProject project) {
    this.project = project;
  }
}
