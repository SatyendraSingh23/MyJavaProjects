import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import java.text.*;
import java.math.*;
class EmployeesByDesignationCodeTestCase
{
	public static void main(String gg[])
	{
		int code = Integer.parseInt(gg[0]);
		EmployeeManagerInterface employeeManager;
		Set<EmployeeInterface> ets=new TreeSet<>();
		DesignationInterface dsn=new Designation();
		try
		{
			employeeManager=EmployeeManager.getEmployeeManager();
			 ets=employeeManager.getEmployeesByDesignationCode(code);
			 for(EmployeeInterface e : ets)
			 {
				 dsn=e.getDesignation();
				 System.out.println("Designation Code : "+dsn.getCode()+" , Designation Title : "+dsn.getTitle());
				 System.out.println("Employee Id. : "+e.getEmployeeId());
				 System.out.println("Name : "+e.getName());
				 System.out.println("Gender : "+e.getGender());
				 System.out.println("Date of Birth : "+e.getDateOfBirth());
				 System.out.println("Is Indian : "+e.getIsIndian());
				 System.out.println("Basic salary : "+e.getBasicSalary());
				 System.out.println("Aadhar card number : "+e.getAadharCardNumber());
				 System.out.println("Pan card number : "+e.getPANNumber());
				 System.out.println("************************************************************************************");
				 System.out.println("************************************************************************************");
			 }
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