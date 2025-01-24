package org.mql.java.projectanalyzer.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.mql.java.projectanalyzer.Project;

public class JavaProjectAnalyzerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Project project;
	private JPanel headerPanel;
	private JScrollPane packageScrollPane;
	private JScrollPane classScrollPane;

	public JavaProjectAnalyzerFrame(Project project) {
		this.project = project;
		headerPanel = new HeaderPanel(this::showClassDiagram);
		
		buildUI();
		setTitle("Analyseur de projet Java");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void buildUI() {
		setLayout(new BorderLayout());
		PackageDiagram packageDiagramPanel = new PackageDiagram(project.getDefaultPackage(),
				project.getPackageRelations(),
				project.getExternalPackages());
		packageScrollPane = new JScrollPane(packageDiagramPanel);
		add(headerPanel, BorderLayout.NORTH);
		add(packageScrollPane, BorderLayout.CENTER);
		JPanel classDiagramPanel = new JPanel();
		classScrollPane = new JScrollPane(classDiagramPanel);
	}
	
	public void showClassDiagram(boolean show) {
		
		if (show) {
			remove(packageScrollPane);
			add(classScrollPane);
		} else {
			remove(classScrollPane);
			add(packageScrollPane);
		}
		repaint();
	}
}
