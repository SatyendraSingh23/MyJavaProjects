package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.*;

public class DesignationModel extends AbstractTableModel
{
	private java.util.List<DesignationInterface> designationsList;
	private DesignationManagerInterface designationManager;
	private Set<DesignationInterface> designations;
	private Object data[][];
	private String title[];
	public DesignationModel()
	{
		try
		{
			this.designationManager=DesignationManager.getDesignationManager();
		}catch(BLException ble)
		{
			//
		}
		this.designationsList=new LinkedList<>();
		this.designations=new TreeSet<>();
		this.title=new String[3];
		populateDataStructures();
	}
	private void populateDataStructures()
	{
		title[0]="S.No.";
		title[1]="Designation";
		title[2]="Code";
		this.designations=designationManager.getDesignations();
		for(DesignationInterface d : this.designations)
		{
			designationsList.add(d);
		}
		Collections.sort(this.designationsList,new Comparator<DesignationInterface>()
		{
			public int compare(DesignationInterface left, DesignationInterface right)
			{
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
	}
	public int getRowCount()
	{
		return this.designationsList.size();
	}
	public int getColumnCount()
	{
		return this.title.length;
	}
	public String getColumnName(int columnIndex)
	{
		return this.title[columnIndex];
	}
	public boolean isCellEditable(int rowIndex,int columnIndex)
	{
		return false;
	}
	public Class getColumnClass(int columnIndex)
	{
		Class c = null;
		try
		{
		if(columnIndex==0|| columnIndex==2)c=Class.forName("java.lang.Integer");
		if(columnIndex==1)c=Class.forName("java.lang.String");
		}catch(Exception e)
		{
			System.out.println(e);
		}
		return c;
	}
	public Object getValueAt(int rowIndex,int columnIndex)
	{
		if(columnIndex==0)return rowIndex+1;
		if(columnIndex==2)return designationsList.get(rowIndex).getCode();
		if(columnIndex==1)return " "+designationsList.get(rowIndex).getTitle();
		return new String(" ");
	}
	public void add(DesignationInterface designation) throws BLException
	{
		try
		{
			designationManager.addDesignation(designation);
			this.designationsList.add(designation);
			Collections.sort(this.designationsList,new Comparator<DesignationInterface>()
			{
				public int compare(DesignationInterface left, DesignationInterface right)
				{
					return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase ());
				}
			});
			fireTableDataChanged();
		}catch(BLException blException)
		{
			blException.setGenericException(blException.getException("title"));
			throw blException;
		}
	}
	public int indexOfDesignation(DesignationInterface designation)throws BLException
	{ 
		int index=0;
		Iterator<DesignationInterface> iterator = this.designationsList.iterator();
		DesignationInterface d;
		while(iterator.hasNext())
		{
			d=iterator.next();
			if(d.equals(designation))
			{
				return index;
			}
			index++;
		}
		BLException blException=new BLException();
		blException.setGenericException("Invalid designation : "+designation.getTitle());
		return index;
	}
	public int indexOfTitle(String title, boolean partialLeftSearch)throws BLException
	{
		int index=0;
		Iterator<DesignationInterface> iterator = this.designationsList.iterator();
		DesignationInterface d;
		while(iterator.hasNext())
		{
			d=iterator.next();
			if(partialLeftSearch)
			{
				if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))
				{
					return index;
				}
			}
			else
			{
				if(d.getTitle().equalsIgnoreCase(title))
				{
					return (index++);
				}
			}
		index++;
		}
		BLException blException=new BLException();
		blException.setGenericException("Invalid title : "+title);
		throw blException;
	}
	public void update(DesignationInterface designation) throws BLException
	{
		try
		{
			designationManager.updateDesignation(designation);
			this.designationsList.remove(indexOfDesignation(designation));
			this.designationsList.add(designation);
			Collections.sort(this.designationsList,new Comparator<DesignationInterface>()
			{
				public int compare(DesignationInterface left, DesignationInterface right)
				{
					return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
				}
			});
			fireTableDataChanged();
		}catch(BLException blException)
		{
			blException.setGenericException(blException.getException("title"));
			throw blException;
		}
	}
	public void remove(int code) throws BLException
	{
		try
		{
		int index=0;
		designationManager.removeDesignation(code);
		Iterator<DesignationInterface> iterator = this.designationsList.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().getCode()==code)break;
			index++;
		}
		if(index==this.designationsList.size())
		{
			BLException blException=new BLException();
			blException.setGenericException("Invalid designation code : "+code);
			throw blException;
		}
		this.designationsList.remove(index);
		fireTableDataChanged();
		}catch(BLException blException)
		{
			blException.setGenericException(blException.getMessage());
			throw blException;
		}
	}
	public DesignationInterface getDesignationAt(int index)throws BLException
	{
		if(index<0 || index>=this.designationsList.size())
		{
			BLException blException=new BLException();
			blException.setGenericException("Invalid Index : "+index);
			throw blException;
		}
		return this.designationsList.get(index);
	}
	public void exportToPDF(File file) throws BLException
	{
		try
		{
			if(file.exists())file.delete();
			PdfWriter pdfWriter=new PdfWriter(file);
			PdfDocument pdfDocument=new PdfDocument(pdfWriter);
			Document doc = new Document(pdfDocument);
			Image logo = new Image(ImageDataFactory.create(this.getClass().getResource("/Icons/Logo.png")));
			Paragraph logoPara=new Paragraph();
			logoPara.add(logo);
			Paragraph companyNamePara=new Paragraph();
			companyNamePara.add("Thinking Machines");
			PdfFont companyNameFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			companyNamePara.setFont(companyNameFont);
			companyNamePara.setFontSize(18);
			Paragraph reportTitlePara=new Paragraph("List of designations");
			PdfFont reportTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			reportTitlePara.setFont(reportTitleFont);
			reportTitlePara.setFontSize(15);
			PdfFont pageNumberFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			PdfFont columnTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			Paragraph columnTitle1=new Paragraph("S.NO.");
			columnTitle1.setFont(columnTitleFont);
			columnTitle1.setFontSize(14);
			Paragraph columnTitle2=new Paragraph("Designations");
			columnTitle2.setFont(columnTitleFont);
			columnTitle2.setFontSize(14);
			Paragraph columnTitle3=new Paragraph("Code");
			columnTitle3.setFont(columnTitleFont);
			columnTitle3.setFontSize(14);
			Paragraph pageNumberParagraph;
			Paragraph dataParagraph;
			float topTableColumnWidhts[]={1,6};
			float dataTableColumnWidths[]={1,6,2};
			int sno,x,pageSize;
			pageSize=10;
			boolean newPage=true;
			Table pageNumberTable;
			Table topTable;
			Table dataTable=null;
			Cell cell;
			int numberOfPages=this.designationsList.size()/pageSize;
			if((this.designationsList.size()%pageSize)!=0) numberOfPages++;
			int pageNumber=0;
			DesignationInterface designation;
			sno=0;
			x=0;
			while(x<this.designationsList.size())
			{
				if(newPage==true)
				{
					//create new Page header
					pageNumber++;
					topTable=new Table(UnitValue.createPercentArray(topTableColumnWidhts));
					cell=new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(logoPara);
					topTable.addCell(cell);
					cell=new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(companyNamePara);
					cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
					topTable.addCell(cell);
					doc.add(topTable);
					pageNumberParagraph=new Paragraph("Page : "+pageNumber+"/"+numberOfPages);
					pageNumberParagraph.setFont(pageNumberFont);
					pageNumberParagraph.setFontSize(13);
					pageNumberTable=new Table(1);
					pageNumberTable.setWidth(UnitValue.createPercentValue(100));
					cell=new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(pageNumberParagraph);
					cell.setTextAlignment(TextAlignment.RIGHT);
					pageNumberTable.addCell(cell);
					doc.add(pageNumberTable);
					dataTable=new Table(UnitValue.createPercentArray(dataTableColumnWidths));
					dataTable.setWidth(UnitValue.createPercentValue(100));
					cell=new Cell(1,3);
					cell.add(reportTitlePara);
					cell.setTextAlignment(TextAlignment.CENTER);
					dataTable.addHeaderCell(cell);
					dataTable.addHeaderCell(columnTitle1);
					dataTable.addHeaderCell(columnTitle2);
					dataTable.addHeaderCell(columnTitle3);
					newPage=false;
				}
				designation=this.designationsList.get(x);
				//add row to table;
				sno++;
				cell=new Cell();
				dataParagraph=new Paragraph(String.valueOf(sno));
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				cell.setTextAlignment(TextAlignment.RIGHT);
				dataTable.addCell(cell);
				
				cell=new Cell();
				dataParagraph=new Paragraph(designation.getTitle());
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				dataTable.addCell(cell);
				
				cell=new Cell();
				dataParagraph=new Paragraph(String.valueOf(designation.getCode()));
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				cell.setTextAlignment(TextAlignment.LEFT);
				dataTable.addCell(cell);
				
				x++;
				if(sno%pageSize==0 || x==this.designationsList.size())
				{
					//creare footer
					doc.add(dataTable);
					doc.add(new Paragraph("Software by : Satyendra Singh"));
					if(x<this.designationsList.size())
					{
						//add new page to documnet
						doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
						newPage=true;
					}
				}
			}
			doc.close();
		}catch(Exception exception)
		{
			BLException blException;
			blException=new BLException();
			System.out.println("***EXCEPTION**** : "+exception.getMessage());
			blException.setGenericException(exception.getMessage());
			throw blException;
		}
	}
}