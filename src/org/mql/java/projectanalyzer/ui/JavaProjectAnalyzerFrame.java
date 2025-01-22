package org.mql.java.projectanalyzer.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.mql.java.projectanalyzer.Project;

public class JavaProjectAnalyzerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Project project;

	public JavaProjectAnalyzerFrame(Project project) {
		this.project = project;
		buildUI();
		setTitle("Analyseur de projet Java");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void buildUI() {
		JPanel packageDiagramPanel = new PackageDiagram(project.getPackages(), project.getPackageRelations());
		JScrollPane scrollPane = new JScrollPane(packageDiagramPanel);
		add(scrollPane);
	}
}
