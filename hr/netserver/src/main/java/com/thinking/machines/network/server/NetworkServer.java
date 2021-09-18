package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.net.*;
public class NetworkServer
{
	private RequestHandlerInterface requestHandler;
	public NetworkServer(RequestHandlerInterface requestHandler) throws NetworkException
	{
		if(requestHandler==null)
		{
			throw new NetworkException("Request handler missing.");
		}
		this.requestHandler=requestHandler;
	}
	public void start() throws NetworkException// will sun on main thread
	{
		ServerSocket serverSocket=null;
		int port=Configuration.getPort();
		try
		{
			serverSocket=new ServerSocket(port);
		}catch(Exception exceptions)
		{
			throw new NetworkException("Unable to start server on port : "+port);
		}
		try
		{
			Socket socket;
			RequestProcessor rp;
			while(true)
			{
				System.out.println("Server is ready to accept on port : "+port);
				socket=serverSocket.accept();
				rp=new RequestProcessor(socket,requestHandler);
			}
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
}