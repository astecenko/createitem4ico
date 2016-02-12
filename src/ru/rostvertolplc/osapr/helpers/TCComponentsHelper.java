package ru.rostvertolplc.osapr.helpers;

import com.teamcenter.rac.aif.ApplicationDef;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevision;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevisionType;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetType;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentViewType;
import com.teamcenter.rac.kernel.TCComponentViewTypeType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class TCComponentsHelper
 {
   private static final TCSession session = (TCSession)AIFUtility.getDefaultSession();
   
   public static TCComponentDataset createDataset(String type, String name) throws TCException {
     return createDataset(type, name, "");
   }
   
   public static TCComponentDataset createDataset(String type, String name, String tool) throws TCException {
     TCComponentDatasetType typeDS = (TCComponentDatasetType)session.getTypeComponent("Text");
     TCComponentDataset dataSet = typeDS.create(name, name, type, tool);
     return dataSet;
   }
   
   public static TCComponentItem createItem(String id, String rev, String type, String name) throws TCException {
     TCComponentItem search = searchItem(id);
     if (search == null) {
       TCComponentItemType typeItem = (TCComponentItemType)session.getTypeComponent("Item");
       search = typeItem.create(id, rev, type, name, "", new TCComponent());
       return search;
     }
     return null;
     
   }
   
   public static TCComponentItem searchItem(String id) throws TCException
   {
     TCComponentItemType type = (TCComponentItemType)session.getTypeComponent("Item");
     TCComponentItem item = type.find(id);
     return item;
   }
   
   public static void addToNewStuff(TCComponentItem object) {
     try {
       TCComponentFolder newStuffFolder = session.getUser().getNewStuffFolder();
       addItemToFolder(object, newStuffFolder);
     } catch (TCException e) {
       e.printStackTrace();
     }
   }
   
   public static void addItemToFolder(TCComponentItem createItem, TCComponentFolder folder) {
     if (folder == null) {
       return;
     }
     try {
       folder.add("contents", createItem);
     }
     catch (Exception localException) {}
   }
   
   public static void openInTC(TCComponent comp) {
     ApplicationDef applicationDef = AIFUtility.getAIFApplicationDefMgr().getApplicationDef("com.teamcenter.rac.ui.perspectives.navigatorPerspective");
     applicationDef.openApplication(new com.teamcenter.rac.aif.kernel.InterfaceAIFComponent[] { comp });
   }
   
 
 
 
   public static void copyToClipBoard(TCComponent comp) {}
   
 
 
   public static void createDefaultBomView(TCComponentItem item)
   {
     try
     {
       TCComponentItemRevision rev = item.getLatestItemRevision();
       TCComponentBOMViewRevision createBVR = createBVR(rev, "View");
       createBVR.save();
       rev.lock();
       rev.save();
       rev.unlock();
     } catch (TCException e) {
       e.printStackTrace();
     }
   }
   
   public static TCComponentViewType findBOMViewType(String sBOMViewType, TCSession session) {
     TCComponentViewType vt = null;
     try {
       TCComponentViewTypeType tc_vtt = (TCComponentViewTypeType)session.getTypeComponent("PSViewType");
       TCComponent[] array_vt = tc_vtt.extent();
       for (int i = 0; i < array_vt.length; i++)
         if (array_vt[i].toString().equals(sBOMViewType)) {
           vt = (TCComponentViewType)array_vt[i];
           break;
         }
     } catch (TCException ex) {
       ex.printStackTrace();
     }
     return vt;
   }
   
   public static TCComponentBOMViewRevision createBVR(TCComponentItemRevision ir, String bv_type) throws TCException {
     TCSession session = ir.getSession();
     TCComponentBOMViewRevision bvr = null;
     TCComponentViewType vt = findBOMViewType(bv_type, session);
     TCComponentBOMViewRevisionType tc_bvr = (TCComponentBOMViewRevisionType)session.getTypeComponent("BOMView Revision");
     bvr = tc_bvr.create(ir.getItem().getProperty("item_id"), ir.getProperty("item_revision_id"), vt, false);
     return bvr;
   }
 }