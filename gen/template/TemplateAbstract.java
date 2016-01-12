package template;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import write.OutPut;
import database.TableInfo;

public abstract class TemplateAbstract {
	protected TableInfo tableInfo;
	protected String path;
	
	/**
	 * 马农
	 * @return
	 * @throws Exception
	 */
	protected abstract String coding()throws Exception ;
	/**
	 * 马路
	 * @return
	 */
	protected abstract String path();
	/**
	 * 马名
	 * @return
	 */
	protected abstract String nameJavaFile();
	/**
	 * 马类
	 * @return
	 */
	protected abstract String nameJavaClass();
	
	/**
	 * 上下文
	 * @return
	 */
	public abstract Map<String,String> context();
	
	/**
	 * 外部接口
	 * @throws Exception 
	 */
	public String template() throws Exception{
		OutPut.createDir(path());
		String code = coding();
		String javaFileName = nameJavaFile();
		OutPut.writeJavaFile(path(), javaFileName, code);
		return code;
	} 
	
	/**
	 * 驼峰命名
	 * @return
	 */
	protected String naming(){
		StringBuffer className = new StringBuffer();
		String tableName = tableInfo.getTableName();
		String[] splitNames = tableName.split("_");
		for(String split : splitNames){
			String uperFirst = String.valueOf(split.charAt(0)).toUpperCase();
			String left = split.substring(1);
			className.append(uperFirst).append(left);
		}
		return className.toString();
	}
	/**
	 * 包
	 * @return
	 */
	protected String packaging(){
		String current = System.getProperty("user.dir");
		current = current.replaceAll("\\\\", ".");
		String packPrefix = current+".app.";
		String thePath = path.replaceAll("\\\\", ".");
		return thePath.replaceAll(packPrefix, "");
	} 
	/**
	 * 代码创建时间
	 * @return
	 */
	protected String codingDate(){
		DateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date());
	}
	/**
	 * 字段命名
	 * @param column
	 * @return
	 */
	protected String varNaming(String column){
		StringBuffer varName = new StringBuffer();
		String[] splitNames = column.split("_");
		for(int i=0;i<splitNames.length;i++){
			if(i==0){
				varName.append(splitNames[i]);
			}else{
				String uperFirst = String.valueOf(splitNames[i].charAt(0)).toUpperCase();
				String left = splitNames[i].substring(1);
				varName.append(uperFirst).append(left);
			}
		} 
		return varName.toString();
	}
	
	/**
	 * setter
	 * @param column
	 * @return
	 */
	protected String setterMethodNaming(String column){
		StringBuffer methodName = new StringBuffer();
		String[] splitNames = column.split("_");
		for(int i=0;i<splitNames.length;i++){
			String uperFirst = String.valueOf(splitNames[i].charAt(0)).toUpperCase();
			String left = splitNames[i].substring(1);
			methodName.append(uperFirst).append(left);
		} 
		return "set"+methodName.toString();
	}
	/**
	 * getter
	 * @param column
	 * @return
	 */
	protected String getterMethodNaming(String column){
		StringBuffer methodName = new StringBuffer();
		String[] splitNames = column.split("_");
		for(int i=0;i<splitNames.length;i++){
			String uperFirst = String.valueOf(splitNames[i].charAt(0)).toUpperCase();
			String left = splitNames[i].substring(1);
			methodName.append(uperFirst).append(left);
		} 
		return "get"+methodName.toString();
	}
}
