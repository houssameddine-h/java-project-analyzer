package org.mql.java.projectanalyzer.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;

import org.mql.java.projectanalyzer.Package;
import org.mql.java.projectanalyzer.relations.Relation;

public class PackageDiagram extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Package defaultPackage;
	private Relation<Package, Package>[] dependencies;
	private Package[] externalPackages;
	
	public PackageDiagram(Package defaultPackage,
			Relation<Package, Package>[] dependencies,
			Package[] externalPackages) {
		super();
		this.defaultPackage = defaultPackage;
		this.dependencies = dependencies;
		this.externalPackages = externalPackages;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point size = defaultPackage.draw(g, 100, 100);
		int posX = 0;
		Point size2 = new Point(0, 0);
		for (Package pckg : externalPackages) {
			Point pckgSize = pckg.draw(g, posX, size.y + 150);
			posX += pckgSize.x + 100;
			size2.translate(pckgSize.x, pckgSize.y);
			
		}
		setPreferredSize(new Dimension(size.x + size2.x, size.y + size2.x));
		
		for (Relation<Package, Package> relation : dependencies) {
			Package s = relation.getSource();
			s.drawRelation(g, relation);
		}
	}

}
