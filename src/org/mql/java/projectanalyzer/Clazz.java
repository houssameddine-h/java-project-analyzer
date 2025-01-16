package org.mql.java.projectanalyzer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mql.java.projectanalyzer.enums.ClassType;
import org.mql.java.projectanalyzer.relations.Association;
import org.mql.java.projectanalyzer.relations.Relation;
import org.mql.java.projectanalyzer.relations.RelationManager;
import org.mql.java.projectanalyzer.relations.RelationType;

public class Clazz {
	private Class<?> wrappedClass;
	private String name;
	private ClassType type;
	private RelationManager relationManager;
	private boolean foundRelations; // used to defer getting relations
	
	public Clazz(Class<?> wrappedClass) {
		this.wrappedClass = wrappedClass;
		name = wrappedClass.getSimpleName();
		type = getClassType(wrappedClass);
		foundRelations = false;
		relationManager = new RelationManager();
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
	
	public Relation[] getRelations() {
		if (!foundRelations) {
			findRelations(); // updates relation manager
			foundRelations = true;
		}

		return relationManager.getRelations();
	}
	
	private void findRelations() {
		// extension
		Optional<Relation> extensionOpt = getExtension();
		extensionOpt.ifPresent(ext -> relationManager.addRelation(ext));
		
		// realisations
		relationManager.addRelation(getRealisations());
		
		// associations
		relationManager.addRelation(getAssociations());
		
		// dependencies
		relationManager.addRelation(getDependencies());
	}
	
	private Optional<Relation> getExtension() {
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
	
	private Relation[] getRealisations() {
		List<Relation> realisations = new ArrayList<>();
		// annotations can't implement interfaces
		if (type != ClassType.ANNOTATION) {
			Class<?> interfaces[] = wrappedClass.getInterfaces();
			for (Class<?> interf : interfaces) {
				realisations.add(
					new Relation(RelationType.REALISATION, wrappedClass, interf)
				);
			}
		}
		return realisations.toArray(Relation[]::new);
	}
	
	private Association[] getAssociations() {
		List<Association> associations = new ArrayList<>();
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
		return associations.toArray(Association[]::new);
	}
	
	private Relation[] getDependencies() {
		List<Relation> dependencies = new ArrayList<>();
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
		return dependencies.toArray(Relation[]::new);
	}
}

















