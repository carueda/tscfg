# tscfg-maven-plugin

Maven plugin to generate config class file based on template file using tscfg.

## Usage
```xml
<plugin>
    <groupId>com.sentiance</groupId>
    <artifactId>tscfg-maven-plugin</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <configuration>
        <templateFile>config-spec/aligner.spec.conf</templateFile>
        <packageName>com.sentiance.service.aligner.config</packageName>
        <className>AlignerConfig</className>
    </configuration>
</plugin>
```

## Configuration
* templateFile: typesafe configuration template file
* className: the name of the generated config class 
* packageName: the package of the generated config class
* outputDirectory: the output directory for the generated class, default is `target/generated-sources/tscfg/`
