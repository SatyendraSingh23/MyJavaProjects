import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeGetByEmployeeIdTestCase
{
	public static void main(String gg[])
	{
		String employeeId = gg[0];
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		EmployeeDTOInterface employeeDTO;
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		try
		{
			employeeDTO=employeeDAO.getByEmployeeID(employeeId);
			System.out.println("Name : "+employeeDTO.getName()+"\n"+"Employee ID : "+employeeDTO.getEmployeeId()+"\n"+"Designation Code : "+employeeDTO.getDesignationCode()+"\n"+"Date of Birth : "+simpleDateFormat.format(employeeDTO.getDateOfBirth())+"\n"+"Gender : "+employeeDTO.getGender()+"\n"+"Is INDIAN : "+employeeDTO.getIsIndian()+"\n"+"Basic Salary : "+employeeDTO.getBasicSalary()+"\n"+"PAN Number: "+employeeDTO.getPANNumber()+"\n"+"Aadhar Card Number : "+employeeDTO.getAadharCardNumber());
			
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}