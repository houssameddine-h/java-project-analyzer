package org.mql.java.projectanalyzer.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class HeaderPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean classDiagramVisible;
	private Consumer<Boolean> showClassDiagram;

	public HeaderPanel(Consumer<Boolean> showClassDiagram) {
		this.showClassDiagram = showClassDiagram;
		classDiagramVisible = false;
        setLayout(new FlowLayout());

        JRadioButton packageDiagramButton = new JRadioButton("Diagramme de package");
        packageDiagramButton.setSelected(true);
        JRadioButton classDiagramButton = new JRadioButton("Diagramme de Class");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(packageDiagramButton);
        buttonGroup.add(classDiagramButton);

        EventHandler eventHandler = new EventHandler();
        
        packageDiagramButton.addActionListener(eventHandler);

        classDiagramButton.addActionListener(eventHandler);

        add(packageDiagramButton);
        add(classDiagramButton);
	}
	
	class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			classDiagramVisible = !classDiagramVisible;
			showClassDiagram.accept(classDiagramVisible);
		}
		
	}
}
