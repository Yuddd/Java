package com.webserver.servlets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		String username1 = request.getParameter("username");
		String password1 = request.getParameter("password");
//		System.out.println("������û���"+username1);
//		System.out.println("���������"+password1);
		Map<String,String> map = new HashMap<String,String>();
		try (RandomAccessFile raf=new RandomAccessFile("user.dat","rw");){
			for(int i=0;i<raf.length()/100;i++){
				//���û���
				//1�ȶ�ȡ32�ֽ�
				byte[] data=new byte[32];//32�ֽ�����
				raf.read(data);
				//2���ֽ�����ת��Ϊ�ַ���
				String username=new String(data, "UTF-8").trim();//.trim()����ȡ������(�հ��ֽ�)
				
				//��ȡ����
				raf.read(data);//��33�ֽڿ�ʼ��32�ֽڵ�64
				String password=new String(data,"UTF-8").trim();
				
				//���ǳ�
				raf.read(data);//��65�ֽڿ�ʼ��32�ֽڵ�96
				String nickname=new String(data,"UTF-8").trim();
				
				//������
				int age=raf.readInt();
//				System.out.println(username+","+password+","+nickname+","+age);
				map.put(username, password);
			}
			Set<Entry<String,String>> entrySet=map.entrySet();
			boolean flag = false;
			for(Entry<String,String> e:entrySet){
				String key = e.getKey();
				String value = e.getValue();
//				System.out.println(key+"������"+value);
				if(key.equals(username1)&&value.equals(password1)){
					forward("/myweb/login_success.html",request,response);
				//	response.setEntity(new File("webapps/myweb/login_success.html"));
					flag = true;
				}
				if(key.equals(username1)&&!value.equals(password1)){
					forward("/myweb/login_fail2.html",request,response);
				//	response.setEntity(new File("webapps/myweb/login_fail2.html"));
					flag = true;
				}
			}
			System.out.println("flag:"+flag);
			if(!flag){
				forward("/myweb/login_fail.html",request,response);
			//	response.setEntity(new File("webapps/myweb/login_fail.html"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//package com.webserver.servlets;
//
//import java.io.File;
//import java.io.RandomAccessFile;
//import com.webserver.http.HttpRequest;
//import com.webserver.http.HttpResponse;
//
//public class LoginServlet {
//	public void service(HttpRequest request,HttpResponse response){
//		System.out.println("��ʼ�����¼ҵ��");
//		
//		//1
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		
//		System.out.println(username);
//		System.out.println(password);
//		
//		try (
//				RandomAccessFile raf = new RandomAccessFile("user.dat","r");
//			){
//				boolean flag = false;
//				for(int i=1;i<=raf.length()/100;i++){
//					byte[] data = new byte[32];
//					raf.read(data);
//					String readusername = new String(data,"UTF-8").trim();
//					
//					raf.read(data);
//					String readpassword = new String(data,"UTF-8").trim();
//					
//					raf.seek(100*i);
//					
//					if(username.equals(readusername)){
//						if(password.equals(readpassword)){
//							//3��Ӧ�ͻ��˵�¼�ɹ�ҳ��
//							System.out.println("��½�ɹ�");
//							flag = true;
//							response.setEntity(new File("webapps/myweb/login_sucess.html"));
//							break;
//						}
//					}
//				}
//				if(!flag){
//					System.out.println("��¼ʧ��");
//					response.setEntity(new File("webapps/myweb/login_fail.html"));
//				}
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		
//	}
//}