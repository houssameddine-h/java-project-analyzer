package org.mql.java.projectanalyzer.relations;

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
	
	public String toString() {
		return "[ " + type + ", " +
				new Clazz(sourceClass).getName() + ", " +
				new Clazz(targetClass).getName() + " ]";
	}
}
