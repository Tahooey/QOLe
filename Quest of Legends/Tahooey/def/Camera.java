package def;

import def.fil.*;
import wrld.*;
import wrld.blcks.*;

public class Camera {
	
	public static int x = 0, y=0;
	public static int dx = 0, dy = 0;

	public static void update() {
		x=MobsOnMap.m[QOL.mobControlled].finalx*-1+256;
		y=MobsOnMap.m[QOL.mobControlled].finaly*-1+256;
		x = x + dx;
		y = y + dy;
		if(y<-(WorldRunner.B[1].length*(Block.h*def.Frame.SCALE))+def.Frame.HEIGHT){
			y=-(WorldRunner.B[1].length*(Block.h*def.Frame.SCALE))+def.Frame.HEIGHT;
		}
		if(x<-WorldRunner.B[1][1].length*(Block.w*def.Frame.SCALE)+def.Frame.WIDTH){
			x=-WorldRunner.B[1][1].length*(Block.w*def.Frame.SCALE)+def.Frame.WIDTH;
		}
		if(y>0){
			y=0;
		}
		if(x>0){
			x=0;
		}
	}

	public static void move(int dir) {
		if (dir == Frame.STILL) {
			setDX(0);
			setDY(0);
		}
		if (dir == Frame.LEFT) {
			setDY(0);
			setDX(1);
		}
		if (dir == Frame.UP) {
			setDX(0);
			setDY(1);
		}
		if (dir == Frame.RIGHT) {
			setDX(-1);
			setDY(0);
		}
		if (dir == Frame.DOWN) {
			setDX(0);
			setDY(-1);
		}
	}
	
	public static void setX(int i){
		x=Frame.SPEED*i;
	}
	public static void setY(int i){
		y=Frame.SPEED*i;
	}

	public static void setDX(int i) {
		dx = i*Frame.SPEED;
	}

	public static void setDY(int i) {
		dy = i*Frame.SPEED;
	}

}
