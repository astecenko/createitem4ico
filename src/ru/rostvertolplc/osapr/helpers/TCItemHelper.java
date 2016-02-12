package ru.rostvertolplc.osapr.helpers;

public class TCItemHelper {
	
	private String m_id;
	private String m_rev;
	private String m_type;
	private String m_name;
	private String m_uom;
	
	public TCItemHelper(String id, String rev, String type, String name, String uom) {
		m_id = id;
		m_rev = rev;
		m_type = type;
		m_name = name;
		m_uom = uom;
	}
	
	public void setId(String paramString) {
		this.m_id = paramString;
	}
	
	public String getId() {
		return this.m_id;
	}
	
	public void setRev(String paramString) {
		this.m_rev = paramString;
	}
	
	public String getRev() {
		return this.m_rev;
	}
	
	public void setType(String paramString) {
		this.m_type = paramString;
	}
	
	public String getType() {
		return this.m_type;
	}
	
	public void setName(String paramString) {
		this.m_name = paramString;
	}
	
	public String getName() {
		return this.m_name;
	}
	
	public String getUom() {
		return this.m_uom;
	}

}
