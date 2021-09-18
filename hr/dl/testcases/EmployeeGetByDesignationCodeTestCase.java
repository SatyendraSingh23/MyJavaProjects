import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeGetByDesignationCodeTestCase
{
	public static void main(String args[])
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		Set<EmployeeDTOInterface> employees=new TreeSet<>();
		EmployeeDTOInterface employeeDTO=new EmployeeDTO();
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		try
		{
			employees=employeeDAO.getByDesignationCode(5);
			for(EmployeeDTOInterface employee : employees)
			{
				System.out.println("Name : "+employee.getName()+"\n"+"Employee ID : "+employee.getEmployeeId()+"\n"+"Designation Code : "+employee.getDesignationCode()+"\n"+"Date of Birth : "+simpleDateFormat.format(employee.getDateOfBirth())+"\n"+"Gender : "+employee.getGender()+"\n"+"Is INDIAN : "+employee.getIsIndian()+"\n"+"Basic Salary : "+employee.getBasicSalary()+"\n"+"PAN Number: "+employee.getPANNumber()+"\n"+"Aadhar Card Number : "+employee.getAadharCardNumber());
				System.out.println("**********************************************************");
			}
			
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}