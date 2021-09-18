import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
public class DesignationManagerGetDesignationsTestCase
{
	public DesignationManagerInterface designationManager;
	public Set<DesignationInterface> designations;
	public DesignationManagerGetDesignationsTestCase()
	{
		designations=new TreeSet<>();
		try
		{
		designationManager=DesignationManager.getDesignationManager();
		designations=designationManager.getDesignations();
		System.out.println(designations.size());
		}catch(BLException b)
		{
			//
		}
	}
}
class getDesignationsPsp
{
	public static void main(String gg[])
	{
		DesignationManagerGetDesignationsTestCase d = new DesignationManagerGetDesignationsTestCase();
		for(DesignationInterface dd : d.designations)
		{
			System.out.println(dd.getCode()+", "+dd.getTitle());
		}
	}
}