package com.webserver.http;

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
 * HttpЭ��������ݶ���
 * @author tedu
 *
 */
public class HttpContext {
	/**
	 * �س���CR
	 */
	public static final int CR = 13;
	/**
	 * ���з�LF
	 */
	public static final int LF = 10;
	
	/*
	 * ״̬�������Ӧ״̬����
	 * key:״̬����
	 * value:״̬����
	 */
	private static Map<Integer,String> Status_code_reason_mapping = new HashMap<Integer,String>();
	
	/*
	 * ��������ӳ��
	 * key����Դ��׺��
	 * value����������(Content-Type��Ӧ��ֵ)
	 */
	private static Map<String,String> mime_mapping = new HashMap<String,String>();
	//��̬��
	static{
		//��ʼ����̬��Ա
		initStatusMapping();
		try {
			initMimeMapping();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ���ʻ���������
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	private static void initMimeMapping() throws FileNotFoundException, DocumentException {
//		mime_mapping.put("html","text/html");
//		mime_mapping.put("png","image/png");
//		mime_mapping.put("gif","image/gif");
//		mime_mapping.put("jpg","image/jpeg");
//		mime_mapping.put("css","text/css");
//		mime_mapping.put("js","application/javascript");
		/*
		 * ����conf/web.xml�ļ���������ǩ������Ϊ<mime-mapping>���ӱ�ǩ��ȡ����
		 * �����ñ�ǩ�е��ӱ�ǩ<mime-type>�м���ı���Ϊvalue���浽mime_mapping���Map����ɳ�ʼ��������
		 */
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new FileInputStream("conf/web.xml"));
		Element root = doc.getRootElement();
		List<Element> empList = root.elements("mime-mapping");
		//System.out.println(empList.size());
		for(Element str: empList){
			String name = str.element("extension").getText();
			String value = str.element("mime-type").getText();			
			mime_mapping.put(name,value);
		}
		
	}
	/**
	 * ��ʼ��״̬�������Ӧ����
	 */
	private static void initStatusMapping(){
		Status_code_reason_mapping.put(200,"OK");
		Status_code_reason_mapping.put(201,"Created");
		Status_code_reason_mapping.put(202,"Accepted");
		Status_code_reason_mapping.put(204,"No Content");
		Status_code_reason_mapping.put(301,"Moved Permanently");
		Status_code_reason_mapping.put(302,"Moved Temporarily");
		Status_code_reason_mapping.put(304,"Not Modified");
		Status_code_reason_mapping.put(400,"Bad Request");
		Status_code_reason_mapping.put(401,"Unauthorized");
		Status_code_reason_mapping.put(403,"Forbidden");
		Status_code_reason_mapping.put(404,"Not Found");
		Status_code_reason_mapping.put(500,"Internal Server Error");
		Status_code_reason_mapping.put(501,"Not Implemented");
		Status_code_reason_mapping.put(502,"Bad Gateway");
		Status_code_reason_mapping.put(503,"Service Unavailable");		  
	}
	/**
	 * ����״̬�����ȡ��Ӧ��״̬����
	 * @param code
	 * @return
	 */
	public static String getStatusReason(int code){
		return Status_code_reason_mapping.get(code);
	}
	public static String getMimeType(String ext){
		return mime_mapping.get(ext);
	}
	public static void main(String[] args) {
//		String reason = getStatusReason(500);
		String reason = getMimeType("mp4");
		System.out.println(reason);
	}
	
}

















