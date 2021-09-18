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
import java.sql.*;
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
		
		
		Connection connection = null;
		PreparedStatement prepareStatement;
		ResultSet resultSet;
		try
		{
			connection=DAOConnection.getConnection();
			prepareStatement=connection.prepareStatement("select code from designation where code = ?");
			prepareStatement.setInt(1,designationCode);
			resultSet=prepareStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code"+designationCode);
			}
			resultSet.close();
			prepareStatement.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
		if(dateOfBirth==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Date of birth is null");
		}
		char gender = employeeDTO.getGender();
		if(gender==' ')
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Gender not set to Male/Female");
		}
		boolean isIndian =employeeDTO.getIsIndian();
		BigDecimal basicSalary=employeeDTO.getBasicSalary();
		if(basicSalary==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic salary is null");
		}
		if(basicSalary.signum()==-1)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic salary is negative");
		}
		String panNumber=employeeDTO.getPANNumber();
		if(panNumber==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("PAN Number is null");
		}
		panNumber=panNumber.trim();
		if(panNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Length of pan number is zero");
		}
		String aadharCardNumber=employeeDTO.getAadharCardNumber();
		if(aadharCardNumber==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Aadhar card number is null");
		}
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Aadhar card number is zero");
		}
		try
		{
			boolean panNumberExists;
			prepareStatement=connection.prepareStatement("select gender from employee where pan_number = ?");
			prepareStatement.setString(1,panNumber);
			resultSet=prepareStatement.executeQuery();
			panNumberExists=resultSet.next();
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("select gender from employee where aadhar_Card_Number = ?");
			prepareStatement.setString(1,aadharCardNumber);
			boolean aadharCardNumberExists;
			resultSet=prepareStatement.executeQuery();
			aadharCardNumberExists=resultSet.next();
			resultSet.close();
			prepareStatement.close();			
			if(panNumberExists==true && aadharCardNumberExists==true)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+") exists");
			}
			if(panNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number ("+panNumber+") exists"); 
			}
			if(aadharCardNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists"); 
			}
			prepareStatement=connection.prepareStatement("insert into employee(name,designation_code,date_of_birth,basic_salary,gender,is_Indian,pan_number,aadhar_Card_Number) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1,name);
			prepareStatement.setInt(2,designationCode);
			java.sql.Date sqlDateOfBirth=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
			prepareStatement.setDate(3,sqlDateOfBirth);
			prepareStatement.setBigDecimal(4,basicSalary);
			prepareStatement.setString(5,String.valueOf(gender));
			prepareStatement.setBoolean(6,isIndian);
			prepareStatement.setString(7,panNumber);
			prepareStatement.setString(8,aadharCardNumber);
			prepareStatement.executeUpdate();
			resultSet=prepareStatement.getGeneratedKeys();
			resultSet.next();
			int generatedEmployeeId=resultSet.getInt(1);
			resultSet.close();
			prepareStatement.close();
			connection.close();
			employeeDTO.setEmployeeId("A"+(10000000+generatedEmployeeId));
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		
	}
	public void update(EmployeeDTOInterface employeeDTO)throws DAOException
	{
		if(employeeDTO==null)throw new DAOException("Employee is null");
		String employeeID=employeeDTO.getEmployeeId();
		if(employeeID==null)
		{
			throw new DAOException("Employee Id. is null");
		}
		employeeID=employeeID.trim();
		if(employeeID.length()==0)
		{
			throw new DAOException("Length of employee Id. is zero");
		}
		int actualEmployeeId;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeID .substring(-1));
		}catch(Exception  exception)
		{
			throw new DAOException("Invalid employee Id.");
		}
		String name=employeeDTO.getName();
		if(name==null)throw new DAOException("Name is null");
		name=name.trim();
		if(name.length()==0)throw new DAOException("Length of name is zero");
		int designationCode=employeeDTO.getDesignationCode();
		if(designationCode<=0)throw new DAOException("Invalid designation code : "+designationCode);
			
		Connection connection = null;
		PreparedStatement prepareStatement;
		ResultSet resultSet;
		try
		{
			connection=DAOConnection.getConnection();
			prepareStatement=connection.prepareStatement("select code from designation where code = ?");
			prepareStatement.setInt(1,designationCode);
			resultSet=prepareStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code"+designationCode);
			}
			resultSet.close();
			prepareStatement.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
		if(dateOfBirth==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Date of birth is null");
		}
		char gender = employeeDTO.getGender();
		if(gender==' ')
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Gender not set to Male/Female");
		}
		boolean isIndian =employeeDTO.getIsIndian();
		BigDecimal basicSalary=employeeDTO.getBasicSalary();
		if(basicSalary==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic salary is null");
		}
		if(basicSalary.signum()==-1)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic salary is negative");
		}
		String panNumber=employeeDTO.getPANNumber();
		if(panNumber==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("PAN Number is null");
		}
		panNumber=panNumber.trim();
		if(panNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Length of pan number is zero");
		}
		String aadharCardNumber=employeeDTO.getAadharCardNumber();
		if(aadharCardNumber==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Aadhar card number is null");
		}
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Aadhar card number is zero");
		}
		try
		{
			boolean panNumberExists;
			prepareStatement=connection.prepareStatement("select gender from employee where pan_number = ? and employee_id<>?");
			prepareStatement.setString(1,panNumber);
			prepareStatement.setInt(2,actualEmployeeId);
			resultSet=prepareStatement.executeQuery();
			panNumberExists=resultSet.next();
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("select gender from employee where aadhar_Card_Number = ? and employee_id<>?");
			prepareStatement.setString(1,aadharCardNumber);
			prepareStatement.setInt(2,actualEmployeeId);
			boolean aadharCardNumberExists;
			resultSet=prepareStatement.executeQuery();
			aadharCardNumberExists=resultSet.next();
			resultSet.close();
			prepareStatement.close();			
			if(panNumberExists==true && aadharCardNumberExists==true)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+") exists");
			}
			if(panNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number ("+panNumber+") exists"); 
			}
			if(aadharCardNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists"); 
			}
			prepareStatement=connection.prepareStatement("update employee set name=?,desigantion=?,dateOfBirth=?,basicSalary=?,gender=?,is_Indian=?,pan_number=?,aadhar_Card_Number=? where employee_id=?");
			prepareStatement.setString(1,name);
			prepareStatement.setInt(2,designationCode);
			java.sql.Date sqlDateOfBirth=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
			prepareStatement.setDate(3,sqlDateOfBirth);
			prepareStatement.setBigDecimal(4,basicSalary);
			prepareStatement.setString(5,String.valueOf(gender));
			prepareStatement.setBoolean(6,isIndian);
			prepareStatement.setString(7,panNumber);
			prepareStatement.setString(8,aadharCardNumber);
			prepareStatement.setInt(9,actualEmployeeId);
			prepareStatement.executeUpdate();
			resultSet.close();
			prepareStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public void delete(String employeeID)throws DAOException
	{
		if(employeeID==null)
		{
			throw new DAOException("Employee Id. is null");
		}
		employeeID=employeeID.trim();
		if(employeeID.length()==0)
		{
			throw new DAOException("Length of employee Id. is zero");
		}
		int actualEmployeeId;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeID .substring(-1));
		}catch(Exception  exception)
		{
			throw new DAOException("Invalid employee Id.");
		}
		Connection connection=DAOConnection.getConnection();
		PreparedStatement prepareStatement;
		ResultSet resultSet;
		try
		{
			prepareStatement=connection.prepareStatement("select gender from employee where employee_id = ?");
			prepareStatement.setInt(1,actualEmployeeId);
			resultSet=prepareStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid employee Id. "+employeeID);
			}
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("delete from employee where employee_id=?");
			prepareStatement.setInt(1,actualEmployeeId);
			prepareStatement.executeUpdate();
			resultSet.close();
			prepareStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public Set<EmployeeDTOInterface> getAll()throws DAOException
	{
		Set<EmployeeDTOInterface> employees=new TreeSet<>();
		try
		{
			Connection connection = DAOConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from employee");
			EmployeeDTOInterface employeeDTO;
			java.util.Date utilDateOfBirth;
			java.sql.Date sqlDateOfBirth;
			String gender;
			while(resultSet.next())
			{
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId("A"+(10000000+resultSet.getInt("employee_id")));
				employeeDTO.setName(resultSet.getString("name").trim());
				employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDateOfBirth=resultSet.getDate("date_of_birth");
				utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
				employeeDTO.setDateOfBirth(utilDateOfBirth);
				employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				gender=resultSet.getString("gender");
				if(gender.equals("M"))
				{
					employeeDTO.setGender(GENDER.MALE);
				}
				if(gender.equals("F"))
				{
					employeeDTO.setGender(GENDER.FEMALE);
				}
				employeeDTO.setIsIndian(resultSet.getBoolean("Is_Indian"));
				employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
				employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_Card_Number").trim());
				employees.add(employeeDTO);
			}
			resultSet.close();
			statement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employees;
	}
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode)throws DAOException
	{
		Set<EmployeeDTOInterface>employees=new TreeSet<>();
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select code from designation where code = ?");
			prepareStatement.setInt(1,designationCode);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code : "+designationCode);
			}
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("select * from employee where designation_code=?");
			prepareStatement.setInt(1,designationCode);
			resultSet=prepareStatement.executeQuery();
			EmployeeDTOInterface employeeDTO;
			java.util.Date utilDateOfBirth;
			java.sql.Date sqlDateOfBirth;
			String gender;
			while(resultSet.next())
			{
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId("A"+(10000000+resultSet.getInt("employee_id")));
				employeeDTO.setName(resultSet.getString("name").trim());
				employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDateOfBirth=resultSet.getDate("date_of_birth");
				utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
				employeeDTO.setDateOfBirth(utilDateOfBirth);
				employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				gender=resultSet.getString("gender");
				if(gender.equals("M"))
				{
					employeeDTO.setGender(GENDER.MALE);
				}
				if(gender.equals("F"))
				{
					employeeDTO.setGender(GENDER.FEMALE);
				}
				employeeDTO.setIsIndian(resultSet.getBoolean("Is_Indian"));
				employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
				employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_Card_Number").trim());
				employees.add(employeeDTO);
			}
			resultSet.close();
			prepareStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employees;
	}
	public boolean isDesignationAlloted(int designationCode)throws DAOException
	{
		boolean designationAlloted=false;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select code from designation where code = ?");
			prepareStatement.setInt(1,designationCode);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code : "+designationCode);
			};
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("select gender from employee where designation_code=?");
			prepareStatement.setInt(1,designationCode);
			resultSet=prepareStatement.executeQuery();
			designationAlloted=true;
			
			resultSet.close();
			prepareStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return designationAlloted;
	}
	public EmployeeDTOInterface getByEmployeeID(String employeeID)throws DAOException
	{
		if(employeeID==null)throw new DAOException("Invalid employee Id. : "+employeeID);
		employeeID=employeeID.trim();
		if(employeeID.length()==0)throw new DAOException("Invalid employee Id. : employee Id is of Zero length");
		int actualEmployeeId=0;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeID .substring(-1));
		}catch(Exception  exception)
		{
			throw new DAOException("Invalid employee Id.");
		}
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select * from employee where employee_id = ?");
			prepareStatement.setInt(1,actualEmployeeId);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid employee Id. : "+employeeID);
			}
			EmployeeDTOInterface employeeDTO=null;
			java.util.Date utilDateOfBirth;
			java.sql.Date sqlDateOfBirth;
			String gender;
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId("A"+(10000000+resultSet.getInt("employee_id")));
			employeeDTO.setName(resultSet.getString("name").trim());
			employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
			sqlDateOfBirth=resultSet.getDate("date_of_birth");
			utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
			employeeDTO.setDateOfBirth(utilDateOfBirth);
			employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
			gender=resultSet.getString("gender");
			if(gender.equals("M"))
			{
				employeeDTO.setGender(GENDER.MALE);
			}
			if(gender.equals("F"))
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(resultSet.getBoolean("Is_Indian"));
			employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
			employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_Card_Number").trim());
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return employeeDTO;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public EmployeeDTOInterface getByPANNumber(String PANNumber)throws DAOException
	{ 
		if(PANNumber==null)throw new DAOException("Invalid PAN Number : "+PANNumber);
		PANNumber=PANNumber.trim();
		if(PANNumber.length()==0)throw new DAOException("Invalid PAN number Id. : PAN number is of Zero length");
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select * from employee where pan_number = ?");
			prepareStatement.setString(1,PANNumber);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid PAN number : "+PANNumber);
			}
			EmployeeDTOInterface employeeDTO=null;
			java.util.Date utilDateOfBirth;
			java.sql.Date sqlDateOfBirth;
			String gender;
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId("A"+(10000000+resultSet.getInt("employee_id")));
			employeeDTO.setName(resultSet.getString("name").trim());
			employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
			sqlDateOfBirth=resultSet.getDate("date_of_birth");
			utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
			employeeDTO.setDateOfBirth(utilDateOfBirth);
			employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
			gender=resultSet.getString("gender");
			if(gender.equals("M"))
			{
				employeeDTO.setGender(GENDER.MALE);
			}
			if(gender.equals("F"))
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(resultSet.getBoolean("Is_Indian"));
			employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
			employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_Card_Number").trim());
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return employeeDTO;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public EmployeeDTOInterface getByAadharNumber(String aadharCardNumber)throws DAOException
	{
		if(aadharCardNumber==null)throw new DAOException("Invalid aadhar card number  : "+aadharCardNumber);
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)throw new DAOException("Invalid aadhar card number Id. : aadhar card number is of Zero length");
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select * from employee where aadhar_Card_Number = ?");
			prepareStatement.setString(1,aadharCardNumber);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Invalid aadhar card number : "+aadharCardNumber);
			}
			EmployeeDTOInterface employeeDTO=null;
			java.util.Date utilDateOfBirth;
			java.sql.Date sqlDateOfBirth;
			String gender;
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId("A"+(10000000+resultSet.getInt("employee_id")));
			employeeDTO.setName(resultSet.getString("name").trim());
			employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
			sqlDateOfBirth=resultSet.getDate("date_of_birth");
			utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
			employeeDTO.setDateOfBirth(utilDateOfBirth);
			employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
			gender=resultSet.getString("gender");
			if(gender.equals("M"))
			{
				employeeDTO.setGender(GENDER.MALE);
			}
			if(gender.equals("F"))
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(resultSet.getBoolean("Is_Indian"));
			employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
			employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_Card_Number").trim());
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return employeeDTO;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public boolean employeeIDExists(String employeeID)throws DAOException
	{
		boolean exists=false;
		if(employeeID==null)return false;
		employeeID=employeeID.trim();
		if(employeeID.length()==0)return false;
		int actualEmployeeId=0;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeID .substring(-1));
		}catch(Exception  exception)
		{
			throw new DAOException("Invalid employee Id.");
		}
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select * from employee where employee_id = ?");
			prepareStatement.setInt(1,actualEmployeeId);
			ResultSet resultSet=prepareStatement.executeQuery();
			exists=resultSet.next();
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return exists;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public boolean panNumberExists(String panNumber)throws DAOException
	{
		boolean exists=false;
		if(panNumber==null)return false;
		panNumber=panNumber.trim();
		if(panNumber.length()==0)return false;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select * from employee where pan_number = ?");
			prepareStatement.setString(1,panNumber);
			ResultSet resultSet=prepareStatement.executeQuery();
			exists=resultSet.next();
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return exists;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException
	{
		boolean exists=false;
		if(aadharCardNumber==null)return false;
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)return false;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select * from employee where aadhar_Card_Number = ?");
			prepareStatement.setString(1,aadharCardNumber);
			ResultSet resultSet=prepareStatement.executeQuery();
			exists=resultSet.next();
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return exists;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public int getCount()throws DAOException
	{
		int count=0;
		try
		{
			Connection connection=DAOConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet=statement.executeQuery("select count(*) as cnt from employee");
			resultSet.next();
			count=resultSet.getInt("cnt");
			resultSet.close();
			statement.close();
			connection.close();
			return count;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public int getCountByDesignation(int designationCode)throws DAOException
	{
		int count=0;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement("select count(*) as cnt from employee where designation_code=?");
			prepareStatement.setInt(1,designationCode);
			ResultSet resultSet=prepareStatement.executeQuery();
			resultSet.next();
			count=resultSet.getInt("cnt");
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return count;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
}
