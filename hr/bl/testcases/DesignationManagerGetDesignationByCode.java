import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
class DesignationManagerGetDesignationByCode
{
	public static void main(String gg[])
	{
		int code=Integer.parseInt(gg[0]);
		try
		{
			DesignationInterface designation=new Designation();
			DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
			designation=designationManager.getDesignationByCode(code);
			System.out.println("Code : "+designation.getCode()+" Title : "+designation.getTitle());
		}catch(BLException blException)
		{
			if(blException.hasGenericException())
			{
				System.out.println(blException.getGenericException());
			}
			List<String>properties=blException.getProperties();
			for(String property:properties)
			{
				System.out.println(blException.getException(property));
			}
		}
	}
}