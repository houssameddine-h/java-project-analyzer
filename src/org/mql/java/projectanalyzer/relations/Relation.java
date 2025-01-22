package org.mql.java.projectanalyzer.relations;

import java.util.Objects;

public class Relation<S, T> {
	private RelationType type;
	private S source;
	private T target;

	public Relation(RelationType type, S source, T target) {
		this.type = type;
		this.source = source;
		this.target = target;
	}
	
	public Relation(S source, T target) {
		this(RelationType.DEPENDENCY, source, target);
	}

	public RelationType getType() {
		return type;
	}

	public S getSource() {
		return source;
	}

	public T getTarget() {
		return target;
	}
	
	public boolean hasItem(Object item) {
		return (source.equals(item) || target.equals(item));
	}
	
	/**
	 * Compares this relation with @param relation (ignoring the type)
	 * @return:
	 *   1 : same
	 * 	 0 : different
	 * 	-1 : same relation but with reversed source and target classes
	 */
	@SuppressWarnings("unlikely-arg-type")
	public int compareToIgnoreType(Relation<S, T> relation) {
		if (this == relation)
			return 1;
		if (relation == null)
			return 0;
		if (Objects.equals(source, relation.source) &&
			Objects.equals(target, relation.target)) {
			return 1;
		}
		if (Objects.equals(source, relation.target) &&
			Objects.equals(target, relation.source)) {
			return -1;
		}
		return 0;
	}
	
	public int compareTo(Relation<S, T> relation) {
		if (relation != null && type != relation.type) {
			return 0;
		}
		return compareToIgnoreType(relation);
	}

	public boolean equals(Relation<S, T> relation) {
		if (this == relation)
			return true;
		if (relation == null)
			return false;
		
		return type == relation.type &&
			   Objects.equals(source, relation.source) &&
			   Objects.equals(target, relation.target);
	}

	public String toString() {
		return "[ " + type + ", " +
				source + ", " +
				target + " ]";
	}
}
