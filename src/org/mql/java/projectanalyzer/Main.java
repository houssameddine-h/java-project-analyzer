package org.mql.java.projectanalyzer;

import org.mql.java.projectanalyzer.relations.Relation;

public class Main {
	private static final String PROJECT_PATH = "D:\\BackUp\\School\\Master\\Java\\JavaProjectAnalyzer";
	public Main() {
		projectAnalyzerExample();
	}
	
	void projectAnalyzerExample() {
		Project project = new Project(PROJECT_PATH);
		for (Package pack : project.getPackages()) {
			log("📁 " + pack.getName());
			for (Clazz clz : pack.getClasses()) {
				log("    📜 " + clz.getName() + " : " + clz.getType());
				for (Relation<Class<?>, Class<?>> relation : project.getRelations(clz)) {
					log("\t🔗 " + relation);
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
