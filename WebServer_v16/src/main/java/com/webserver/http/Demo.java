package com.webserver.http;

public class Demo {
	public static void main(String[] args) {
		//获取文件的后缀部分
		String fileName = "logo.png";
//		int index = fileName.lastIndexOf(".");
//		String d = fileName.substring(index+1);
		String[] str = fileName.split("\\.");
		String a = str[str.length-1];
		System.out.println(a);
		HttpContext context = new HttpContext();
		String b = context.getMimeType(a);
		System.out.println(b);
	}
}
