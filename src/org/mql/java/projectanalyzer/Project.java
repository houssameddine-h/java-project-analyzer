package org.mql.java.projectanalyzer;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mql.java.projectanalyzer.filesystem.DynamicClassLoader;
import org.mql.java.projectanalyzer.relations.Relation;
import org.mql.java.projectanalyzer.relations.RelationManager;

public class Project {
	public static final String JAVA_FILE_EXTENSION = ".java";
	
	private String srcPath;
	private String binPath;
	private List<Package> packages;
	private RelationManager relationManager;
	
	private DynamicClassLoader classLoader;
	
	public Project(String projectPath) {
		packages = new ArrayList<>();
		packages.add(new Package()); // add default package
		relationManager = new RelationManager();
		
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
		// find relations between classes
		findClassRelations();
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
					Clazz clz = new Clazz(cls);
					destPackage.addClass(clz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean containsPackage(String packageName) {
		for (Package packg : packages) {
			if (packg.getName() == packageName) {
				return true;
			}
		}
		return false;
	}
	
	public Package[] getPackages() {
		return packages.toArray(Package[]::new);
	}
	
	public Relation[] getRelations() {
		return relationManager.getRelations();
	}
	
	public Relation[] getRelations(Clazz clz) {
		return Arrays.stream(getRelations())
//					.filter(relation -> relation.hasClass(clz))
					.filter(relation -> relation.getSourceClass().equals(clz.getWrappedClass()))
					.toArray(Relation[]::new);
	}
	
	private void findClassRelations() {
		for (Package packg : packages) {
			for (Clazz clz : packg.getClasses()) {
				relationManager.addCleanedRelations(clz.getRelations());
			}
		}
	}
}
