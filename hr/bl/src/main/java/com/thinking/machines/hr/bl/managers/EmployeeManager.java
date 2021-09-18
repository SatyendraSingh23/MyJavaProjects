package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.math.*;
import java.util.*;
import java.text.*;
import com.thinking.machines.enums.*;
public class EmployeeManager implements EmployeeManagerInterface
{
	private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
	private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
	private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
	private Set<EmployeeInterface>employeesSet;
	private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
	private static EmployeeManager employeeManager=null;
	private EmployeeManager()throws BLException
	{
		populateDataStructures();
	}
	public static EmployeeManagerInterface getEmployeeManager()throws BLException
	{
		if(employeeManager==null) employeeManager=new EmployeeManager();
		return employeeManager;
	}
	private void populateDataStructures()throws BLException
	{
		this.employeeIdWiseEmployeesMap=new HashMap<>();
		this.panNumberWiseEmployeesMap=new HashMap<>();
		this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
		this.employeesSet=new TreeSet<>();
		this.designationCodeWiseEmployeesMap=new HashMap<>();
		Set<EmployeeInterface> ets;
		try
		{
			Set<EmployeeDTOInterface> dlEmployees;
			dlEmployees=new EmployeeDAO().getAll(); 
			EmployeeInterface employee;
			DesignationManagerInterface designationManager;
			designationManager=DesignationManager.getDesignationManager();
			DesignationInterface designation;
			for(EmployeeDTOInterface dlEmployee:dlEmployees)
			{
				employee=new Employee();
				employee.setEmployeeId(dlEmployee.getEmployeeId());
				employee.setName(dlEmployee.getName());
				designation=designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
				employee.setDesignation(designation);
				employee.setDateOfBirth((Date)dlEmployee.getDateOfBirth().clone());
				if(dlEmployee.getGender()=='M') employee.setGender(GENDER.MALE);
				else employee.setGender(GENDER.FEMALE);
				employee.setIsIndian(dlEmployee.getIsIndian());
				employee.setBasicSalary(dlEmployee.getBasicSalary());
				employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
				employee.setPANNumber(dlEmployee.getPANNumber());
				this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
				this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
				this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
				this.employeesSet.add(employee);
				ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
				if(ets==null)
				{
					ets=new TreeSet<>();
					ets.add(employee);
					designationCodeWiseEmployeesMap.put(new Integer(designation.getCode()),ets);
				}
				else
				{
					ets.add(employee);
				}
			}
		}catch(DAOException daoException)
		{
			BLException blException=new BLException();
			blException.setGenericException(daoException.getMessage());
			throw blException;
		}
	}
	public void addEmployee(EmployeeInterface employee)throws BLException
	{
		BLException blException=new BLException();
		if(employee==null)
		{
			blException.setGenericException("employee required.");
			throw blException;
		}
		String employeeId=employee.getEmployeeId();
		String name=employee.getName();
		int designationCode=0;
		Date dateOfBirth=(Date)employee.getDateOfBirth().clone();
		DesignationInterface designation=employee.getDesignation();
		char gender=employee.getGender();
		Boolean isIndian=employee.getIsIndian();
		BigDecimal basicSalary=employee.getBasicSalary();
		String panNumber=employee.getPANNumber();
		String aadharCardNumber=employee.getAadharCardNumber();
		if(employeeId!=null )
		{
			employeeId=employeeId.trim();
			if(employeeId.length()>0)
			{
				blException.addException("Employee Id.","Employee Id. should be nil/empty");
			}
		}
		if(name==null)
		{
			blException.addException("Name","Name required");
		}	
		else
		{ 
			name=name.trim();
			if(name.length()==0)blException.addException("Name","Name required");
		}	
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(designation==null)blException.addException("Designation","Designation required");
		else
		{
			designationCode=designation.getCode();
			if(designationManager.designationCodeExists(designation.getCode())==false)
			{
				blException.addException("Designation","Invalid designation");
			}
		}	
		if(dateOfBirth==null)
		{
			blException.addException("DateOfBirth","Date of birth required");
		}
		if(gender==' ')
		{
			blException.addException("Gender","Gender required");
		}
		if(basicSalary==null)
		{
			blException.addException("BasicSalary","Basic salary required");
		}
		else
		{
			if(basicSalary.signum()==-1)
			{
				blException.addException("BasicSalary","Basic salary cannot be negative");
			}
		}
		if(aadharCardNumber==null)
		{
			blException.addException("AadharCardNumber","Aadhar card number required");
		}
		else
		{
			aadharCardNumber=aadharCardNumber.trim();
			if(aadharCardNumber.length()==0)blException.addException("AadharCardNumber","Aadhar card number is required");
		}
		if(panNumber==null)
		{
			blException.addException("PanNumber","Pan number is required");
		}
		else
		{
			panNumber=panNumber.trim();
			if(panNumber.length()==0)blException.addException("PanNumber","Pan number is required");
		}
		if(panNumber!=null && panNumber.length()>0)
		{
			if(panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
			{
				blException.addException("PanNumber","Pan number "+panNumber+" exists");
			}
		}
		if(aadharCardNumber!=null && aadharCardNumber.length()>0)
		{
			if(aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
			{
				blException.addException("AadharCardNumber","Aadhar card number "+aadharCardNumber+" exists");
			}
		}
		if(blException.hasException()==true)
		{	
			throw blException;
		}
		Set<EmployeeInterface> ets=new TreeSet<>();
		try
		{
			EmployeeDAOInterface employeeDAO;
			employeeDAO=new EmployeeDAO();
			EmployeeDTOInterface dlEmployee;
			dlEmployee=new EmployeeDTO();
			dlEmployee.setName(name);
			
			dlEmployee.setDesignationCode(designation.getCode());
			dlEmployee.setDateOfBirth((Date)dateOfBirth.clone());
			dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
			dlEmployee.setIsIndian(isIndian);
			dlEmployee.setBasicSalary(basicSalary);
			dlEmployee.setPANNumber(panNumber);
			dlEmployee.setAadharCardNumber(aadharCardNumber);
			employeeDAO.add(dlEmployee);
			employee.setEmployeeId(dlEmployee.getEmployeeId());
			EmployeeInterface dsEmployee=new Employee();
			dsEmployee.setEmployeeId(employee.getEmployeeId());
			dsEmployee.setName(employee.getName());
			dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
			dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
			dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
			dsEmployee.setIsIndian(isIndian);
			dsEmployee.setBasicSalary(basicSalary);
			dsEmployee.setPANNumber(panNumber);
			dsEmployee.setAadharCardNumber(aadharCardNumber);
			this.employeesSet.add(dsEmployee);
			this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
			this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
			this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
			ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
			if(ets==null)
			{
				ets=new TreeSet<>();
				ets.add(dsEmployee);
				designationCodeWiseEmployeesMap.put(new Integer(dsEmployee.getDesignation().getCode()),ets);
			}
			else
			{
				ets.add(dsEmployee);
			}
		}catch(DAOException daoException)
		{
			blException.setGenericException(blException.getMessage());
			throw blException;
		}
		
	}
	public void updateEmployee(EmployeeInterface employee)throws BLException
	{
		BLException blException=new BLException();
		if(employee==null)
		{
			blException.setGenericException("employee required.");
			throw blException;
		}
		String employeeId=employee.getEmployeeId();
		String name=employee.getName();
		int designationCode=0;
		Date dateOfBirth=(Date)employee.getDateOfBirth().clone();
		DesignationInterface designation=employee.getDesignation();
		char gender=employee.getGender();
		Boolean isIndian=employee.getIsIndian();
		BigDecimal basicSalary=employee.getBasicSalary();
		String panNumber=employee.getPANNumber();
		String aadharCardNumber=employee.getAadharCardNumber();
		if(employeeId==null )
		{
			blException.addException("Employee Id.","Employee Id. required.");
		}
		else
		{
			employeeId=employeeId.trim();
			if(employeeId.length()==0)blException.addException("Employee Id","Employee Id required.");
			else
			{
				if(!employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())) 
				{
					blException.addException("Employee Id","Invalid employee Id. "+employeeId);
					throw blException;
				}
			}
		}
		if(name==null)
		{
			blException.addException("Name","Name required");
		}	
		else
		{ 
			name=name.trim();
			if(name.length()==0)blException.addException("Name","Name required");
		}	
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(designation==null)blException.addException("Designation","Designation required");
		else
		{
			designationCode=designation.getCode();
			if(designationManager.designationCodeExists(designation.getCode())==false)
			{
				blException.addException("Designation","Invalid designation");
			}
		}	
		if(dateOfBirth==null)
		{
			blException.addException("DateOfBirth","Date of birth required");
		}
		if(gender==' ')
		{
			blException.addException("Gender","Gender required");
		}
		if(basicSalary==null)
		{
			blException.addException("BasicSalary","Basic salary required");
		}
		else
		{
			if(basicSalary.signum()==-1)
			{
				blException.addException("BasicSalary","Basic salary cannot be negative");
			}
		}
		if(aadharCardNumber==null)
		{
			blException.addException("AadharCardNumber","Aadhar card number required");
		}
		else
		{
			aadharCardNumber=aadharCardNumber.trim();
			if(aadharCardNumber.length()==0)blException.addException("AadharCardNumber","Aadhar card number is required");
		}
		if(panNumber==null)
		{
			blException.addException("PanNumber","Pan number is required");
		}
		else
		{
			panNumber=panNumber.trim();
			if(panNumber.length()==0)blException.addException("PanNumber","Pan number is required");
		}
		if(panNumber!=null && panNumber.length()>0)
		{
			EmployeeInterface ee=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
			if(ee!=null && ee.getEmployeeId().equalsIgnoreCase(employeeId)==false)
			{
				blException.addException("PanNumber","Pan number "+panNumber+" exists");
			}
		}
		if(aadharCardNumber!=null && aadharCardNumber.length()>0)
		{
			EmployeeInterface ee=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
			if(ee!=null && ee.getEmployeeId().equalsIgnoreCase(employeeId)==false)
			{
				blException.addException("AadharCardNumber","Aadhar card number "+aadharCardNumber+" exists");
			}
		}
		if(blException.hasException())
		{	
			throw blException;
		}
		try
		{
			EmployeeInterface dsEmployee;
			dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
			String oldPANNumber=dsEmployee.getPANNumber();
			String oldAadharCardNumber=dsEmployee.getAadharCardNumber();
			int oldDesignationCode=dsEmployee.getDesignation().getCode();
			EmployeeDAOInterface employeeDAO;
			employeeDAO=new EmployeeDAO();
			EmployeeDTOInterface dlEmployee;
			dlEmployee=new EmployeeDTO();
			dlEmployee.setEmployeeId(dsEmployee.getEmployeeId().toUpperCase());
			dlEmployee.setName(name);
			dlEmployee.setDesignationCode(designation.getCode());
			dlEmployee.setDateOfBirth((Date)dateOfBirth.clone());
			dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
			dlEmployee.setIsIndian(isIndian);
			dlEmployee.setBasicSalary(basicSalary);
			dlEmployee.setPANNumber(panNumber);
			dlEmployee.setAadharCardNumber(aadharCardNumber);
			employeeDAO.update(dlEmployee);
			
			dsEmployee.setName(employee.getName());
			dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
			dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
			dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
			dsEmployee.setIsIndian(isIndian);
			dsEmployee.setBasicSalary(basicSalary);
			dsEmployee.setPANNumber(panNumber);
			dsEmployee.setAadharCardNumber(aadharCardNumber);
			employeesSet.remove(dsEmployee);
			employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
			panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
			aadharCardNumberWiseEmployeesMap.remove(oldAadharCardNumber.toUpperCase());			
			this.employeesSet.add(dsEmployee);
			this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
			this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
			this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
			if(oldDesignationCode!=dsEmployee.getDesignation().getCode())
			{
				Set<EmployeeInterface> ets;
				ets=this.designationCodeWiseEmployeesMap.get(oldDesignationCode);
				ets.remove(dsEmployee);
				ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
				if(ets==null)
				{
					ets=new TreeSet<>();
					ets.add(dsEmployee);
					designationCodeWiseEmployeesMap.put(new Integer(designation.getCode()),ets);
				}
				else
				{
					ets.add(dsEmployee);
				}
			}
		}catch(DAOException daoException)
		{
			blException.setGenericException(blException.getMessage());
			throw blException;
		}
	}
	public void removeEmployee(String employeeId)throws BLException
	{
		if(employeeId==null )
		{
			BLException blException=new BLException();
			blException.addException("Employee Id.","Employee Id. required.");
			throw blException;
		}
		else
		{
			employeeId=employeeId.trim();
			if(employeeId.length()==0)
			{
				BLException blException=new BLException();
				blException.addException("Employee Id","Employee Id required.");
				throw blException;
			}
			else
			{
				if(!employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())) 
				{
					BLException blException=new BLException();
					blException.addException("Employee Id","Invalid employee Id. "+employeeId);
					throw blException;
				}
			}
		}
		try
		{
			EmployeeInterface dsEmployee;
			dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
			EmployeeDAOInterface employeeDAO;
			employeeDAO=new EmployeeDAO();
			employeeDAO.delete(dsEmployee.getEmployeeId());	
			employeesSet.remove(dsEmployee);
			employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
			panNumberWiseEmployeesMap.remove(dsEmployee.getPANNumber().toUpperCase());
			aadharCardNumberWiseEmployeesMap.remove(dsEmployee.getAadharCardNumber().toUpperCase());
			Set<EmployeeInterface> ets;
			ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
			ets.remove(dsEmployee);			
		}catch(DAOException daoException)
		{
			BLException blException=new BLException();
			blException.setGenericException(blException.getMessage());
			throw blException;
		}
	}
	public EmployeeInterface getEmployeeByEmployeeId(String employeeId)throws BLException
	{
		EmployeeInterface dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
		if(dsEmployee==null)
		{
			BLException blException=new BLException();
			blException.addException("Employee Id.","Invalid employee Id . : "+employeeId);
			throw blException;
		}
		EmployeeInterface employee=new Employee();
		employee.setEmployeeId(dsEmployee.getEmployeeId());
		employee.setName(dsEmployee.getName());
		DesignationInterface dsDesignation=dsEmployee.getDesignation();
		DesignationInterface designation=new Designation();
		designation.setCode(dsDesignation.getCode());
		designation.setTitle(dsDesignation.getTitle());
		dsEmployee.setDesignation(designation);
		employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
		employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
		employee.setIsIndian(dsEmployee.getIsIndian());
		employee.setBasicSalary(dsEmployee.getBasicSalary());
		employee.setPANNumber(dsEmployee.getPANNumber());
		employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
		return employee;
	}
	public EmployeeInterface getEmployeeByPANNumber(String panNumber)throws BLException
	{
		EmployeeInterface dsEmployee=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
		if(dsEmployee==null)
		{
			BLException blException=new BLException();
			blException.addException("Pan number","Invalid PAN number . : "+panNumber);
			throw blException;
		}
		EmployeeInterface employee=new Employee();
		employee.setEmployeeId(dsEmployee.getEmployeeId());
		employee.setName(dsEmployee.getName());
		DesignationInterface dsDesignation=dsEmployee.getDesignation();
		DesignationInterface designation=new Designation();
		designation.setCode(dsDesignation.getCode());
		designation.setTitle(dsDesignation.getTitle());
		dsEmployee.setDesignation(designation);
		employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
		employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
		employee.setIsIndian(dsEmployee.getIsIndian());
		employee.setBasicSalary(dsEmployee.getBasicSalary());
		employee.setPANNumber(dsEmployee.getPANNumber());
		employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
		return employee;
	}
	public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber)throws BLException
	{
		EmployeeInterface dsEmployee=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
		if(dsEmployee==null)
		{
			BLException blException=new BLException();
			blException.addException("Aadhar card number number","Invalid Aadhar card number . : "+aadharCardNumber);
			throw blException;
		}
		EmployeeInterface employee=new Employee();
		employee.setEmployeeId(dsEmployee.getEmployeeId());
		employee.setName(dsEmployee.getName());
		DesignationInterface dsDesignation=dsEmployee.getDesignation();
		DesignationInterface designation=new Designation();
		designation.setCode(dsDesignation.getCode());
		designation.setTitle(dsDesignation.getTitle());
		dsEmployee.setDesignation(designation);
		employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
		employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
		employee.setIsIndian(dsEmployee.getIsIndian());
		employee.setBasicSalary(dsEmployee.getBasicSalary());
		employee.setPANNumber(dsEmployee.getPANNumber());
		employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
		return employee;
	}
	public int getEmployeeCount()
	{
		return employeesSet.size();
	}
	public boolean employeeIdExists(String employeeId)
	{
		return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
	}
	public boolean employeePANNumberExists(String panNumber)
	{
		return this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
	}
	public boolean employeeAadharCardNumberExists(String aadharCardNumber)
	{
		return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
	}
	public Set<EmployeeInterface> getEmployees()
	{
		EmployeeInterface employee;;
		DesignationInterface dsDesignation;
		DesignationInterface designation;
		Set<EmployeeInterface> employees=new TreeSet<>();
		for(EmployeeInterface dsEmployee:employeesSet)
		{
			employee=new Employee();
			employee.setEmployeeId(dsEmployee.getEmployeeId());
			employee.setName(dsEmployee.getName());
			dsDesignation=dsEmployee.getDesignation();
			designation=new Designation();
			designation.setCode(dsDesignation.getCode());
			designation.setTitle(dsDesignation.getTitle());
			dsEmployee.setDesignation(designation);
			employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
			employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
			employee.setIsIndian(dsEmployee.getIsIndian());
			employee.setBasicSalary(dsEmployee.getBasicSalary());
			employee.setPANNumber(dsEmployee.getPANNumber());
			employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
			employees.add(employee);
		}
		return employees;
	}
	public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode)throws BLException
	{
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(designationManager.designationCodeExists(designationCode)==false)
		{
			BLException blException =new BLException();
			blException.setGenericException("Invalid designation code "+designationCode);
		}
		EmployeeInterface employee;
		DesignationInterface dsDesignation;
		DesignationInterface designation;
		Set<EmployeeInterface> employees=new TreeSet<>();
		Set<EmployeeInterface>ets;
		ets=designationCodeWiseEmployeesMap.get(designationCode);
		if(ets==null)
		{
			return employees;
		}
		for(EmployeeInterface dsEmployee:ets)
		{
			employee=new Employee();
			employee.setEmployeeId(dsEmployee.getEmployeeId());
			employee.setName(dsEmployee.getName());
			dsDesignation=dsEmployee.getDesignation();
			designation=new Designation();
			designation.setCode(dsDesignation.getCode());
			designation.setTitle(dsDesignation.getTitle());
			dsEmployee.setDesignation(designation);
			employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
			employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
			employee.setIsIndian(dsEmployee.getIsIndian());
			employee.setBasicSalary(dsEmployee.getBasicSalary());
			employee.setPANNumber(dsEmployee.getPANNumber());
			employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
			employees.add(employee);
		}
		return ets;
	}
	public int getEmployeeCountByDesignationCode(int designationCode)throws BLException
	{
		Set<EmployeeInterface> ets;
		ets=this.designationCodeWiseEmployeesMap.get(designationCode);
		if(ets==null)return 0;
		return ets.size();
	}
	public boolean designationAlloted(int designationCode)throws BLException
	{
		return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
	}
}