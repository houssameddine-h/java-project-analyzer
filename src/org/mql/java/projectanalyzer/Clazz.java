package org.mql.java.projectanalyzer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mql.java.projectanalyzer.enums.ClassType;
import org.mql.java.projectanalyzer.relations.Association;
import org.mql.java.projectanalyzer.relations.Relation;
import org.mql.java.projectanalyzer.relations.RelationType;

public class Clazz {
	private Class<?> wrappedClass;
	private String name;
	private ClassType type;
	
	public Clazz(Class<?> wrappedClass) {
		this.wrappedClass = wrappedClass;
		name = wrappedClass.getSimpleName();
		type = getClassType(wrappedClass);
	}

	public String getName() {
		return name;
	}
	
	public ClassType getType() {
		return type;
	}
	
	public boolean hasRelationWith(RelationType relationType, Clazz with) {
		
		return true;
	}
	
	public static ClassType getClassType(Class<?> cls) {
		ClassType type = ClassType.CLASS;
		if (cls.isAnnotation()) type = ClassType.ANNOTATION;
		else if (cls.isEnum()) type = ClassType.ENUMERATION;
		else if (cls.isInterface()) type = ClassType.INTERFACE;
		
		return type;
	}
	
	public Relation getExtension() {
		if (type == ClassType.CLASS) { // other types can't have custom super calss
			Class<?> superClass = wrappedClass.getSuperclass();
			if (!Object.class.equals(superClass)) {
				return new Relation(RelationType.EXTENSION, this, new Clazz(superClass));
			}
		}
		return null;
	}
	
	public Collection<Association> getAssociations() {
		List<Association> associations = new ArrayList<>();
		Field fields[] = wrappedClass.getDeclaredFields();
		for (Field field : fields) {
			Class<?> fieldType = field.getType();
			if (fieldType.isArray()) {
				fieldType = fieldType.getComponentType();
			}
			if (!fieldType.isPrimitive()) {
				associations.add(
					new Association(this, new Clazz(fieldType))
				);
			}
		}
		return associations;
	}
}

















