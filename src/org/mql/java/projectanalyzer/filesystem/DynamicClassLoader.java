package org.mql.java.projectanalyzer.filesystem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader {
	private ClassLoader loader;

	public DynamicClassLoader(String path) throws MalformedURLException {
		File binDirectory = new File(path);
		if (binDirectory.exists() || binDirectory.isDirectory()) {
			loader = new URLClassLoader(
				new URL[] {binDirectory.toURI().toURL()},
				this.getClass().getClassLoader()
			);
		}
	}
	
	public Class<?> forName(String name) throws ClassNotFoundException {
		return Class.forName(name, true, loader);
	}
}
