import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import java.text.*;
import java.math.*;
class EmployeeManagerRemoveTestCase
{
	public static void main(String gg[])
	{
		try
		{
			String employeeId=gg[0];
			DesignationInterface designation = new Designation(); 
			EmployeeManagerInterface employeeManager;
			employeeManager=EmployeeManager.getEmployeeManager();
			employeeManager.removeEmployee(employeeId);
			System.out.printf("Employee deleted ."); 
		}catch(BLException blException)
		{
			if(blException.hasGenericException())System.out.println(blException.getGenericException());
			List<String>properties =blException.getProperties();
			for(String property : properties)
			{
				System.out.println(blException.getException(property));
			}
		}
	}
}	