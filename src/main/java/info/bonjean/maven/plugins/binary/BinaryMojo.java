/*
 * Copyright 2016 Julien Bonjean <julien@bonjean.info>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.bonjean.maven.plugins.binary;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "binary", defaultPhase = LifecyclePhase.PACKAGE)
public class BinaryMojo extends AbstractMojo {
	@Parameter(defaultValue = "${project.artifactId}-${project.version}.jar")
	private String jarName;

	@Parameter(defaultValue = "${project.artifactId}")
	private String binaryName;

	@Parameter
	private String javaOptions;

	@Parameter(defaultValue = "${project.build.directory}")
	private File outputDirectory;

	public void execute() throws MojoExecutionException {
		File jarFile = new File(outputDirectory, jarName);
		if (!jarFile.exists())
			getLog().info("jar file not found: " + jarFile);
		getLog().info("jar file: " + jarFile);

		File finalFile = new File(outputDirectory, binaryName);
		getLog().info("output file: " + finalFile);

		InputStream jarFileInputStream = null;
		OutputStream finalFileInputStream = null;
		InputStream wrapperFileInputStream = null;

		try {
			jarFileInputStream = FileUtils.openInputStream(jarFile);
			finalFileInputStream = FileUtils.openOutputStream(finalFile);

			String wrapper = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("wrapper.sh"),
					StandardCharsets.UTF_8);
			wrapper = wrapper.replace("JAVA_OPTIONS", javaOptions == null ? "" : javaOptions);
			wrapperFileInputStream = new ByteArrayInputStream(wrapper.getBytes(StandardCharsets.UTF_8));

			getLog().info("generating binary file");
			IOUtils.copy(wrapperFileInputStream, finalFileInputStream);
			IOUtils.copy(jarFileInputStream, finalFileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(jarFileInputStream);
			IOUtils.closeQuietly(finalFileInputStream);
			IOUtils.closeQuietly(wrapperFileInputStream);
		}

		finalFile.setExecutable(true);
	}
}
