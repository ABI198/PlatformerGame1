package GameControl;

import java.awt.Graphics2D;

public abstract class GameCondition 
{
	protected GameConditionManager gcm;
	
	public abstract void initialize(); 
	public abstract void tick(); 
	public abstract void draw(Graphics2D graphic); 
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
}
