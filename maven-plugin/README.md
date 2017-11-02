# tscfg-maven-plugin

Maven plugin to generate config class file based on template file using tscfg.

## Usage
```xml
<plugin>
    <groupId>com.github.carueda</groupId>
    <artifactId>tscfg-maven-plugin</artifactId>
    <version>0.2.0</version>
    <configuration>
        <templateFile>config-spec/aligner.spec.conf</templateFile>
        <packageName>com.sentiance.service.aligner.config</packageName>
        <className>AlignerConfig</className>
    </configuration>
     <executions>
        <execution>
            <id>tscfg-sources</id>
            <goals>
                <goal>generate-config-class</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Configuration
* templateFile: typesafe configuration template file
* className: the name of the generated config class 
* packageName: the package of the generated config class
* outputDirectory: the output directory for the generated class, default is `target/generated-sources/tscfg/`

## Current limitations
* Currently only Java class generated is supported. It should be easy to extend this plugin to generate Scala files.
* This plugin always generates java classes for Java 8 and above. This should also be easy to extend.   
