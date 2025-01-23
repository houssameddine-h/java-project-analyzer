package org.mql.java.projectanalyzer.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import org.mql.java.projectanalyzer.Package;
import org.mql.java.projectanalyzer.relations.Relation;

public class PackageDiagram extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Package defaultPackage;
	private Relation<Package, Package>[] dependencies;
	
	public PackageDiagram(Package defaultPackage, Relation<Package, Package>[] dependencies) {
		super();
		this.defaultPackage = defaultPackage;
		this.dependencies = dependencies;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point size = defaultPackage.draw(g, 0, 0);
		setPreferredSize(new Dimension(size.x, size.y));
	}

}
