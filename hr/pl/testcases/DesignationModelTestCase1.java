import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.awt.*;
import javax.swing.*;

class DesignationModelTestCase1 extends JFrame
{
private DesignationModel designationModel;
private JTable tb;
private Container container;
DesignationModelTestCase1()
{
this.designationModel=new DesignationModel();
tb=new JTable(this.designationModel);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(tb);
setLocation(100,300);
setSize(500,400);
setVisible(true);
}
public static void main(String []gg)
{
DesignationModelTestCase1 d=new DesignationModelTestCase1();
}
}