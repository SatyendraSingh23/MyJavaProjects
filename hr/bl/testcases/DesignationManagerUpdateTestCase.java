import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
class DesignationManagerUpdateTestCase
{
	public static void main(String gg[])
	{
		int code=Integer.parseInt(gg[0]);
		String title=gg[1];
		DesignationInterface designation=new Designation();
		designation.setTitle(title);
		designation.setCode(code);
		try
		{
			DesignationManagerInterface designationManager;
			designationManager=DesignationManager.getDesignationManager();
			designationManager.updateDesignation(designation);
			System.out.println("Designation updated with code as : "+designation.getCode());
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