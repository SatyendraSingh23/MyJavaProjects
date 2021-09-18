import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeGetCountTestCase
{
	public static void main(String gg[])
	{
		try
		{
		EmployeeDTOInterface employeeDTO;
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		System.out.println("Number of employees : "+employeeDAO.getCount());
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}