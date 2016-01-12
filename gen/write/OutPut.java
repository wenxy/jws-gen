package write;

import java.io.File;

public class OutPut {

	public static void createDir(String path){
		File file = new File(path);
		if(file.exists()){
			return ;
		}else{
			file.mkdirs();
		}
	}
	
	public static void writeJavaFile(String path,String fileName,String code){
		
	}
	 
}
