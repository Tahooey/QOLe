package wrld.mobs;

import java.awt.*;

import wrld.WorldRunner;
import wrld.blcks.Block;

import def.*;

public class Mob {

	public int finalx, finaly;

	public int dx = 0, dy = 0;
	
	//private int preDirection;
	
	public int animateFrame=1;

	public int direction = 0;
	
	public Image[][] IMGS;

	public Image IMG_TO_DRAW;

	public static final int WIDTH = 16, HEIGHT = 16;

	public Rectangle l, r, u, d;

	public int finalw, finalh;

	public int x = 0, y = 0;
	public String name = "";
	public boolean isControlled = false;

	public int ID = 0;

	public int path = 99;

	public static int STRAIGHTUP = 0, LEFT = 1, STRAIGHTDOWN = 2,
			STRAIGHTRIGHT = 3;

	public Mob(int ID) {
		this.ID = ID;
	}

	public Mob(String name, int x, int y, boolean controlled, Mob m) {
		this.name = name;
		ID = m.ID;
		this.x = x;
		this.y = y;
		isControlled = controlled;
	}

	public Mob(int x, int y, String name, boolean controlled) {
		this.x = x;
		this.y = y;
		this.name = name;
		isControlled = controlled;
	}

	public Mob(int x, int y, String name, boolean controlled, int Path) {
		this.x = x;
		this.y = y;
		this.name = name;
		isControlled = controlled;
		path = Path;
	}
	
	public void Animate(){
		if(QOL.tick==0){
			animateFrame=1;
		}else if(QOL.tick==16){
			animateFrame=2;
		}else if(QOL.tick==32){
			animateFrame=3;
		}else if(QOL.tick==48){
			animateFrame=4;
		}
	}
	
	public void Frame(){
		if(direction!=4){
			IMG_TO_DRAW=IMGS[direction][animateFrame];
		}else{
			animateFrame=1;
		}
	}

	public void update() {
		Frame();
		
		Animate();
		
		finalx = finalx + dx;
		finaly = finaly + dy;
		
		x = finalx / finalw;
		y = finaly / finalh;

		u.setLocation(finalx + Camera.x, finaly - (def.Frame.SPEED*2) + Camera.y);
		d.setLocation(finalx + Camera.x, finaly + finalh + Camera.y+def.Frame.SPEED);
		l.setLocation(finalx + Camera.x - (def.Frame.SPEED*2), finaly + Camera.y);
		r.setLocation(finalx + Camera.x + finalw+def.Frame.SPEED, finaly + Camera.y);

		if (!canMove(direction)) {
			move(def.Frame.STILL);
		}
		
		if(direction==def.Frame.STILL){
			if(finalx%finalw==0){
				setDX(0);
				if(finaly%finalh==0){
					setDY(0);
				}
			}
		}
	}

	public void draw(Graphics g) {
		if (IMG_TO_DRAW == null) {
			g.fillRect(finalx + Camera.x, finaly + Camera.y, finalw, finalh);
		} else {
			g.drawImage(IMG_TO_DRAW, finalx + Camera.x, finaly + Camera.y,
					finalw, finalh, null);
		}
	}

	public boolean canMove(int dir) {
		if (dir == def.Frame.UP) {
			return (canMoveUp());
		}
		if (dir == def.Frame.DOWN) {
			return (canMoveDown());
		}
		if (dir == def.Frame.LEFT) {
			return (canMoveLeft());
		}
		if (dir == def.Frame.RIGHT) {
			return (canMoveRight());
		}
		return false;
	}

	public Block getBlockLeft(int layer) {
		int xc = x-1;
		int yc = y;
		if (yc < 0) {
			yc = 0;
		}
		if (xc < 0) {
			xc = 0;
		}
		if(yc>WorldRunner.B[layer].length-1){
			yc=WorldRunner.B[layer].length-1;
		}
		if(xc>WorldRunner.B[layer][y].length-1){
			xc=WorldRunner.B[layer][y].length-1;
		}
		return WorldRunner.B[layer][yc][xc];
	}

	public Block getBlockRight(int layer) {
		int xc = x + 1;
		int yc = y;
		if (yc < 0) {
			yc = 0;
		}
		if (xc < 0) {
			xc = 0;
		}
		if(yc>WorldRunner.B[layer].length-1){
			yc=WorldRunner.B[layer].length-1;
		}
		if(xc>WorldRunner.B[layer][y].length-1){
			xc=WorldRunner.B[layer][y].length-1;
		}
		return WorldRunner.B[layer][yc][xc];
	}

