package com.fp2p.deprecated;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * 生产序列号工具类
 * @author Administrator
 *
 */
public class SequenceNumberUtil {

	public static Log log = LogFactory.getLog(SequenceNumberUtil.class);
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
	private static final String inputXml = "sequenceNumber.xml";

	/**
	 * 生产指定位数不重复的序列号
	 * @param length
	 * @return
	 * @throws DocumentException 
	 */
	public static String createSequenceNumber(String prefix, long length) throws DocumentException{
		
		 SAXReader saxReader = new SAXReader();
		 Document document = saxReader.read(inputXml);
		return null;
		
	}
	
	/**
	 * 添加一xml节点
	 * @param elementName
	 * @param elementValue
	 */
	@SuppressWarnings("unused")
	private static void addElement(String elementName, String elementValue, Document document){
		
		Element catalogElement = document.addElement("catalog");
		catalogElement.addComment("An XML Catalog");
	    catalogElement.addProcessingInstruction("target","text");
	}
	
	/**
	 * 更新一个节点
	 * @param elementName
	 * @param elementValue
	 */
	@SuppressWarnings("unused")
	private static void updateElement(String elementName){
		
	}
	
	/**
	 * 更新Document
	 * @param document
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean updateDocument(Document document){
		
		
		try {
			XMLWriter output = new XMLWriter(
					new FileWriter( new File(inputXml)));
			output.write( document );
			output.close();
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 读取配置文件
	 * @return Document
	 * @throws DocumentException 
	 */
	private static Document getDocument() throws DocumentException{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(inputXml);
		return document;
	}
}
