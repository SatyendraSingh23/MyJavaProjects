package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.sql.*;
public class DesignationDAO implements DesignationDAOInterface
{
	private final static String FILE_NAME = "designation.data";
	public void add(DesignationDTOInterface designationDTO) throws DAOException
	{
		if(designationDTO==null)throw new DAOException("Designation is null");
		String title=designationDTO.getTitle();
		if(title==null)throw new DAOException("Designation is null");
		title=title.trim();
		if(title.length()==0) throw new DAOException("Length of designation is zero");
		try
		{
			Connection connection = DAOConnection.getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement("select code from designation WHERE title = ?");
			prepareStatement.setString(1,title);
			ResultSet r = prepareStatement.executeQuery();
			if(r.next())
			{
				r.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Designation : "+title+" exists.");
			}
			r.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("insert into designation (title) values (?)",Statement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1,title);
			prepareStatement.executeUpdate();
			r=prepareStatement.getGeneratedKeys();
			r.next();
			int code=r.getInt(1);
			r.close();
			connection.close();
			designationDTO.setCode(code);
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public void update(DesignationDTOInterface designationDTO) throws DAOException
	{
		if(designationDTO==null) throw new DAOException("Designation is null");
		int code=designationDTO.getCode();
		if(code<=0)throw new DAOException("Invalid code : "+code);
		String title=designationDTO.getTitle();
		if(title==null) throw new DAOException("Designation is null");
		title=title.trim();
		if(title.length()==0)throw new DAOException("Length of designation is zero");
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement prepareStatement=connection.prepareStatement("select code from designation WHERE code = ?");
			prepareStatement.setInt(1,code);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Code : "+code+"does not exist.");
			}
			prepareStatement=connection.prepareStatement("select code from designation where title=? and code <> ?");
			prepareStatement.setString(1,title);
			prepareStatement.setInt(2,code);
			resultSet=prepareStatement.executeQuery();
			if(resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Designation : "+title+" exists.");
			}
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("update designation set title = ? where code = ?");
			prepareStatement.setString(1,title);
			prepareStatement.setInt(2,code);
			prepareStatement.executeUpdate();
			prepareStatement.close();
			connection.close();
			
		}catch(SQLException sqlException)
		{
			System.out.println(sqlException.getMessage());
		}
	}
	public void delete(int code) throws DAOException
	{
		if(code<=0)throw new DAOException("Invalid code : "+code);
		try
		{
			Connection connection = DAOConnection.getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement("select * from designation where code = ?");
			prepareStatement.setInt(1,code);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Code : "+code+"does not exist.");
			}
			String title=resultSet.getString("title").trim();
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("select gender from employee where designation_code=?");
			prepareStatement.setInt(1,code);
			resultSet=prepareStatement.executeQuery();
			if(resultSet.next())
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Cannot delete designation : "+title+" as it has been alloted to employee(s).");
			}
			resultSet.close();
			prepareStatement.close();
			prepareStatement=connection.prepareStatement("delete from designation where code = ?");
			prepareStatement.setInt(1,code);
			prepareStatement.executeUpdate();
			prepareStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public Set<DesignationDTOInterface> getAll() throws DAOException
	{
		Set<DesignationDTOInterface> designations;
		designations = new TreeSet<>();
		try
		{
			Connection connection=DAOConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet r=statement.executeQuery("select * from designation");
			DesignationDTOInterface designationDTO;
			while(r.next())
			{
				designationDTO=new DesignationDTO();
				designationDTO.setCode(r.getInt("code"));
				designationDTO.setTitle(r.getString("title").trim());
				designations.add(designationDTO);
			}
			r.close();
			statement.close();
			connection.close();
			return designations;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public DesignationDTOInterface getByCode(int code) throws DAOException
	{
		try
		{
			Connection connection = DAOConnection.getConnection();
			PreparedStatement prepareStatement;
			prepareStatement=connection.prepareStatement("select * from designation where code = ?");
			prepareStatement.setInt(1,code);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Code "+code+"does not exist.");
			}
			DesignationDTOInterface designationDTO=new DesignationDTO();
			designationDTO.setCode(code);
			designationDTO.setTitle(resultSet.getString("title").trim());
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return designationDTO;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}		
	}
	public DesignationDTOInterface getByTitle(String title) throws DAOException
	{
		if(title==null || title.trim().length()==0)throw new DAOException("Invalid title : "+title);
		title=title.trim();
		try
		{
			Connection connection = DAOConnection.getConnection();
			PreparedStatement prepareStatement;
			prepareStatement=connection.prepareStatement("select * from designation where title = ?");
			prepareStatement.setString(1,title);
			ResultSet resultSet=prepareStatement.executeQuery();
			if(resultSet.next()==false)
			{
				resultSet.close();
				prepareStatement.close();
				connection.close();
				throw new DAOException("Title "+title+"does not exist.");
			}
			DesignationDTOInterface designationDTO=new DesignationDTO();
			designationDTO.setCode(resultSet.getInt("code"));
			designationDTO.setTitle(resultSet.getString("title").trim());
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return designationDTO;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public boolean codeExists(int code) throws DAOException
	{
		if(code<=0)return false;
		try
		{
			boolean exists;
			Connection connection = DAOConnection.getConnection();
			PreparedStatement prepareStatement;
			prepareStatement=connection.prepareStatement("select code from designation where code = ?");
			prepareStatement.setInt(1,code);
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
	public boolean titleExists(String title) throws DAOException
	{
		if(title==null || title.trim().length()==0)return false;
		try
		{
			boolean exists;
			Connection connection = DAOConnection.getConnection();
			PreparedStatement prepareStatement;
			prepareStatement=connection.prepareStatement("select code from designation where title = ?");
			prepareStatement.setString(1,title);
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
	public int getCount() throws DAOException
	{
		try
		{
			Connection connection=DAOConnection.getConnection();
			Statement statement=connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select count (*) as cnt from designation");
			resultSet.next();
			int count=resultSet.getInt("count");
			resultSet.close();
			statement.close();
			connection.close();
			return count;
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
}