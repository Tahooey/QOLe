package def.fil;

import java.awt.image.*;
import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;

import def.QOL;

import wrld.blcks.Block;
import wrld.mobs.Mob;

public class ImgDyn {
	
	public static BufferedImage MENU;
	
	public static ImgDyn loader = new ImgDyn();
	
	public static BufferedImage terrainSheet;
	public static BufferedImage[] blocks;
	
	public static BufferedImage[][][] mobs;
	
	public static void loadTerrainSheet(){
		try {
			terrainSheet = loadFromDirectory("images/terrain.png");
			loadBlocks();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadMenuImage(){
		try {
			MENU=loadImage("Title Screen.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage loadImage(String path) throws IOException {
		URL url = loader.getClass().getResource(path);
		return ImageIO.read(url);
	}
	
	public static void splitMobs(){
		for(int i=0;i<mobs.length;i++){
			for(int j=0;j<mobs[i].length;j++){
				for(int k=1;k<mobs[i][j].length;k++){
					try {
						mobs[i][j][k] = loadFromDirectory("images/mob"+i+".png").getSubimage((j)*Mob.WIDTH, (k-1)*Mob.HEIGHT, Mob.WIDTH, Mob.HEIGHT);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void loadMobs(){
		mobs = new BufferedImage[MobsDynamics.b.length][4][5];
		for(int i=0;i<mobs.length;i++){
			try {
				mobs[i][0][0] = loadFromDirectory("images/mob"+i+".png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createImages(){
		String SEP = System.getProperty("file.separator");
		File f = new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+"images");
		if(!f.exists()||QOL.willDownload){
			if(f.exists()){
				f.delete();
			}
			System.out.println("CREATING IMAGES WARNING THIS MAY UP TO 2 MINS DEPENDING ON CONNECTION");
			f.mkdir();
			try {
				createTerrain();
				createMobs();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("IMAGES CREATED SUCCESFULLY");
		}
	}
	
	public static void createMobs() throws MalformedURLException, IOException{
		String SEP = System.getProperty("file.separator");
		BufferedImage b;
		File f;
		for(int i=0;i<MobsDynamics.b.length;i++){
			b = ImageIO.read(new URL(def.Frame.getInternetFile("images/mob"+i+".png")));
			f = new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+"images"+SEP+"mob"+i+".png");
			ImageIO.write(b, "png", f);
		}
	}
	
	public static void createTerrain() throws MalformedURLException, IOException{
		String SEP = System.getProperty("file.separator");
		BufferedImage b = ImageIO.read(new URL(def.Frame.getInternetFile("images/terrain.png")));
		File f = new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+"images"+SEP+"terrain.png");
		ImageIO.write(b, "png", f);
	}
	
	public static void loadBlocks(){
		blocks = new BufferedImage[BlocksDynamics.b.length];
		for(int i=0;i<blocks.length;i++){
			blocks[i]=getSubImage(terrainSheet,i*Block.w,0,Block.w,Block.h);
		}
	}
	
	public static BufferedImage loadFromDirectory(String i) throws IOException{
		String SEP = System.getProperty("file.separator");
		File f = new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+i);
		return ImageIO.read(f);
	}
	
	public static BufferedImage getSubImage(BufferedImage i,int x,int y,int w,int h){
		return i.getSubimage(x, y, w, h);
	}
	
	public static String defaultDirectory(){
		String os = System.getProperty("os.name").toUpperCase();
		String folder = "";
		
		String SEP = "/";

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

}
