package org.mql.java.projectanalyzer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mql.java.projectanalyzer.ui.Entity;

public class Package implements Entity {
	private String name;
	private List<Clazz> classes;
	
	public Package() {
		name = "default"; // default package
		classes = new ArrayList<>();
	}

	public Package(String name) {
		this.name = name;
		classes = new ArrayList<>();
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
	
	public boolean isEmpty() {
		return classes.isEmpty();
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
	public int draw(Graphics g, int x, int y) {
		int totalSize = 0;
		FontMetrics metrics = g.getFontMetrics();
		int textWidth = metrics.stringWidth(name);
		int padding = 20;
		Dimension cape = new Dimension(50, 20);
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(x, y, cape.width, cape.height);
		g.drawRect(x, y + cape.height, textWidth + padding * 2, 75);
		g.drawString(name, x + padding, y + cape.height + padding);
		
		totalSize = textWidth + padding * 2;
		return totalSize;
	}
	
}
