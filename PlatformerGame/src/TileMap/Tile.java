package TileMap;

import java.awt.image.BufferedImage;

public class Tile 
{
	private BufferedImage img;
	private int type;
	
	//tile types
	public final static int NonSolid=0;
	public final static int Solid=1;
	
	public Tile(BufferedImage img,int type)
	{
		this.img=img;
		this.type=type;
	}
	public BufferedImage getImage()
	{
		return img;
	}
	public int getType()
	{
		return type;
	}
}
