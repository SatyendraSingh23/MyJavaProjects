import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
public class DesignationDAOGetAll
{
	public static void main(String gg[])
	{
		Set<DesignationDTOInterface> designations=new TreeSet<>();
		try
		{
			DesignationDTOInterface designationDTO;
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			designations=designationDAO.getAll();
			designations.forEach((d)->{System.out.printf("code : %d, Title %s \n",d.getCode(),d.getTitle());});
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}
