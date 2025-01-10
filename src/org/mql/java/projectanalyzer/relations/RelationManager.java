package org.mql.java.projectanalyzer.relations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationManager {
	private List<Relation> relations;

	public RelationManager() {
		relations = new ArrayList<>();
	}
	
	public Relation[] getRelations() {
		return relations.toArray(Relation[]::new);
	}
	
	public void addRelation(Relation[] relations) {
		for (Relation rel : relations) {
			addRelation(rel);
		}
	}
	
	public void addRelation(Relation relation) {
		/* 
		 * TODO: if type is association and already exists, update
		 * its cardinalities instead of adding new one
		 */
		if (!isRedundant(relation)) {
			removeRedundant(relation);
			relations.add(relation);
		}
	}
	
	/*
	 * add relations already cleared of redundancies
	 */
	public void addClearedRelations(Relation[] rels) {
		/* 
		 * TODO: if type is association and already exists, update
		 * its cardinalities instead of adding new one
		 */
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
