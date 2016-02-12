package ru.rostvertolplc.osapr.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLConfigurationProperties {
	
	private static String DEFAULT_PACKAGE = "/resources/xmlConfigs/";
	
	private static Map<String, Document> xmls = new HashMap();
  
	public static List<Element> getElemntsFromXml(String xmlConfigFile, String property) {
		Document doc = getDocument(xmlConfigFile);
		if (doc == null) {
			return new ArrayList();
		}
		List<Element> values = new ArrayList();
		NodeList elementsByTagName = doc.getElementsByTagName(property);
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			Element el = (Element)elementsByTagName.item(i);
			values.add(el);
		}
		return values;
	}
  
	private static Document getDocument(String xmlConfigFile) {
		if (xmls.containsKey(xmlConfigFile)) {
			return (Document)xmls.get(xmlConfigFile);
		}
		InputStream resource = XMLConfigurationProperties.class.getResourceAsStream(DEFAULT_PACKAGE + (xmlConfigFile.endsWith(".xml") ? xmlConfigFile : new StringBuilder(String.valueOf(xmlConfigFile)).append(".xml").toString()));
		if (resource == null) {
			return null;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(resource);
			doc.getDocumentElement().normalize();
			xmls.put(xmlConfigFile, doc);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}