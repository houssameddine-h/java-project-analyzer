package org.mql.java.projectscanner;

import org.mql.java.projectscanner.enums.ClassType;

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
