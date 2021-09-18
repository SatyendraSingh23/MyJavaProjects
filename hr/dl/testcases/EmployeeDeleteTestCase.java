import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeDeleteTestCase
{
	public static void main(String gg[])
	{
		String employeeId = gg[0];
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		try
		{
			employeeDAO.delete(employeeId);
			System.out.println("Employee deleted");
			
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}