package ru.rostvertolplc.osapr.createitem4ico.components;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.text.PlainView;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import ru.rostvertolplc.osapr.helpers.TCItemHelper;

public class NewItemChooser extends JPanel {
	private JTextField id;
	private JTextField name;
	private JTextField rev;
	private JComboBox type;
	private JButton okButton;
	private JDialog dialog;
	final  AtomicBoolean userChoice = new AtomicBoolean(false);
	private static Frame parentFrame = null;
	//private boolean ok = false;
	
	public NewItemChooser(){
		setLayout(new BorderLayout());
		dialog = new JDialog();
		
		// сконструировать панель с полями
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,2));
		panel.add(new JLabel("Идентификатор:"));
		panel.add(id = new JTextField(""));
		panel.add(new JLabel("Наименование:"));
		panel.add(name = new JTextField(""));
		panel.add(new JLabel("Ревизия:"));
		panel.add(rev = new JTextField(""));
		panel.add(new JLabel("Тип объекта:"));
		panel.add(type = new JComboBox());
		type.addItem("H47_Complect");
		type.addItem("H47_Detal");
		type.addItem("H47_Document");
		type.addItem("H47_GeomMaterial");
		type.addItem("H47_Material");
		type.addItem("H47_Prochee");
		type.addItem("H47_SE");
		type.addItem("H47_Standart_Izd");
		add(panel, BorderLayout.CENTER);
		
		// создать кнопки Ok и Cancel
		okButton = new JButton("Создать");		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userChoice.set(true);
				dialog.setVisible(false);				
			}
		});
		
		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);				
			}
		});
		
		// ввести кнопки в нижней части окна у юной его границы
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);		
	}
	
	/**
	 * Устанавливает диалоговое окно в исходное состояние
	 * @param defObj Данные об объекте по умолчанию
	 */
	public void setTCItem(TCItemHelper defObj) {
		id.setText(defObj.getId());
		name.setText(defObj.getName());
		rev.setText(defObj.getRev());
	}
	
	/**
	 * Получает даные введенные в диалоговом окне
	 * @return Возвращает объект типа TCItemHelper, состояние которого
	 * 			отражает введенные ползователем данные
	 */
	public TCItemHelper getTCItem(){
		return new TCItemHelper(id.getText(), rev.getText(),
			type.getItemAt(type.getSelectedIndex()).toString(), name.getText(),"");
	}
	
	 /**
     * Shows the dialog in the parent frame
     */
    public static void show(JDialog dialog, String title)
    {    		    	
    	// parentFrame defined as private static Frame parentFrame = null; outside method
    	
    	// disable the shell and create an invisible parent frame in the UI thread
    	Display.getDefault().syncExec(new Runnable()
    	{
    		public void run()
    		{        			
    			IWorkbench workbench = PlatformUI.getWorkbench();
    			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    			Shell shell = activeWorkbenchWindow.getShell();
    			shell.setEnabled(false);

    			Frame frame = new Frame();
    			frame.setLocation(shell.getLocation().x, shell.getLocation().y);
    			frame.setSize(shell.getSize().x, shell.getSize().y);
    			frame.setAlwaysOnTop(true);    			
    			parentFrame = frame;    				  		     
    		}
    	});

    	if (parentFrame == null)
    	{
    		System.out.println("No parent frame!");
    		return;
    	}    
        
    	// center the dialog over the invisible parent frame in the same location as the shell
    	dialog.setLocation(parentFrame.getLocation().x
    			+ (parentFrame.getSize().width / 2)
    			- (dialog.getWidth() / 2)
    			, parentFrame.getLocation().y
    			+ (parentFrame.getSize().height / 2)
    			- (dialog.getHeight() / 2));
        
    	dialog.setVisible(true);  
        
    	parentFrame.dispose();
    	
    	// re-enable the shell
    	Display.getDefault().syncExec(new Runnable()
    	{

    		public void run()
    		{
    			IWorkbench workbench = PlatformUI.getWorkbench();
    			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    			Shell shell = activeWorkbenchWindow.getShell();
    			shell.setEnabled(true);				
    		}
    	});			
    }
	
	
	/**
	 * Отображает панель для ввода пароля в диалоговом окне
	 * @param parent Компонент из фрейма-владельца или
	 * 				пустое значение null
	 * @param title Заголовок диалогового окна
	 */
	public boolean showDialog(Component parent, String title) {
		userChoice.set(false);
		dialog.add(this);
		dialog.getRootPane().setDefaultButton(okButton);
		dialog.pack();

		show(dialog, title);
		
		
		Display.getDefault().syncExec(new Runnable()
    	{
    		public void run()
    		{        			
    			IWorkbench workbench = PlatformUI.getWorkbench();
    			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    			Shell shell = activeWorkbenchWindow.getShell();
    			shell.setEnabled(false);

    			Frame frame = new Frame();
    			frame.setLocation(shell.getLocation().x, shell.getLocation().y);
    			frame.setSize(shell.getSize().x, shell.getSize().y);
    			frame.setAlwaysOnTop(true);    			
    			parentFrame = frame;    				  		     
    		}
    	});
		
		if (parentFrame == null)
    	{
    		System.out.println("No parent frame!");
    		return false;
    	}
		
		JDialogForRun dialogFroRun = new JDialogForRun(dialog);
		
		PlatformUI.getWorkbench().getDisplay().syncExec(dialogFroRun); 
		
		parentFrame.dispose();
    	
    	// re-enable the shell
    	Display.getDefault().syncExec(new Runnable()
    	{

    		public void run()
    		{
    			IWorkbench workbench = PlatformUI.getWorkbench();
    			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    			Shell shell = activeWorkbenchWindow.getShell();
    			shell.setEnabled(true);				
    		}
    	});			
				
		return userChoice.get(); 
		//To generate U.I, we make sure to call the U.I thread,
        //otherwise we get an U.I exception.
     /*   PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
            	
                if (dialog == null) {
                	dialog = new JDialog(parent, true);
                }
                boolean okPressed = MessageDialog.openConfirm(parent, "prof err",
                        "Flag is not set in options. Would you like to add/rebuild?");

                if (okPressed) {
                    userChoice.set(true);
                } else
                    userChoice.set(false); 
                }
        }); 

        //Retrieve the value that the runnable changed. 
        return userChoice.get(); 
		*/
		
		// обнаружить фрейм-владелец
	/*	Frame owner = null;
		if (parent instanceof Frame) owner = (Frame)parent;
		else owner= (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
		
		// создать новое диалоговое окно при первом обращении
		// или изменении фрейма-владельца
		if (dialog == null || dialog.getOwner() != owner) {			
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		
		//установить заголовок и отобразить окно
		dialog.setTitle(title);
				
		 SwingUtilities.invokeLater(new Runnable()
	     {
	       public void run()
	      {  
	    	   dialog.setAlwaysOnTop(true);
	      	   dialog.setVisible(true);  
	      }
		    });
		   	   						
		//dialog.setVisible(true);
		return this.ok;
		
		*/
	}
}
