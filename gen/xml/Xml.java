package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import context.Context;
import context.ContextKey;
import log.Log;

/**
 * JWS配置部分，包含biz/cache及biz/database
 * memcache默认为client0
 * database默认为dbbase
 * 需要修改，请在代码生成之后调整
 * @author Administrator
 *
 */
public class Xml {
	
	/**
	 * 处理DDL相关配置
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void configDDLConfig() throws DocumentException, IOException{
		String classXml = Context.get(ContextKey.XML_CLASSES_PATH);
		String dbXml = Context.get(ContextKey.XML_CLUSTER_DB_PATH);
		String cacheXml = Context.get(ContextKey.XML_CACHE_PATH);
		String cacheClusterXml = Context.get(ContextKey.XML_CLUSTER_CACHE_PATH);
		
		String packaging = Context.get(ContextKey.DDL_PACKAGE_NAME);
		String className = Context.get(ContextKey.DDL_CLASS_NAME);
		String comment = Context.get(ContextKey.DDL_CLASSES_COMMENT);
		
		//classes.xml配置
		SAXReader reader = new SAXReader();
		Document document = reader.read( new File(classXml)); 
		Element element  = find(document,"//classes/package/@name",packaging);
		if( element == null ){
			Element opElm =  find(document,"//classes",null);
			Map attrs = new HashMap();
			attrs.put("name", packaging);
			addElement(opElm,"package",attrs);
		}else{
			Log.print("package is exisit,name="+packaging);
		}
		writeDocument(document,classXml);
		
		element  = find(document,"//classes/package/class/@name",className);
		if( element == null ){
			Element opElm =  find(document,"//classes/package/@name",packaging);
			Map attrs = new HashMap();
			attrs.put("name", className);
			attrs.put("comment", comment);
			addElement(opElm,"class",attrs); 
		}else{
			Log.print("class is exisit,class="+className);
		}
		writeDocument(document,classXml);
		
		
		//cluster-configs.xml
 		document = reader.read( new File(dbXml)); 
		element  = find(document,"//configs/config/@class",packaging+"."+className);
		if( element == null ){
			Element opElm =  find(document,"//configs",null);
			Map attrs = new HashMap();
			attrs.put("class", packaging+"."+className);
			attrs.put("source", "mysql");
			attrs.put("shard-type", "none");
 			attrs.put("comment", comment);
			addElement(opElm,"config",attrs); 
		}else{
			Log.print("classes.xml->class is exisit,class="+className);
		}
		
		writeDocument(document,dbXml);
		
		if(true){
			Element opElm =  find(document,"//configs/config/@class",packaging+"."+className);
			if( opElm.elements() == null || opElm.elements().size() == 0){
				Map attrs = new HashMap();
	 			attrs.put("client", "dbbase");
				addElement(opElm,"map",attrs); 
			}else{
				Log.print("cluster-configs.xml->db map is exisit,class="+className);
			}
		}
		writeDocument(document,dbXml);
		
		
		//cache.xml配置
				
		document = reader.read(new File(cacheXml));
		element = find(document, "//config/row-cache/cache/@prefix", className + "_v1");
		if (null == element) {
			Element opElm = find(document, "//config/row-cache", null);
			Map attrs = new HashMap();
			// attrs.put("name", listName);
			attrs.put("class", packaging + "." + className);
			attrs.put("prefix", className + "_v1");
			attrs.put("enabled", "true");
			attrs.put("comment", comment);
			addElement(opElm, "cache", attrs);
		} else {
			Log.print("cache.xml->rowCache,row-cache is exisit.prefix=" + className + "_v1");
		}
		writeDocument(document, cacheXml);
		
		/*//cache.xml
		document = reader.read( new File(cacheXml)); 
		element  = find(document,"//caches/row-cache/cache/@class",packaging+"."+className);
		if( element == null ){
			Element opElm =  find(document,"//caches/row-cache",null);
			Map attrs = new HashMap();
			attrs.put("class", packaging+"."+className);
			attrs.put("prefix", className+"_v1");
			attrs.put("enabled", "true");
 			attrs.put("comment", comment);
			addElement(opElm,"cache",attrs); 
		}else{
			Log.print("cache.xml->db row-cache is exisit,class="+className);
		}
		writeDocument(document,cacheXml);
		
