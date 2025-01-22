package org.mql.java.projectanalyzer.relations;

public class Association extends Relation<Class<?>, Class<?>> {
	private Navigability navigability;
	private boolean isSelfAssociation;
	public Association(
			Class<?> sourceClass,
			Class<?> targetClass)
	{
		this(sourceClass, targetClass, Navigability.DEFAULT);
	}
	
	public Association(
			Class<?> sourceClass,
			Class<?> targetClass, Navigability navigability)
	{
		super(RelationType.ASSOCIATION, sourceClass, targetClass);
		this.navigability = navigability;
		isSelfAssociation = sourceClass.equals(targetClass);
	}
	
	public boolean isSelfAssociation() {
		return isSelfAssociation;
	}

	public Navigability getNavigability() {
		return navigability;
	}

	public void setNavigability(Navigability navigability) {
		this.navigability = navigability;
	}

	@Override
	public String toString() {
		return super.toString().replaceFirst(" ]$", ", " + navigability + " ]");
	}
}
