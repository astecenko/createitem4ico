package ru.rostvertolplc.osapr.createitem4ico.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.eclipse.ui.internal.FastViewPane;

public class NewItemFrame extends JFrame {
	
	private String itemName;
	
	public NewItemFrame(String itemName) {
		this.itemName = itemName;
		initGuiSettings();
	}
	
	private void setCenterLocation() {
	     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	     int x = (screenSize.width - getWidth()) / 2;
	     int y = (screenSize.height - getHeight()) / 2;
	     setLocation(x, y);
	}
	
	private void initGuiSettings() {
		//getContentPane().setLayout(new GridBagLayout());
		setLayout(new FlowLayout());
		initGui();
		setSize(new Dimension(350,200));
		setAlwaysOnTop(true);
		setResizable(false);
		setCenterLocation();		
	}
	
	private void initGui(){		
		JTextField text1 = new JTextField("Наименование объекта");
		this.add(text1);
		JButton add = new JButton("Добавить");
		add.addActionListener(getAddActionListener(text1));
	    this.add(add);
		JButton cancel = new JButton("Отмена");
		cancel.addActionListener(getCancelActionListener());		
		this.add(cancel);		
	}
	
	private ActionListener getCancelActionListener() {
		 return new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 NewItemFrame.this.setVisible(false);
			 }
		 };
	}
		
	private ActionListener getAddActionListener(final JTextField text1) {
		 return new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 itemName = text1.getText();
				 NewItemFrame.this.setVisible(false);
			 }
		 };
	}
}