		document = reader.read( new File(cacheClusterXml)); 
		element  = find(document,"//configs/config/@prefix",className+"_v1");
		if( element == null ){
			Element opElm =  find(document,"//configs",null);
			Map attrs = new HashMap();
			attrs.put("client", "client0");
			attrs.put("prefix", className+"_v1");
			attrs.put("cache_time", "864000");
 			attrs.put("comment", comment);
			addElement(opElm,"config",attrs); 
		}else{
			Log.print("cluster-configs.xml->cache row-cache is exisit,class="+className);
		}
		writeDocument(document,cacheClusterXml);*/
		
	} 
	 
	/**
	 * 配置listCache缓存
	 * @param listName
	 * @param sql
	 * @param key
	 * @param params
	 * @param cacheItem
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void configListCacheConfig(String listName,String sql,String key,String params,String cacheItem) throws DocumentException, IOException{
		String cacheXml = Context.get(ContextKey.XML_CACHE_PATH);
		String cacheClusterXml = Context.get(ContextKey.XML_CLUSTER_CACHE_PATH);
		
		String packaging = Context.get(ContextKey.DDL_PACKAGE_NAME);
		String className = Context.get(ContextKey.DDL_CLASS_NAME);
		String comment = Context.get(ContextKey.DDL_CLASSES_COMMENT);
		
		Element element = null;
		//cache.xml配置
		SAXReader reader = new SAXReader();
		Document document = reader.read( new File(cacheXml)); 
		/*element  = find(document,"//config/row-cache/cache/@prefix",className+"_v1");
		if( null == element ){
			Element opElm =  find(document,"//config/row-cache",null);
			Map attrs = new HashMap();
			//attrs.put("name", listName);
			attrs.put("class", packaging+"."+className);
			attrs.put("prefix", className+"_v1");
			attrs.put("enabled", "true");
 			attrs.put("comment", comment);
			addElement(opElm,"cache",attrs); 
		}else{
			Log.print("cache.xml->rowCache,row-cache is exisit.prefix="+className+"_v1");
		}
		writeDocument(document,cacheXml); */
		
		element  = find(document,"//config/list-cache/cache/@prefix",listName+"_");
		if(element == null){
			Element opElm  = find(document,"//config/list-cache",null);
			Map attrs = new HashMap(); 
			attrs.put("name", listName);
			attrs.put("class", packaging+"."+className);
			attrs.put("prefix", listName+"_");
			attrs.put("enabled", "true");
 			attrs.put("comment", comment);
			addElement(opElm,"cache",attrs);
			attrs.clear();
			opElm  = find(document,"//config/list-cache/cache/@prefix",listName+"_");
			addElement(opElm,"sql",attrs,sql); 
			addElement(opElm,"key",attrs,key); 
			addElement(opElm,"params",attrs,params); 
			addElement(opElm,"cache-item",attrs,cacheItem);
			addElement(opElm,"offset",attrs,"0");
			addElement(opElm,"count",attrs,"100");
 		}else{
			Log.print("cache.xml->listCache,list-cache is exisit.listName="+listName);
		}
		writeDocument(document,cacheXml);
		
		// cacheClusterXml配置
		reader = new SAXReader();
		document = reader.read(new File(cacheClusterXml));
		element  = find(document,"//configs/config/@prefix",listName+"_");
		if( element == null ){
			Element opElm =  find(document,"//configs",null);
			Map attrs = new HashMap();
			attrs.put("client", "client0");
			attrs.put("prefix", listName+"_");
			attrs.put("cache_time", "864000");
 			attrs.put("comment", comment);
			addElement(opElm,"config",attrs); 
		}else{
			Log.print("cluster-configs.xml->cache list-cache is exisit,listName="+listName);
		}
		writeDocument(document,cacheClusterXml); 
	}
	/**
	 * 查找Element
	 * @param document
	 * @param searchPath
	 * @param attrName
	 * @return
	 */
	private static Element find(Document document,String searchPath,String attrName){
		List list = document.selectNodes(searchPath);
		Iterator itList = list.iterator();
		while( itList.hasNext() ){
			if( StringUtils.isEmpty(attrName) ){
				return (Element)itList.next();
			}else if(searchPath.contains("@")) {
				Attribute attr = (Attribute)itList.next();
				if( attrName.equals(attr.getText()) ){
					return attr.getParent();
				}
			} 
		}
		return null;
	}
	/**
	 * 在父element下增加元素
	 * @param parent
	 * @param nodeName
	 * @param attrs
	 */
	private static void addElement(Element parent,String nodeName,Map<String,String> attrs){
		if( null == parent  || StringUtils.isEmpty(nodeName) ){
			return ;
		}
		Element element = parent.addElement(nodeName);
		if( attrs != null ){
			for(String key :attrs.keySet()){
				element.addAttribute(key, attrs.get(key));
			}
		}
	}
	private static void addElement(Element parent,String nodeName,Map<String,String> attrs,String text){
		if( null == parent || StringUtils.isEmpty(nodeName) ){
			return ;
		}
		Element element = parent.addElement(nodeName);
		if(attrs !=null ){
			for(String key :attrs.keySet()){
				element.addAttribute(key, attrs.get(key));
			}
		}
		if(!StringUtils.isEmpty(text)){
			element.addText(text);
		}
		 
	}
	/**
	 * 写回xml文件
	 * @param document
	 * @param file
	 * @throws IOException
	 */
	private static void writeDocument(Document document,String file) throws IOException{
		if( document == null || StringUtils.isEmpty(file) ){
			return ;
		}
		OutputFormat format = OutputFormat.createPrettyPrint(); 
        format.setEncoding( "UTF-8"); 
		XMLWriter writer = new XMLWriter( new FileOutputStream( new File(file)),format);
		writer.write(document); 
		writer.close();
	}
}
