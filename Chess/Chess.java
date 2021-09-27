import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Map.Entry;

class Piece
{
	private JButton piece;
	private int x;
	private int y;
	private char color;
	private String name;
	public void setPiece(JButton piece)
	{
		this.piece=piece;
	}
	public JButton getPiece()
	{
		return this.piece;
	}
	public void setX(int x)
	{
		this.x=x;
	}
	public int getX()
	{
		this.x;
	}
	public void setY(int y)
	{
		this.y=y;
	}
	public int getY()
	{
		this.y;
	}
	public void color(char color)
	{
		this.color=color;
	}
	public  char color()
	{
		return this.color;
	}
	public void name(String name)
	{
		this.name=name;
	}
	public String name()
	{
		return this.name;
	}
}

class Co_ordinates
{
	private int X;
	private int Y;
	Co_ordinates(int X,int Y)
	{
		this.X=X;
		this.Y=Y;
	}
	public void setX(int X)
	{	
		if (X<8) this.X=X;
		else this.X=0;
	}
	public void sety(int Y)
	{
		if (Y<8) this.Y=Y;
		else this.Y=0;
	}
	public int getX()
	{
		return this.X;
	}
	public int getY()
	{
		return this.Y;
	}

   	public int hashCode() 
   	{
    	return Objects.hash(this.X, this.Y);
	}
    public boolean equals(Object obj)
    {
    		Co_ordinates c=(Co_ordinates)obj;
    		//System.out.println("--------------[X = "+c.getX()+", Y = "+c.getY());
    		if(this.X==c.getX() && this.Y==c.getY()) return true;
    		return false;
    }
}


class MoveValidator
{
	public static boolean Pawn(Co_ordinates selectedBtn,Co_ordinates pressedBtn,Map<Co_ordinates,JButton> map)
	{
		int x1=selectedBtn.getX();
		int x2=pressedBtn.getX();
		
		int y1=selectedBtn.getY();
		int y2=pressedBtn.getY();
		System.out.println("[pressed x = "+x2+", y = "+y2+"], Selected	["+"x = "+x1+", y = "+y1+"]");
		Co_ordinates c;
		JButton b=new JButton();
		int i=y2;
		while(y2==7 && y1==5 && i>5)
		{
			i--;
			c=new Co_ordinates(x1,i);
			b=map.get(c);
			System.out.println(" Button text : "+b.getText());
			//b.setIcon(null);
			//System.out.println("X = "+x1+", Y = "+i+" ["+((ImageIcon)b.getIcon()).getDescription()+"]");
			//b.setText("------------------------");
			if(!(b.getText().equals(" ")))
			{
 
				return false;
			}
		}
		if(y2==7 && y1==5 && i==5)
		{
			return true;
		}
		c=new Co_ordinates(x1,y1);
		b=map.get(c);
		System.out.println("Button text : "+b.getText());
		if( ( x2-x1==0 && y2-y1==1 && b.getText().equals(" ")) )return true;
		else if((x2-x1==-1 && y2-y1==1) && ((b.getText().equals(" "))==false)) return true;
		else if((x2-x1==1 && y2-y1==1) &&  ((b.getText().equals(" "))==false)) return true;
		return false;
	}
	public static boolean Rook(Co_ordinates selectedBtn,Co_ordinates pressedBtn,Map<Co_ordinates,JButton> map)
	{

		int x1=selectedBtn.getX();
		int x2=pressedBtn.getX();
		
		int y1=selectedBtn.getY();
		int y2=pressedBtn.getY();

		Co_ordinates c;
		JButton b=new JButton();
		while(x2-x1==0 && y2>y1)
		{
			y2--;
			c=new Co_ordinates(x2,y2);
			b=map.get(c);
			if(y1!=y2 && !(b.getText().equals(" "))) return false;
		}
		if(x2-x1==0 && y2==y1)
		{
			ImageIcon i=((ImageIcon)(b.getIcon()));
			if(i!=null && (i.getDescription().charAt(0)=='B')) return true;
			return true;
		}
		while(x2-x1==0 && y2<y1)
		{
			y2++;
			c=new Co_ordinates(x2,y2);
			b=map.get(c);
			if(y1!=y2 && !(b.getText().equals(" ")))return false;
		}
		if(x2-x1==0 && y2==y1)
		{
			ImageIcon i=((ImageIcon)(b.getIcon()));
			if(i!=null && (i.getDescription().charAt(0)=='B')) return true;
			return true;	
		} 
		while(y2-y1==0 && x2<8)
		{
			x2++;
			c=new Co_ordinates(x2,y2);
			b=map.get(c);
			ImageIcon i=((ImageIcon)(b.getIcon()));
			if(i==null)
			{
				System.out.println("---------X1 = "+x1+",X2 = "+x2+(i==null));
				return false;
			}	
		}
		if(y2-y1==0 && x2==x1)
		{
			System.out.println("[[[[[[[[[[x2 = "+x2+", y2 = "+y2);
			ImageIcon i=((ImageIcon)(b.getIcon()));
			if(i!=null && (i.getDescription().charAt(0)=='B')) return true;
			return true;	
		}
		while(y2-y1==0 && x2>x1)
		{
			x2--;
			c=new Co_ordinates(x2,y2);
			b=map.get(c);
			if(x1!=x2 && (!(b.getText().equals(" "))))return false;
		}
		if(y2-y1==0 && x2==x1) 
		{
			ImageIcon i=((ImageIcon)(b.getIcon()));
			if(i!=null && (i.getDescription().charAt(0)=='B')) return true;
			return true;	
		}
		return false;
	}
}	
public class Chess extends JFrame
{
	 
