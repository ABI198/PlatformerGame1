package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GameLoop;


public class Background 
{
	private BufferedImage img;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveControl; //This is the scale which the background moves.
	
	//Constructor
	public Background(String str,Double mc) //mc->moveControl!
	{
		try
		{
			img=ImageIO.read(getClass().getResourceAsStream(str));
			moveControl=mc;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x,double y)
	{
		/*We don't want this thing to keep going all the way 
		 out of the screen.If it goes past the screen then we want
		 it to reset.So it will be smooth scrolling!
		 */
		
		this.x=(x*moveControl) % GameLoop.Width;
		this.y=(x*moveControl) % GameLoop.Height;
	}
	
	public void setVector(double dx,double dy) ////if we want to background to automatically scroll,we can use dx and dy!
	{
		this.dx=dx;
		this.dy=dy;
	}
	
	public void tick()
	{
		x+=dx;  
		y+=dy;
	}
	
	public void draw(Graphics2D graphic)//Background animation setting!
	{
		/*This is a scrolling background so we have to make sure that 
		it's always filling the screen.If the background is going to
		scroll to the left of the screen we have to make sure to draw 
		another instance of the background to the right in order to fill up the space!
		*/
		graphic.drawImage(img,(int)x,(int)y,null);
		if(x<0)//This means that we're going to draw an extra image to the right!
		{
			
				x+=(int)x+GameLoop.Width; //infinite animation!!!
				graphic.drawImage(img,(int)x+GameLoop.Width,(int)y, null);//x<0 we have to draw an extra image to the right!!
			
		}
		if(x>0) //x>0 we have to draw an extra image to the left!!
		{	
			graphic.drawImage(img,(int)x-GameLoop.Width,(int)y, null);
		}
	}
}
