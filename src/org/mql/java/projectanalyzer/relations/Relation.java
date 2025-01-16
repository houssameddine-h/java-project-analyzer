package org.mql.java.projectanalyzer.relations;

import java.util.Objects;

import org.mql.java.projectanalyzer.Clazz;

public class Relation {
	private RelationType type;
	private Class<?> sourceClass;
	private Class<?> targetClass;

	public Relation(RelationType type, Class<?> sourceClass, Class<?> targetClass) {
		this.type = type;
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	public RelationType getType() {
		return type;
	}

	public Class<?> getSourceClass() {
		return sourceClass;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}
	
	public boolean hasClass(Class<?> cls) {
		return (sourceClass.equals(cls) || targetClass.equals(cls));
	}
	
	public boolean hasClass(Clazz clz) {
		Class<?> cls = clz.getWrappedClass();
		return (sourceClass.equals(cls) || targetClass.equals(cls));
	}
	
	/**
	 * Compares this relation with @param relation (ignoring the type)
	 * @return:
	 *   1 : same
	 * 	 0 : different
	 * 	-1 : same relation but with reversed source and target classes
	 */
	public int compareToIgnoreType(Relation relation) {
		if (this == relation)
			return 1;
		if (relation == null)
			return 0;
		if (Objects.equals(sourceClass, relation.sourceClass) &&
			Objects.equals(targetClass, relation.targetClass)) {
			return 1;
		}
		if (Objects.equals(sourceClass, relation.targetClass) &&
			Objects.equals(targetClass, relation.sourceClass)) {
			return -1;
		}
		return 0;
	}
	
	public int compareTo(Relation relation) {
		if (relation != null && type != relation.type) {
			return 0;
		}
		return compareToIgnoreType(relation);
	}

	public boolean equals(Relation relation) {
		if (this == relation)
			return true;
		if (relation == null)
			return false;
		
		return type == relation.type &&
			   Objects.equals(sourceClass, relation.sourceClass) &&
			   Objects.equals(targetClass, relation.targetClass);
	}

	public String toString() {
		return "[ " + type + ", " +
				sourceClass.getSimpleName() + ", " +
				targetClass.getSimpleName() + " ]";
	}
}
