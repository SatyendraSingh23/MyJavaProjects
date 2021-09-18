package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.net.*;
import java.io.*;
class RequestProcessor extends Thread
{
	private Socket socket;
	private RequestHandlerInterface requestHandler;
	public RequestProcessor(Socket socket,RequestHandlerInterface requestHandler) throws NetworkException
	{
		this.socket=socket;
		this.requestHandler=requestHandler;
		start();
	}
	public void run()
	{
		try
		{
			InputStream is=socket.getInputStream();
			OutputStream os=socket.getOutputStream();
			int bytesToRecieve=1024;
			byte tmp[]=new byte[1024];
			byte header[]=new byte[1024];
			int bytesReadCount;
			int i,j,k;
			i=0;
			j=0;
			while(j<bytesToRecieve)
			{
				bytesReadCount=is.read(tmp);
				if(bytesReadCount==-1)continue;
				for(k=0;k<bytesReadCount;k++)
				{
					header[i]=tmp[k];					
					i++;
				}
				j+=bytesReadCount;
			}
			int requestLength=0;
			i=1;
			j=1023;
			while(j>=0)
			{
				requestLength=requestLength+(header[j]*i);
				i*=10;
				j--;
			}
			byte ack[]=new byte[1];
			ack[0]=1;
			os.write(ack,0,1);
			os.flush();
			byte requestBytes[]=new byte[requestLength];
			bytesToRecieve=requestLength;
			i=0;
			j=0;
			StringBuffer sb=new StringBuffer();
			System.out.println("bytesToRecieve= "+bytesToRecieve);
			while(j<bytesToRecieve)
			{
				bytesReadCount=is.read(tmp);
				if(bytesReadCount==-1)continue;
				for(k=0;k<bytesReadCount;k++)
				{
					requestBytes[i]=tmp[k];
					i++;
				}
				j=j+bytesReadCount;
				
			}
			
			ByteArrayInputStream bais=new ByteArrayInputStream(requestBytes);
			ObjectInputStream ois=new ObjectInputStream(bais);
			
			Request request=(Request)ois.readObject();
			Response response = requestHandler.process(request);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(baos);
			oos.writeObject(response);
			oos.flush();
			byte objectBytes[]=baos.toByteArray();
			int responseLength=objectBytes.length;
			int x;
			i=1023;
			x=responseLength;
			header=new byte[1024];
			while(x>0)
			{
				header[i]=(byte)(x%10);
				x=x/10;
				i--;
			}
			os.write(header,0,1024);
			os.flush();
			System.out.println("Response header sent : "+responseLength);
			while(true)
			{
				bytesReadCount=is.read(ack);
				if(bytesReadCount==-1)continue;
				break;
			}
			System.out.println("Acknowledgement recieved");
			int bytesToSend=responseLength;
			int chunkSize=1024;
			j=0;
			while(j<bytesToSend)
			{
				if((bytesToSend-j)<chunkSize)chunkSize=bytesToSend-j;
				os.write(objectBytes,j,chunkSize);
				os.flush();
				j+=chunkSize;
			}
			System.out.println("Response sent");
			while(true)
			{
				bytesReadCount=is.read(ack);
				if(bytesReadCount==-1)continue;
				break;
			}
			System.out.println("Acknowledgement recieved");
			socket.close();
		}catch(Exception  e)
		{
			System.out.println(e);
		}
	}
}