package org.mql.java.projectanalyzer.relations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mql.java.projectanalyzer.Clazz;

public class RelationManager {
	private List<Relation> relations;
	private Clazz testClz; // creating a both direction association for testing (to be removed)

	public RelationManager() {
		relations = new ArrayList<>();
	}
	
	public Relation[] getRelations() {
		return getCleanedRelations();
	}
	
	/*
	 * - Combining double associations with invert classes into one with no navigability;
	 * - Not doing it when adding relatoins to avoid possible loss of information;
	 * - Not updating relations field for the same reason;
	 */
	public Relation[] getCleanedRelations() {
		List<Relation> rels = new ArrayList<Relation>();
		for (Relation relation : relations) {
			if (relation instanceof Association association) {
				Optional<Association> invert = getInvertIfPresent(association);
				if (invert.isPresent()) {
					if (!rels.contains(invert.get())) {
						association.setNavigability(Navigability.NONE);
						rels.add(association);
					}
					continue;						
				}
			}
			rels.add(relation);
		}
		
		return rels.toArray(Relation[]::new);
	}
	
	public Association[] getAssociations() {
		return relations.stream()
				.filter(relation -> relation.getType() == RelationType.ASSOCIATION)
				.toArray(Association[]::new);
	}
	
	public void addRelation(Relation[] relations) {
		for (Relation rel : relations) {
			addRelation(rel);
		}
	}
	
	public void addRelation(Relation relation) {
		// Check if stronger relations already exist
		if (!isRedundant(relation)) {
			// Remove weaker relations
			removeRedundant(relation);
			relations.add(relation);
		}
	}
	
	/*
	 * add relations already cleaned of redundancies
	 */
	public void addCleanedRelations(Relation[] rels) {
		for (Relation relation : rels) {
			relations.add(relation);
		}
	}
	
	public boolean contains(Relation relation) {
		for (Relation rel : relations) {
			if (rel.equals(relation)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Search within the relations for an association with inverted source and target
	 * classes of the { @param relation }
	 */
	public Optional<Association> getInvertIfPresent(Association association) {
		for (Association rel : getAssociations()) {
			if (rel.compareTo(association) == -1) {
				return Optional.of(rel);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Remove weaker relations than { @param relation }
	 */
	private void removeRedundant(Relation relation) {
		List<RelationType> redundantTypes =
				RelationType.getWeakerThan(relation.getType());
		for (Iterator<Relation> i = relations.iterator(); i.hasNext();) {
			Relation rel = i.next();
			if (redundantTypes.contains(relation.getType()) &&
				rel.compareToIgnoreType(relation) == 1) {
				i.remove();
			}
		}
	}
	
	/**
	 * Checks if a stronger relation than { @param relation } exists
	 */
	private boolean isRedundant(Relation relation) {
		List<RelationType> redundantTypes =
				RelationType.getStrongerThan(relation.getType());
		for (Relation rel : relations) {
			if (redundantTypes.contains(relation.getType()) &&
					rel.compareToIgnoreType(relation) == 1) {
				return true;
			}
		}
		return false;
	}
}
