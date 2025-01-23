package org.mql.java.projectanalyzer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.mql.java.projectanalyzer.ui.Entity;

public class Package implements Entity {
	private static final int DEFAULT_HEIGHT = 75;
	private static final int PADDING = 20;
	private static final int MARGIN = 50;
	private static final int TAB_WIDTH = 50;
	private static final int TAB_HEIGHT = 20;
	private static final boolean IGNORE_EMPTY = true;
	
	private String name;
	private List<Package> packages;
	private List<Clazz> classes;
	
	// UI props
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Package(String name) {
		this.name = name;
		classes = new ArrayList<>();
		packages = new ArrayList<>();
		
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	
	public Package() {
		this("default"); // default package
	}
	
	public String getName() {
		return name;
	}
	
	public Clazz[] getClasses() {
		return classes.toArray(Clazz[]::new);
	}
	
	public void addClass(Clazz cls) {
		classes.add(cls);
	}
	
	/*
	 * Returns all descendant sub packages (childs and childs of childs and so on)
	 */
	public List<Package> getDescendantPackages () {
		List<Package> result = new ArrayList<>();
		result.addAll(packages);
		for (Package pckg : packages) {
			result.addAll(pckg.getDescendantPackages());
		}
		
		return result;
	}
	
	/*
	 * Returns direct sub packages of the current package
	 */
	public Package[] getPackages () {
		return packages.toArray(Package[]::new);
	}
	
	
	public void addPackage(Package pckg) {
		packages.add(pckg);
	}
	
	public boolean isEmpty() {
		return classes.isEmpty();
	}
	
	public boolean hasParentRelationWith(Package with) {
		// common part should be at the beginneing, hence comparing with 0
		if (with.getName().indexOf(name) == 0 || name.indexOf(with.getName()) == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Package other = (Package) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "[" + name + "]";
	}

	@Override
	public Point draw(Graphics g, int x, int y) {
		Point totalSize = new Point(0, DEFAULT_HEIGHT); // width is calculated based on package name
		
		Point currentPosition = new Point(MARGIN, MARGIN);
		for (Package pckg : packages) {
			int nextX = IGNORE_EMPTY && pckg.isEmpty() ? x : x + currentPosition.x + PADDING;
			int nextY = IGNORE_EMPTY && pckg.isEmpty() ? y : y + PADDING * 3;
			
			Point size = pckg.draw(g, nextX, nextY);
			if (IGNORE_EMPTY && pckg.isEmpty()) {
				continue;
			}
			currentPosition.translate(size.x, 0);
			totalSize.translate(size.x, size.y);
		}
		
		if (IGNORE_EMPTY && isEmpty()) {
			return totalSize;
		}
		
		FontMetrics metrics = g.getFontMetrics();
		int textWidth = metrics.stringWidth(name);
		
		g.setColor(Color.DARK_GRAY);
		
		// package tab rectangle
		g.drawRect(x + MARGIN, y + MARGIN, TAB_WIDTH, TAB_HEIGHT);
		
		// package body rectangle
		int rectX = this.x = x + MARGIN;
		int rectY = this.y = y + MARGIN + TAB_HEIGHT;
		int rectWidth = this.width = totalSize.x + textWidth + PADDING * 2;
		int rectHeight = this.height = totalSize.y;

		g.drawRect(rectX, rectY, rectWidth, rectHeight);
		
		// package name
		g.drawString(name, rectX + PADDING, rectY + PADDING);
		
		totalSize.translate(textWidth + PADDING * 2 + MARGIN, 0);
		return totalSize;
	}
	
}
