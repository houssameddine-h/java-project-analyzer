package org.mql.java.projectscanner;

public class Main {

	public Main() {
		projectScannerExample();
	}
	
	void projectScannerExample() {
		Project project = new Project("D:\\BackUp\\School\\Master\\Java\\p03-reflection-and-annotations_prof");
//		for (Package pack : project.getPackages()) {
//			log(pack.getName());
//			for (Clazz cls : pack.getClasses()) {
//				log("    " + cls.getName());
//			}
//		}
		Package[] packages = project.getPackages();
		Clazz[] cls = packages[packages.length - 1].getClasses();
		log(cls[cls.length - 1].getName());
		try {
			Class<?> c = Class.forName(cls[cls.length - 1].getName());			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void log(Object o) {
		System.out.println(o);
	}

	public static void main(String[] args) {
		new Main();
	}
}
