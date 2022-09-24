package Main;

import javax.swing.JFrame;

public class MainGame 
{
	public static void main(String[] args)
	{
		JFrame gameWindow=new JFrame("Embedded Platform Game");
		GameLoop gl=new GameLoop();
		gameWindow.setContentPane(gl);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setResizable(false);
		gameWindow.pack();
		gameWindow.setVisible(true);
	}
}
