package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.webserver.core.EmptyRequestException;

/**
 * �������
 * ÿ��ʵ����ʾ�ͻ��˷��͹�����һ����������
 * @author tedu
 *
 */
public class HttpRequest {
	/*
	 * �����������Ϣ����
	 */
	//����ʽ
	private String method;
	//��Դ·��
	private String url;
	//Э��汾
	private String protocol;
	//url�е����󲿷�
	private String requestURI;
	//url�еĲ�������
	private String queryString;
	//ÿ������
	private Map<String,String> parameters = new HashMap<String,String>();
	/*
	 * ��Ϣͷ�����Ϣ����
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	/*
	 * ��Ϣ���������Ϣ����
	 */
	
	//�ͻ������������Ϣ
	private Socket socket;
	private InputStream in;
	
	/**
	 * ��ʼ������
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException{
		try {
			this.socket=socket;
			this.in=socket.getInputStream();
			/*
			 * ��������
			 * 1������������
			 * 2��������Ϣͷ
			 * 3��������Ϣ����
			 */
			parseRequestLine();
			parseHeaders();
			parseContent();
		} catch(EmptyRequestException e){
			throw e;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void parseRequestLine() throws EmptyRequestException{
		System.out.println("��ʼ����������...");
		try {
			String line=readLine();
			System.out.println("������:"+line);
			/*
			 * �������н��в�֣���ÿ�������� ��Ӧ�����õ������ϡ�
			 * 
			 * �����ں������й����п��ܻ���������±�Խ����������������HTTPЭ�����пͻ��˷���һ�������������
			 * ����ʱͨ���ո��ֺ��ǵò����������ݵġ�
			 */
			String[] str=line.split(" ");//   line.split("\\s")
			if(str.length!=3){
				//������
				throw new EmptyRequestException();
			}
			method=str[0];
			url=str[1];
			//��һ������URL
			parseURL();
			protocol=str[2];
			System.out.println("method:"+method);
			System.out.println("url:"+url);
			System.out.println("protocol:"+protocol);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("�����н�����ϣ�");
	}
	/**
	 * ��һ������URL
	 * url�п��ܻ������ָ�ʽ���������Ͳ�������
	 * 1�����������磺
	 *  /myweb/index.html
	 *  2,�������磺
	 *  myweb/reg?username=yuxiansheng&password=123456&age=men&nickname=aixinjueluo
	 * @throws IOException 
	 */
	private void parseURL() throws IOException{
		/*
		 * �����жϵ�ǰurl�Ƿ��в������жϵ������ǿ�url�Ƿ���"?",��������Ϊ���url�ǰ��������ģ�
		 * ����ֱ�ӽ�url��ֵ��requestURI���ɡ�
		 * 
		 * 
		 * ���в�����
		 * 1����url����"?"���Ϊ�����֣���һ����Ϊ���󲿷֣���ֵ��requestURI
		 * �ڶ�����Ϊ�������֣���ֵ��queryString
		 * 
		 * 2���ٶ�queryString��һ����֣��Ȱ���"&"��ֳ�ÿ���������ٽ�ÿ����������"="���Ϊ�����������ֵ��
		 * ������parameters���Map�С�
		 * 
		 * ����������Ҫע��url�ļ����ر������
		 * 1��url���ܺ���"?"����û�в�������
		 * �磺
		 *  /myweb/reg?
		 *  
		 *  2:���������п���ֻ�в�����û�в���ֵ
		 *  �磺
		 *  /myweb/reg?username=&password=123456&age=men&nickname=aixinjueluo
		 * 
		 */
//		System.out.println("url111111"+url);
//		String[] str = url.split("\\?");
//		if(str.length==2){
//			System.out.println("�в�");
//			requestURI = str[0];
//			queryString = str[1];
//			String[] str1 = queryString.split("&");
//			for(int i =0 ;i<str1.length;i++){
//				String[] map = str1[i].split("=");
//				parameters.put(map[0],map[1]);
//			}
//			
//		}else {
//			requestURI = str[0];
//			queryString ="";
//		}
		if(url.indexOf("?")!=-1){
			//���գ����
			String[] data = url.split("\\?");
			requestURI = data[0];
			//�жϣ������Ƿ��в���
			if(data.length>1){
				queryString =data[1];
				//��һ��������������
				parseParameter(queryString);
			}
			}else{
			//�����У�
			requestURI =url;
		}
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
	}
	private void parseHeaders(){
		System.out.println("��ʼ������Ϣͷ...");
		try {
			/*
			 * ������Ϣͷ�����̣�
			 * ѭ������readLine��������ȡÿһ����Ϣͷ
			 * ��readLine��������ֵΪ���ַ���ʱֹͣѭ��
			 * (��Ϊ���ؿ��ַ���˵��������ȡ��CRLF��������Ϊ��Ϣͷ�����ı�־)
			 * �ڶ�ȡ��ÿ����Ϣͷ�󣬸��ݡ�����(ð�ſո�)���в�֣�������Ϣͷ��������Ϊkey��
			 * ��Ϣͷ��Ӧ��ֵ��Ϊvalue���浽����headers���Map����ɽ�������
			 */
			while(true){
				String line=readLine();
				//System.out.println("��Ϣͷ:"+line);
				if("".equals(line)){
					break;
				}			
				String[] str=line.split(":\\s");
				String key = str[0];
				String value = str[1];
				headers.put(str[0],str[1]);
//				System.out.println("key:"+key);
//				System.out.println("value:"+value); 
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("headers:"+headers);
		System.out.println("��Ϣͷ������ϣ�");
	}
	private void parseContent(){
		System.out.println("��ʼ������Ϣ����...");
		/*
		 * ������Ϣͷ�Ƿ���Content-Length�����������Ƿ�����Ϣ����
		 */
		
		try {
			if(headers.containsKey("Content-Length")){
				//������Ϣ����
				int length = Integer.parseInt(headers.get("Content-Length"));
				//��ȡ��Ϣ��������
				byte[] data = new byte[length];
					in.read(data);
					
					/*
					 * ������ϢͷContent-Tpye�жϸ���Ϣ���ĵ���������
					 */
					String contentType = headers.get("Content-Type");
					//�ж��Ƿ�Ϊform���ύ����
					if("application/x-www-form-urlencoded".equals(contentType)){
						/*
						 * �����������൱��ԭGET�����ַ����url��"?"�Ҳ�����
						 */
						String line = new String(data,"ISO8859-1");
						System.out.println("�������ݣ�"+line);
						parseParameter(line);
					}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		System.out.println("��Ϣ���Ľ�����ϣ�");
	}
	/**
	 * ��������
	 * ��ʽ��name=value&name=value&...
	 * @param line
	 */
	private void parseParameter(String line){
		/*
		 * �Ƚ������е�"%XX"�����ݰ��ն�Ӧ�ַ���(�����ͨ����UTF-8����ԭΪ��Ӧ����
		 */
		try {
			/*
			 * URLDecoder��decode�������Խ��������ַ����е�"%XX"����תΪ��Ӧ2�����ֽ�
			 * Ȼ���ո������ַ�������Щ�ֽڻ�ԭΪ��Ӧ�ַ����滻��Щ"%XX"���֣�Ȼ�󽫻��õ��ַ������ء�
			 * username=%E4%BD%99%E5%A4%9A&password=123456&nickname=%E4%BD%99%E5%85%88%E7%94%9F&age=2
			 * ת����Ϻ�Ϊ��
			 * username=���&password=123456&nickname=������&age=2
			 */
			System.out.println("�Բ���ת��ǰ��"+line);
			line = URLDecoder.decode(line, "UTF-8");
			System.out.println("�Բ���ת���"+line);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//����&���ÿһ������
		String[] paraArr = line.split("\\&");
		//����ÿ���������в��
		for(String para:paraArr){
			//�ٰ���"="���ÿ������
			String[] paras = para.split("=");
			if(paras.length>1){
				//�ò�����ֵ
				parameters.put(paras[0], paras[1]);
			}else{
				//û��ֵ
				parameters.put(paras[0],null);
			}
			
		}
	}
	/**
	 * ��ȡһ���ַ�������������ȡCR��LFʱֹͣ
	 * ����֮ǰ��������һ���ַ�����ʽ���ء�
	 * @return
	 * @throws IOException
	 */
	private String readLine() throws IOException{
		StringBuilder builder = new StringBuilder();
		//���ζ�ȡ���ֽ�
		int d = -1;
		//c1��ʾ�ϴζ�ȡ���ַ���c2��ʾ���ζ�ȡ���ַ�
		char c1='a';
		char c2='a';
		while((d=in.read())!=-1){
			c2=(char)d;
			if(c1==HttpContext.CR&&c2==HttpContext.LF){
				break;
			}
			builder.append(c2);
			c1 = c2;
		}
		return builder.toString().trim();
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	/**
	 * ���ݸ�������Ϣͷ�����ֻ�ȡ��Ӧ��Ϣͷ��ֵ
	 * @param name
	 * @return
	 */
	public String getHeader(String name){
		return headers.get(name);
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return parameters.get(name);
	}

}














