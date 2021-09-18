import java.util.*;
import javax.swing.*;
import java.awt.*;
class test extends JFrame
{
	private JFrame frame;
	public test()
	{
		frame=new JFrame();
		frame.setLayout(null);
		frame.setSize(500,700);
		frame.setLocation(500,80);
		frame.setVisible(true);
	}
}
class testpsp
{
	public static void main(String gg[])
	{
		test t = new test();
	}
}