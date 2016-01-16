package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import log.Log;

public class OutPut {

	public static void createDir(String path){
		File file = new File(path);
		if(file.exists()){
			return ;
		}else{
			file.mkdirs();
		}
	}
	
	public static void writeJavaFile(String path,String fileName,String code) throws IOException{
		FileWriter fw = null;
		BufferedWriter bw = null;
		try{
			File file = new File(path+File.separator+fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(code);
			bw.flush();
			bw.close();
		}catch(Exception e){
			Log.err(e.getMessage());
		}finally{
			if( fw != null){fw.close();}
			if( bw != null){bw.close();}
		}
	}
	 
}
