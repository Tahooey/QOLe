package def;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import def.fil.*;

import wrld.WorldRunner;

public class QOL extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public String mapName;
	
	boolean gameIsRunning=false,menuIsOpen=true,loaded=false,loading=false;
	public boolean downloading=false;

	public static boolean willDownload=false;
	
	public static int tick=0;

	private Image dbImage;
	private Graphics dbg;
	static final int W = Frame.WIDTH, H = Frame.HEIGHT-20;
	static final Dimension gameDim = new Dimension(W, H);
	
	public static int mobControlled=0;
	
	public boolean blankScreen=false;

	private Thread game;
	private volatile boolean running = false;
	//private long period = 6 * 1000000;

	public QOL() {
		setPreferredSize(gameDim);
		setBackground(Color.WHITE);
		setFocusable(true);
		requestFocus();
		// Handle all key inputs from user
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int KeyCode=e.getKeyCode();
				if(KeyCode==KeyEvent.VK_W){
					//Camera.move(Frame.UP);
					MobsOnMap.m[mobControlled].move(Frame.UP);
				}
				if(KeyCode==KeyEvent.VK_S){
					//Camera.move(Frame.DOWN);
					MobsOnMap.m[mobControlled].move(Frame.DOWN);
				}
				if(KeyCode==KeyEvent.VK_A){
					//Camera.move(Frame.LEFT);
					MobsOnMap.m[mobControlled].move(Frame.LEFT);
				}
				if(KeyCode==KeyEvent.VK_D){
					//Camera.move(Frame.RIGHT);
					MobsOnMap.m[mobControlled].move(Frame.RIGHT);
				}
				if(KeyCode==KeyEvent.VK_O){
					if(!loaded){
						bootGame();
					}
				}
				if(KeyCode==KeyEvent.VK_P){
					if(!gameIsRunning){
						openGame();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(gameIsRunning&&!Frame.needsNewJar){
					MobsOnMap.m[mobControlled].move(Frame.STILL);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}
		});

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//Mouse.isPressed=false;
				//Mouse.isReleased=true;
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				//Mouse.isPressed=true;
				//Mouse.isReleased=false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				//Mouse.isPressed=false;
				//Mouse.isReleased=true;
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				/**int x=e.getX();
				int y=e.getY();
				
				Mouse.x=x;
				Mouse.y=y;**/

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				/**int x=e.getX();
				int y=e.getY();
				
				Mouse.x=x;
				Mouse.y=y;**/

			}
		});
	}
	
	public void getControlledMob(){
		for(int i=0;i<MobsOnMap.m.length;i++){
			if(MobsOnMap.m[i].isControlled){
				mobControlled=i;
			}
		}
		System.out.println("Found Player!");
		downloading=false;
		willDownload=false;
	}

	@Override
	public void run() {
		while (running) {
			gameUpdate();
			gameRender();
			paintScreen();
		}
	}
	
	static Thread WorldUpdater = new Thread(){
		public void run(){
			WorldRunner.updateBlocks();
		}
	};
	static Thread MobsUpdater = new Thread(){
		public void run(){
			WorldRunner.updateMobs();
		}
	};
	static Thread CamUpdater = new Thread(){
		public void run(){
			Camera.update();
		}
	};
	
	public static String defaultDirectory(){
		String os = System.getProperty("os.name").toUpperCase();
		String folder = "";
		
		String SEP =  System.getProperty("file.separator");

		if(os.contains("MAC")){
			folder = System.getProperty("user.home") + SEP + "Library" + SEP + "Application Support" + SEP + "Tahooey"+SEP+"QOL"+SEP;
		}
		else if(os.contains("WIN")){
			folder = System.getProperty("user.home") + SEP + "AppData" + SEP + "Roaming" + SEP + "Tahooey"+SEP+"QOL"+SEP;
		}
		else {
			folder = System.getProperty("user.home") + SEP + "Tahooey"+SEP+"QOL"+SEP;
		}
		File f = new File(folder);
		if(!f.exists()) f.mkdir();
		return folder;
	}

	private void gameUpdate() {
		if (running && game != null) {
			if(gameIsRunning&&loaded){
				CamUpdater.run();
				WorldUpdater.run();
				MobsUpdater.run();
				AnimationTick.run();
			}
		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.blue);
		if(Frame.needsNewJar){
			g.drawString("YOU NEED A NEW JAR FILE, CHECK OUT @Tahooey's TWITTER FEED FOR MORE INFO!!",30,30);
		}
		if(gameIsRunning&&!menuIsOpen&&!Frame.needsNewJar){
			WorldRunner.drawBlocksLower(g);
			WorldRunner.drawMobs(g);
			WorldRunner.drawBlocksHigher(g);
		}
		if(menuIsOpen&&!gameIsRunning){
			g.drawImage(ImgDyn.MENU, 0, 0, W, H, null);
			if(!loaded&&loading){
				g.setColor(Color.blue);
				g.drawString("Loading your Game",30,30);
				if(downloading){
					g.drawString("Downloading Files, This may Take a while!",30,40);
				}
			}else{				
				if(willDownload&&!downloading&&!Frame.needsNewJar){
					g.drawString("Press O to Download Map!",30,30);
				}else{
					g.drawString("Press P to Start the Game!",30,30);
				}
			}
		}
	}
	
	static Thread AnimationTick = new Thread(){
		public void run(){
			AnimationTick();
		}
	};
	
	public static void AnimationTick(){
		if(tick<64){
			tick++;
		}else{
			tick=0;
		}
	}

	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(W, H);
			if (dbImage == null) {
				System.err.println("dbImage is still null!");
				return;
			} else {
				dbg = dbImage.getGraphics();
			}
		}
		dbg.setColor(Frame.BACKGROUND);
		dbg.fillRect(0, 0, W, H);
		draw(dbg);
	}

	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics();
			if (dbImage != null && g != null) {
				g.drawImage(dbImage, 0, 0, null);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void getMapName(){
		File directory = new File(defaultDirectory());
		if(!directory.exists()){
			directory.mkdirs();
			willDownload=true;
		}
		File f = new File(defaultDirectory()+"map.qol");
		FileWriter fw;
		if(!f.exists()){
			try {
				f.createNewFile();
				fw = new FileWriter(f);
				fw.write("TahooeytestMapv2");
				
				fw.flush();
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Scanner s = new Scanner(f);
			mapName=s.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File ff = new File(defaultDirectory()+mapName);
		if(!ff.exists()){
			ff.mkdir();
		}else{
			checkMapVersion();
		}
		
	}
	
	public void CheckJarVersion(){
		try {
			Scanner s = new Scanner(new URL(def.Frame.getJarCheckFile("QOLe/jarVersion.qol")).openStream());
			
			if(s.nextInt()==Frame.release){
				Frame.needsNewJar=false;
			}else{
				Frame.needsNewJar=true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openGame(){
		if(loaded&&!loading){
			gameIsRunning=true;
			menuIsOpen=false;
		}else{
			bootGame();
			gameIsRunning=true;
			menuIsOpen=false;
		}
	}
	
	public void checkMapVersion(){
		boolean FileExists=true;
		int thisMapVersion=0,onlineMapVersion=0;
		File f = new File(defaultDirectory()+mapName+System.getProperty("file.separator")+"version.qol");
		Scanner s;
		try {
			if(f.exists()){
				s=new Scanner(f);
				thisMapVersion=s.nextInt();
			}else{
				FileExists=false;
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(FileExists){
			try {
				s=new Scanner(new URL(def.Frame.getInternetFile("version.qol")).openStream());				
				onlineMapVersion=s.nextInt();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(thisMapVersion!=onlineMapVersion){
				willDownload=true;
			}
		}else{
			try {
				s=new Scanner(new URL(def.Frame.getInternetFile("version.qol")).openStream());				
				onlineMapVersion=s.nextInt();
				
				FileWriter fw = new FileWriter(f);
				
				fw.write(onlineMapVersion);
				
				fw.flush();
				fw.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Checked Map Version");
	}
	
	public void bootGame(){
		loading=true;
		CheckJarVersion();
		if(!Frame.needsNewJar){
			BlocksDynamics.boot();
			MobsDynamics.boot();
			ImgDyn.createImages();
			ImgDyn.loadTerrainSheet();
			ImgDyn.loadMobs();
			ImgDyn.splitMobs();
			MobsOnMap.boot();
			World.boot();
			WorldRunner.build();
			getControlledMob();
			loaded=true;
		}
		loading=false;
	}

	public void addNotify() {
		super.addNotify();
		getMapName();
		ImgDyn.loadMenuImage();
		startGame();
	}

	private void startGame() {
		if (game == null || !running) {
			game = new Thread(this);
			running = true;
			game.start();
		}
	}

	public void stopGame() {
		if (running) {
			running = false;
		}
	}

	public void log(String i) {
		System.out.println(i);
	}

}
