import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ThinkingMachines
{
	public int value() default 10;
	public String city() default "Ujjain";
}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Institute
{

}

@ThinkingMachines(value=40,city="Indore")
@Institute
class abcd
{

}
class psp
{
public static void main(String gg[])
{
	Class a=abcd.class;
	Annotation an[]=a.getAnnotations();
	for(int e=0;e<an.length;e++)
	{
		System.out.println(an[e]);
		if(an[e] instanceof Institute)
		{
			System.out.println("Institute annotation has been applied.");
		}
		if(an[e] instanceof ThinkingMachines)
		{
			ThinkingMachines tm=(ThinkingMachines)an[e];
			System.out.println("Thinking Machines annotation applied with value as : "+tm.value()+" and city as : "+tm.city());
		}
	}
}	
}