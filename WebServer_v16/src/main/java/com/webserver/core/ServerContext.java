package com.webserver.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端相关配置信息
 * @author tedu
 *
 */
public class ServerContext {
	/*
	 * Servlet映射关系
	 * key:请求路径
	 * value:Servlet的名字
	 */
	private static Map<String,String> servletMapping = new HashMap<String,String>();

	static{
		try {
			initServletMapping();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化Servlet映射
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	private static void initServletMapping() throws FileNotFoundException, DocumentException{
//		servletMapping.put("/myweb/reg","com.webserver.servlets.RegServlet");
//		servletMapping.put("/myweb/login","com.webserver.servlets.LoginServlet");
//		servletMapping.put("/myweb/change","com.webserver.servlets.ChangeServlet");
		/*
		 * 加载conf/servlets.xml文件初始化
		 */
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new FileInputStream("conf/servlets.xml"));
		Element root = doc.getRootElement();
		List<Element> empList = root.elements("servlet");
		for(Element str:empList){
			String url = str.attributeValue("url");
			String className = str.attributeValue("className");
			servletMapping.put(url, className);
		}
	}
	/**
	 * 根据请求路径获取对应的Servlet名字
	 * @param url
	 * @return
	 */
	public static String getServletName(String url){
		return servletMapping.get(url);
	}
//	public static void main(String[] args) {
//		System.out.println(getServletName("/myweb/reg"));
//	}
}
