package org.mql.java.projectanalyzer;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mql.java.projectanalyzer.filesystem.DynamicClassLoader;
import org.mql.java.projectanalyzer.relations.PackageRelationManager;
import org.mql.java.projectanalyzer.relations.Relation;
import org.mql.java.projectanalyzer.relations.RelationManager;

public class Project {
	public static final String JAVA_FILE_EXTENSION = ".java";
	
	private String srcPath;
	private String binPath;
	private List<Package> externalPackages;
	private Package defaultPackage;
	private RelationManager relationManager;
	private PackageRelationManager packageRelationManager;
	
	private DynamicClassLoader classLoader;
	
	public Project(String projectPath) {
		 externalPackages = new ArrayList<>();
		// packages.add(new Package()); // add default package
		defaultPackage = new Package();
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
		scanDir(srcDir, defaultPackage);
		
		packageRelationManager = new PackageRelationManager(getPackages());
		
		// find relations between classes
		findClassRelations();
		findPackageRelations();
	}
	
	// scan a package (directory) and put found sub-packages & classes into destPackage
	private void scanDir(File dir, Package destPackage) {
		if (!dir.exists()) {
			return;
		}
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				String packageName = file.getPath().replace(srcPath + "\\", "").replace("\\", ".");
				Package pckg = new Package(packageName);
//				packages.add(pckg);
				destPackage.addPackage(pckg);
				scanDir(file, pckg);
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
	
//	public boolean containsPackage(String packageName) {
//		for (Package packg : packages) {
//			if (packg.getName() == packageName) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	public Package getDefaultPackage() {
		return defaultPackage;
	}
	
	public List<Package> getPackages() {
		List<Package> result = new ArrayList<>();
		// Ignore default package if it has no classes
		if (!defaultPackage.isEmpty()) {
			result.add(defaultPackage);
		}
		result.addAll(defaultPackage.getDescendantPackages());
		return result;
	}
	
	public Relation<Class<?>, Class<?>>[] getRelations() {
		return relationManager.getRelations();
	}
	
	@SuppressWarnings("unchecked")
	public Relation<Class<?>, Class<?>>[] getRelations(Clazz clz) {
		return Arrays.stream(getRelations())
//					.filter(relation -> relation.hasItem(clz))
					.filter(relation -> relation.getSource().equals(clz.getWrappedClass()))
					.toArray(Relation[]::new);
	}
	
	private void findClassRelations() {
		for (Package packg : getPackages()) {
			for (Clazz clz : packg.getClasses()) {
				relationManager.addCleanedRelations(clz.getRelations());
			}
		}
	}
	
	public void findPackageRelations() {
		Relation<Class<?>, Class<?>>[] classRelations = getRelations();
		List<Package> packages = getPackages();
		
		for (Relation<Class<?>, Class<?>> relation : classRelations) {
			String sourcePackageName = relation.getSource().getPackageName();
			String targetPackageName = relation.getTarget().getPackageName();

			Package sourcePackage = packageRelationManager.getPackage(sourcePackageName);
			Package targetPackage = packageRelationManager.getPackage(targetPackageName);
			if (sourcePackage == null) {
				sourcePackage = new Package(sourcePackageName, true);
				packageRelationManager.addPackage(sourcePackage);
			}
			
			if (targetPackage == null) {
				targetPackage = new Package(targetPackageName, true);
				packageRelationManager.addPackage(targetPackage);
			}
			
			packageRelationManager.addRelation(
				new Relation<Package, Package>(
					sourcePackage, targetPackage
				)
			);
		}
	}
	
	public Package[] getExternalPackages() {
		return packageRelationManager.getExternalPackages();
	}
	
	public Relation<Package, Package>[] getPackageRelations() {
		return packageRelationManager.getRelations();
	}
	
}