package def.fil;

import java.io.*;
import java.net.*;
import java.util.*;
import def.QOL;

import wrld.blcks.*;

public class BlocksDynamics {
	
	public static File blockDyn;
	public static FileWriter fw;
	public static Scanner s;
	
	public static Block[] b;
	
	public static void boot(){
		blockDyn=getFileInData("blocksDyn.qol");
		if(!blockDyn.exists()||QOL.willDownload){
			try {
				if(blockDyn.exists()){
					blockDyn.delete();
				}
				blockDyn.createNewFile();
				write(blockDyn,"0");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!blockDyn.exists()){
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
		String SEP = "/";
		return new File(defaultDirectory()+def.Frame.QOL.mapName+SEP+i);
	}
	
	public static void write(File f,String i) throws IOException{
		def.Frame.QOL.downloading=true;
		System.out.println("Downloading Block Dynamincs");
		Scanner s = new Scanner(new URL(def.Frame.getInternetFile("blocksDyn.qol")).openStream());
		fw = new FileWriter(f);
		int blocks = s.nextInt();
		fw.write(blocks+"\r\n");
		for(int j=0;j<=blocks;j++){
			fw.write(s.nextLine()+"\r\n");
		}
		
		fw.flush();
		fw.close();
		
		System.out.println("Downloaded");
	}
	
	public static void readFiles() throws FileNotFoundException{
		s=new Scanner(blockDyn);
		b = new Block[s.nextInt()];
		int ID=0;
		boolean coll=false;
		boolean vis=false;
		for(int i=0;i<b.length;i++){
			ID=s.nextInt();
			if(s.next().equals("true")){
				coll=true;
			}else{
				coll=false;
			}
			if(s.next().equals("true")){
				vis=true;
			}else{
				vis=false;
			}
			b[i]=new Block(ID,coll,vis,s.next());
		}
	}
	
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

}
