package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import GameControl.GameConditionManager;


public class GameLoop extends JPanel implements Runnable,KeyListener
{
	//Game Thread
	private Thread thread;
	private int FPS=60; //Frame Per Second
	private long targetTime=1000/FPS; //target time will be use for thread waiting!
	private boolean running;
	
	//Game Condition Manager
	private GameConditionManager gcm;
	
	//Game Window Dimensions
	public final static int Width=320;
	public final static int Height=240;
	public final static int Scale=2;
	
	//Canvas Elements
	private BufferedImage img;
	private Graphics2D graphic;
	
	//Constructor
	public GameLoop()
	{
		//set game window dimensions
		Dimension ds=new Dimension(Width*Scale,Height*Scale);
		setPreferredSize(ds);
		setFocusable(true);
		requestFocus();
	}
	
	private void initialize()
	{
		running=true; 
		img=new BufferedImage(Width,Height,BufferedImage.TYPE_INT_RGB);
		graphic=(Graphics2D)img.getGraphics(); //For drawing!
		
		gcm=new GameConditionManager();
	}
	
	
	public void addNotify()//The game panel is done loading so a I can go ahead and start a new thread!!
	{
		super.addNotify();
		if(thread==null) //if thread is null,then create a new thread!
		{
			thread=new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	

	public void run()
	{
		initialize();
		
		//timers
		long start;
		long timeTaken;
		long waiting;
		
		
		//Game Loop
		while(running) 
		{
			start=System.nanoTime();
			
			tick();
			draw();
			drawGameWindow();
			
			timeTaken=System.nanoTime()-start;
			
			
			waiting=targetTime-timeTaken/1000000; //TargetTime->ms and elapsed->ns  so it has been divided by 1000000
			if(waiting<0) //This checking is important because there is no any negative waiting time.This is nonsense
				waiting=5;
			
			try
			{
				Thread.sleep(waiting); //so current thread must wait for a bit while
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	
	private void tick() //For updates
	{
		gcm.tick();
	}
	
	
	private void draw() //Every graphics will be draw in here!
	{
		gcm.draw(graphic);
	}
	
	private void drawGameWindow()
	{
		Graphics graphicForWindow=getGraphics(); //This is just game window graphics object
		graphicForWindow.drawImage(img,0,0,Width*Scale,Height*Scale,null);
		graphicForWindow.dispose(); //This is necessary to quit the frame!
	}

	public void keyPressed(KeyEvent key) 
	{
		gcm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) 
	{
		gcm.keyReleased(key.getKeyCode());
	}
	public void keyTyped(KeyEvent key) 
	{
	
	}
}
