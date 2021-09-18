import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.math.*;
import java.util.*;
import java.text.*;
public class EmployeeUpdateTestCase
{
	public static void main(String gg[])
	{
		String employeeId=gg[0];
		String name = gg[1];
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		int designatioCode=Integer.parseInt(gg[2]);
		Date dateOfBirth=null;
		try
		{
			dateOfBirth=sdf.parse(gg[3]);
		}catch(ParseException pe)
		{
			System.out.println(pe.getMessage());
		}
		char gender=gg[4].charAt(0);
		boolean isIndian=Boolean.parseBoolean(gg[5]);
		BigDecimal basicSalary=new BigDecimal(gg[6]);
		String panNumber=gg[7];
		String aadharCardNumber=gg[8];
		try
		{
			EmployeeDTOInterface employeeDTO;
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId(employeeId);
			employeeDTO.setName(name);
			employeeDTO.setDesignationCode(designatioCode);
			employeeDTO.setDateOfBirth(dateOfBirth);
			if(gender=='M')
			{
			employeeDTO.setGender(GENDER.MALE);	
			}
			if(gender=='F')
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(isIndian);
			employeeDTO.setBasicSalary(basicSalary);
			employeeDTO.setPANNumber(panNumber);
			employeeDTO.setAadharCardNumber(aadharCardNumber);
			EmployeeDAOInterface employeeDAO=new EmployeeDAO();
			employeeDAO.update(employeeDTO);
			System.out.println("Employee updated with  employeeID with "+employeeDTO.getEmployeeId());
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}
