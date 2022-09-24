package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;


import Main.GameLoop;

public class TileMap 
{
	//positions
	private double x;
	private double y;
	
	//Bounds
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;
	
	private double smoothCameraMove; //It's supposed to smoothly scroll the camera towards the player instead of just rigidly following the player!
	
	//Map
	private int map[][];
	private int mapTileSize;
	private int rowNumber;
	private int columnNumber;
	
	//Dimension of the map ->amounts of pixel number!
	private int mapWidth;
	private int mapHeight;
	
	//Tiles
	private BufferedImage tileSheet;
	private int tileNumAcross;
	private Tile tiles[][];
	
	//Drawing
	private int whichRowDraw;
	private int whichColumnDraw;
	private int rowNumToDraw;
	private int columnNumToDraw;
	
	//Constructor
	public TileMap(int mapTileSize)
	{
		this.mapTileSize=mapTileSize;
		rowNumToDraw=GameLoop.Height/mapTileSize +2;
		columnNumToDraw=GameLoop.Width/mapTileSize+2;
		smoothCameraMove=0.07;
	}
	
	public void loadTiles(String str)
	{
		try
		{
			tileSheet=ImageIO.read(getClass().getResourceAsStream(str));
			tileNumAcross=tileSheet.getWidth()/mapTileSize;
			
			tiles=new Tile[2][tileNumAcross];
			
			BufferedImage subImage;
			for(int i=0; i<tileNumAcross; ++i) //i represents row or col!
			{
				subImage=tileSheet.getSubimage(i*mapTileSize, 0, mapTileSize, mapTileSize);
				tiles[0][i]=new Tile(subImage,Tile.NonSolid);
				subImage=tileSheet.getSubimage(i*mapTileSize, mapTileSize, mapTileSize, mapTileSize);
				tiles[1][i]=new Tile(subImage,Tile.Solid);
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void loadMap(String str)
	{
		try
		{
			InputStream input=getClass().getResourceAsStream(str);
			BufferedReader reader=new BufferedReader(new InputStreamReader(input));
			
			columnNumber=Integer.parseInt(reader.readLine());
			rowNumber=Integer.parseInt(reader.readLine());
			
			map=new int[rowNumber][columnNumber];
			mapWidth=columnNumber*mapTileSize;
			mapHeight=rowNumber*mapTileSize;
			
			xMin=GameLoop.Width-mapWidth;
			xMax=0;
			yMin=GameLoop.Height-mapHeight;
			yMax=0;
			
			String delimeters="\\s+";
			for(int i=0; i<rowNumber; ++i)
			{
				String lineScanned=reader.readLine();
				String clearedLine[]=lineScanned.split(delimeters);
				
				for(int j=0; j<columnNumber; ++j)
				{
					map[i][j]=Integer.parseInt(clearedLine[j]);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Get methods
	public int getmapTileSize()
	{
		return mapTileSize;		
	}
	public int get_x()
	{
		return (int)x;
	}
	public int get_y()
	{
		return (int)y;
	}
	public int getRowNumber()
	{
		return rowNumber;
	}
	public int getColumnNumber()
	{
		return columnNumber;
	}
	public int getMapWidth()
	{
		return mapWidth;
	}
	public int getMapHeight()
	{
		return mapHeight;
	}
	public int getTypeOfTile(int row,int column) //Algorithm!
	{
		int rc=map[row][column];
		int r=rc/tileNumAcross;
		int c=rc%tileNumAcross;
		return tiles[r][c].getType();
	}
	
	public void setCameraPosition(double x,double y)
	{
		this.x+=(x-this.x)*smoothCameraMove;
		this.y+=(y-this.y)*smoothCameraMove;
		
		fixCameraBounds();
		
		whichColumnDraw=(int)-this.x/mapTileSize;
		whichRowDraw=(int)-this.y/mapTileSize;
	}
	
	private void fixCameraBounds()
	{
		if(x<xMin) 
			x=xMin;
		if(x>xMax) 
			x=xMax;
		if(y<yMin) 
			y=yMin;
		if(y>yMax) 
			y=yMax;
	}
	
	public void draw(Graphics2D graphic)
	{
		for(int i=whichRowDraw; i<whichRowDraw+rowNumToDraw; ++i)
		{
			if(i>=rowNumber)
				break;
			
			for(int j=whichColumnDraw; j<whichColumnDraw+columnNumToDraw; ++j)
			{
				if(j>=columnNumber)
					break;
				if(map[i][j]==0)
					continue;
				
				int rc=map[i][j];
				int r=rc/tileNumAcross;
				int c=rc%tileNumAcross;
				
				graphic.drawImage(tiles[r][c].getImage(),(int)x+j*mapTileSize,(int)y+i*mapTileSize,null);
			}
		}
	}
}
