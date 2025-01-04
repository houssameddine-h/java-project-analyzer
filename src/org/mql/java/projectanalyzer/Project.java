package org.mql.java.projectanalyzer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.projectanalyzer.filesystem.DynamicClassLoader;

public class Project {
	public static final String JAVA_FILE_EXTENSION = ".java";
	
	private String srcPath;
	private String binPath;
	private List<Package> packages;
	
	private DynamicClassLoader classLoader;
	
	public Project(String projectPath) {
		packages = new ArrayList<>();
		packages.add(new Package()); // add default package
		
		File srcDir = new File(projectPath + "/src");
		File binDir = new File(projectPath + "/bin");
		srcPath = srcDir.getPath();
		binPath = binDir.getPath();

		try {
			classLoader = new DynamicClassLoader(binPath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// scan for packages and classes
		scanDir(srcDir, packages.getFirst());
	}
	
	// scan a package (directory) and put found sub-packages & classes into destPackage
	private void scanDir(File dir, Package destPackage) {
		if (!dir.exists()) {
			return;
		}
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				String packageName = file.getPath().replace(srcPath + "\\", "").replace("\\", ".");
				Package pack = new Package(packageName);
				packages.add(pack);
				scanDir(file, pack);
			}
			else if (file.getName().endsWith(JAVA_FILE_EXTENSION)) {
				String className = file.getPath()
						.replace(srcPath + "\\", "")
						.replace("\\", ".")
						.replaceAll("\\" + JAVA_FILE_EXTENSION + "$", "");
				try {
					Class<?> cls = classLoader.forName(className);
					Clazz clz = new Clazz(cls); //.replace(destPackage.getName() + ".", ""));
					destPackage.addClass(clz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Package[] getPackages() {
		Package[] packs = new Package[packages.size()];
		return packages.toArray(packs);
	}

}
