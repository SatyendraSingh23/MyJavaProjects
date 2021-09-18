import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeAadharCardNumberExistsTestCase
{
	public static void main(String gg[])
	{
		try
		{
		String AadharCardNumber= gg[0];
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		EmployeeDTOInterface employeeDTO;
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		if(employeeDAO.aadharCardNumberExists(AadharCardNumber))
		System.out.println("Aadhar card number : "+AadharCardNumber+" exists");
		else
		System.out.println("Aadhar card number : "+AadharCardNumber+" not exists");
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}