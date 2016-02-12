package ru.rostvertolplc.osapr.createitem4ico.components;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;

import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.pse.dialogs.RollupTemplateDialog.addMouseListenerToHeaderInTable;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.common.lov.LOVUIComponent;

import ru.rostvertolplc.osapr.helpers.*;

public class NewItemDialog extends TitleAreaDialog {

    private String selectedButton;
    private TCItemHelper resultItem;
    private String title, bodyMsg; 
    private String selectedUom;
    private String[] allUoms;
    private int msgType; //IMessageProvider  
    private TCItemHelper m_item; 
    private Text textId;
    private Text textName;
    private Text textRev; 
    private Combo comboType;
    private Combo comboUom;
    private LOVUIComponent uomLovComboBox;

    /** 
     * @param parentShell   - Parent Shell 
     * @param title         - Title of the dialogue. 
     * @param bodyMsg       - Body message of the dialogue. 
     * @param item 			- Item default value. 
     * @param msgType       - 'IMessageProvider.INFORMATION '  Can be one of: NONE ERROR INFORMATION WARNING
     */
    public NewItemDialog(
            Shell parentShell, String title, String bodyMsg, 
            TCItemHelper item, String[] allUoms, 
            int msgType)  { //for type see: IMessageProvider
   
        super(parentShell);

        this.m_item = item;

         
        //Set labels. 
        this.title = title;
        this.bodyMsg = bodyMsg;
      
        
        //set type
        this.msgType = msgType;
        
        // avoid help button poping up.
        this.setHelpAvailable(false);
        
        this.allUoms = allUoms;
        
        resultItem = null;

        selectedButton = null;
    }

    /** Dialogue constructor */
    @Override
    public void create() {

        super.create();

        //The 'Message' of a TitleArea dialogue only spans 1-2 lines. Then text is cut off. 
        //It is not very efficient for longer messages. 
        //Thus we utilize it as a 'title' and instaed we appeng a label to act as body. (see below). 
        setMessage(this.title, this.msgType); //$NON-NLS-1$
        //setTitle(); //not used.
        
        //Set the size of the dialogue. 
        //We avoid hard-coding size, instead we tell it to figure out the most optimal size.
        //this.getShell().setSize(650, 550); //Hard-Coded = bad.
        this.getShell().setSize(getInitialSize());
    }

    /** Return the buttonID of the button that the user selected if he pressed ok. 
     * 
     * @return ButtonID of selected button.
     */
    public String getSelectedButton() {
        return selectedButton;
    }
    
    public TCItemHelper getResultItem() {
    	return resultItem;
    }


    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout(2, false);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(layout);
        
        Label label = new Label(container, 0);
        label.setText(this.bodyMsg);
        label = new Label(container, 0);
        label.setText("");
        label = new Label(container, 0);
        
        label.setText("Идентификатор:");
        textId = new Text(container, SWT.BORDER);
        label = new Label(container, 0);
        label.setText("Наименование:");       
        textName = new Text(container, SWT.BORDER);
        label = new Label(container, 0);
        label.setText("Ревизия:");
        textRev = new Text(container, SWT.BORDER);
        label = new Label(container, 0);
        label.setText("Тип объекта:");        
        comboType = new Combo(container, SWT.BORDER | SWT.DROP_DOWN | SWT.V_SCROLL | SWT.READ_ONLY);
        String[] preferenceValue = PreferenceHelper.getPreferenceValueArray("RVT_ITEM4ICO_TYPES");
        if ((preferenceValue == null) || (preferenceValue.length == 0)) {
        	comboType.add("H47_Material");
        	comboType.add("H47_Standart_Izd");
        } else {
        	for (String str : preferenceValue) {
        		comboType.add(str);
              }        	
        	/*for (int i = 0; i < preferenceValue.length; i++) {
        		comboType.add(preferenceValue[i]);
        	} */
        }
        comboType.select(0);
        label = new Label(container, 0);
        label.setText("Единица измерения:");
        comboUom = new Combo(container, SWT.BORDER | SWT.DROP_DOWN | SWT.V_SCROLL);
        if ((allUoms != null) && (allUoms.length > 0)) {
        	for (String str: allUoms) {
        		comboUom.add(str);
        	}        	
        }
        //Composite comp = new Composite(container,0);
        //comp.set
        
        
        //uomLovComboBox = new LOVUIComponent((TCSession)AIFUtility.getCurrentApplication().getSession(), "Unit of Measures");
        
        

        
        if (m_item != null) {
        	textId.setText(m_item.getId());
        	textRev.setText(m_item.getRev());
        	textName.setText(m_item.getName());
        	comboUom.setText(m_item.getUom());
        }        
        return area;
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    protected void saveInput() {
    	resultItem = new TCItemHelper(textId.getText()  , textRev.getText(), comboType.getText(), textName.getText(), comboUom.getText());
        
    }

    /** Called when the ok button is pressed */
    @Override
    protected void okPressed() {
        saveInput(); // save input.
        super.okPressed(); // close dialogue
    }

}