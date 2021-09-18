import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessBoard extends JFrame
{

	Color c=new Color(252,204,116);
	boolean flag=true;
	private JFrame frame;
	private Container container;
	private JPanel panel;
	private JLabel board;

	private JLabel wordsLabel;
	private JLabel numbersLabel;
	private JButton[][] boxes=new JButton[8][8];
	ChessBoard()
	{
		frame=new JFrame();
		board=new JLabel();
		panel=new JPanel(new BorderLayout());
		//setTitle("Chess");
		container=getContentPane();
		panel.add(new JLabel("  		A  			B  			C  			D  			E  			F  			G  			H"),BorderLayout.NORTH);
		panel.add(new JLabel("  		A  			B  			C  			D  			E  			F  			G  			H"),BorderLayout.SOUTH);
		//panel.add(new JLabel("			1 			2 			3 			4 			5 			6 			7 			8"),BorderLayout.WEST);
		//panel.add(new JLabel("			1 			2 			3 			4 			5 			6 			7 			8"),BorderLayout.EAST);
		//container.setLayout(new GridLayout(1,1));
		
		for(int i=0;i<8;i++)
		{
			if(flag==true)
			{
				c=new Color(87,58,46);
				flag=false;
			}
			else
			{
				c=new Color(252,204,116);
				flag=true;
			}			
			for(int j=0;j<8;j++)
			{
				if(flag==true)
				{
					c=new Color(87,58,46);
					flag=false;
				}
				else
				{
					c=new Color(252,204,116);
					flag=true;
				}
				boxes[i][j]=new JButton();
				boxes[i][j].setForeground(c);
				boxes[i][j].setBackground(c);
				if(i==1)boxes[i][j].setIcon(new ImageIcon("WhitePawn.png"));
				if(i==6)boxes[i][j].setIcon(new ImageIcon("BlackPawn.png"));
 				//board.add(boxes[i][j]);				
			}
		}
		
		boxes[0][0].setIcon(new ImageIcon("WhiteRook.png"));
		boxes[0][7].setIcon(new ImageIcon("WhiteRook.png"));
		boxes[0][2].setIcon(new ImageIcon("WhiteBishop.png"));
		boxes[0][5].setIcon(new ImageIcon("WhiteBishop.png"));
		//panel.add(board,BorderLayout.CENTER);
		frame.add(panel);
		frame.setSize(1024,760);
		//frame.setLocation(100,100);
		frame.show();
	}
	
	public static void main(String gg[])
	{
		ChessBoard s=new ChessBoard();
	}

}
