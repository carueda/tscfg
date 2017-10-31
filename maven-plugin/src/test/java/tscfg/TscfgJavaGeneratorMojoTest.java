package tscfg;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.*;
import static org.assertj.core.api.Assertions.assertThat;
import static tscfg.TscfgJavaGeneratorMojo.UTF_8;

@RunWith(MockitoJUnitRunner.class)
public class TscfgJavaGeneratorMojoTest {

  private TscfgJavaGeneratorMojo mojo = new TscfgJavaGeneratorMojo();

  @Rule
  public TemporaryFolder templateFolder = new TemporaryFolder();
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private MavenProject project;

  @Before
  public void setUp() throws Exception {
    mojo.setProject(project);

    File templateFile = templateFolder.newFile("test.spec.conf");
    Files.write(templateFile.toPath(), templateContent(), CREATE, WRITE, TRUNCATE_EXISTING);
    mojo.setTemplateFile(templateFile);

    mojo.setOutputDirectory(outputFolder.getRoot().getAbsolutePath());
    mojo.setClassName("TestConfig");
    mojo.setPackageName("com.test.config");
  }

  @Test
  public void execute() throws Exception {
    mojo.execute();

    Mockito.verify(project).addCompileSourceRoot(outputFolder.getRoot().getAbsolutePath());

    File resultFile = new File(outputFolder.getRoot(), "com/test/config/TestConfig.java");
    assertThat(resultFile).exists();

    String result = new String(Files.readAllBytes(resultFile.toPath()), UTF_8);
    assertThat(result).contains("package com.test.config;");
    assertThat(result).contains("public class TestConfig {");
  }

  @Test
  public void executeFullyOverwritesGeneratedFile() throws Exception {
    mojo.execute();

    File resultFile = new File(outputFolder.getRoot(), "com/test/config/TestConfig.java");
    Files.write(resultFile.toPath(), "extra".getBytes(UTF_8), StandardOpenOption.APPEND);

    mojo.execute();

    String result = new String(Files.readAllBytes(resultFile.toPath()), UTF_8);
    assertThat(result).doesNotContain("extra");
  }

  @Test
  public void templateFileDoesNotExists() throws Exception {
    expectedException.expect(MojoExecutionException.class);
    expectedException.expectMessage("Failed to read template file (");
    expectedException.expectMessage("unexisting.spec.conf):");

    mojo.setTemplateFile(new File(outputFolder.getRoot(), "unexisting.spec.conf"));
    mojo.execute();
  }

  private byte[] templateContent() {
    String templateConfig = "test {\n  server: \"string\"\n  port: \"int\"\n}";
    return templateConfig.getBytes(UTF_8);
  }

}