	public Block getBlockUp(int layer) {
		int xc = x;
		int yc = y-1;
		if (yc < 0) {
			yc = 0;
		}
		if (xc < 0) {
			xc = 0;
		}
		return WorldRunner.B[layer][yc][xc];
	}

	public Block getBlockDown(int layer) {
		int xc = x;
		int yc = y + 1;
		if (yc < 0) {
			yc = 0;
		}
		if (xc < 0) {
			xc = 0;
		}
		if(yc>WorldRunner.B[layer].length-1){
			yc=WorldRunner.B[layer].length-1;
		}
		if(xc>WorldRunner.B[layer][y].length-1){
			xc=WorldRunner.B[layer][y].length-1;
		}
		return WorldRunner.B[layer][yc][xc];
	}

	public void boot() {
		l = new Rectangle();
		r = new Rectangle();
		u = new Rectangle();
		d = new Rectangle();

		finalx = x * WIDTH * def.Frame.SCALE;
		finaly = y * HEIGHT * def.Frame.SCALE;

		finalw = WIDTH * def.Frame.SCALE;
		finalh = HEIGHT * def.Frame.SCALE;

		l.setSize(def.Frame.SPEED, finalh);
		r.setSize(def.Frame.SPEED, finalh);
		u.setSize(finalw, def.Frame.SPEED);
		d.setSize(finalw, def.Frame.SPEED);
		
		IMG_TO_DRAW=IMGS[0][1];
	}

	public void setDX(int i) {
		dx = i * def.Frame.SPEED;
	}

	public void setDY(int i) {
		dy = i * def.Frame.SPEED;
	}

	public boolean canMoveLeft() {
		if (getBlockLeft(1).isCollidable) {
			if(l.intersects(getBlockLeft(1).r)){
				return false;
			}else{
				return true;
			}
		} else if(!getBlockLeft(0).isCollidable){
			if(l.intersects(getBlockLeft(0).r)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	public boolean canMoveRight() {
		if (getBlockRight(1).isCollidable) {
			if(r.intersects(getBlockRight(1).r)){
				return false;
			}else{
				return true;
			}
		} else if(!getBlockRight(0).isCollidable){
			if(r.intersects(getBlockRight(0).r)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	public boolean canMoveUp() {
		if (getBlockUp(1).isCollidable) {
			if(u.intersects(getBlockUp(1).r)){
				return false;
			}else{
				return true;
			}
		} else if(!getBlockUp(0).isCollidable){
			if(u.intersects(getBlockUp(0).r)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	public boolean canMoveDown() {
		if (getBlockDown(1).isCollidable) {
			if(d.intersects(getBlockDown(1).r)){
				return false;
			}else{
				return true;
			}
		} else if(!getBlockDown(0).isCollidable){
			if(d.intersects(getBlockDown(0).r)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	public void move(int dir) {
		if(dir==def.Frame.LEFT){
			if(finaly%finalh==0){
				if(canMoveLeft()){
					setDX(-1);
				}else{
					setDX(0);
				}
				setDY(0);
				//preDirection=dir;
				direction=dir;
			}
		}
		if(dir==def.Frame.RIGHT){
			if(finaly%finalh==0){
				if(canMoveRight()){
					setDX(1);
				}else{
					setDX(0);
				}
				setDY(0);
				//preDirection=dir;
				direction=dir;
			}
		}
		if(dir==def.Frame.UP){
			if(finalx%finalw==0){
				if(canMoveUp()){
					setDY(-1);
				}else{
					setDX(0);
				}
				setDX(0);
				//preDirection=dir;
				direction=dir;
			}
		}
		if(dir==def.Frame.DOWN){
			if(finalx%finalw==0){
				if(canMoveDown()){
					setDY(1);
				}else{
					setDX(0);
				}
				setDX(0);
				//preDirection=dir;
				direction=dir;
			}
		}
		if(dir==def.Frame.STILL){
			direction=dir;
			if(finalx%finalw==0){
				setDX(0);
				if(finaly%finalh==0){
					setDY(0);
				}
			}
		}
	}

}
