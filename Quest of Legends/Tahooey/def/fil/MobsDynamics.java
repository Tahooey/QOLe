package def.fil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import def.QOL;
import wrld.mobs.Mob;

public class MobsDynamics {
	
	public static File mobsDyn;
	public static FileWriter fw;
	public static Scanner s;
	
	public static Mob[] b;
	
	public static void boot(){
		mobsDyn=getFileInData("mobsDyn.qol");
		if(!mobsDyn.exists()||QOL.willDownload){
			if(mobsDyn.exists()){
				mobsDyn.delete();
			}
			try {
				mobsDyn.createNewFile();
				write(mobsDyn,"0");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!mobsDyn.exists()){
				System.out.println("AN ERROR HAS OCCURED IN FILE WRITING");
			}
		}
		try {
			readFiles();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static File getFileInData(String i){
		String SEP = System.getProperty("file.separator");
		return new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+i);
	}
	
	public static void write(File f,String i) throws IOException{
		System.out.println("Downloading Mobs Dynamics");
		Scanner s = new Scanner(new URL(def.Frame.getInternetFile("mobsDyn.qol")).openStream());
		fw = new FileWriter(f);
		int mobs = s.nextInt();
		fw.write(mobs+"\r\n");
		for(int j=0;j<=mobs;j++){
			fw.write(s.nextLine()+"\r\n");
		}
		
		fw.flush();
		fw.close();
		
		System.out.println("Downloaded");
	}
	
	public static void readFiles() throws FileNotFoundException{
		s=new Scanner(mobsDyn);
		b = new Mob[s.nextInt()];
		for(int i=0;i<b.length;i++){
			b[i]=new Mob(s.nextInt());
		}
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
