package org.mql.java.projectanalyzer;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.mql.java.projectanalyzer.enums.ClassType;
import org.mql.java.projectanalyzer.relations.Association;
import org.mql.java.projectanalyzer.relations.Relation;
import org.mql.java.projectanalyzer.relations.RelationType;

public class Clazz {
	private Class<?> wrappedClass;
	private String name;
	private ClassType type;
//	private Relation[] relations;
	
	public Clazz(Class<?> wrappedClass) {
		this.wrappedClass = wrappedClass;
		name = wrappedClass.getSimpleName();
		type = getClassType(wrappedClass);
//		relations = findClassRelations(wrappedClass);
	}

	public String getName() {
		return name;
	}
	
	public ClassType getType() {
		return type;
	}
	
	public Relation[] getRelations() {
		return findClassRelations(wrappedClass);
	}
	
	public static ClassType getClassType(Class<?> cls) {
		ClassType type = ClassType.CLASS;
		if (cls.isAnnotation()) type = ClassType.ANNOTATION;
		else if (cls.isEnum()) type = ClassType.ENUMERATION;
		else if (cls.isInterface()) type = ClassType.INTERFACE;
		
		return type;
	}
	
	private Relation[] findClassRelations(Class<?> cls) {
		ArrayList<Relation> relations = new ArrayList<>();
		// look for extension
		if (type == ClassType.CLASS) { // other types can't have custom super calss
			Class<?> superClass = cls.getSuperclass();
			if (!Object.class.equals(superClass)) {
				relations.add(
					new Relation(RelationType.EXTENSION, this, new Clazz(superClass))
				);
			}
		}
		
		Relation[] r = new Relation[relations.size()];
		return relations.toArray(r);
	}
}
