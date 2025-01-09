package org.mql.java.projectanalyzer.relations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationManager {
	private List<Relation> relations;

	public RelationManager() {
		relations = new ArrayList<>();
	}
	
	public boolean contains(Relation relation) {
		for (Relation rel : relations) {
			if (rel.equals(relation)) {
				return true;
			}
		}
		return false;
	}
	
	public void addRelation(Relation[] relation) {
		for (Relation rel : relation) {
			addRelation(rel);
		}
	}
	
	public void addRelation(Relation relation) {
		removeRedundant(relation);
		/* TODO: check if type is dependency and
		 * already exists before adding new one
		 */
		/* TODO: if type is association and already exists, update
		 * its cardinalities instead of adding new one
		 */
		relations.add(relation);
	}
	
	public void removeRedundant(Relation relation) {
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

}
