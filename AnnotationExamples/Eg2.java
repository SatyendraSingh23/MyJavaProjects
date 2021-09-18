import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ThinkingMachines
{
	public int value() default 10;
	public string city() default "Ujjain"; 
}
@ThinkingMachines(values=40,city="Indore")
class abcd
{

}
class psp
{
	public static void main(String gg[])
	{
		Class a=abcd.class; //Class a = Class.forName("abcd");
		Annotation aa=a.getAnnotation(ThinkingMachines.class);
		if(aa!=null)
		{
			ThinkingMachines tm=(ThinkingMachines.class);
			System.out.println(tm.value);
			System.out.println(tm.city);
		}
	}
}