package template;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import context.Context;
import context.ContextKey;
import write.OutPut;
import xml.Xml;
import database.TableInfo;

public abstract class TemplateAbstract {
	protected TableInfo tableInfo;
	protected String path;
	protected String workspace;
	protected Map<String,String[]> queryGroup = new HashMap<String,String[]>();
	
	public TemplateAbstract(TableInfo _tableInfo,String _path,String _workspace,Map<String,String[]> _queryGroup){
		tableInfo = _tableInfo;
		path = _path;
		workspace = _workspace;
		if( null != _queryGroup){
			queryGroup = _queryGroup;
		}
		initContext();
	} 
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
	 * 外部接口
	 * @throws Exception 
	 */
	public String template() throws Exception{
		OutPut.createDir(path());
		String code = coding();
		String javaFileName = nameJavaFile();
		OutPut.writeJavaFile(path(), javaFileName, code);
		Xml.configDDLConfig();
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
			className.append(firstLetterUp(split));
		}
		return className.toString();
	}
	/**
	 * 包
	 * @return
	 */
	protected String packaging(){
		String current = workspace;
		if( StringUtils.isEmpty(workspace) ){
			workspace = System.getProperty("user.dir");
		}
		
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
				varName.append(firstLetterUp(splitNames[i]));
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
			methodName.append(firstLetterUp(splitNames[i]));
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
			methodName.append(firstLetterUp(splitNames[i]));
		} 
		return "get"+methodName.toString();
	} 
	
	protected String firstLetterUp(String str){
		if(StringUtils.isEmpty(str)){return "";}
		return String.valueOf(str.charAt(0)).toUpperCase()+str.substring(1);
	}
	
	private void initContext(){ 
		if( StringUtils.isEmpty(workspace) ){
			workspace = System.getProperty("user.dir");
		}
		Context.set(ContextKey.XML_CLASSES_PATH, workspace+File.separator+"conf"+File.separator+"biz"+File.separator+"database"+File.separator+"classes.xml");
		Context.set(ContextKey.XML_CLUSTER_DB_PATH, workspace+File.separator+"conf"+File.separator+"biz"+File.separator+"database"+File.separator+"cluster-configs.xml");
		Context.set(ContextKey.XML_CLUSTER_CACHE_PATH, workspace+File.separator+"conf"+File.separator+"biz"+File.separator+"cache"+File.separator+"cluster-configs.xml");
		Context.set(ContextKey.XML_CACHE_PATH, workspace+File.separator+"conf"+File.separator+"biz"+File.separator+"cache"+File.separator+"caches.xml");
	} 
}