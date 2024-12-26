package org.mql.java.projectanalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.projectanalyzer.filesystem.DynamicClassLoader;

public class Project {
	private String srcPath;
	private List<Package> packages;
	
	public Project(String projectPath) {
		packages = new ArrayList<>();
		packages.add(new Package()); // add default package
		File srcFolder = new File(projectPath + "/src/");
		srcPath = srcFolder.getPath();
		scanFolder(srcFolder, packages.getFirst());
		try {
			DynamicClassLoader.addToClasspath(projectPath + "/bin");
		} catch (Exception e) {
			System.out.println("Can't load classes!");
			e.printStackTrace();
		}
	}
	
	private void scanFolder(File folder, Package destPackage) {
		if (!folder.exists()) {
			return;
		}
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				String packageName = file.getPath().replace(srcPath + "\\", "").replace("\\", ".");
				Package pack = new Package(packageName);
				packages.add(pack);
				scanFolder(file, pack);
			}
			else if (file.getName().endsWith(".java")) {
				String className = file.getPath()
						.replace(srcPath + "\\", "")
						.replace("\\", ".")
						.replaceAll("\\.java$", "");
				Clazz cls = new Clazz(className); //.replace(destPackage.getName() + ".", ""));
				destPackage.addClass(cls);
			}
		}
	}

	public Package[] getPackages() {
		Package[] packs = new Package[packages.size()];
		return packages.toArray(packs);
	}
}
