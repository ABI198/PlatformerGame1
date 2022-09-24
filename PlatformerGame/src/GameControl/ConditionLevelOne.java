package GameControl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Entity.Player;
import Gamer.Gamer;
import Main.GameLoop;
import TileMap.Background;
import TileMap.TileMap;

public class ConditionLevelOne extends GameCondition
{
	private TileMap tileMap;
	private Background background;
	public Player player;
	private long time;
	//Gamer Score 
	public long GamerScoreTime;
	

	
	public ConditionLevelOne(GameConditionManager gcm)
	{
		
	
		this.gcm=gcm;
		initialize();
	}
	public void initialize() 
	{
		time=System.nanoTime();
		tileMap=new TileMap(30);
		tileMap.loadTiles("/Tilesets/jump.gif");
		tileMap.loadMap("/Maps/Jumper.map");
		tileMap.setCameraPosition(0, 0);
		
		background=new Background("/Backgrounds/Jumpbg1.png",0.1);
		player=new Player(tileMap);
		player.setPosition(100, 100);
	}
	public void tick() 
	{
		player.tick();
		if(player.get_y()>480) //Controlling Died or not!
		{
			GameControl.ConditionDied.gameOverFlag=1;
			//GamerScoreTime=(System.nanoTime()-time)/1000000000;  //nanosec to sec operation!
			GameControl.ConditionGameMenu.gamerList.get( GameControl.ConditionGameMenu.gamerList.size()-1).score_time=0;  //newPlayer gameScoreTime!
			
			//Object Serialization
			File file=new File("GamerScore.txt");
			try 
			{
				ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("GamerScore.txt"));
				oos.writeObject(GameControl.ConditionGameMenu.gamerList.get( GameControl.ConditionGameMenu.gamerList.size()-1));
				oos.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			//Update Gamer Score and show!
			try
			{
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream("GamerScore.txt"));
				
				for(Gamer g:GameControl.ConditionGameMenu.gamerList)
				{
					System.out.println("Name:"+g.name+"    ScoreTime:"+g.score_time);
				}
				System.out.println("\n\n\n");  //for such a updating gamer's data
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			player.setPosition(100, 100);
			gcm.setCondition(GameConditionManager.DIED);
		}
		else if(player.get_x()>1792) //FINISHED!!
		{
			GamerScoreTime=(System.nanoTime()-time)/1000000000;  //nanosec to sec operation!
			GameControl.ConditionGameMenu.gamerList.get( GameControl.ConditionGameMenu.gamerList.size()-1).score_time=GamerScoreTime;
			
			for(Gamer g:GameControl.ConditionGameMenu.gamerList)
			{
				System.out.println("Name:"+g.name+"    ScoreTime:"+g.score_time);
			}
			System.out.println("\n\n\n");  //for such a updating gamer's data
			
			
			GameControl.ConditionFinished.finishedFlag=1;
			player.setPosition(100, 100);
			gcm.setCondition(GameConditionManager.FINISHED);
			
		}
			
		tileMap.setCameraPosition(GameLoop.Width/2-player.get_x(), GameLoop.Height/2-player.get_y());
	}
	public void draw(Graphics2D graphic)
	{
		//Draw background
		background.draw(graphic);
		
		//Draw Tile Map
		tileMap.draw(graphic);
		
		//Draw Player
		player.draw(graphic);
	}
	public void keyPressed(int k) 
	{
		if(k==KeyEvent.VK_A)
			player.setLeft(true);
		if(k==KeyEvent.VK_D)
			player.setRight(true);
		if(k==KeyEvent.VK_W)
			player.setUp(true);
		if(k==KeyEvent.VK_S)
			player.setDown(true);
		if(k==KeyEvent.VK_SPACE)
			player.setJumping(true);
		if(k==KeyEvent.VK_SHIFT)
			player.setGliding(true);
		if(k==KeyEvent.VK_NUMPAD4)
			player.setScratching();
		if(k==KeyEvent.VK_NUMPAD5)
			player.setFireBallAttacking();
		
	}
	public void keyReleased(int k) 
	{
		if(k==KeyEvent.VK_A)
			player.setLeft(false);
		if(k==KeyEvent.VK_D)
			player.setRight(false);
		if(k==KeyEvent.VK_W)
			player.setUp(false);
		if(k==KeyEvent.VK_S)
			player.setDown(false);
		if(k==KeyEvent.VK_SPACE)
			player.setJumping(false);
		if(k==KeyEvent.VK_SHIFT)
			player.setGliding(false);
		if(k==KeyEvent.VK_NUMPAD4)
			player.setScratching();
		if(k==KeyEvent.VK_NUMPAD5)
			player.setFireBallAttacking();
	}
}
