package com.webserver.servlets;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ����ע��ҵ��
 * @author tedu
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){//�̳������д���󷽷�
		/*
		 * ע���������:
		 * 1:��ȡ�û��ύ��ע����Ϣ
		 * 2:��ע����Ϣд���ļ�user.dat
		 * 3:��Ӧ�ͻ���ע��ɹ���ҳ��
		 * 
		 * 1
		 * ͨ��request.getParameter()������ȡ�û��ύ����������ʱ�����ݵĲ�������ַ�����ֵ
		 * Ӧ����ҳ����from�����Ӧ������������(name���Ե�ֵ)
		 */
		System.out.println("��ʼ����ע��ҵ�񣡣���");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
//		System.out.println("username:"+username);
//		System.out.println("password:"+password);
//		System.out.println("nickname:"+nickname);
//		System.out.println("age:"+age);
		
		/*
		 * 2
		 * ÿ����¼ռ100�ֽڣ������û��������룬�ǳ�Ϊ�ַ�������ռ32�ֽ�
		 * ����Ϊintֵռ4�ֽڡ�д�뵽user.dat�ļ���
		 */
		try (
				RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
				){
			   //�Ƚ�ָ���ƶ����ļ�ĩβ
			raf.seek(raf.length());
			//д�û���
			//1.�Ƚ��û���ת�ɶ�Ӧ��һ���ֽ�
			byte[] data=username.getBytes("UTF-8");
			//2.������������Ϊ32�ֽ�
			data=Arrays.copyOf(data, 32);
			//3.�����ֽ�����һ����д���ļ�
			raf.write(data);
			//д����
			data=password.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//д����
			//д�ǳ�
			data=nickname.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			raf.writeInt(age);
			System.out.println("ע����ϣ�");
			//3��Ӧ�ͻ���ע��ɹ�ҳ�� 
			forward("/myweb/reg_success.html",request,response);
			//response.setEntity(new File("webapps/myweb/reg_success.html"));
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
