import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Chess extends JFrame
{
	public JButton[][] boxes=new JButton[8][8];
	private JFrame frame;
	private JPanel topAlphabetsPanel;
	private JPanel bottomAlphabetsPanel;
	private JPanel leftNumbersPanel;
	private JPanel rightNumbersPanel;
	private JLabel board;
	private JPanel boardPanel;
	private JButton pressedButton;
	public Icon selectedButtonImage=new ImageIcon();
	Chess()
	{
		frame=new JFrame();
		selectedButtonImage=null;
		boardPanel=new JPanel(new GridLayout(8,8));
		topAlphabetsPanel=new JPanel(new GridLayout(1,10));
		bottomAlphabetsPanel=new JPanel(new GridLayout(1,10));
		leftNumbersPanel=new JPanel(new GridLayout(8,1));
		rightNumbersPanel=new JPanel(new GridLayout(8,1));
		topAlphabetsPanel.add(new JLabel(""));
		bottomAlphabetsPanel.add(new JLabel(""));
		for(int i=0;i<8;i++)
		{	
			topAlphabetsPanel.add(new JLabel(new String((char)(i+65)+"")),BorderLayout.CENTER);
			bottomAlphabetsPanel.add(new JLabel(new String((char)(i+65)+"")),BorderLayout.CENTER);
			leftNumbersPanel.add(new JLabel(new String(" "+(i+1))),BorderLayout.CENTER);
			rightNumbersPanel.add(new JLabel(new String((i+1)+" ")),BorderLayout.CENTER);
		}
		frame.add(topAlphabetsPanel,BorderLayout.NORTH);
		frame.add(bottomAlphabetsPanel,BorderLayout.SOUTH);
		frame.add(leftNumbersPanel,BorderLayout.WEST);
		frame.add(rightNumbersPanel,BorderLayout.EAST);
		frame.add(boardPanel,BorderLayout.CENTER);
		Color c=new Color(252,204,116);
		boolean flag=true;
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
				boxes[i][j].setPreferredSize(new Dimension(90, 90));
				if(i==1)boxes[i][j].setIcon(new ImageIcon("WhitePawn.png"));
				if(i==0 && (j==0 || j==7))boxes[i][j].setIcon(new ImageIcon("WhiteRook.png"));
				if(i==0 && (j==1 || j==6))boxes[i][j].setIcon(new ImageIcon("WhiteKnight.png"));
				if(i==0 && (j==2 || j==5))boxes[i][j].setIcon(new ImageIcon("WhiteBishop.png"));
				if(i==0 && j==4)boxes[i][j].setIcon(new ImageIcon("WhiteQueen.png"));
				if(i==0 && j==3)boxes[i][j].setIcon(new ImageIcon("WhiteKing.png"));
				if(i==6)boxes[i][j].setIcon(new ImageIcon("BlackPawn.png"));
				if(i==7 && (j==0 || j==7))boxes[i][j].setIcon(new ImageIcon("BlackRook.png"));
				if(i==7 && (j==1 || j==6))boxes[i][j].setIcon(new ImageIcon("BlackKnight.png"));
				if(i==7 && (j==2 || j==5))boxes[i][j].setIcon(new ImageIcon("BlackBishop.png"));
				if(i==7 &&  j==3)boxes[i][j].setIcon(new ImageIcon("BlackKing.png"));
				if(i==7 &&  j==4)boxes[i][j].setIcon(new ImageIcon("BlackQueen.png"));
				boxes[i][j].addActionListener(new ActionListener() {
         					public void actionPerformed(ActionEvent e)
         					{
         						JButton selectedButton=(JButton)e.getSource();
         						if(selectedButton.getIcon()!=null && selectedButtonImage==null)
         						{
         							selectedButtonImage=selectedButton.getIcon();
         							pressedButton=selectedButton;
         						}
         						else if(selectedButton.getIcon()==null && selectedButtonImage!=null)
         						{
         							pressedButton.setIcon(null);
         							selectedButton.setIcon(selectedButtonImage);
         							selectedButtonImage=null;
         						}
         						else
         						{
         							pressedButton=null;
         							selectedButtonImage=null;
         							System.out.println(".");	
         						}
         					}
      					});
 				boardPanel.add(boxes[i][j]);				
			}
		}
		//boxes[0][4].setPreferredSize(new Dimension(90, 90));
		frame.setSize(924,800);
		frame.show();
	}

	public static void main(String gg[])
	{
		Chess c=new Chess();
	}
}