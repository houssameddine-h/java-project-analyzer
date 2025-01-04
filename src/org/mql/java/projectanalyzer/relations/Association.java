package org.mql.java.projectanalyzer.relations;

import org.mql.java.projectanalyzer.Clazz;

public class Association extends Relation {
//	private CardinalityValue sourceCardinality;
//	private CardinalityValue targetCardinality;
	
	public Association(
			Clazz sourceClass,
			Clazz targetClass)
//			CardinalityValue sourceCardinality,
//			CardinalityValue targetCardinality)
	{
		super(RelationType.ASSOCIATION, sourceClass, targetClass);
//		this.sourceCardinality = sourceCardinality;
//		this.targetCardinality = targetCardinality;
	}

//	public CardinalityValue getSourceCardinality() {
//		return sourceCardinality;
//	}
//
//	public CardinalityValue getTargetCardinality() {
//		return targetCardinality;
//	}
}
