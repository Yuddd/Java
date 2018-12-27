package com.webserver.servlets;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class ChangeServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		String usernameinput = request.getParameter("username");
		String passwordold = request.getParameter("passwordold");
		String passwordnew = request.getParameter("passwordnew");
//		System.out.println(usernameinput);
//		System.out.println(passwordold);
//		System.out.println(passwordnew);
		try (RandomAccessFile raf = new RandomAccessFile("user.dat","rw");){
			for(int i=0;i<raf.length()/100;i++){
				raf.seek(100*i);//��ʱָ����0λ
				byte[] data=new byte[32];
				raf.read(data);//��1�ֽڿ�ʼ��32�ֽڵ�32�ֽ�
				String username=new String(data,"UTF-8").trim();
				if(username.equals(usernameinput)){
					//raf.seek(100*i+32);//��ʱָ��Ĭ����32λ!
					raf.read(data);
					String password = new String(data,"UTF-8").trim();
					if(password.equals(passwordold)){
						raf.seek(100*i+32);//��ʱָ��Ĭ����64λ��������32λ
					byte[] data2 = passwordnew.getBytes("UTF-8");//��input2ת��ΪUTF-8�����ʽ����data2��data2λ32�ֽ����飩
					data2=Arrays.copyOf(data2,32);
					raf.write(data2);
					System.out.println("�޸ĳɹ�");
					forward("/myweb/changesuccess.html",request,response);
					//response.setEntity(new File("webapps/myweb/changesuccess.html"));
					break;
					}else{
						System.out.println("�������");
						forward("/myweb/changefail2.html",request,response);
						//response.setEntity(new File("webapps/myweb/changefail2.html"));
						break;
					}
				}
				if(i==(raf.length()/100-1)){
					System.out.println("���޴���");
					forward("/myweb/changefail.html",request,response);
					//response.setEntity(new File("webapps/myweb/changefail.html"));
					break;
				}
			}
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//package com.webserver.servlets;
//
//import java.io.File;
//import java.io.RandomAccessFile;
//import java.util.Arrays;
//
//import com.webserver.http.HttpRequest;
//import com.webserver.http.HttpResponse;
//
///**
// * �޸�����ҵ��
// * @author ta
// *
// */
//public class UpdateServlet {
//	public void service(HttpRequest request,HttpResponse response) {
//		/*
//		 * 1:��ȡ�û���Ϣ
//		 */
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		String newPassword = request.getParameter("newpassword");
//		
//		/*
//		 * 2:�޸�
//		 */
//		try(
//			RandomAccessFile raf
//				= new RandomAccessFile("user.dat","rw");
//		){
//			//ƥ���û�
//			boolean check = false;
//			for(int i=0;i<raf.length()/100;i++) {
//				raf.seek(i*100);
//				//��ȡ�û���
//				byte[] data = new byte[32];
//				raf.read(data);
//				String name = new String(data,"UTF-8").trim();
//				if(name.equals(username)) {
//					check = true;
//					//�ҵ����û���ƥ������
//					raf.read(data);
//					String pwd = new String(data,"UTF-8").trim();
//					if(pwd.equals(password)) {
//						//ƥ���Ϻ��޸�����
//						//1�Ƚ�ָ���ƶ�������λ��
//						raf.seek(i*100+32);
//						//2������������д��
//						data = newPassword.getBytes("UTF-8");
//						data = Arrays.copyOf(data, 32);
//						raf.write(data);
//						//3��Ӧ�޸����ҳ��
//						response.setEntity(new File("webapps/myweb/update_success.html"));
//					}else {
//						//ԭ������������
//						response.setEntity(new File("webapps/myweb/update_fail.html"));
//					}
//					break;
//				}
//			}
//			if(!check) {
//				//û�д���
//				response.setEntity(new File("webapps/myweb/no_user.html"));
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//}



