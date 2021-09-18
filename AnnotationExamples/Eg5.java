import com.google.gson.*;
import com.google.gson.annotations.*;

class student implements java.io.Serializable
{
	@Expose
	public int rollNumber;
	@Expose(serialize=true,deserialize=true)
	public String name;
	@Expose(serialize=true,deserialize=true)
	public char gender;
}
class Eg5
{
	public static void main(String gg[])
	{
		try
		{
			student s=new student();
			s.name="Satyendra Singh";
			s.rollNumber=101;
			s.gender='M';
			Gson g=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			String jsonString=g.toJson(s);
			System.out.println("Json String : "+jsonString);
			student s2=(student)g.fromJson(jsonString,student.class);
			System.out.println(s2.rollNumber+","+s2.name+","+s2.gender);
		}catch(Exception exception)
		{
			System.out.println(exception.getMessage());
		}
	}
}