import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao .*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao .*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeDesignationAllotedTestCase
{
	public static void main(String gg[])
	{
		int designationCode =Integer.parseInt(gg[0]);
		try
		{
		EmployeeDTOInterface employeeDTO=new EmployeeDTO();
		EmployeeDAOInterface employeeDAO=new EmployeeDAO();
		System.out.println("Employee with Designation Code "+designationCode+" exists "+employeeDAO.isDesignationAlloted(designationCode));
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}