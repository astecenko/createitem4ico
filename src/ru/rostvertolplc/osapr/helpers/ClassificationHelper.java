package ru.rostvertolplc.osapr.helpers;

 import com.teamcenter.rac.aif.AbstractAIFUIApplication;
 import com.teamcenter.rac.aifrcp.AIFUtility;
 import com.teamcenter.rac.classification.common.G4MInClassDialog;
 import com.teamcenter.rac.classification.common.G4MUserAppContext;
 import com.teamcenter.rac.kernel.TCClassificationService;
 import com.teamcenter.rac.kernel.TCComponent;
 import com.teamcenter.rac.kernel.TCComponentItem;
 import com.teamcenter.rac.kernel.TCComponentItemRevision;
 import com.teamcenter.rac.kernel.TCSession;
 import com.teamcenter.rac.kernel.ics.ICSSearchResult;
 import com.teamcenter.rac.util.MessageBox;
 import java.awt.Component;
 import java.awt.Container;
 import java.awt.event.ActionEvent;
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 import javax.swing.JButton;
 import javax.swing.JPanel;
 
 public class ClassificationHelper
 {
   private static boolean cancelClassification;
   
   public static synchronized TCComponentItem getMaterialFromClassificator(TCComponent tcComponent)
   {
     try
     {
       cancelClassification = false;
       AbstractAIFUIApplication currentApplication = AIFUtility.getCurrentApplication();
       G4MInClassDialog dlg = new G4MInClassDialog(currentApplication);
       G4MUserAppContext dialogContext = dlg.getDialogContext();
       if (tcComponent != null) {
         dialogContext.openComponent(tcComponent);
       }
       Component[] components = dlg.getContentPane().getComponents();
       Component[] components2 = ((JPanel)components[1]).getComponents();
       Component[] components3 = ((JPanel)components2[0]).getComponents();
       ((JButton)components3[1]).addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(ActionEvent e) {
           ClassificationHelper.cancelClassification = true;
         }
       });
       dlg.addWindowListener(getListener());
       dlg.setModal(true);
       dlg.setVisible(true);
       if (cancelClassification) {
         return null;
       }
       ICSSearchResult[] SR = dlg.getSelectedICOs();
       if ((SR == null) || (SR.length <= 0) || (SR[0] == null)) {
         return null;
       }
       String sUid = SR[0].getWsoUid();
       if ((sUid == null) || (sUid.length() == 0)) {
         MessageBox.post("Выбранная запись Классификатора не связана с объектом (Item)", "Внимание", 4);
         return null;
       }
       
       TCComponent selectedWSO = ((TCSession)currentApplication.getSession()).getClassificationService().getTCComponent(SR[0].getWsoUid());
       TCComponentItem i = null;
       if ((selectedWSO instanceof TCComponentItem)) {
         i = (TCComponentItem)selectedWSO;
       } else if (!(selectedWSO instanceof TCComponentItemRevision)) {}
       return ((TCComponentItemRevision)selectedWSO).getItem();
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
     return null;
   }
   
   private static java.awt.event.WindowListener getListener() {
     new WindowAdapter()
     {
       public void windowClosing(WindowEvent arg0) {
         ClassificationHelper.cancelClassification = true;
         super.windowClosing(arg0);
       }
     };
     return null;
   }
 }