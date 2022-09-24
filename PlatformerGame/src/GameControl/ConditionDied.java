package GameControl;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Entity.Player;
import TileMap.Background;

public class ConditionDied extends GameCondition
{
	//GameOverControl
	public static int gameOverFlag;


	
	JFrame gui=new JFrame();	
	public JLabel label;
	public JButton button1,button2;

	
	ConditionLevelOne level1;
	
	private Background background;
	
	public ConditionDied(GameConditionManager gcm)
	{
		this.gcm=gcm;
		initialize();
		
		try
		{
			background=new Background("/Backgrounds/gameover.png",1.0);
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
		if(gameOverFlag==1)
		{
			gui.setLayout(new FlowLayout());
			label=new JLabel("Do you want to play a new game or not?   Y/N");
			gui.add(label);
			button1=new JButton("Yes");
			gui.add(button1);
			button2=new JButton("No");
			gui.add(button2);
			gameOverFlag=0;
			gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			gui.setResizable(false);
			gui.setSize(400,100);
			gui.setVisible(true);
			gui.setLocation(130,40);
			gui.setTitle("Game Over");
			
			
			event e1=new event();
			button1.addActionListener(e1);
			
			event2 e2=new event2();
			button2.addActionListener(e2);
		}
		
		
	
		background.tick();
	}
	
	//BUTTON EVENTS
	public class event implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			gui.dispose();
			gui.getContentPane().removeAll();
			gcm.setCondition(GameConditionManager.GAMEMENU);
			
		}
	}
	public class event2 implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			System.exit(0);
		}
	}
	
	public void draw(Graphics2D graphic) 
	{
		background.draw(graphic);
	}
	public void keyPressed(int k) 
	{
		
		
	}
	public void keyReleased(int k) 
	{
		
		
	}
}
