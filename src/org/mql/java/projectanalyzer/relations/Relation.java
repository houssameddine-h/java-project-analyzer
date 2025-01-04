package org.mql.java.projectanalyzer.relations;

import org.mql.java.projectanalyzer.Clazz;

public class Relation {
	private RelationType type;
	private Clazz sourceClass;
	private Clazz targetClass;

	public Relation(RelationType type, Clazz sourceClass, Clazz targetClass) {
		this.type = type;
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	public RelationType getType() {
		return type;
	}

	public Clazz getSourceClass() {
		return sourceClass;
	}

	public Clazz getTargetClass() {
		return targetClass;
	}
	
	public String toString() {
		return "[ " + type + ", " +
				sourceClass.getName() + ", " +
				targetClass.getName() + " ]";
	}
}
