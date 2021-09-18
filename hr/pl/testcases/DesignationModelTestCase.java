import com.thinking.machines.pl.model.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
public class DesignationModelTestCase extends JFrame
{
	private JTable table;
	private JScrollPane jsp;
	private Container container;
	public DesignationModel dm;
	DesignationModelTestCase()
	{
		dm=new DesignationModel();
		table=new JTable(dm);
		Font font = new Font("Times New Roman",Font.PLAIN,24);
		table.setFont(font);
		table.setRowHeight(30);
		jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		container=getContentPane();
		container.setLayout(new BorderLayout());
		container.add(jsp);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width=800;
		int height=600;
		setSize(width,height);
		int x=(d.width/2)-(width/2);
		int y=(d.height/2)-(height/2);
		setLocation(x,y);
		setVisible(true);
	}
}
class DesignationView
{
	public static void main(String args[])
	{
		DesignationModelTestCase d=new DesignationModelTestCase();
	}
}