import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
public class DesignationDAODeleteTestCase
{
	public static void main(String gg[])
	{
		Set<DesignationDTOInterface> designations=new TreeSet<>();
		int code=Integer.parseInt(gg[0]);
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			System.out.println("Code is "+code);
			designationDAO.delete(code);
			designations=designationDAO.getAll();
			designations.forEach((d)->{System.out.printf("code : %d, Title %s \n",d.getCode(),d.getTitle());});
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}
