package org.mql.java.projectanalyzer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
	
	public Class<?> getWrappedClass() {
		return wrappedClass;
	}
	
//	public boolean hasRelationWith(RelationType relationType, Clazz with) {
//		return true;
//	}
	
	private static ClassType getClassType(Class<?> cls) {
		ClassType type = ClassType.CLASS;
		if (cls.isAnnotation()) type = ClassType.ANNOTATION;
		else if (cls.isEnum()) type = ClassType.ENUMERATION;
		else if (cls.isInterface()) type = ClassType.INTERFACE;
		
		return type;
	}
	
	public Collection<Relation> getRelations() {
		List<Relation> relations = new ArrayList<>();
		Optional<Relation> extensionOpt = getExtension();
		Collection<Association> associations = getAssociations();
		// add associations that are not already in extension
		for (Association association : associations) {
			if (!(extensionOpt.isPresent() &&
					extensionOpt.get().hasClass(association.getTargetClass()))) {
				relations.add(association);
			}
		}
		Collection<Relation> dependencies = getDependencies();
		// add dependencies that are not already in associations or extension
		outerForeach:
		for (Relation dependency : dependencies) {
			if (extensionOpt.isPresent() && extensionOpt.get().hasClass(dependency.getTargetClass())) {
				continue;
			}
			for (Association association : associations) {
				if (association.hasClass(dependency.getTargetClass())) {
					continue outerForeach;
				}
			}
			// will reach this part only when the relation is a new one
			relations.add(dependency);
		}
		return relations;
	}
	
	public Optional<Relation> getExtension() {
		if (type == ClassType.CLASS) { // other types can't have custom super calss
			Class<?> superClass = wrappedClass.getSuperclass();
			if (!Object.class.equals(superClass)) {
				return Optional.of(new Relation(RelationType.EXTENSION, wrappedClass, superClass));
			}
		}
		return Optional.empty();
	}
	
	public Collection<Association> getAssociations() {
		List<Association> associations = new ArrayList<>();
		Field fields[] = wrappedClass.getDeclaredFields();
		for (Field field : fields) {
			Class<?> fieldType = field.getType();
			if (fieldType.isArray()) {
				fieldType = fieldType.getComponentType();
			}
			// enum values are instances of the enum type, so we ignore them
			if (!fieldType.isPrimitive() &&
					!(type == ClassType.ENUMERATION && fieldType.equals(wrappedClass))) {
				associations.add(
					new Association(wrappedClass, fieldType)
				);
			}
		}
		return associations;
	}
	
	public Collection<Relation> getDependencies() {
		List<Relation> dependencies = new ArrayList<>();
		Method methods[] = wrappedClass.getDeclaredMethods();
		for (Method method : methods) {
			// return type dependency
			Class<?> returnType = method.getReturnType();
			if (returnType.isArray()) {
				returnType = returnType.getComponentType();
			}
			// ignore void and self dependencies
			if (!returnType.isPrimitive() &&
				!returnType.equals(void.class)
				&& !returnType.equals(wrappedClass)) {
				dependencies.add(
					new Relation(RelationType.DEPENDENCY, wrappedClass, returnType)
				);
			}
			// parameters dependencies
			Class<?> paramsTypes[] = method.getParameterTypes();
			for (Class<?> paramType : paramsTypes) {
				if (paramType.isArray()) {
					paramType = paramType.getComponentType();
				}
				// ignore self dependencies
				if (!paramType.isPrimitive() && !paramType.equals(wrappedClass)) {
					dependencies.add(
						new Relation(RelationType.DEPENDENCY, wrappedClass, paramType)
					);
				}
			}
		}
		return dependencies;
	}
}

















