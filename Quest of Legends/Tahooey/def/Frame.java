package def;

import java.awt.*;

import javax.swing.*;

public class Frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public static final String TahooeyMaps = "https://dl.dropbox.com/u/94622838/";
	public static final int SCALE=8;
	public static final int WIDTH=80*SCALE,HEIGHT=85*SCALE;	
	public static final int SPEED=4;
	public static final String NAME="QOLe Alpha Release 001";
	public static final int release=1;
	public static final Color BACKGROUND=Color.BLACK;
	
	public static boolean needsNewJar;
	
	public static QOL QOL = new QOL();
	public static final int UP=0,DOWN=1,LEFT=2,RIGHT=3,STILL=4;
	
	public static String getInternetFile(String i){
		return TahooeyMaps+QOL.mapName+"/"+i;
	}
	
	public static String getJarCheckFile(String i){
		return TahooeyMaps+i;
	}
	
	public Frame(){
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setTitle(NAME);
		setBackground(BACKGROUND);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		add(QOL);
	}

}
