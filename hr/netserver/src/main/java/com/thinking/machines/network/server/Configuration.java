package com.thinking.machines.network.server;
import java.io.*;
import org.xml.sax.*;
import javax.xml.xpath.*;
import com.thinking.machines.network.common.exceptions.*;
import com.thinking.machines.network.common.*;
import javax.net.*;

class Configuration
{
	private static boolean malformed=false;
	private static boolean fileMissing=false;
	private static int port=-1;
	static
	{
		try
		{
			File file=new File("server.xml");
			if(file.exists())
			{
				InputSource inputSource = new InputSource("server.xml");
				XPath xpath=XPathFactory.newInstance().newXPath();
				String port=xpath.evaluate("//server/@port",inputSource);
				Configuration.port=Integer.parseInt(port);
			}
			else 
			{
				fileMissing=true;
			}
		}catch(Exception exception)
		{
			malformed=true;
		}
	}
	
	public static int getPort() throws NetworkException
	{
		if(fileMissing)throw new NetworkException("server.xml missing,read documentation.");
		if(malformed) throw new NetworkException("server.xml not configured.");
		if(port<0 || port>49151) throw new NetworkException("server.xml contains invalid port number.");
		return port;
	}
}