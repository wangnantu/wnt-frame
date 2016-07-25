package com.wnt.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 *  
 */
public final class SingleGetCon {

	static SAXReader saxreder = null;
	static Document document = null;
	static String xmlPath = null;
	static String driverclass = "";
	static String url = "";
	static String user = "";
	static String pass = "";
    static Connection conn = null;
    private static SingleGetCon getconSingle = null;
 
    public static SingleGetCon getInitGetcon() {
        if (getconSingle == null) {
            synchronized (SingleGetCon.class) {
                if (getconSingle == null) {
                	getconSingle = new SingleGetCon();
                }
            }
        }
        return getconSingle;
    }
 
    static {
        try {
    		xmlPath = File.separator+ getProjectPath()
			+ "WEB-INF"+File.separator+"classes"+File.separator+"spring-mybatis.xml";
			saxreder = new SAXReader();
			saxreder.setEntityResolver(new IgnoreDTDEntityResolver());
			File xmlfile = new File(xmlPath);
			Reader xmlStreamReader;
			try {
				xmlStreamReader = new InputStreamReader(new FileInputStream(xmlfile), "UTF-8");
				document = saxreder.read(xmlStreamReader);
				Element root = document.getRootElement();
				for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
					Element been = (Element) i.next();
					if ("dataSource".equals(been.attributeValue("id"))) {
						List<Element> property = been.elements();
						driverclass = property.get(0).element("value").getText();
						url = property.get(1).element("value").getText();
						user = property.get(2).element("value").getText();
						pass = property.get(3).element("value").getText();
						break;
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
            Class.forName(driverclass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    } 
    
    public static String getProjectPath() {
		String projectPath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();

		projectPath = projectPath.substring(1);
		projectPath = projectPath.substring(0, projectPath.indexOf("WEB-INF"));

		try {
			projectPath = URLDecoder.decode(projectPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return projectPath;
	}
   }