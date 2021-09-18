import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
public class DesignationTitleExistTestCase
{
	public static void main(String gg[])
	{
		Set<DesignationDTOInterface> designations=new TreeSet<>();
		String title=gg[0].trim();
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			System.out.println(title);
			if(designationDAO.titleExists(title))System.out.println("Found");
			else System.out.println("Not Found");
			designations=designationDAO.getAll();
			designations.forEach((d)->{System.out.printf("code : %d, Title %s \n",d.getCode(),d.getTitle());});
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}
