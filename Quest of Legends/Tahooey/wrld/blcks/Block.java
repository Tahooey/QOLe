package wrld.blcks;

import java.awt.*;
import java.awt.image.*;

import def.Camera;

public class Block {
	
	public int x=1,y=1;
	public static int w=16,h=16;
	
	public int finalw=0,finalh=0;
	
	public int finalx=20,finaly=20;
	
	public int dx=0,dy=0;
	
	public boolean isCollidable=true;
	public boolean isVisible=false;
	
	public Rectangle r;
	
	public String Name="";
	
	public int ID=0;
	
	public BufferedImage IMG;
	
	public Block(int ID,boolean Collidable,boolean Visible,BufferedImage imgs){
		this.ID=ID;
		isCollidable=Collidable;
		isVisible=Visible;
		IMG=imgs;
	}
	
	public Block(int ID,boolean Collidable,boolean Visible,String Name){
		this.ID=ID;
		isCollidable=Collidable;
		isVisible=Visible;
		this.Name=Name;
	}
	
	public void boot(){
		x=x*w;
		y=y*h;
		
		finalw=w*def.Frame.SCALE;
		finalh=h*def.Frame.SCALE;
		
		finalx=x*def.Frame.SCALE;
		finaly=y*def.Frame.SCALE;
		
		r=new Rectangle();
		r.setSize(finalw, finalh);
	}
	
	public void update(){
		finalx=finalx+dx;
		finaly=finaly+dy;
		
		r.setLocation(finalx+Camera.x, finaly+Camera.y);
	}
	
	public void draw(Graphics g){
		if(isVisible){
			g.drawImage(IMG, finalx+Camera.x, finaly+Camera.y, finalw, finalh,null);
		}
	}

}
