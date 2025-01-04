package org.mql.java.projectanalyzer;

import org.mql.java.projectanalyzer.relations.Relation;

public class Main {
	private static final String PROJECT_PATH = "D:\\BackUp\\School\\Master\\Java\\p03-reflection-and-annotations_prof";
	public Main() {
		projectAnalyzerExample();
	}
	
	void projectAnalyzerExample() {
		Project project = new Project(PROJECT_PATH);
		for (Package pack : project.getPackages()) {
			String packageName = pack.getName();
			log(packageName);
			for (Clazz clz : pack.getClasses()) {
				log("    " + clz.getName().replace(packageName + ".", "") + " : " + clz.getType());
				Relation relations[] = clz.getRelations();
				for (Relation relation : relations) {
					log("    🔗 " + relation);
				}
			}
		}
	}
	
	private void log(Object o) {
		System.out.println(o);
	}

	public static void main(String[] args) {
		new Main();
	}
}
