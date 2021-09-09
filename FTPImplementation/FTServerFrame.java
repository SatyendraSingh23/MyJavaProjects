import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
class RequestProcessor extends Thread
{
	private Socket socket;
	private FTServerFrame frame;
	private String ID;
	public RequestProcessor(Socket socket,String ID,FTServerFrame frame)
	{
		this.ID=ID;
		this.frame=frame;
		try
		{
			this.socket=socket;
			start();
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void run()
	{
		try
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					frame.updateLog("Client connected and ID alloted is "+ID);
				}
			});
			OutputStream os=this.socket.getOutputStream();
			InputStream is=this.socket.getInputStream();
			long fileLength=0;
			byte header[]=new byte[1024];
			byte tmp[]=new byte[1024];
			int i=0;
			int bytesReadCount;
			while(i<1024)
			{
				bytesReadCount=is.read(tmp);
				if(bytesReadCount==-1)continue;
				for(int k=0;k<bytesReadCount;k++)
				{
					header[i]=tmp[k];
					i++;
				}
			}
			i=0;
			int j=1;
			while(header[i]!=',')
			{
				fileLength=fileLength+(header[i]*j);
				i++;
				j=j*10;
			}
			i++;
			StringBuffer sb=new StringBuffer();
			while(i<1024)
			{
				sb.append((char)header[i]);
				i++;
			}
			String fileName=new String();
			fileName=sb.toString().trim();
			String name=fileName;
			long lenOfFile=fileLength;
			SwingUtilities.invokeLater(new Runnable(){
				public void run()
				{
					frame.updateLog("Name : "+name+", Length : "+lenOfFile);
				}
			});
			byte ack[]=new byte[1];
			ack[0]=1;
			os.write(ack,0,1);
			os.flush();
			File file = new File("Downloads"+File.separator+fileName);
			if(file.exists()) file.delete();
			FileOutputStream fos=new FileOutputStream(file);
			long x=0;
			byte bytes[]=new byte[4096];
			while(x<fileLength)
			{
				bytesReadCount=is.read(bytes);
				if(bytesReadCount==-1)continue;
				fos.write(bytes,0,bytesReadCount);
				fos.flush();
				x+=bytesReadCount;
			}
			os.flush();
			fos.close();
			ack[0]=1;
			os.write(ack,0,1);
			SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						frame.updateLog(name+" is uploaded to "+file.getAbsolutePath());
					}
				}
			);
			this.socket.close();
			SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						frame.updateLog("Connection with client whose ID is :"+ID+" closed.");
					}
				}
			);
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
class FTServer extends Thread
{
	private ServerSocket serverSocket;
	private FTServerFrame ftServerFrame;
	public FTServer(FTServerFrame ftServerFrame)
	{
		try
		{
			this.ftServerFrame=ftServerFrame;
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void run()
	{
		try
		{
			serverSocket=new ServerSocket(5500);
			startListening();
		}catch(Exception exception)
		{
			System.out.println(exception);
		}
	}
	public void shutDown()
	{
		try
		{
			serverSocket.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	private void startListening()
	{
		try
		{
			Socket socket;
			RequestProcessor rp;
			while(true)
			{
				System.out.println("Server started.");
				SwingUtilities.invokeLater(new Thread()
					{
						public void run()
						{
							ftServerFrame.updateLog("Server started and is listening on 5500");
						}
					}
				);
				socket=this.serverSocket.accept();
				rp=new RequestProcessor(socket,UUID.randomUUID().toString(),ftServerFrame);
			}
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
class FTServerFrame extends JFrame implements ActionListener
{
	private JButton startButton;
	private JTextArea textArea;
	private JScrollPane jsp;
	private FTServer ftServer;
	private Container container;
	private boolean serverState=false;
	FTServerFrame()
	{
		container=getContentPane();
		container.setLayout(new BorderLayout());
		startButton=new JButton("Start");
		textArea=new JTextArea();
		jsp=new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container.add(startButton,BorderLayout.SOUTH);
		container.add(jsp,BorderLayout.CENTER);
		startButton.addActionListener(this);
		ftServer=new FTServer(this);
		setLocation(100,100);
		setSize(500,500);
		setVisible(true);
	}
	public void updateLog(String message)
	{
		textArea.append(message+"\n");
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(serverState==false)
		{
			ftServer=new FTServer(this);
			ftServer.start();
			serverState=true;
			startButton.setText("STOP");
		}
		else
		{
			ftServer.shutDown();
			serverState=false;
			startButton.setText("start");
			textArea.append("Server stopped\n");
		}
	}
	public static void main(String gg[])
	{
		FTServerFrame ftServerFrame=new FTServerFrame();
	}
}