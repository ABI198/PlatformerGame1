package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Locale.FilteringMode;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Player extends AnyObject
{
	//Player Features
	private int health;
	private int maxHealth;
	private int fireAvailability;
	private int maxFireAvailability;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	//FireBall
	private boolean fireballAttacking;
	private int fireballCost;
	private int fireballDamage;
	
	//Scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchAttackRange;
	
	//Gliding like flying
	private boolean gliding;
	
	private ArrayList<BufferedImage[]> sprites;
	private final int actionFrameNumbers[]={2,8,1,2,4,2,5};
	
	//Animation Actions
	private final static int IDLE=0;
	private final static int WALKING=1;
	private final static int JUMPING=2;
	private final static int FALLING=3;
	private final static int GLIDING=4;
	private final static int FIREBALL=5;
	private final static int SCRATCHING=6;
	
	
	//Constructor
	public Player(TileMap tileMap)
	{
		super(tileMap);
		
		sheetWidth=30;
		sheetHeight=30;
		c_Width=20;
		c_Height=20;
		
		moveVel=0.3;
		maximumVel=1.6;
		stopVel=0.4;
		fallVel=0.15;
		maxFallVel=4.0;
		jumpStart=-4.8;
		stopJumpVel=0.3;
		
		rightDirOrLeftDir=true; //right direction!!
		
		//Health
		health=maxHealth=5;
		
		//FireBall Attacking
		fireAvailability=maxFireAvailability=2500;
		fireballCost=200;
		fireballDamage=5;
		
		//Scratch Attacking
		scratchDamage=8;
		scratchAttackRange=40;
		
		
		//load sprites
		try
		{
			BufferedImage spriteSheet=ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
			sprites=new ArrayList<BufferedImage[]>();
			
			for(int i=0; i<7; ++i)
			{
				BufferedImage[] img=new BufferedImage[actionFrameNumbers[i]];
				for(int j=0; j<actionFrameNumbers[i]; ++j)
				{
					if(i!=SCRATCHING)
						img[j]=spriteSheet.getSubimage(j*sheetWidth, i*sheetHeight, sheetWidth, sheetHeight);
					else
						img[j]=spriteSheet.getSubimage(j*sheetWidth*2, i*sheetHeight, sheetWidth*2, sheetHeight);
				}
				sprites.add(img);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		animation=new Animation();
		currentMovement=IDLE;
		animation.setFrame(sprites.get(IDLE));
		animation.setDelay(400);
	}
	
	public int getHealth()
	{
		return health;
	}
	public int getMaxHealth()
	{
		return maxHealth;
	}
	public int getFireAvailability()
	{
		return fireAvailability;
	}
	public int getMaxFireAvailability()
	{
		return maxFireAvailability;
	}
	public void setFireBallAttacking()
	{
		fireballAttacking=true;
	}
	public void setScratching()
	{
		scratching=true;
	}
	public void setGliding(boolean b)
	{
		gliding=b;
	}
	
	private void getFutureCoordinate()
	{
		if(left)
		{
			dx-=moveVel;
			if(dx<-maximumVel)
				dx=-maximumVel;
		}
		else if(right)
		{
			dx+=moveVel;
			if(dx>maximumVel)
				dx=maximumVel;
		}
		else
		{
			if(dx>0)
			{
				dx-=stopVel;
				if(dx<0)
					dx=0;
			}
			else if(dx<0)
			{
				dx+=stopVel;
				if(dx>0)
				{
					dx=0;
				}
			}
		}
		
		//cannot move while attacking except in air!!
		if((currentMovement==SCRATCHING || currentMovement==FIREBALL ) && !(jumping || falling))
			dx=0;
		
		//jumping
		if(jumping && !falling)
		{
			dy=jumpStart;
			falling=true;
		}
		
		//falling
		if(falling)
		{
			if(dy>0 && gliding)
				dy+=fallVel*0.1;
			else
				dy+=fallVel;
					
			if(dy>0)
				jumping=false;
			if(dy<0 && !jumping)
				dy+=stopJumpVel;
					
			if(dy>maxFallVel)
				dy=maxFallVel;
		}
	}
	
	public void tick()
	{
		
		
		//update position!
		getFutureCoordinate();
		checkMapCollision();
		setPosition(x_temp, y_temp);
		
		//setAnimation
		if(scratching && currentMovement!=SCRATCHING)
		{
			currentMovement=SCRATCHING;
			animation.setFrame(sprites.get(SCRATCHING));
			animation.setDelay(50);
			sheetWidth=60;
		}
		else if(fireballAttacking && currentMovement!=FIREBALL)
		{
			currentMovement=FIREBALL;
			animation.setFrame(sprites.get(FIREBALL));
			animation.setDelay(100);
			sheetWidth=30;
		}
		else if(dy>0)
		{
			if(gliding)
			{
				if(currentMovement!=GLIDING)
				{
					currentMovement=GLIDING;
					animation.setFrame(sprites.get(GLIDING));
					animation.setDelay(100);
					sheetWidth=30;	
				}
			}
			else if(currentMovement!=FALLING)
			{
				currentMovement=FALLING;
				animation.setFrame(sprites.get(FALLING));
				animation.setDelay(100);
				sheetWidth=30;
			}
		}
		else if(dy<0) //If we are going to upwards!
		{
			if(currentMovement!=JUMPING)
			{
				currentMovement=JUMPING;
				animation.setFrame(sprites.get(JUMPING));
				animation.setDelay(-1); //-1 olmasýnýn nedeni SpriteSheet'te jumping için sadece bir adet frame var.Bunun anlamý herhangi bir animasyona gerek yoktur!
				sheetWidth=30;
			}
		}
		else if(left || right)
		{
			if(currentMovement!=WALKING)
			{
				currentMovement=WALKING;
				animation.setFrame(sprites.get(WALKING));
				animation.setDelay(40);
				sheetWidth=30;
			}
		}
		else
		{
			if(currentMovement!=IDLE)
			{
				currentMovement=IDLE;
				animation.setFrame(sprites.get(IDLE));
				animation.setDelay(400);
				sheetWidth=30;
			}
		}
		animation.tick();
		
		//set player action direction
		if(currentMovement!=SCRATCHING && currentMovement!=FIREBALL)
		{
			if(right)
				rightDirOrLeftDir=true;
			if(left)
				rightDirOrLeftDir=false;
		}
	}
	public void draw(Graphics2D graphic)
	{
		setMapPosition();
		
		//draw player
		
		
		if(rightDirOrLeftDir)
		{
			graphic.drawImage(animation.getImage(),(int)(x+xMap-sheetWidth/2),(int)(y+yMap-sheetHeight/2),null);
		}
		else
		{
			graphic.drawImage(animation.getImage(),(int)(x+xMap-sheetWidth/2+sheetWidth),(int)(y+yMap-sheetHeight/2),-sheetWidth,sheetHeight,null);
		}
	}
}
