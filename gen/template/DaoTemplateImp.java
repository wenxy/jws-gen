package template;

import java.io.File;
import java.util.Map;

import xml.Xml;
import context.Context;
import context.ContextKey;
import database.ColumnInfo;
import database.TableInfo;

public class DaoTemplateImp extends TemplateAbstract {
 
	public DaoTemplateImp(TableInfo _tableInfo, String _path, String _workspace,Map<String,String[]> queryGroup) {
		super(_tableInfo, _path, _workspace,queryGroup);
	} 

	private String packingName(){
		String pName = packaging()+".dao";
		return pName;
	}
	@Override
	protected String coding() throws Exception {
 		String ddlClassName = Context.get(ContextKey.DDL_CLASS_NAME);

		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(packingName()).append(";\n\n");
		//import
		sb.append("import java.util.ArrayList;").append("\n");
		sb.append("import java.util.List;").append("\n");
		sb.append("import jws.dal.Dal;").append("\n");
		sb.append("import jws.dal.sqlbuilder.Shard;").append("\n");
		sb.append("import ").append(Context.get(ContextKey.DDL_PACKAGE_NAME)).append(".").append(ddlClassName).append(";\n"); 
		 
		
		//comment
		sb.append("/**").append("\n");
		sb.append(" * ").append(tableInfo.getTableDesc()).append(" Dao\n");
		sb.append(" * ").append("@author auto").append("\n");
		sb.append(" * ").append("@createDate ").append(codingDate()).append("\n");
		sb.append(" **/").append("\n");
		
		//class start
 		sb.append("public class ").append(nameJavaClass()).append("{").append("\n");
 		//insert
 		sb.append("\tpublic static boolean insert (").append(ddlClassName).append(" param,Shard shard){\n");
 		sb.append("\t\tif(param==null){return false;}\n");
 		sb.append("\t\tif(Dal.rowcacheInsert(param) >0){\n");
 		sb.append("\t\t\tlistCacheUpdate(param,shard);\n");
 		sb.append("\t\t\treturn true;\n");
 		sb.append("\t\t}\n");
 		sb.append("\t\treturn false;\n");
 		sb.append("\t}\n");
 		
 		//delete
 		sb.append("\tpublic static boolean delete (long id,Shard shard){\n");
 		sb.append("\t\tif(id<0){return false;}\n");
 		sb.append("\t\t").append(ddlClassName).append(" param = get(id);\n");
 		sb.append("\t\tif(Dal.rowcacheDelete(").append(ddlClassName).append(".class,id,shard)>0){\n");
  		sb.append("\t\t\tlistCacheUpdate(param,shard);\n");
 		sb.append("\t\t\treturn true;\n");
 		sb.append("\t\t}\n"); 
 		sb.append("\t\treturn false;\n");
 		sb.append("\t}\n");
 		
 		//update
 		sb.append("\tpublic static boolean update (").append(ddlClassName).append(" param,String update,Shard shard){\n");
 		sb.append("\t\tif(param==null){return false;}\n");
 		sb.append("\t\tif(Dal.rowcacheUpdate(param,update,shard)>0){\n");
   		sb.append("\t\t\tlistCacheUpdate(param,shard);\n");
 		sb.append("\t\t\treturn true;\n");
 		sb.append("\t\t}\n");
 		sb.append("\t\treturn false;\n");
 		sb.append("\t}\n");
 		
 		sb.append("\tpublic static ").append(ddlClassName).append(" get(long id){\n");
 		sb.append("\t\treturn Dal.rowcacheSelect(").append(ddlClassName).append(".class,id);\n");
 		sb.append("\t}\n");
 		
 		if(tableInfo.getPKColumn()==null){
 			throw new Exception("不存在主键，请设置主键");
 		}
 		String pkName =  tableInfo.getPKColumn().getColumnName();
 		String pkType = tableInfo.getPKColumn().getJavaType();
 		String varDDL = "ddl";
 		
 		StringBuffer listCacheReflash = new StringBuffer();
 		StringBuffer listQuery = new StringBuffer();
 		StringBuffer queryCount = new StringBuffer();
 		
 		listCacheReflash.append("\tpublic static void listCacheUpdate(").append(ddlClassName).append(" ").append(varDDL).append(",").append("Shard shard){\n");
 		//query method
 		for(String key:queryGroup.keySet()){
 			String[] qKey = queryGroup.get(key);
 			queryCount.append("\tpublic static int ");
 			listQuery.append("\tpublic static List<").append(ddlClassName).append("> ");
 			StringBuffer listMethod = new StringBuffer("listBy");
 			StringBuffer countMethod = new StringBuffer("countBy");
 			StringBuffer params = new StringBuffer();
 			StringBuffer setterBody = new StringBuffer();
 			StringBuffer sqlKey = new StringBuffer();
 			StringBuffer sql = new StringBuffer("select ").append(pkName).append(" from ").append(tableInfo.getTableName()).append(" where 1=1 ");
 			for(int i=0;i<qKey.length;i++){
 				ColumnInfo column = tableInfo.getColumn(qKey[i]);
 				if( column == null ){
 					throw new Exception("column not exisit.params="+qKey[i]);
 				}
 				sql.append(" And ").append(column.getColumnName()).append("=? ");
 				params.append(column.getJavaType()).append(" ").append(varNaming(qKey[i])).append(",");
 				if( i != qKey.length-1 ){
 					listMethod.append(firstLetterUp(varNaming(qKey[i]))).append("And");
 					countMethod.append(firstLetterUp(varNaming(qKey[i]))).append("And");
 					sqlKey.append(qKey[i]).append(",");
 				}else{
 					listMethod.append(firstLetterUp(varNaming(qKey[i])));
 					countMethod.append(firstLetterUp(varNaming(qKey[i])));
 					sqlKey.append(qKey[i]);
 				}
 				setterBody.append("\t\t").append(varDDL).append(".").append(setterMethodNaming(qKey[i]));
 				setterBody.append("(").append(varNaming(qKey[i])).append(");\n");
 			}
 			//listMethod
 			listQuery.append(listMethod).append("(").append(params).append("int offset,int count,Shard shard){\n");
 			listQuery.append("\t\t").append(ddlClassName).append(" ").append(varDDL).append( "  = new ").append(ddlClassName).append("();\n");
 			listQuery.append(setterBody);
 			listQuery.append("\t\tList<").append(ddlClassName).append("> idList = Dal.listcacheSelect(").append("\"").append(listMethod).append("\",").append(varDDL).append(",offset,count,shard);\n");
 			listQuery.append("\t\tList<").append(pkType).append("> ids = new ArrayList<").append(pkType).append(">();\n");
 			listQuery.append("\t\tif(idList!=null && idList.size()>0){\n");
 			listQuery.append("\t\t\tfor(").append(ddlClassName).append(" o:idList){\n");
 			listQuery.append("\t\t\t\tids.add(o.").append(getterMethodNaming(pkName)).append("());\n");
 			listQuery.append("\t\t\t}\n");
 			listQuery.append("\t\t}\n");
 			listQuery.append("\t\treturn Dal.rowcacheSelectMulti(").append(ddlClassName).append(".class,ids);\n");
 			listQuery.append("\t}\n");
 			
 			//countMethod
 			queryCount.append(countMethod).append("(").append(params).append("Shard shard){\n");
 			queryCount.append("\t\t").append(ddlClassName).append(" ").append(varDDL).append( "  = new ").append(ddlClassName).append("();\n");
 			queryCount.append(setterBody);
 			queryCount.append("\t\treturn Dal.listcacheCount(\"").append(listMethod).append("\",").append(varDDL).append(",shard);\n");
 			queryCount.append("\t}\n");
 			
 			listCacheReflash.append("\t\tDal.listcacheUpdate(\"").append(listMethod).append("\",").append(varDDL).append(",shard);\n");
 			
 			Xml.configListCacheConfig(listMethod.toString(), sql.toString(), sqlKey.toString(), sqlKey.toString(), pkName);
 		}
 		listCacheReflash.append("\t}\n");
 		sb.append(listQuery);
 		sb.append(queryCount);
 		sb.append(listCacheReflash);
 		sb.append("}\n");
		return sb.toString();
	}

	@Override
	protected String path() {
		return path+File.separator+"dao";
	}

	@Override
	protected String nameJavaFile() {
		return naming()+"AutoDao.java";
	}

	@Override
	protected String nameJavaClass() {
		return naming()+"AutoDao";
	}
}
