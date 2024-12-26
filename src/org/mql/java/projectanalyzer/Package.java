package org.mql.java.projectanalyzer;

import java.util.ArrayList;
import java.util.List;

public class Package {
	private String name;
	private Package[] packages;
	private List<Clazz> classes;
	
	public Package() {
		name = "default"; // default package
		classes = new ArrayList<>();
	}

	public Package(String name) {
		this.name = name;
		classes = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public Package[] getPackages() {
		return packages;
	}
	
	public Clazz[] getClasses() {
		Clazz[] data = new Clazz[classes.size()];
		return classes.toArray(data);
	}
	
	public void addClass(Clazz cls) {
		classes.add(cls);
	}
}
