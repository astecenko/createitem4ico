package ru.rostvertolplc.osapr.createitem4ico.components;

import javax.swing.JDialog;

public class JDialogForRun implements Runnable {
	
	private JDialog m_dialog;
    
	public JDialogForRun(JDialog dialog) {
		this.m_dialog = dialog;
	}
	
	@Override
	public void run() {
		m_dialog.setVisible(true);
	}

}
