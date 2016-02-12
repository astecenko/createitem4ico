package ru.rostvertolplc.osapr.createitem4ico.handlers;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import ru.rostvertolplc.osapr.createitem4ico.components.*;
import ru.rostvertolplc.osapr.helpers.*;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import com.teamcenter.rac.aif.AIFClipboard;
import com.teamcenter.rac.aif.AIFPortal;
import com.teamcenter.rac.aif.AIFTransferable;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import java.util.AbstractMap.SimpleEntry;


import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.AbstractAIFSession;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.*;
import com.teamcenter.services.rac.core.DataManagementService;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.Registry;
import com.teamcenter.rac.util.Utilities;
import com.teamcenter.rac.classification.*;
import com.teamcenter.rac.classification.common.*;
import com.teamcenter.rac.classification.common.operations.G4MOpenOperation;
import com.teamcenter.services.rac.core._2007_01.DataManagement.*;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler implements ClipboardOwner {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		AbstractAIFUIApplication currentApplication = AIFUtility.getCurrentApplication();
		DataManagementService localDataManagementService = DataManagementService.getService((TCSession)currentApplication.getSession());
		GetItemCreationRelatedInfoResponse response;
		//response = localDataManagementService.getItemCreationRelatedInfo(selectedItemType, null);
		response = localDataManagementService.getItemCreationRelatedInfo("Item", null);
		
		String[] arrayOfUoms = response.uoms;
		
		
        //--- Set Dialogue options.
        String title = "Создание записи в классификаторе совместно с классифицированным объектом\n"
        		+ "\tПеред созданием объекта необходимо выбрать целевой класс хранения!";
        String body = "Поля \"Наименование\" и \"Тип объекта\" обязательны для заполнения\n"
                + "Оставьте поля \"Идентификатор\" и \"Ревизия\" пустыми, для автоназначения";        
        
        int msgType = IMessageProvider.INFORMATION;  //Can be one of: NONE ERROR INFORMATION WARNING
        //--- Instantiate & open the dialogue.
        
        NewItemDialog myDialog =
        	new NewItemDialog(window.getShell(), title, body, null, arrayOfUoms, msgType);
        int retVal = myDialog.open();
        
        
        

        //Handle dialogue outcome.
        switch (retVal) {
        case Window.OK:  //Avoid using generic '1' and '0' as it's confusing. Use defined constants as shown.
        	// Нажато Ok - создаем и классифициуем объект
        	TCItemHelper itemInfo;
        	itemInfo = myDialog.getResultItem();
        	TCComponentItem item;
        	
    		try {
    			item = TCComponentsHelper.createItem(itemInfo.getId(),itemInfo.getRev(),itemInfo.getType(),itemInfo.getName());
    			
    			// Объекты необходимы для проверки наличия ед.измерения в справочнике 
    			Object localObject2 = null; 
    			Object localObject0;
    			TCComponent[] localObject4;
    			
    			if (((itemInfo.getUom() instanceof String)) && (((String)itemInfo.getUom()).length() > 0))
    	        {
    	          // получают массив идениц измерения и проверяют есть ли в нем выбранная	
    	          localObject0 = (TCComponentUnitOfMeasureType)((TCSession)currentApplication.getSession()).getTypeComponent("UnitOfMeasure");
    	          localObject4 = ((TCComponentUnitOfMeasureType)localObject0).extent();
    	          if ((localObject4 != null) && (localObject4.length > 0)) {
    	            for (int j = 0; j < localObject4.length; j++) {
    	              if (localObject4[j].toString().equals(itemInfo.getUom()))
    	              {
    	                localObject2 = localObject4[j];
    	                break;
    	              }
    	            }
    	          }
    	        }
    	        if (localObject2 != null) {
    	          item.setReferenceProperty("uom_tag", (TCComponent)localObject2);
    	        }
    			
    			TCComponentItemRevision itemRevision = item.getLatestItemRevision();
    			/*
    			 * Скопируем созданный компонент в буфер
    			 */

    			//Vector localVector = new Vector();
    			//localVector.addElement(item);
    			AIFClipboard localAIFClipboard = AIFPortal.getClipboard();
    			
    			Registry m_reg = currentApplication.getRegistry();
    			AbstractG4MContext m_context = ((AbstractG4MApplication)currentApplication).getG4MContext();
    			//AbstractAIFSession localAbstractAIFSession = currentApplication.getSession();
    			//AIFTransferable localAIFTransferable = new AIFTransferable(localVector);
    			AIFTransferable localAIFTransferable = new AIFTransferable(itemRevision);
    			localAIFClipboard.setContents(localAIFTransferable, this); //??? можно вместо this поставить null !!!
    			
    			/*
    			 * Теперь надо выполнить вставку
    			 */
    			currentApplication.getSession().setStatus("Paste Tc Component from Clipbord");
    			
    			try {
    				Object localObject3;
        			localObject3 = new G4MOpenOperation(m_context, m_reg.getString("g4mOpenOperation.MESSAGE"));
                    ((G4MOpenOperation)localObject3).setComponent((InterfaceAIFComponent)itemRevision);
                    ((G4MOpenOperation)localObject3).executeOperation();
					
				} catch (Exception e) {
					MessageBox.post(e);
				}
				currentApplication.getSession().setReadyStatus();
				
    		} catch (TCException e) {
    			MessageDialog.openInformation(
    					window.getShell(),
    					"Ошибка при создании объека Item типа " + itemInfo.getType(),
    					e.getError());
    		}
        	
        	
            //System.out.println("You selected option: " +  myDialog.getResultItem().getName());
            break;
        case Window.CANCEL:
            //System.out.println("You clicked cancle");
            break;
        default:
            System.out.println("Unexpected closure of dialogue.");
            break;
        }
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	 public void lostOwnership(Clipboard paramClipboard, Transferable paramTransferable) {}
	
}
