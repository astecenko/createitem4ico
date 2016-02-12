package ru.rostvertolplc.osapr.helpers;

import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;

public class PreferenceHelper {
	
	private static TCPreferenceService service = ((TCSession)com.teamcenter.rac.aifrcp.AIFUtility.getDefaultSession()).getPreferenceService();
	
	public static String getPreferenceValue(String id) {
		return service.getString(0, id);
	}

	public static String[] getPreferenceValueArray(String id) {
		return service.getStringArray(0, id);
	}
}
