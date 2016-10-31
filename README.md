# Binary Maven Plugin

Simple Maven plugin that wraps a jar file inside a shell script to obtain a
single executable.
It should be used in conjunction with Maven Shade plugin.

* Usage:
```
<plugin>
	<groupId>info.bonjean.maven.plugins</groupId>
	<artifactId>binary-maven-plugin</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<executions>
		<execution>
			<phase>package</phase>
			<goals>
				<goal>binary</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

* Configuration options example:
```
<configuration>
	<binaryName>my_executable</binaryName>
	<javaOptions>-Xmx64m</javaOptions>
</configuration>
```

* The plugin is not available on Maven Central, but you can use my GitHub Maven
repository:
```
<pluginRepositories>
	<pluginRepository>
		<snapshots />
		<id>jbonjean</id>
		<url>https://github.com/jbonjean/maven-repository/raw/master</url>
	</pluginRepository>
</pluginRepositories>
```
