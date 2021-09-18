import java.lang.annotation.*;
import java.lang.reflect.*;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
@interface Whatever
{

}
class abcd
{
	private String data1;
	private int data2;
	private int data3;
	public int data4;
	protected int data5;
	int data6;
}
class Eg4
{
	public static void main(String gg[])
	{
		Class a=abcd.class;
		Field [] flds;
				// to get all protected fields
		flds=a.getFields(); // getDeclaredFields() se sari private properties ke bare me pata lag jata he. 
		for(int e=0;e<flds.length;e++)
		{
			System.out.println("Property : "+flds[e].getName());
		}
	}
}