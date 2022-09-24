package Entity;

import java.awt.image.BufferedImage;

public class Animation 
{
	private BufferedImage[] frames;
	private int currentFrame;
	
	//timer
	private long start;
	private long delay;
	
	private boolean playedAlready;
	
	//constructor
	public void Animation()
	{
		playedAlready=false;
	}
	
	public void setFrame(BufferedImage[] frames)
	{
		this.frames=frames;
		currentFrame=0;
		start=System.nanoTime();
		playedAlready=false;
	}
	public void setDelay(long d)
	{
		delay=d;
	}
	public void setFrame(int f)
	{
		currentFrame=f;
	}
	
	public void tick()
	{
		if(delay==-1)
			return;
		
		long timeTaken=(System.nanoTime()-start)/1000000;
		if(timeTaken>delay)
		{
			++currentFrame;
			start=System.nanoTime();
		}
		if(currentFrame==frames.length)
		{
			currentFrame=0;  //This means that one action has completed!
			playedAlready=true;  
		}
	}
	
	public int getFrame()
	{
		return currentFrame;
	}
	public BufferedImage getImage()
	{
		return frames[currentFrame];
	}
	public boolean hasPlayedOnce()
	{
		return playedAlready;
	}
}
