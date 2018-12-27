package com.webserver.servlets;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ����Servlet�ĳ���
 * @author tedu
 *
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request,HttpResponse response);
	/**
	 * ��ת��ָ��·��
	 * ע��TOMCAT��ʵ�ʸ÷�������ת����������ͨ��request��ȡ��
	 * @param path
	 * @param request
	 * @param response
	 */
	public void forward(String path,HttpRequest request,HttpResponse response){
		response.setEntity(new File("webapps"+path));
	}
}
