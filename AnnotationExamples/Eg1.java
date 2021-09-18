import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ThinkingMachines
{

}
@ThinkingMachines
class abcd
{

}
class pqrst
{

}
class psp
{
	public static void main(String gg[])
	{
		Class a = abcd.class;// Class a = Class.forName("abcd");
		Class p=pqrst.class; // Class p=Class.forName("pqrst");
		Annotation aa=a.getAnnotation(ThinkingMachines.class);
		System.out.println(aa);
	}
}