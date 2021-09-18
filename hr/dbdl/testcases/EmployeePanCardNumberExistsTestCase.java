import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeePanCardNumberExistsTestCase
{
	public static void main(String gg[])
	{
		try
		{
		String PANCardNumber= gg[0];
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		EmployeeDTOInterface employeeDTO;
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		if(employeeDAO.panNumberExists(PANCardNumber))
		System.out.println("PAN card number : "+PANCardNumber+" exists");
		else
		System.out.println("PAN card number : "+PANCardNumber+" not exists");
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}