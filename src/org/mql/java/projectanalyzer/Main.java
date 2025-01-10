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
			String packageName = pack.getName();
			log("📁 " + packageName);
			for (Clazz clz : pack.getClasses()) {
				log("    📜 " + clz.getName() + " : " + clz.getType());
				for (Relation relation : clz.getRelations()) {
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
