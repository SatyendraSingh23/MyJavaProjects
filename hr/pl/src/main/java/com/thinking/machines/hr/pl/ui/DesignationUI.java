package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.table.*;
import javax.swing.event.*; 
import javax.swing.JFileChooser.*;
public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private JLabel titleLabel;
private JLabel searchLabel;
private JLabel searchErrorLabel;
private JTextField searchTextField;
private JButton clearSearchTextFieldButton;
private JTable designationTable;
private JScrollPane scrollPane;
private DesignationModel designationModel;
private Container container;
private DesignationPanel designationPanel;
private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF};
private MODE mode;
private ImageIcon logoIcon;
private ImageIcon addIcon;
private ImageIcon editIcon;
private ImageIcon deleteIcon;
private ImageIcon clearIcon;
private ImageIcon cancelIcon;
private ImageIcon updateIcon;
private ImageIcon pdfIcon;
private ImageIcon saveIcon;
private ImageIcon designationIcon;
private ImageIcon employeeIcon;
private JButton designationButton;
private JButton employeeButton;
private ImageIcon searchIcon;
public DesignationUI()
{
initComponents();
setAppearance();
addListeners();
setViewMode();
designationPanel.setViewMode();
}
private void initComponents()
{
designationModel=new DesignationModel();
titleLabel=new JLabel("Designations");
searchLabel=new JLabel("Search");
searchErrorLabel=new JLabel("");
searchTextField=new JTextField();
designationTable=new JTable(designationModel);
scrollPane=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container = getContentPane();
logoIcon = new ImageIcon(this.getClass().getResource("/Icons/Logo.png"));
designationIcon=new ImageIcon("Designations");
employeeIcon=new ImageIcon("E");
addIcon=new ImageIcon(this.getClass().getResource("/Icons/addIcon.png"));
editIcon=new ImageIcon(this.getClass().getResource("/Icons/editIcon.png"));
updateIcon=new ImageIcon(this.getClass().getResource("/Icons/updateIcon.png"));
deleteIcon=new ImageIcon(this.getClass().getResource("/Icons/deleteIcon.png"));
saveIcon=new ImageIcon(this.getClass().getResource("/Icons/saveIcon.png"));
clearIcon=new ImageIcon(this.getClass().getResource("/Icons/clearIcon.png"));
pdfIcon=new ImageIcon(this.getClass().getResource("/Icons/pdfIcon.png"));
searchIcon=new ImageIcon(this.getClass().getResource("/Icons/searchIcon.png"));
setIconImage(logoIcon.getImage());
cancelIcon=new ImageIcon(this.getClass().getResource("/Icons/cancelIcon.png"));
setIconImage(logoIcon.getImage());
clearSearchTextFieldButton=new JButton(clearIcon);
}
private void setAppearance()
{
Font titleFont = new Font("Verdana",Font.BOLD,18);
Font columnFont = new Font("Verdana",Font.BOLD,16);
Font captionFont = new Font("Verdana",Font.BOLD,16);
Font dataFont = new Font("Verdana",Font.PLAIN,16);
Font searchErrorFont = new Font("Verdana",Font.BOLD,12);
titleLabel.setFont(titleFont);
searchLabel.setFont(captionFont);
searchTextField.setFont(dataFont);
titleLabel.setFont(titleFont);
searchErrorLabel.setFont(searchErrorFont);
searchErrorLabel.setForeground(Color.red);
designationTable.setFont(dataFont);
designationTable.setRowHeight(30);
designationTable.getColumnModel().getColumn(0).setPreferredWidth(10);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(400);
designationTable.getColumnModel().getColumn(2).setPreferredWidth(10);
JTableHeader header=designationTable.getTableHeader();
header.setFont(columnFont);
designationTable.setRowSelectionAllowed(true);
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
container.setLayout(null);
header.setReorderingAllowed(false);
header.setResizingAllowed(false);
designationPanel=new DesignationPanel();
int lm,tm;
lm=0; 
tm=0;
designationButton=new JButton(designationIcon);
employeeButton=new JButton(employeeIcon);
employeeButton.setBounds(lm+10,tm+20,100,20);
searchErrorLabel.setBounds(lm+10+100+400+10-55,tm+20+20,100,20);
titleLabel.setBounds(lm+10,tm+10,200,40);
searchLabel.setBounds(lm+10,tm+10+40+10,100,30);
searchTextField.setBounds(lm+10+100+20,tm+10+40+10,400,30);
clearSearchTextFieldButton.setBounds(lm+20+100+400+10,tm+10+40+10,40,30);
scrollPane.setBounds(lm+10,tm+10+40+10+30+10,570,200);
designationPanel.setBounds(lm+10,tm+10+40+10+30+10+200+10,565,200);
container.add(employeeButton);
container.add(titleLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(scrollPane);
container.add(clearSearchTextFieldButton);
container.add(designationPanel);
container.add(searchErrorLabel);
int w,h;
w=600;
h=700;
setSize(w,h);
Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
setLocation((d.width/2)-(w/2),(d.height/2-h/2));
}
private void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
clearSearchTextFieldButton.addActionListener(new ActionListener()
{
	public void actionPerformed(ActionEvent ev)
	{
		searchTextField.setText("");
		searchTextField.requestFocus();
	}
}
);
designationTable.getSelectionModel().addListSelectionListener(this);
}
private void searchDesignation()
{
	searchErrorLabel.setText("");
	String title=searchTextField.getText().trim();
	System.out.println("Title : "+title);
	if(title.length()==0)return;
	int rowIndex=0;
	try
	{	
		rowIndex=designationModel.indexOfTitle(title,true);
	}catch(Exception e)
	{
		searchErrorLabel.setText("Not found");
		return;
	}
	designationTable.setRowSelectionInterval(rowIndex,rowIndex);
	Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
	designationTable.scrollRectToVisible(rectangle);
}
public void changedUpdate(DocumentEvent e)
{
	searchDesignation();
}
public void removeUpdate(DocumentEvent e)
{
	searchDesignation();
}
public void insertUpdate(DocumentEvent e)
{
	searchDesignation();
}
public void valueChanged(ListSelectionEvent ev)
{
	int selectedRowIndex=designationTable.getSelectedRow();
	try
	{
		DesignationInterface designation=designationModel.getDesignationAt(selectedRowIndex);
		designationPanel.setDesignation(designation);
	}catch(BLException blException)
	{
		designationPanel.clearDesignation();
	}
}
private void setViewMode()
{
	this.mode=MODE.VIEW;
	if(designationModel.getRowCount()==0)
	{
		searchTextField.setEnabled(true);
		clearSearchTextFieldButton.setEnabled(true);
		designationTable.setEnabled(false);
	}
	else
	{
		searchTextField.setEnabled(true);
		clearSearchTextFieldButton.setEnabled(true);
		designationTable.setEnabled(true);
	}
}		
private void setAddMode()
{
	this.mode=MODE.ADD;
	searchTextField.setEnabled(true);
	clearSearchTextFieldButton.setEnabled(true);
	designationTable.setEnabled(false);
}
private void setEditMode()
{
	this.mode=MODE.EDIT;
	searchTextField.setEnabled(true);
	clearSearchTextFieldButton.setEnabled(true);
	designationTable.setEnabled(false);
}
private void setDeleteMode()
{
	this.mode=MODE.DELETE;
	searchTextField.setEnabled(true);
	clearSearchTextFieldButton.setEnabled(true);
	designationTable.setEnabled(false);
}
private void setExportToPDFMode()
{
	this.mode=MODE.EXPORT_TO_PDF;
	searchTextField.setEnabled(true);
	clearSearchTextFieldButton.setEnabled(true);
	designationTable.setEnabled(false);
}
//Inner class starts
	public class DesignationPanel extends JPanel
	{
		private JLabel titleCaptionLabel;
		private JLabel titleLabel;
		private JTextField titleTextField;
		private JButton clearTitleTextFieldButton;
		private JButton addButton;
		private JButton editButton;
		private JButton cancelButton;
		private JButton deleteButton;
		private JButton exportToPDFButton;
		private JPanel 	buttonsPanel;
		private DesignationInterface designation;
		DesignationPanel()
		{
			setBorder(BorderFactory.createLineBorder(new Color(175,175,175)));
			initComponents();
			addListeners();
			setAppearance();
		}
		public void setDesignation(DesignationInterface designation)
		{
			this.designation=designation;
			titleLabel.setText(designation.getTitle()+", Code : "+designation.getCode());
		}
		public void clearDesignation()
		{
			this.designation=null;
			titleLabel.setText("");
		}
		private void initComponents()
		{
			designation=null;
			buttonsPanel=new JPanel();
			titleCaptionLabel=new JLabel("Designation");
			titleLabel=new JLabel("");
			titleTextField=new JTextField();
			clearTitleTextFieldButton=new JButton(clearIcon);
			addButton=new JButton(addIcon);
			editButton=new JButton(editIcon);
			cancelButton=new JButton(cancelIcon);
			deleteButton=new JButton(deleteIcon);
			exportToPDFButton=new JButton(pdfIcon);
		}
		private void setAppearance()
		{
			Font captionFont=new Font("Verdana",Font.BOLD,16);
			Font dataFont=new Font("Verdana",Font.PLAIN,16);
			titleCaptionLabel.setFont(captionFont);
			titleLabel.setFont(dataFont);
			titleTextField.setFont(dataFont);
			setLayout(null);
			int lm,tm;
			lm=0;
			tm=0;
			titleCaptionLabel.setBounds(lm+10,tm+20,110,30);
			titleLabel.setBounds(lm+110+20,tm+20,400,30);
			titleTextField.setBounds(lm+5+110+10,tm+25,350,30);
			clearTitleTextFieldButton.setBounds(lm+10+110+10+350+5,tm+23,30,30);
			buttonsPanel.setBounds(50,tm+20+30+30,465,75);
			buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
			addButton.setBounds(70,12,50,50);
			editButton.setBounds(70+50+20,12,50,50);
			cancelButton.setBounds(70+50+20+50+20,12,50,50);
			deleteButton.setBounds(70+50+20+50+20+50+20,12,50,50);
			exportToPDFButton.setBounds(70+50+20+50+20+50+20+50+20,12,50,50);
			buttonsPanel.setLayout(null);
			buttonsPanel.add(addButton);
			buttonsPanel.add(editButton);
			buttonsPanel.add(cancelButton);
			buttonsPanel.add(deleteButton);
			buttonsPanel.add(exportToPDFButton);
			add(titleCaptionLabel);
			titleTextField.setVisible(false);
			add(titleTextField);
			add(titleLabel);
			add(clearTitleTextFieldButton);
			add(buttonsPanel);
		}
		private boolean addDesignation()
		{
			String title=titleTextField.getText().trim();
			if(title.length()==0)
			{
				JOptionPane.showMessageDialog(this,"Designation required");
				titleTextField.requestFocus();
				return false;
			}
			DesignationInterface d=new Designation();
			d.setTitle(title.toUpperCase());
			try
			{
				designationModel.add(d);
				int rowIndex=0;
				try
				{	
					rowIndex=designationModel.indexOfDesignation(d);
				}catch(Exception e)
				{
					//do nothing
				}
				designationTable.setRowSelectionInterval(rowIndex,rowIndex);
				Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
				designationTable.scrollRectToVisible(rectangle);
				return true;
			}catch(BLException blException)
			{
				if(blException.hasGenericException())
				{
					JOptionPane.showMessageDialog(this,blException.getMessage());	
				}
				else
				{
					if(blException.hasException("title"))JOptionPane.showMessageDialog(this,blException.getMessage());	
				}
				titleTextField.requestFocus();
				return false;
			}
		}
		private boolean updateDesignation()
		{
			String title=titleTextField.getText().trim();
			if(title.length()==0)
			{
				JOptionPane.showMessageDialog(this,"Designation required");
				titleTextField.requestFocus();
				return false;
			}
			DesignationInterface d=new Designation();
			d.setCode(this.designation.getCode());
			d.setTitle(title.toUpperCase());
			try
			{
				designationModel.update(d);
				int rowIndex=0;
				try
				{	
					rowIndex=designationModel.indexOfDesignation(d);
				}catch(Exception e)
				{
					//do nothing
				}
				designationTable.setRowSelectionInterval(rowIndex,rowIndex);
				Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
				designationTable.scrollRectToVisible(rectangle);
				return true;
			}catch(BLException blException)
			{
				if(blException.hasGenericException())
				{
					JOptionPane.showMessageDialog(this,blException.getGenericException());	
				}
				else
				{
					if(blException.hasException("title"))JOptionPane.showMessageDialog(this,blException.getGenericException());	
				}
				titleTextField.requestFocus();
				return false;
			}
		}
		private void removeDesignation()
		{
			try
			{
				String title=this.designation.getTitle();
				int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+title+" ?","Confirmation",JOptionPane.YES_NO_OPTION);
				if(selectedOption==JOptionPane.NO_OPTION)return;
				designationModel.remove(this.designation.getCode());
				JOptionPane.showMessageDialog(this,title+" deleted.");
				//this.clearDesignation();
			}catch(BLException blException)
			{
				System.out.println("PL me : "+blException.getMessage());
			if(blException.hasGenericException())
				{
					JOptionPane.showMessageDialog(this,blException.getGenericException());	
				}
				else
				{
					if(blException.hasException("title"))JOptionPane.showMessageDialog(this,blException.getGenericException());	
				}	
			}
		}
		private void addListeners()
		{
			this.addButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					if(mode==MODE.VIEW)
					{
						setAddMode();
					}
					else
					{
						if(addDesignation())setViewMode();
												
					}
				}
			}
			);
			this.editButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					if(mode==MODE.VIEW)
					{
						setEditMode();
					}
					else 
					{
						if(updateDesignation())setViewMode();
					}
				}
			}
			);
			this.cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					setViewMode();
				}
			}
			);
			this.deleteButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					setDeleteMode();
				}
			}
			);
			this.exportToPDFButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{ 
					JFileChooser jfc = new JFileChooser();
					jfc.setCurrentDirectory(new File("."));
					int selectedOption=jfc.showSaveDialog(DesignationUI.this);
					if(selectedOption==JFileChooser.APPROVE_OPTION)
					{
						try
						{
							File selectedFile=jfc.getSelectedFile();
							String pdfFile=selectedFile.getAbsolutePath();
							if(pdfFile.endsWith("."))pdfFile+="pdf";
							else if(pdfFile.endsWith(".pdf")==false)pdfFile+=".pdf";
							File file=new File(pdfFile);
							File parent=new File(file.getParent());
							if(parent.exists()==false || parent.isDirectory()==false)
							{
								JOptionPane.showMessageDialog(DesignationUI.this,"Incorrect path"+file.getAbsolutePath());
								return;
							}
							if(file.exists())
							{
								int selected=JOptionPane.showConfirmDialog(DesignationPanel.this,file.getAbsolutePath()+" already exists.\n","Confirmation",JOptionPane.YES_NO_OPTION);
								if(selected==JOptionPane.NO_OPTION)return;
								else
								{
									file.delete();
								}
							}
							designationModel.exportToPDF(file);
							JOptionPane.showMessageDialog(DesignationUI.this,"Data exported to "+file.getAbsolutePath());
								return;
						}catch(BLException blException)
						{
							if(blException.hasGenericException())
							{
								System.out.println("111******************************************");
								JOptionPane.showMessageDialog(DesignationUI.this,blException.getGenericException());
							}
						}catch(Exception e)
						{
							System.out.println("2222*************************");
							System.out.println(e);
						}
					}
					else
					{
						System.out.println("Not saving");
					}
				}
			}
			);
		}
		private void setViewMode()
		{
			DesignationUI.this.setViewMode();
			this.addButton.setIcon(addIcon);
			this.editButton.setIcon(editIcon);
			this.titleTextField.setVisible(false);
			this.clearTitleTextFieldButton.setVisible(false);
			this.titleLabel.setVisible(true);
			this.addButton.setEnabled(true);
			this.cancelButton.setEnabled(false);
			if(designationModel.getRowCount()>0)
			{
				this.editButton.setEnabled(true);
				this.deleteButton.setEnabled(true);
				this.exportToPDFButton.setEnabled(true);
			}
			else
			{
				this.editButton.setEnabled(false);
				this.deleteButton.setEnabled(false);
				this.exportToPDFButton.setEnabled(false);
			}
		}
		private void setAddMode()
		{			
			DesignationUI.this.setAddMode();
			this.titleTextField.setText("");
			this.titleLabel.setVisible(false);
			this.clearTitleTextFieldButton.setVisible(true);
			DesignationUI.this.searchTextField.setText("");
			this.titleTextField.setVisible(true);
			addButton.setIcon(saveIcon);
			editButton.setEnabled(false);
			cancelButton.setEnabled(true);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
		}
		private void setEditMode()
		{
			if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount()) 
			{
				JOptionPane.showMessageDialog(this,"Select designation to edit");
				return;
			}
			DesignationUI.this.setEditMode();
			this.titleTextField.setText(this.designation.getTitle());
			this.clearTitleTextFieldButton.setVisible(true);
			this.titleLabel.setVisible(false);
			this.titleTextField.setVisible(true);
			addButton.setEnabled(false);
			cancelButton.setEnabled(true);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
			editButton.setIcon(updateIcon);
		}
		private void setDeleteMode()
		{
			if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount()) 
			{
				JOptionPane.showMessageDialog(this,"Select designation to delete");
				return; 
			}
			DesignationUI.this.setDeleteMode();
			addButton.setEnabled(false);
			editButton.setEnabled(false);
			cancelButton.setEnabled(false);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
			removeDesignation();
			DesignationUI.this.setViewMode();
			this.setViewMode();
		}
		private void setExportToPDFMode()
		{
			DesignationUI.this.setExportToPDFMode();
			addButton.setEnabled(false);
			editButton.setEnabled(false);
			cancelButton.setEnabled(false);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
			this.setViewMode();
		}
	}
//Inner class ends	

}