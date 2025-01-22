package org.mql.java.projectanalyzer.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import org.mql.java.projectanalyzer.Package;
import org.mql.java.projectanalyzer.relations.Relation;

public class PackageDiagram extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Package[] packages;
	private Relation<Package, Package>[] dependencies;
	
	public PackageDiagram(Package[] packages, Relation<Package, Package>[] dependencies) {
		super();
		this.packages = packages;
		this.dependencies = dependencies;
//		setPreferredSize(new Dimension(5000, 5000));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point currentPosition = new Point(50, 50);
		int spacing = 50;
		int totalWidth = 0;
		int lastWidth = 0;
		for (Package pckg : packages) {
			if (pckg.isEmpty()) {
				continue;
			}
			lastWidth = pckg.draw(g, currentPosition.x, currentPosition.y);
			currentPosition.translate(lastWidth + spacing, 0);
			totalWidth += lastWidth + spacing;
		}
		setPreferredSize(new Dimension(totalWidth + spacing, 500));
	}

}
