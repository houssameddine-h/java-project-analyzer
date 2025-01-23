package org.mql.java.projectanalyzer.relations;

import java.util.ArrayList;
import java.util.List;

import org.mql.java.projectanalyzer.Package;

public class PackageRelationManager {
	public static final String IGNORED_PACKAGES = "|java.lang|"; // "|p1|p2|p3|"
	
	private List<Relation<Package, Package>> relations;
	private List<Package> packages;
	
	public PackageRelationManager(List<Package> packages) {
		relations = new ArrayList<>();
		this.packages = packages;
	}
	
	@SuppressWarnings("unchecked")
	public Relation<Package, Package>[] getRelations() {
		return relations.toArray(Relation[]::new);
	}
	
	public void addRelation(Relation<Package, Package> relation) {
		String sourcePackage = relation.getSource().getName();
		String targetPackage = relation.getTarget().getName();
		if (!IGNORED_PACKAGES.contains("|" + sourcePackage + "|") &&
			!IGNORED_PACKAGES.contains("|" + targetPackage + "|") &&
			!sourcePackage.equals(targetPackage) &&
			!contains(relation) &&
			!relation.getSource().hasParentRelationWith(relation.getTarget())) {
			relations.add(relation);			
		}
	}
	
	public void addPackage(Package packg) {
		if (!IGNORED_PACKAGES.contains("|" + packg.getName() + "|") && !containsPackage(packg)) {
			packages.add(packg);
		}
	}
	
	public boolean containsPackage(Package packg) {
		for (Package pckg : packages) {
			if (pckg.equals(packg)) {
				return true;
			}
		}
		return false;
	}
	
	public Package getPackage(String name) {
		for (Package pckg : packages) {
			if (pckg.getName().equals(name)) {
				return pckg;
			}
		}
		return null;
	}
	
	public Package[] getExternalPackages() {
		return packages.stream()
			.filter(pckg -> pckg.isExternal())
			.toArray(Package[]::new);
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
