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
	
	private static Class<?> componentType(Class<?> cls) {
		Class<?> type = cls;
		if (cls != null && cls.isArray()) {
			type = type.getComponentType();
		}
		return type;
	}
	
	public Collection<Relation> getRelations() {
		List<Relation> relations = new ArrayList<>();
		Optional<Relation> extensionOpt = getExtension();
		if (extensionOpt.isPresent()) {
			relations.add(extensionOpt.get());
		}
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
		Collection<Relation> realisations = getRealisations();
		relations.addAll(realisations);
		
		return relations;
	}
	
	public Optional<Relation> getExtension() {
		if (type == ClassType.CLASS) { // other types can't have custom super calss
			Class<?> superClass = wrappedClass.getSuperclass();
			if (!Object.class.equals(superClass)) {
				return Optional.of(
					new Relation(RelationType.EXTENSION, wrappedClass, superClass)
				);
			}
		}
		return Optional.empty();
	}
	
	public Collection<Association> getAssociations() {
		Collection<Association> associations = new ArrayList<>();
		Field fields[] = wrappedClass.getDeclaredFields();
		for (Field field : fields) {
			Class<?> fieldType = componentType(field.getType());
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
		Collection<Relation> dependencies = new ArrayList<>();
		List<Class<?>> foundTypes = new ArrayList<>();
		Method methods[] = wrappedClass.getDeclaredMethods();
		for (Method method : methods) {
			foundTypes.add(componentType(method.getReturnType()));
			Class<?> paramsTypes[] = method.getParameterTypes();
			for (Class<?> paramType : paramsTypes) {
				foundTypes.add(componentType(paramType));
			}
		}
		
		for (Class<?> type : foundTypes) {
			// ignore primitives, void and self dependencies
			if (!type.isPrimitive() &&
				!type.equals(void.class) &&
				!type.equals(wrappedClass)) {
				dependencies.add(
					new Relation(RelationType.DEPENDENCY, wrappedClass, type)
				);
			}
		}
		return dependencies;
	}
	
	public Collection<Relation> getRealisations() {
		Collection<Relation> realisations = new ArrayList<>();
		// annotations can't implement other interfaces
		if (type != ClassType.ANNOTATION) {
			Class<?> interfaces[] = wrappedClass.getInterfaces();
			for (Class<?> interf : interfaces) {
				realisations.add(
					new Relation(RelationType.REALISATION, wrappedClass, interf)
				);
			}
		}
		return realisations;
	}
}

















