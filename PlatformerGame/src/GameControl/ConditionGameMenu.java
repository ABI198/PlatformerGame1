package GameControl;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Gamer.Gamer;
import TileMap.Background;

public class ConditionGameMenu extends GameCondition
{
	
	public static ArrayList<Gamer> gamerList=new ArrayList<Gamer>();;
	
	JFrame gui=new JFrame();
	public JLabel label;
	public JButton button;
	public JTextField textField;
	
	private Background background;

	private int currentSelection=0;
	private String[] menuOptions={"Start","Help","Quit"};
	
	private Color gameTitleColor;
	private Font gameTitleFont;
	
	private Font menuOptionFont;
	
	public ConditionGameMenu(GameConditionManager gcm)
	{
		this.gcm=gcm;
		
		try
		{
			background=new Background("/Backgrounds/JumperBackGround.jpg",1.0);
			background.setVector(-0.1, 0);
			
			gameTitleColor=new Color(128,0,0);
			gameTitleFont=new Font("Century Gothic",Font.PLAIN,28);
			
			menuOptionFont=new Font("Arial",Font.PLAIN,12);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public void initialize() 
	{
	
		
	}
	public void tick() 
	{
		background.tick();
	}
	public void draw(Graphics2D graphic) 
	{
		//draw background
		background.draw(graphic);
		
		//draw title
		graphic.setColor(gameTitleColor);
		graphic.setFont(gameTitleFont);
		graphic.drawString("Platform Game", 65, 30);
		
		//draw menu options
		graphic.setFont(menuOptionFont);
		for(int i=0; i<menuOptions.length;++i)
		{
			if(i==currentSelection)
				graphic.setColor(Color.BLACK);
			else
				graphic.setColor(Color.RED);
			
			graphic.drawString(menuOptions[i],145,50+(i*15));
		}
	}
	
	private void menuOptionSelect()
	{
		if(currentSelection==0)
		{
				gui.setLayout(new FlowLayout());
				label=new JLabel("Name:");
				gui.add(label);
				textField=new JTextField(20);
				gui.add(textField);
				button=new JButton("Save");
				gui.add(button);
				
				
				gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				gui.setResizable(false);
				gui.setSize(400,200);
				gui.setVisible(true);
				gui.setLocation(140,130);
				gui.setTitle("Gamer Register");
				
				event e1=new event();
				button.addActionListener(e1);
					
		}
		else if(currentSelection==1)
		{
			//help
		}
		else if(currentSelection==2)
		{
			System.exit(0);
		}
	}
	
	//SAVE BUTTON EVENT
	public class event implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//Database!
			Gamer newGamer=new Gamer();
			gamerList.add(newGamer);
			newGamer.name=textField.getText();
			
			gui.dispose();
			gui.getContentPane().removeAll();
			gcm.setCondition(GameConditionManager.LEVEL1);
		}
	}
	
	
	
	
	public void keyPressed(int key) 
	{
		if(key==KeyEvent.VK_ENTER)
		{
			menuOptionSelect();
		}
		if(key==KeyEvent.VK_UP)
		{
			--currentSelection;
			if(currentSelection==-1)
				currentSelection=menuOptions.length-1;
		}
		if(key==KeyEvent.VK_DOWN)
		{
			++currentSelection;
			if(currentSelection==menuOptions.length)
			{
				currentSelection=0;
			}
		}
		
	}
	public void keyReleased(int key) 
	{
		
	}
}
