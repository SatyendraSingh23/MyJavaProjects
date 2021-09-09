import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
class FileUploadEvent
{
	private String uploaderID;
	private File file;
	private long numberOfBytesUploaded;
	public FileUploadEvent()
	{
		this.uploaderID=null;
		this.file=null;
		this.numberOfBytesUploaded=0;
	}
	public void setUploaderId(String uploaderID)
	{
		this.uploaderID=uploaderID;
	}
	public String getUploaderID()
	{
		return this.uploaderID;
	}
	public void setFile(File file)
	{
		this.file=file;
	}
	public File getFile()
	{
		return this.file;
	}
	public void setNumberOfBytesUploaded(long numberOfBytesUploaded)
	{
		this.numberOfBytesUploaded=numberOfBytesUploaded;
	}
	public long getNumberOfBytesUploaded()
	{
		return this.numberOfBytesUploaded;
	}
}
interface FileUploadListener
{
	public void fileUploadStatusChanged(FileUploadEvent fileUploadEvent);
}
class FileModel extends AbstractTableModel
{
	private ArrayList<File> files;
	FileModel()
	{
		this.files=new ArrayList<>();
	}
	public int getRowCount()
	{
		return this.files.size();
	}
	public int getColumnCount()
	{
		return 2;
	}
	public String getColumnName(int c)
	{
		if(c==0) return "S.No.";
		return "Files";
	}
	public Class getColumnClass(int c)
	{
		if(c==0)return Integer.class;
		return String.class;
	}
	public boolean isCellEditable(int r,int c)
	{
		return false;
	}
	public Object getValueAt(int r,int c)
	{
		if(c==0)return (r+1);
		return this.files.get(r).getAbsolutePath();
	}
	public void add(File file)
	{
		this.files.add(file);
		fireTableDataChanged();
	}
	public ArrayList<File> getFiles()
	{
		return this.files;
	}
}
class FTClientFrame extends JFrame 
{
	private Container container;
	private String host;
	private int portNumber;
	private FileSelectionPanel fileSelectionPanel;
	private	FileUploadViewPanel fileUploadViewPanel;
	FTClientFrame(String host, int portNumber)
	{
		this.host=host;
		this.portNumber=portNumber;
		fileSelectionPanel=new FileSelectionPanel();
		fileUploadViewPanel=new FileUploadViewPanel();
		container=getContentPane();
		container.setLayout(new GridLayout(1,2));
		container.add(fileSelectionPanel);
		container.add(fileUploadViewPanel);
		setSize(800,500);
		setLocation(100,100);
		setVisible(true);
	}
	class FileSelectionPanel extends JPanel implements ActionListener
	{
		private JLabel titleLabel;
		private JButton addButton;
		private JTable table;
		private FileModel model;
		private JScrollPane jsp;
		FileSelectionPanel()
		{
			setLayout(new BorderLayout());
			titleLabel=new JLabel("Selected files.");
			addButton=new JButton("Add");
			model=new FileModel();
			table=new JTable(model);
			jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			addButton.addActionListener(this);
			add(titleLabel,BorderLayout.NORTH);
			add(jsp,BorderLayout.CENTER);
			add(addButton,BorderLayout.SOUTH);
		}
		public ArrayList<File> getFiles()
		{
			return model.getFiles();
		}
		public void actionPerformed(ActionEvent event)
		{
			JFileChooser jfc=new JFileChooser();
			jfc.setCurrentDirectory(new File("."));
			int selectedOption=jfc.showOpenDialog(this);
			if(selectedOption==jfc.APPROVE_OPTION)
			{
				File selectedFile=jfc.getSelectedFile();
				model.add(selectedFile);
			}
		}
	}
	class FileUploadViewPanel extends JPanel implements ActionListener,FileUploadListener
	{
		private JButton uploadFilesButton;
		private JPanel progressPanelsContainer;
		private JScrollPane jsp;
		private ArrayList<ProgressPanel>progressPanels;
		ArrayList<File> files;
		ArrayList<FileUploadThread> fileUploaders;
		FileUploadViewPanel()
		{
			uploadFilesButton=new JButton("Upload File");
			setLayout(new BorderLayout());
			add(uploadFilesButton,BorderLayout.NORTH);
			uploadFilesButton.addActionListener(this);
		}
		public void actionPerformed(ActionEvent e)
		{
			files=fileSelectionPanel.getFiles();
			if(files.size()==0)
			{
				JOptionPane.showMessageDialog(FTClientFrame.this,"No files selected to upload.");
				return;
			}
			progressPanelsContainer=new JPanel();
			progressPanelsContainer.setLayout(new GridLayout(files.size(),1));
			ProgressPanel progressPanel;
			progressPanels=new ArrayList<>();
			fileUploaders=new ArrayList<>();
			FileUploadThread fut;
			String uploaderID;
			for(File file:files)
			{
				uploaderID=UUID.randomUUID().toString();
				progressPanel=new ProgressPanel(uploaderID,file);
				progressPanels.add(progressPanel);
				progressPanelsContainer.add(progressPanel);
				fut=new FileUploadThread(this,uploaderID,file,host,portNumber);
				fileUploaders.add(fut);
			}
			jsp=new JScrollPane(progressPanelsContainer,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			add(jsp,BorderLayout.CENTER);
			this.revalidate();
			this.repaint();
			for(FileUploadThread fileUploadThread : fileUploaders)
			{
				fileUploadThread.start();
			}
		}
		public void fileUploadStatusChanged(FileUploadEvent fileUploadEvent)
		{
			String uploaderID=fileUploadEvent.getUploaderID();
			long numberOfBytesUploaded=fileUploadEvent.getNumberOfBytesUploaded();
			File file=fileUploadEvent.getFile();
			for(ProgressPanel pp:progressPanels)
			{
				if(pp.getId().equals(uploaderID))
				{
					pp.updateProgressBar(numberOfBytesUploaded);
					break;
				}
			}
		}
		class ProgressPanel extends JPanel
		{
			private String id;
			private File file;
			private JLabel fileNameLabel;
			private JProgressBar progressBar;
			private long fileLength;
			public ProgressPanel(String id,File file)
			{
				this.id=id;
				this.file=file;
				this.fileLength=file.length();
				fileNameLabel=new JLabel("Uploading : "+file.getName());
				progressBar=new JProgressBar(1,100);
				setLayout(new GridLayout(2,1));
				add(fileNameLabel);
				add(progressBar);
			}
			public String getId()
			{
				return this.id;
			}
			public void updateProgressBar(long bytesUploaded)
			{
				int percentage;
				if(bytesUploaded==fileLength)percentage=100;
				else	percentage=(int)((bytesUploaded*100)/fileLength);
				progressBar.setValue(percentage);
				if(percentage==100)
				{
					fileNameLabel.setText("Uploaded : "+file.getName());
				}
			}
		}// progress panel endss
	}// File selection panels ends
	public static void main(String gg[])
	{
		FTClientFrame frame=new FTClientFrame("localhost",5500);
	}
}

class FileUploadThread extends Thread
{
	private String id;
	private String host;
	private int portNumber;
	private File file;
	private FileUploadListener fileUploadListener;
	FileUploadThread(FileUploadListener fileUploadListener,String id,File file,String host,int portNumber)
	{
		this.fileUploadListener=fileUploadListener;
		this.id=id;
		this.portNumber=portNumber;
		this.file=file;
		this.host=host;
	}
	public void run()
	{
		try
		{			
			long fileLength=file.length();
			String name=file.getName();
			if(file.isDirectory())
			{
				System.out.println(name+" is a directory.");
				return;
			}
			byte header[]=new byte[1024];
			int i=0; 
			long x=fileLength;
			while(x>0)
			{
				header[i]=(byte)(x%10);
				i++;
				x/=10;
			}
			header[i]=(byte)',';
			i++;
			int j=0;
			int fileNameLength=name.length();
			while(j<fileNameLength)
			{
				header[i]=(byte)name.charAt(j);
				j++;
				i++;
			}
			while(i<1024)
			{
				header[i]=(byte)32;
				i++;
			}
			Socket socket=new Socket(host,portNumber);
			OutputStream os=socket.getOutputStream();
			InputStream  is=socket.getInputStream();
			os.write(header,0,1024);
			os.flush();
			int bytesToSend=4096;
			int bytesReadCount;
			byte ack[]=new byte[1];
			while(true)
			{
				bytesReadCount=is.read(ack);
				if(bytesReadCount==-1)continue;
				break;
			}
			byte bytes[]=new byte[bytesToSend];
			x=0;
			FileInputStream fis=new FileInputStream(file);
			
			while(x<fileLength)
			{
				bytesReadCount=fis.read(bytes);
				os.write(bytes,0,bytesReadCount);
				os.flush();
				x+=bytesReadCount;
				long brc=x;
				SwingUtilities.invokeLater(()->
				{
						FileUploadEvent fue=new FileUploadEvent();
						fue.setUploaderId(id);
						fue.setFile(file);
						fue.setNumberOfBytesUploaded(brc);
						fileUploadListener.fileUploadStatusChanged(fue);
				});
			}
			fis.close();
			while(true)
			{
				bytesReadCount=is.read(ack);
				if(bytesReadCount==-1)continue;
				break;
			}
			System.out.println("File uploaded.");
			socket.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}		
	}
}