	String selectedChoice;
	private Map<Co_ordinates,JButton> buttons;
	private JFrame frame;
	private JPanel topAlphabetsPanel;
	private JPanel bottomAlphabetsPanel;
	private JPanel leftNumbersPanel;
	private JPanel rightNumbersPanel;
	private JLabel board;
	private JPanel boardPanel;
	private JButton pressedButton;
	private JButton box;
	public Icon selectedButtonImage=new ImageIcon();
	Chess()
	{
		frame=new JFrame();
		buttons=new HashMap<>();
		selectedButtonImage=null;
		pressedButton=null;
		boardPanel=new JPanel(new GridLayout(8,8));
		topAlphabetsPanel=new JPanel(new GridLayout(1,8));
		bottomAlphabetsPanel=new JPanel(new GridLayout(1,8));
		leftNumbersPanel=new JPanel(new GridLayout(8,1));
		rightNumbersPanel=new JPanel(new GridLayout(8,1));
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
		chooseColor();
	}
	public void chooseColor()
	{
		JFrame choiceFrame=new JFrame();
		JPanel choiceTextPanel=new JPanel();
		choiceTextPanel.add(new Label("Select your choice."));
		JPanel choicePanel=new JPanel(new GridLayout(1,2));
		JButton blackChoice=new JButton();
		JButton whiteChoice=new JButton();

		blackChoice.setIcon(new ImageIcon("BlackKing.png"));
		whiteChoice.setIcon(new ImageIcon("WhiteKing.png"));
		blackChoice.addActionListener(new ActionListener(){
			public  void actionPerformed(ActionEvent e)
			{
				selectedChoice="Black";
				arrangeTokens(selectedChoice);
				choiceFrame.disable();
			}
		});
		whiteChoice.addActionListener(new ActionListener(){
			public  void actionPerformed(ActionEvent e)
			{
				selectedChoice="White";
				arrangeTokens(selectedChoice);
				choiceFrame.disable();
			}
		});
		blackChoice.setForeground(new Color(252,204,116));
		blackChoice.setBackground(new Color(252,204,116));
		whiteChoice.setForeground(new Color(87,58,46));
		whiteChoice.setBackground(new Color(87,58,46));


		choiceFrame.add(choiceTextPanel,BorderLayout.NORTH);
		choicePanel.add(blackChoice);
		choicePanel.add(whiteChoice);

		choiceFrame.add(choicePanel,BorderLayout.CENTER);
		choiceFrame.setSize(300,180);
		choiceFrame.setLocation(610,300);
		choiceFrame.show();
	}
	public void arrangeTokens(String selectedChoice)
	{
		String second;
		ImageIcon ic;
		if (selectedChoice.equals("White"))second="Black";
		else second="White";
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
				box=new JButton();
				box.setForeground(c);
				box.setBackground(c);
				box.setPreferredSize(new Dimension(90, 90));
				if(i==1)
				{
					ic=new ImageIcon(second+"Pawn.png");
					ic.setDescription(second+"Pawn");
					box.setIcon(ic);
				}
				else if(i==0 && (j==0 || j==7))
				{
					ic=new ImageIcon(second+"Rook.png");
					ic.setDescription(second+"Rook");
					box.setIcon(ic);
				}
				else if(i==0 && (j==1 || j==6))
				{
					ic=new ImageIcon(second+"Knight.png");
					ic.setDescription(second+"Knight");
					box.setIcon(ic);
				}
				else if(i==0 && (j==2 || j==5))
				{
					ic=new ImageIcon(second+"Bishop.png");
					ic.setDescription(second+"Bishop");
					box.setIcon(ic);
				}
				else if(i==0 && j==4)
				{
					ic=new ImageIcon(second+"King.png");
					ic.setDescription(second+"King");
					box.setIcon(ic);
				}
				else if(i==0 && j==3)
				{
					ic=new ImageIcon(second+"Queen.png");
					ic.setDescription(second+"Queen");
					box.setIcon(ic);
				}
				else if(i==6)
				{
					ic=new ImageIcon(selectedChoice+"Pawn.png");
					ic.setDescription(selectedChoice+"Pawn");
					box.setIcon(ic);
				}
				else if(i==7 && (j==0 || j==7))
				{
					ic=new ImageIcon(selectedChoice+"Rook.png");
					ic.setDescription(selectedChoice+"Rook");
					box.setIcon(ic);
				}
				else if(i==7 && (j==1 || j==6))
				{
					ic=new ImageIcon(selectedChoice+"Knight.png");
					ic.setDescription(selectedChoice+"Knight");
					box.setIcon(ic);
				}
				else if(i==7 && (j==2 || j==5))
				{
					ic=new ImageIcon(selectedChoice+"Bishop.png");
					ic.setDescription(selectedChoice+"Bishop");
					box.setIcon(ic);
				}
				else if(i==7 &&  j==3)
				{
					ic=new ImageIcon(selectedChoice+"King.png");
					ic.setDescription(selectedChoice+"King");
					box.setIcon(ic);
				}
				else if(i==7 &&  j==4)
				{
					ic=new ImageIcon(selectedChoice+"Queen.png");
					ic.setDescription(selectedChoice+"Queen");
					box.setIcon(ic);
				}
				else
				{
					box.setIcon(null);
					box.setText(" ");
				}
				box.addActionListener(new ActionListener() {
        		 			public void actionPerformed(ActionEvent e)
        		 			{

        		 					JButton selectedButton=(JButton)e.getSource();
        		 					ImageIcon icn=(ImageIcon)selectedButton.getIcon();
        		 					if(icn!=null && selectedButtonImage==null)
        		 					{
        		 						selectedButtonImage=icn;
        		 						pressedButton=selectedButton;
        		 					}
        		 					else if( ((selectedButtonImage!=null && icn!=null) && (( ((ImageIcon)selectedButtonImage).getDescription().charAt(0))!=((icn.getDescription()).charAt(0))  ))  || (selectedButtonImage!=null && icn==null))
        		 					{
        		 						if(validateMove(selectedButton,pressedButton))
        		 						{
        		 							pressedButton.setIcon(null);
        		 							selectedButton.setIcon(selectedButtonImage);
        		 						}
        		 						selectedButtonImage=null;
										pressedButton=null;
        		 					}
        		 					else
        		 					{
        		 						pressedButton=null;
        		 						selectedButtonImage=null;	
        		 					}
        		 			}
      					});		
 				boardPanel.add(box);
 				buttons.put(new Co_ordinates(j+1,i+1),box);
			}
		}
	frame.setLocation(200,50);
	frame.setSize(924,800);
	frame.show();	
	}
	public void updateCo_ordinates(JButton selectedBtn,JButton pressedBtn)
	{
		Co_ordinates c1=new Co_ordinates(selectedBtn.getX()%10,selectedBtn.getY()%10);
		Co_ordinates c2=new Co_ordinates(pressedBtn.getX()%10,pressedBtn.getY()%10);
		for(Entry<Co_ordinates, JButton> entry: this.buttons.entrySet())
		{
    	  if(entry.getKey().equals(c2))
    	  {
    	  	entry.getValue().setText(" ");
    	  	System.out.println("Pehle wali : X = "+c2.getX()+", Y = "+c2.getY()+"|| Bad wali : X ="+c1.getX()+", Y = "+c1.getY());
    	  	buttons.put(c1,pressedBtn);
    	    break;
    	  }
    	}
	}
	public boolean validateMove(JButton selectedBtn,JButton pressedBtn)
	{	
		Co_ordinates c1=new Co_ordinates(selectedBtn.getX()%10,selectedBtn.getY()%10);
		Co_ordinates c2=new Co_ordinates(pressedBtn.getX()%10,pressedBtn.getY()%10);
		String piece=((ImageIcon)pressedBtn.getIcon()).getDescription();
		if(piece.equals("WhitePawn"))
		{	
			if(MoveValidator.Pawn(c1,c2,buttons))
			{
				for(Entry<Co_ordinates, JButton> entry: this.buttons.entrySet())
				{
    			  if(entry.getKey().equals(c2))
    			  {
    			  	entry.getValue().setText(" ");
    			  	System.out.println("Pehle wali : X = "+c2.getX()+", Y = "+c2.getY()+"|| Bad wali : X ="+c1.getX()+", Y = "+c1.getY());
    			  	buttons.put(c1,pressedBtn);
    			    break;
    			  }
    			}
    			return true;
			}
		}
		else if(piece.equals("WhiteRook"))
		{
			if(MoveValidator.Rook(c1,c2,buttons))
			{
				for(Entry<Co_ordinates, JButton> entry: this.buttons.entrySet())
				{
    			  if(entry.getKey().equals(c2))
    			  {
    			  	entry.getValue().setText(" ");
    			  	System.out.println("Pehle wali : X = "+c2.getX()+", Y = "+c2.getY()+"|| Bad wali : X ="+c1.getX()+", Y = "+c1.getY());
    			  	buttons.put(c1,pressedBtn);
    			    break;
    			  }
    			}
    			return true;
			}	
		}
		return false;
	}
	public static void main(String gg[])
	{
		Chess c=new Chess();
	}
}