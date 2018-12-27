package com.webserver.core;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlets.HttpServlet;

/**
 * ����ͻ�������
 * @author tedu
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	public void run(){
		try {
			/*
			 * �����̣�
			 * 1����������
			 * 2����������
			 * 3��������Ӧ
			 */
			//1׼������
			//1.1��������,�����������
			HttpRequest request = new HttpRequest(socket);
			//1.2������Ӧ����
			HttpResponse response = new HttpResponse(socket);
			//2��������
			//2.1:��ȡ�������Դ·��
			String url = request.getRequestURI();
			Map<String,String> map = new HashMap<String,String>();
			
			//�жϸ������Ƿ�Ϊ����ҵ��
			//if(map.containsKey(url)){
				String servletName = ServerContext.getServletName(url);
				if(servletName!=null){
				System.out.println("ClientHandler:���ڼ�����Դ"+servletName);
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
				}
		//	}
		else{
			System.out.println(url);
			//2.2:������Դ·��ȥwebappsĿ¼��Ѱ�Ҹ���Դ
			File file = new File("webapps"+url);
			System.out.println(file.exists());
			
			if(file.exists()){
				System.out.println("�ҵ�����Դ");
				//��Ӧ����������Ҫ��Ӧ����Դ����
				response.setEntity(file);
				//����״̬��
				
				
				//������Ӧͷ
				
				
				//������Ӧ����
				
			}else{
				//����״̬����404
				response.setStatusCode(404);
				//����404ҳ��
				response.setEntity(new File("webapps/root/404.html"));
				System.out.println("��Դ������");
			}
			}
			//3��Ӧ�ͻ���
			response.flush();
		} catch(EmptyRequestException e){
			/*
			 * ʵ����HttpRequestʱ�������ǿ�����ʱ�ù��췽���Ὣ���쳣�׳������ﲻ���κδ���
			 * ֱ����finally����ͻ��˶Ͽ����ɡ�
			 */
			System.out.println("������");
			//http://localhost:8088/myweb/index.html
			//InputStream in=socket.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//��ͻ��˶Ͽ�����
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
