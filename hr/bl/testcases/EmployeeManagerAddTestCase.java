import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import java.text.*;
import java.math.*;
class EmployeeManagerAddTestCase
{
	public static void main(String gg[])
	{
		try
		{
			String name="Uddeshya Narayan";
			DesignationInterface designation = new Designation(); 
			designation.setCode(2);
			Date dateOfBirth=new Date();
			boolean isIndian=true;
			BigDecimal basicSalary=new BigDecimal("10000");
			String panNumber="GSLPS1810f";
			String aadharCardNumber="U12342";
			char gender='M';
			EmployeeInterface employee;
			employee=new Employee();
			employee.setName(name);
			employee.setDesignation(designation);
			employee.setDateOfBirth(dateOfBirth);
			employee.setGender(GENDER.MALE);
			employee.setIsIndian(isIndian);
			employee.setBasicSalary(basicSalary);
			employee.setPANNumber(panNumber);
			employee.setAadharCardNumber(aadharCardNumber);
			EmployeeManagerInterface employeeManager;
			employeeManager=EmployeeManager.getEmployeeManager();
			employeeManager.addEmployee(employee);
			System.out.printf("Employee added with employee Id .%s\n",employee.getEmployeeId()); 
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