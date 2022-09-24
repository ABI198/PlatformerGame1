package Entity;

import java.awt.Rectangle;

import Main.GameLoop;
import TileMap.Tile;
import TileMap.TileMap;

public class AnyObject 
{
	protected TileMap tileMap;
	protected int mapTileSize;
	
	
	//positions 
	protected double x;
	protected double y;
	
	//temporary x and y position!
	protected double x_temp;
	protected double y_temp;
	
	//map positions
	protected double xMap;
	protected double yMap;

	//directions object is going
	protected double dx;
	protected double dy;
	
	//destinations where we will be going so these are the next position!
	protected double x_destination;
	protected double y_destination;
	
	//dimensions ->for reading sprite sheet
	protected int sheetWidth;
	protected int sheetHeight;
	
	//collision box
	protected int c_Width; //collision width
	protected int c_Height;//collision height
	
	//collision
	protected int currentRow;  //current row that we're in
	protected int currentCol;  //current column that we're in
	
	//In this game we will use 4 point collision method !! So we are using 4 corners to determine whether hitting or not!
	protected boolean aboveLeft;
	protected boolean aboveRight;
	protected boolean belowLeft;
	protected boolean belowRight;
	
	
	//Animation
	protected Animation animation;
	protected int currentMovement;
	protected int previousMovement;
	protected boolean rightDirOrLeftDir;   //true->right , false->left

	//Movement
	protected boolean up;
	protected boolean down;
	protected boolean right;
	protected boolean left;
	protected boolean jumping;
	protected boolean falling;
	
	//Movement Attributes(Physics)  vel->velocity!
	protected double moveVel;
	protected double maximumVel;
	protected double stopVel;
	protected double fallVel;
	protected double maxFallVel;
	protected double jumpStart;
	protected double stopJumpVel;
	
	//Constructor
	public AnyObject(TileMap tileMap)
	{
		this.tileMap=tileMap;
		mapTileSize=tileMap.getmapTileSize();
	}
	
	public boolean intersects(AnyObject obj)
	{
		Rectangle rec1=getRectangle();
		Rectangle rec2=obj.getRectangle();
		
		return rec1.intersects(rec2);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle((int)x-c_Width,(int)y-c_Height,c_Width,c_Height);
	}
	
	public void calculateCornerPoints(double x,double y)
	{
		int leftEdge=(int)(x-c_Width/2)/mapTileSize;
		int rightEdge=(int)(x+c_Width/2-1)/mapTileSize;
		int topEdge=(int)(y-c_Height/2)/mapTileSize;
		int bottomEdge=(int)(y+c_Height/2-1)/mapTileSize;
		
		if(topEdge < 0 || bottomEdge >= tileMap.getRowNumber() || leftEdge < 0 || rightEdge >= tileMap.getColumnNumber()) 
		{
			aboveLeft = aboveRight = belowLeft = belowRight = false;
			
			if(y>480)
			{
				
			}
			return;
		}
		
		
		//corner point!
		int al=tileMap.getTypeOfTile(topEdge, leftEdge);
		int ar=tileMap.getTypeOfTile(topEdge, rightEdge);
		int bl=tileMap.getTypeOfTile(bottomEdge, leftEdge);
		int br=tileMap.getTypeOfTile(bottomEdge, rightEdge);
		
		aboveLeft=al==Tile.Solid;
		aboveRight=ar==Tile.Solid;
		belowLeft=bl==Tile.Solid;
		belowRight=br==Tile.Solid;
	}
	
	public void checkMapCollision()
	{
		currentCol=(int)x/mapTileSize;
		currentRow=(int)y/mapTileSize;
		
		x_destination=x+dx;
		y_destination=y+dy;
		
		x_temp=x;
		y_temp=y;
		
		calculateCornerPoints(x, y_destination);
		if(dy<0)
		{
			if(aboveLeft || aboveRight)
			{
				dy=0;
				y_temp=currentRow*mapTileSize+c_Height/2;
			}
			else
				y_temp+=dy;
		}
		if(dy>0)
		{
			if(belowLeft || belowRight)
			{
				dy=0;
				falling=false;
				y_temp=(currentRow+1)*mapTileSize-c_Height/2;
			}
			else
				y_temp+=dy;
		}
		
		
		calculateCornerPoints(x_destination, y);
		if(dx<0)
		{
			if(aboveLeft || belowLeft)
			{
				dx=0;
				x_temp=currentCol*mapTileSize+c_Width/2;			
			}
			else
				x_temp+=dx;
		}
		if(dx>0)
		{
			if(aboveRight || belowRight)
			{
				dx=0;
				x_temp=(currentCol+1)*mapTileSize-c_Width/2;
			}
			else
			{
				x_temp+=dx;
			}
		}
		
		if(!falling)
		{
			calculateCornerPoints(x, y_destination+1);
			if(!belowLeft && !belowRight)
				falling=true;
		}	
	}
	
	public int get_x()
	{
		return (int)x;
	}
	public int get_y()
	{
		return (int)y;
	}
	public int getSheetWidth()
	{
		return sheetWidth;
	}
	public int getSheetHeight()
	{
		return sheetHeight;
	}
	public int getc_Width()
	{
		return c_Width;
	}
	public int getc_Height()
	{
		return c_Height;
	}
	public void setPosition(double x,double y)
	{
		this.x=x;
		this.y=y;
	}
	public void setVector(double dx,double dy)
	{
		this.dx=dx;
		this.dy=dy;
	}
	public void setMapPosition()
	{
		xMap=tileMap.get_x();
		yMap=tileMap.get_y();
	}
	public void setLeft(boolean b)
	{
		left=b;
	}
	public void setRight(boolean b)
	{
		right=b;
	}
	public void setUp(boolean b)
	{
		up=b;
	}
	public void setDown(boolean b)
	{
		down=b;
	}
	public void setJumping(boolean b)
	{
		jumping=b;
	}
	public boolean notOnScreen()
	{
		return x+xMap+sheetWidth<0 
				|| x+xMap-sheetWidth>GameLoop.Width 
				|| y+yMap+sheetHeight<0 
				|| y+yMap-sheetHeight>GameLoop.Height; 
	}
}
