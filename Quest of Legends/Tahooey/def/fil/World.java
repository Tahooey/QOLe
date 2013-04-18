package def.fil;

import java.io.*;
import java.net.URL;
import java.util.*;

import def.QOL;

import wrld.*;
import wrld.blcks.*;

public class World {
	
	public static File worldupper;
	public static File worldlower;
	public static FileWriter fw;
	public static Scanner s;
	
	public static void boot(){
		worldupper=getFileInData("worldupper.qol");
		if(!worldupper.exists()||QOL.willDownload){
			if(worldupper.exists()){
				worldupper.delete();
			}
			try {
				worldupper.createNewFile();
				WriteWorldUpper();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
		try {
			ReadWorldUpper();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		worldlower=getFileInData("worldlower.qol");
		if(!worldlower.exists()||QOL.willDownload){
			if(worldlower.exists()){
				worldlower.delete();
			}
			try {
				worldlower.createNewFile();
				WriteWorldLower();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
		try {
			ReadWorldLower();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void WriteWorldLower() throws IOException{
		Scanner s = new Scanner(new URL(def.Frame.getInternetFile("worldlower.qol")).openStream());
		fw = new FileWriter(worldlower);
		int width = s.nextInt();
		int height = s.nextInt();
		fw.write(width+" "+height+"\r\n");
		s.skip(s.nextLine());
		for(int i=0;i<height;i++){
			System.out.println("WRITING WORLD LOWER ROW "+i);
			fw.write(s.nextLine()+"\r\n");
		}
		
		fw.flush();
		fw.close();
		
		s = new Scanner(new URL(def.Frame.getInternetFile("version.qol")).openStream());
		fw = new FileWriter(getFileInData("version.qol"));
		
		if(getFileInData("version.qol").exists()){
			getFileInData("version.qol").delete();
			
		}
		getFileInData("version.qol").createNewFile();
		
		fw.write(s.nextLine());
		
		fw.flush();
		fw.close();
		System.out.println("DOWNLOADED WORLD");
	}
	
	public static void ReadWorldLower() throws FileNotFoundException{
		s = new Scanner(worldlower);
		s.nextLine();
		for(int i=0;i<WorldRunner.B[0].length;i++){
			for(int j=0;j<WorldRunner.B[0][i].length;j++){
				int k = s.nextInt();
				WorldRunner.B[0][i][j]=new Block(BlocksDynamics.b[k].ID,BlocksDynamics.b[k].isCollidable,BlocksDynamics.b[k].isVisible,BlocksDynamics.b[k].Name);
						//BlocksDynamics.b[s.nextInt()];
			}
		}
		System.out.println("Starting Game");
	}
	
	public static void WriteWorldUpper() throws IOException{
		System.out.println("DOWNLOADING WORLD WARNING THIS CAN TAKE UP TO 10 MINS ON BAD CONNECTIONS");
		Scanner s = new Scanner(new URL(def.Frame.getInternetFile("worldupper.qol")).openStream());
		fw = new FileWriter(worldupper);
		int width = s.nextInt();
		int height = s.nextInt();
		fw.write(width+" "+height+"\r\n");
		s.skip(s.nextLine());
		for(int i=0;i<height;i++){
			System.out.println("WRITING WORLD UPPER ROW "+i);
			fw.write(s.nextLine()+"\r\n");
		}
		
		fw.flush();
		fw.close();
	}
	
	public static void ReadWorldUpper() throws FileNotFoundException{
		s = new Scanner(worldupper);		
		WorldRunner.B=new Block[2][s.nextInt()][s.nextInt()];
		for(int i=0;i<WorldRunner.B[1].length;i++){
			for(int j=0;j<WorldRunner.B[1][i].length;j++){
				int k = s.nextInt();
				WorldRunner.B[1][i][j]=new Block(BlocksDynamics.b[k].ID,BlocksDynamics.b[k].isCollidable,BlocksDynamics.b[k].isVisible,BlocksDynamics.b[k].Name);
						//BlocksDynamics.b[s.nextInt()];
			}
		}
	}
	
	public static File getFileInData(String i){
		String SEP = System.getProperty("file.separator");
		return new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+i);
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
