package GameControl;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameConditionManager 
{
	private int currentCondition;
	private ArrayList<GameCondition> gameConditions;
	
	//Game Conditions
	public final static int GAMEMENU=0;
	public final static int LEVEL1=1;
	public final static int DIED=2;
	public final static int FINISHED=3;
	
	
	
	
	//Constructor
	public GameConditionManager()
	{
		currentCondition=GAMEMENU;
		gameConditions=new ArrayList<GameCondition>();
		
		//MENU
		ConditionGameMenu menu=new ConditionGameMenu(this);
		gameConditions.add(menu); //Firstly We have to open the game menu!
		//LEVEL1
		ConditionLevelOne level1=new ConditionLevelOne(this);
		gameConditions.add(level1);
		//DIED
		ConditionDied dying=new ConditionDied(this);
		gameConditions.add(dying);
		//FINISHED
		ConditionFinished finishing=new ConditionFinished(this);
		gameConditions.add(finishing);
	}
	
	public void setCondition(int condition)
	{
		currentCondition=condition;
		gameConditions.get(currentCondition).initialize();
	}
	public void tick()
	{
		gameConditions.get(currentCondition).tick();
	}
	public void draw(Graphics2D graphic)
	{
		gameConditions.get(currentCondition).draw(graphic);
	}
	public void keyPressed(int k)
	{
		gameConditions.get(currentCondition).keyPressed(k);
	}
	public void keyReleased(int k)
	{
		gameConditions.get(currentCondition).keyReleased(k);
	}
}
