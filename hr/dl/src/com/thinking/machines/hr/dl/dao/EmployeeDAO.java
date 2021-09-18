package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.enums.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import java.math.*;
import java.util.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
	private static final String FILE_NAME="employee.data";
	public void add(EmployeeDTOInterface employeeDTO)throws DAOException
	{
		if(employeeDTO==null)throw new DAOException("Employee is null");
		String employeeID;
		String name=employeeDTO.getName();
		if(name==null)throw new DAOException("Name is null");
		name=name.trim();
		if(name.length()==0)throw new DAOException("Length of name is zero");
		int designationCode=employeeDTO.getDesignationCode();
		if(designationCode<=0)throw new DAOException("Invalid designation code : "+designationCode);
		DesignationDAOInterface designationDAO=new DesignationDAO();
		boolean isDesignationCodeValid=designationDAO.codeExists(designationCode);
		if(isDesignationCodeValid==false)throw new DAOException("Invalid designation code"+designationCode);;
		Date dateOfBirth=employeeDTO.getDateOfBirth();
		if(dateOfBirth==null)throw new DAOException("Date of birth is null");
		char gender = employeeDTO.getGender();
		if(gender==' ')throw new DAOException("Gender not set to Male/Female");
		boolean isIndian =employeeDTO.getIsIndian();
		BigDecimal basicSalary=employeeDTO.getBasicSalary();
		if(basicSalary==null)throw new DAOException("Basic salary is null");
		if(basicSalary.signum()==-1)throw new DAOException("Basic salary is negative");
		String panNumber=employeeDTO.getPANNumber();
		if(panNumber==null)throw new DAOException("PAN Number is null");
		panNumber=panNumber.trim();
		if(panNumber.length()==0)throw new DAOException("Length of pan number is zero");
		String aadharCardNumber=employeeDTO.getAadharCardNumber();
		if(aadharCardNumber==null)throw new DAOException("Aadhar card number is null");
		if(aadharCardNumber.length()==0)throw new DAOException("Aadhar card number is zero");
		try
		{
			int lastGeneratedEmployeeId=10000000;
			String lastGeneratedEmployeeIdString="";
			int recordCount=0;
			String recordCountString="";
			File file=new File(FILE_NAME);
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				lastGeneratedEmployeeIdString=String.format("%-10s","10000000");
				randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
				recordCountString=String.format("%-10s","0");
				randomAccessFile.writeBytes(recordCountString+"\n");		
			}
			else
			{
				lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
				recordCount=Integer.parseInt(randomAccessFile.readLine().trim());	
			}
			boolean panNumberExists,aadharCardNumberExists;
			int x;
			String fPANNumber;
			String fAadharCardNumber;
			panNumberExists=false;
			aadharCardNumberExists=false;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				for(x=1;x<=7;x++)randomAccessFile.readLine();
				fPANNumber=randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(panNumberExists==false && fPANNumber.equalsIgnoreCase(panNumber))
				{
					panNumberExists=true;
				}
				if(aadharCardNumberExists==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
				{
					aadharCardNumberExists=true;
				}
				if(panNumberExists && aadharCardNumberExists)break;
			}
			if(panNumberExists==true && aadharCardNumberExists==true)
			{
				randomAccessFile.close();
				throw new DAOException("PAN number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+") exists");
			}
			if(panNumberExists)
			{
				randomAccessFile.close();
				throw new DAOException("PAN number ("+panNumber+") exists"); 
			}
			if(aadharCardNumberExists)
			{
				randomAccessFile.close();
				throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists"); 
			}
			lastGeneratedEmployeeId++;
			employeeID="A"+lastGeneratedEmployeeId;
			recordCount++;
			randomAccessFile.writeBytes(employeeID+"\n");
			randomAccessFile.writeBytes(name+"\n");
			randomAccessFile.writeBytes(designationCode+"\n");
			SimpleDateFormat simpleDateFormat;
			simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
			randomAccessFile.writeBytes(gender+"\n");
			randomAccessFile.writeBytes(isIndian+"\n");
			randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
			randomAccessFile.writeBytes(panNumber+"\n");
			randomAccessFile.writeBytes(aadharCardNumber+"\n");
			randomAccessFile.seek(0);
			lastGeneratedEmployeeIdString=String.format("%-10d",lastGeneratedEmployeeId);
			recordCountString=String.format("%-10d",recordCount);
			randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
			randomAccessFile.writeBytes(recordCountString+"\n");
			randomAccessFile.close();
			employeeDTO.setEmployeeId(employeeID);		
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public void update(EmployeeDTOInterface employeeDTO)throws DAOException
	{
		if(employeeDTO==null)throw new DAOException("Employee is null");
		String employeeID=employeeDTO.getEmployeeId();
		String name=employeeDTO.getName();
		if(employeeID==null)throw new DAOException("Employee Id. is null");
		if(employeeID.length()==0)throw new DAOException("Length of Employee Id. is zero");
		if(name==null)throw new DAOException("Name is null");
		name=name.trim();
		if(name.length()==0)throw new DAOException("Length of name is zero");
		int designationCode=employeeDTO.getDesignationCode();
		if(designationCode<=0)throw new DAOException("Invalid designation code : "+designationCode);
		DesignationDAOInterface designationDAO=new DesignationDAO();
		boolean isDesignationCodeValid=designationDAO.codeExists(designationCode);
		if(isDesignationCodeValid==false)throw new DAOException("Invalid designation code"+designationCode);;
		Date dateOfBirth=employeeDTO.getDateOfBirth();
		if(dateOfBirth==null)throw new DAOException("Date of birth is null");
		char gender = employeeDTO.getGender();
		if(gender==' ')throw new DAOException("Gender not set to Male/Female");
		boolean isIndian =employeeDTO.getIsIndian();
		BigDecimal basicSalary=employeeDTO.getBasicSalary();
		if(basicSalary==null)throw new DAOException("Basic salary is null");
		if(basicSalary.signum()==-1)throw new DAOException("Basic salary is negative");
		String panNumber=employeeDTO.getPANNumber();
		if(panNumber==null)throw new DAOException("PAN Number is null");
		panNumber=panNumber.trim();
		if(panNumber.length()==0)throw new DAOException("Length of pan number is zero");
		String aadharCardNumber=employeeDTO.getAadharCardNumber();
		if(aadharCardNumber==null)throw new DAOException("Aadhar card number is null");
		if(aadharCardNumber.length()==0)throw new DAOException("Aadhar card number is zero");
		try
		{
			File file =new File(FILE_NAME);
			if(!file.exists())throw new DAOException("Invalid employeeId : "+employeeID);
			RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee Id. : "+employeeID);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			Date fDateOfBirth;
			char fGender;
			boolean fIsIndian;
			BigDecimal fBasicSalary;
			String fPANNumber;
			String fAadharCardNumber;
			int x;
			boolean employeeIDFound=false;
			boolean panNumberFound=false;
			boolean aadharCardNumberFound=false;
			String panNumberFoundAgainstEmployeeId="";
			String aadharNumberFoundAgainstEmployeeId="";
			long foundAt=0;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				if(employeeIDFound==false)foundAt=randomAccessFile.getFilePointer();
				fEmployeeId=randomAccessFile.readLine();
				for(x=1;x<=6;x++)randomAccessFile.readLine();
				fPANNumber=randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(employeeIDFound==false && fEmployeeId.equalsIgnoreCase(employeeID))
				{
					employeeIDFound=true;
				}
				if(panNumberFound==false && fPANNumber.equalsIgnoreCase(panNumber))
				{
					panNumberFound=true;
					panNumberFoundAgainstEmployeeId=fEmployeeId;
				}
				if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
				{
					aadharCardNumberFound=true;
					aadharNumberFoundAgainstEmployeeId=fEmployeeId;
				}
				if(employeeIDFound && aadharCardNumberFound && panNumberFound) break;
			}
			if(employeeIDFound==false)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee Id."+employeeID);
			}
			boolean panNumberExists=false;
			if(panNumberFound && (panNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeID)==false))
			{
				panNumberExists=true;
			}
			boolean aadharCardNumberExists=false;
			if(aadharCardNumberFound && (aadharNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeID)==false))
			{
				aadharCardNumberExists=true;
			}
			if(panNumberExists==true && aadharCardNumberExists==true)
			{
				randomAccessFile.close();
				throw new DAOException("PAN number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+") exists");
			}
			if(panNumberExists)
			{
				randomAccessFile.close();
				throw new DAOException("PAN number ("+panNumber+") exists"); 
			}
			if(aadharCardNumberExists)
			{
				randomAccessFile.close();
				throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists"); 
			}
			randomAccessFile.seek(foundAt);
			for(x=1;x<=9;x++)randomAccessFile.readLine();
			File tmpFile=new File("tmp.tmp");
			if(tmpFile.exists())tmpFile.delete();
			RandomAccessFile tmpRandomAccessFile= new RandomAccessFile(tmpFile,"rw");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");	
			}
			randomAccessFile.seek(foundAt);
			randomAccessFile.writeBytes(employeeID+"\n");
			randomAccessFile.writeBytes(name+"\n");
			randomAccessFile.writeBytes(designationCode+"\n");
			randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
			randomAccessFile.writeBytes(gender+"\n");
			randomAccessFile.writeBytes(isIndian+"\n");
			randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
			randomAccessFile.writeBytes(panNumber+"\n");
			randomAccessFile.writeBytes(aadharCardNumber+"\n");
			tmpRandomAccessFile.seek(0);
			while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
			{
				randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
			}
			randomAccessFile.setLength(randomAccessFile.getFilePointer());
			tmpRandomAccessFile.setLength(0);
			tmpRandomAccessFile.close();
			randomAccessFile.close();
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public void delete(String employeeID)throws DAOException
	{
		if(employeeID==null)throw new DAOException("Employee Id. is null");
		if(employeeID.length()==0)throw new DAOException("Length of Employee Id. is zero");
		try
		{
			File file =new File(FILE_NAME);
			if(!file.exists())throw new DAOException("Invalid employeeId : "+employeeID);
			RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee Id. : "+employeeID);
			}
			randomAccessFile.readLine();
			int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
			String fEmployeeId;
			int x;
			boolean employeeIDFound=false;
			long foundAt=0;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				foundAt=randomAccessFile.getFilePointer();
				fEmployeeId=randomAccessFile.readLine();
				for(x=1;x<=8;x++)randomAccessFile.readLine();
				if(fEmployeeId.equalsIgnoreCase(employeeID))
				{
					employeeIDFound=true;
					break;
				}
			}
			if(employeeIDFound==false)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee Id."+employeeID);
			}
			File tmpFile=new File("tmp.tmp");
			if(tmpFile.exists())tmpFile.delete();
			RandomAccessFile tmpRandomAccessFile= new RandomAccessFile(tmpFile,"rw");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");	
			}
			randomAccessFile.seek(foundAt);
			tmpRandomAccessFile.seek(0);
			while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
			{
				randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
			}
			randomAccessFile.setLength(randomAccessFile.getFilePointer());
			tmpRandomAccessFile.setLength(0);
			recordCount--;
			String recordCountString=String.format("%-10d",recordCount);
			randomAccessFile.seek(0);
			randomAccessFile.readLine();
			randomAccessFile.writeBytes(recordCountString+"\n");
			tmpRandomAccessFile.setLength(0);
			randomAccessFile.close();
			tmpRandomAccessFile.close();
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public Set<EmployeeDTOInterface> getAll()throws DAOException
	{
		Set<EmployeeDTOInterface>employees=new TreeSet<>();
		try
		{
			File file =new File(FILE_NAME);
			if(file.exists()==false)return employees;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return employees;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			EmployeeDTOInterface employeeDTO;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			char fGender;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId(randomAccessFile.readLine());
				employeeDTO.setName(randomAccessFile.readLine());
				employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
				try
				{
					employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
				}catch(ParseException parseException)
				{
					//do nothing
				}
				fGender=randomAccessFile.readLine().charAt(0);
				if(fGender=='M')
				{
					employeeDTO.setGender(GENDER.MALE);
				}
				if(fGender=='F')
				{
					employeeDTO.setGender(GENDER.FEMALE);
				}
				employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
				employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
				employeeDTO.setPANNumber(randomAccessFile.readLine());
				employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
				employees.add(employeeDTO);
			}
			randomAccessFile.close();
			return employees;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode)throws DAOException
	{
		Set<EmployeeDTOInterface>employees=new TreeSet<>();
		try
		{
			File file =new File(FILE_NAME);
			if(file.exists()==false)return employees;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return employees;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			EmployeeDTOInterface employeeDTO;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			int x;
			char fGender;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				fName=randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				if(fDesignationCode!=designationCode)
				{
					for(x=1;x<=6;x++)randomAccessFile.readLine();
					continue;
				}
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId(fEmployeeId);
				employeeDTO.setName(fName);
				employeeDTO.setDesignationCode(fDesignationCode);
				try
				{
					employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
				}catch(ParseException parseException)
				{
					//do nothing
				}
				fGender=randomAccessFile.readLine().charAt(0);
				if(fGender=='M')
				{
					employeeDTO.setGender(GENDER.MALE);
				}
				if(fGender=='F')
				{
					employeeDTO.setGender(GENDER.FEMALE);
				}
				employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
				employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
				employeeDTO.setPANNumber(randomAccessFile.readLine());
				employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
				employees.add(employeeDTO);
			}
			randomAccessFile.close();
			return employees;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean isDesignationAlloted(int designationCode)throws DAOException
	{
		try
		{
			File file =new File(FILE_NAME);
			if(file.exists()==false)return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();	
			int fDesignationCode;
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				randomAccessFile.readLine();
				randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				if(fDesignationCode==designationCode)
				{
					randomAccessFile.close();
					return true;
				}
			for(x=1;x<=6;x++)randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public EmployeeDTOInterface getByEmployeeID(String employeeID)throws DAOException
	{
		if(employeeID==null)throw new DAOException("Invalid employee Id. : "+employeeID);
		employeeID=employeeID.trim();
		if(employeeID.length()==0)throw new DAOException("Invalid employee Id. : employee Id is Zero");
		EmployeeDTOInterface employeeDTO;
		try
		{
			File file=new File(FILE_NAME);
			if(file.exists()==false)throw new DAOException("Invalid employee Id. :"+employeeID);
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee Id. :"+employeeID);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fEmployeeId;
			SimpleDateFormat simpleDateFormat;
			simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				if(fEmployeeId.equalsIgnoreCase(employeeID))
				{
					employeeDTO=new EmployeeDTO();
					employeeDTO.setEmployeeId(fEmployeeId);
					employeeDTO.setName(randomAccessFile.readLine());
					employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
					try
					{
					employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
					}catch(ParseException pe)
					{
						// do nothing
					}
					employeeDTO.setGender((randomAccessFile.readLine().charAt(0)=='M')?GENDER.MALE:GENDER.FEMALE);
					employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
					employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
					employeeDTO.setPANNumber(randomAccessFile.readLine());
					employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
					randomAccessFile.close();
					return employeeDTO;
				}
				for(x=1;x<=8;x++)randomAccessFile.readLine();
			}
			randomAccessFile.close();
			throw new DAOException("Invalid Employee Id. :"+employeeID);
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public EmployeeDTOInterface getByPANNumber(String PANNumber)throws DAOException
	{ 
		if(PANNumber==null)throw new DAOException("Invalid employee PAN Number : "+PANNumber);
		PANNumber=PANNumber.trim();
		if(PANNumber.length()==0)throw new DAOException("Invalid employee PAN Number :"+PANNumber+" employee PANNumber is of Zero length");
		EmployeeDTOInterface employeeDTO;
		try
		{
			File file=new File(FILE_NAME);
			if(file.exists()==false)throw new DAOException("Invalid employee PAN Number :"+PANNumber);
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee PAN Number. :"+PANNumber);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			Date fDateOfBirth=new Date();
			char fGender;
			boolean fIsIndian;
			BigDecimal fBasicSalary;
			String fPANNumber;
			String fAadharCardNumber;
			SimpleDateFormat simpleDateFormat;
			simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				fName=randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				try
				{
					fDateOfBirth=simpleDateFormat.parse(randomAccessFile.readLine());
				}catch(ParseException pe)
				{
					//do nothing
				}
				fGender=randomAccessFile.readLine().charAt(0);
				fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
				fBasicSalary= new BigDecimal(randomAccessFile.readLine());
				fPANNumber=randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(fPANNumber.equalsIgnoreCase(PANNumber))
				{
					employeeDTO=new EmployeeDTO();
					employeeDTO.setEmployeeId(fEmployeeId);
					employeeDTO.setName(fName);
					employeeDTO.setDesignationCode(fDesignationCode);
					employeeDTO.setDateOfBirth(fDateOfBirth);
					employeeDTO.setGender((fGender=='M')?GENDER.MALE:GENDER.FEMALE);
					employeeDTO.setIsIndian(fIsIndian);
					employeeDTO.setBasicSalary(fBasicSalary);
					employeeDTO.setPANNumber(fPANNumber);
					employeeDTO.setAadharCardNumber(fAadharCardNumber);
					randomAccessFile.close();
					return employeeDTO;
				}
			}
			randomAccessFile.close();
			throw new DAOException("Invalid PAN Number : "+PANNumber);
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public EmployeeDTOInterface getByAadharNumber(String aadharNumber)throws DAOException
	{
		if(aadharNumber==null)throw new DAOException("Invalid employee aadhar Number : "+aadharNumber);
		aadharNumber=aadharNumber.trim();
		if(aadharNumber.length()==0)throw new DAOException("Invalid employee aadhar Number :"+aadharNumber+" employee aadhar card number is of Zero length");
		EmployeeDTOInterface employeeDTO;
		try
		{
			File file=new File(FILE_NAME);
			if(file.exists()==false)throw new DAOException("Invalid employee aadhar Number :"+aadharNumber);
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee aadhar Number. :"+aadharNumber);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			Date fDateOfBirth=new Date();
			char fGender;
			boolean fIsIndian;
			BigDecimal fBasicSalary;
			String fPANNumber;
			String fAadharCardNumber;
			SimpleDateFormat simpleDateFormat;
			simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				fName=randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				try
				{
					fDateOfBirth=simpleDateFormat.parse(randomAccessFile.readLine());
				}catch(ParseException pe)
				{
					//do nothing
				}
				fGender=randomAccessFile.readLine().charAt(0);
				fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
				fBasicSalary= new BigDecimal(randomAccessFile.readLine());
				fPANNumber=randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(fAadharCardNumber.equalsIgnoreCase(aadharNumber))
				{
					employeeDTO=new EmployeeDTO();
					employeeDTO.setEmployeeId(fEmployeeId);
					employeeDTO.setName(fName);
					employeeDTO.setDesignationCode(fDesignationCode);
					employeeDTO.setDateOfBirth(fDateOfBirth);
					employeeDTO.setGender((fGender=='M')?GENDER.MALE:GENDER.FEMALE);
					employeeDTO.setIsIndian(fIsIndian);
					employeeDTO.setBasicSalary(fBasicSalary);
					employeeDTO.setPANNumber(fPANNumber);
					employeeDTO.setAadharCardNumber(fAadharCardNumber);
					randomAccessFile.close();
					return employeeDTO;
				}
			}
			randomAccessFile.close();
			throw new DAOException("Invalid aadhar Number : "+aadharNumber);
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean employeeIDExists(String employeeID)throws DAOException
	{
		if(employeeID==null)return false;
		employeeID=employeeID.trim();
		if(employeeID.length()==0)return false;
		EmployeeDTOInterface employeeDTO;
		try
		{
			File file=new File(FILE_NAME);
			if(file.exists()==false)return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fEmployeeId;
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				if(fEmployeeId.equalsIgnoreCase(employeeID))
				{
					randomAccessFile.close();
					return true;
				}
				for(x=1;x<=8;x++)randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean panNumberExists(String panNumber)throws DAOException
	{
		if(panNumber==null)return false;
		panNumber=panNumber.trim();
		if(panNumber.length()==0)return false;
		EmployeeDTOInterface employeeDTO;
		try
		{
			File file=new File(FILE_NAME);
			if(file.exists()==false)return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fPanNumber;
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				for(x=0;x<=6;x++)randomAccessFile.readLine();
				fPanNumber=randomAccessFile.readLine();
				if(fPanNumber.equalsIgnoreCase(panNumber))
				{
					randomAccessFile.close();
					return true;
				}
				randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException
	{
		if(aadharCardNumber==null)return false;
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)return false;
		EmployeeDTOInterface employeeDTO;
		try
		{
			File file=new File(FILE_NAME);
			if(file.exists()==false)return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fAadharCardNumber;
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				for(x=0;x<=7;x++)randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
				{
					randomAccessFile.close();
					return true;
				}
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public int getCount()throws DAOException
	{
		String fCount="";
		int count=0;
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists())return count;
			if(file.length()==0)return count;
			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return count;
			}	
			randomAccessFile.readLine();
			fCount=randomAccessFile.readLine();
			fCount=fCount.trim();
			count=Integer.parseInt(fCount);
			randomAccessFile.close();
			return count;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public int getCountByDesignation(int designationCode)throws DAOException
	{
		int count=0;
		try
		{
			if(new DesignationDAO().codeExists(designationCode)==false)throw new DAOException("Invalid Designation Code"+designationCode);
			File file=new File(FILE_NAME);
			if(!file.exists())return count;
			if(file.length()==0)return count;
			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file,"rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return count;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			int fDesignationCode;
			int x;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				randomAccessFile.readLine();
				randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				if(fDesignationCode==designationCode)count++;
				for(x=1;x<=6;x++)randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return count;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
}
