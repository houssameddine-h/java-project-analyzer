package org.mql.java.projectanalyzer.ui;

import java.awt.Graphics;
import java.awt.Point;

public interface Entity {
	/*
	 * Returns the width of the drawn entity;
	 * Used Point instead of Dimension for additional functionalities.
	 */
	public Point draw(Graphics g, int x, int y);
}
