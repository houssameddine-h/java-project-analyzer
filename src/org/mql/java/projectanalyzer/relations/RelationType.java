package org.mql.java.projectanalyzer.relations;

import java.util.ArrayList;
import java.util.List;

public enum RelationType {
	//	composition and aggregation are generalized within association
	EXTENSION, ASSOCIATION, DEPENDENCY, REALISATION;
	
	public static List<RelationType> getWeakerThan(RelationType than) {
		List<RelationType> types = new ArrayList<>();
		switch(than) {
			case EXTENSION:
				types.add(REALISATION);
			case REALISATION:
				types.add(ASSOCIATION);
			case ASSOCIATION:
				types.add(DEPENDENCY);
				break;
			case DEPENDENCY:
				types.add(DEPENDENCY); // only one dependency is allowed to the same entity/type
		}
		return types;
	}
	
	public static List<RelationType> getStrongerThan(RelationType than) {
		List<RelationType> types = new ArrayList<>();
		switch(than) {
			case DEPENDENCY:
				types.add(DEPENDENCY); // only one dependency is allowed to the same entity/type
			case ASSOCIATION:
				types.add(REALISATION);
			case REALISATION:
				types.add(EXTENSION);
			default:
		}
		return types;
	}
}
