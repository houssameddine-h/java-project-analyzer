package org.mql.java.projectanalyzer;

import org.mql.java.projectanalyzer.enums.ClassType;

public class Clazz {
	private String name;
	private ClassType type;
	
	public Clazz(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public ClassType getType() {
		return type;
	}
}
