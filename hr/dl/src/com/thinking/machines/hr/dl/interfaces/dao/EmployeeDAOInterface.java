package com.thinking.machines.hr.dl.interfaces.dao;
import java.util.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
public interface EmployeeDAOInterface
{
	public void add(EmployeeDTOInterface employeeDTO)throws DAOException;
	public void update(EmployeeDTOInterface employeeDTO)throws DAOException;
	public void delete(String employeeId)throws DAOException;
	public Set<EmployeeDTOInterface> getAll()throws DAOException;
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode)throws DAOException;
	public boolean isDesignationAlloted(int designationCode)throws DAOException;
	public EmployeeDTOInterface getByEmployeeID(String employeeID)throws DAOException;
	public EmployeeDTOInterface getByPANNumber(String PANNumber)throws DAOException;
	public EmployeeDTOInterface getByAadharNumber(String aadharNumber)throws DAOException;
	public boolean employeeIDExists(String employeeID)throws DAOException;
	public boolean panNumberExists(String panNumber)throws DAOException;
	public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException;
	public int getCount()throws DAOException;
	public int getCountByDesignation(int designationCode)throws DAOException;
}
