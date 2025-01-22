package org.mql.java.projectanalyzer.relations;

import java.util.ArrayList;
import java.util.List;

import org.mql.java.projectanalyzer.Package;

public class PackageRelationManager {
	public static final String IGNORED_PACKAGES = "|java.lang|"; // "|p1|p2|p3|"
	
	private List<Relation<Package, Package>> relations;
	public PackageRelationManager() {
		relations = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public Relation<Package, Package>[] getRelations() {
		return relations.toArray(Relation[]::new);
	}
	
	public void addRelation(Relation<Package, Package> relation) {
		String sourcePackage = relation.getSource().getName();
		String targetPackage = relation.getTarget().getName();
		if (!(IGNORED_PACKAGES.contains("|" + sourcePackage + "|") ||
			IGNORED_PACKAGES.contains("|" + targetPackage + "|") ||
			sourcePackage.equals(targetPackage) ||
			contains(relation))) {
			relations.add(relation);			
		}
	}
	
	public boolean contains(Relation<Package, Package> relation) {
		for (Relation<Package, Package> rel : relations) {
			if (rel.equals(relation)) {
				return true;
			}
		}
		return false;
	}
}
