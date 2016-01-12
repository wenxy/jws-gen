package template;

import java.io.File;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.common.DbType;
import database.ColumnInfo;
import database.TableInfo;

public class DDLTemplateImp extends TemplateAbstract{
	
	public DDLTemplateImp(TableInfo _tableInfo,String _path){
		tableInfo = _tableInfo;
		path = _path;
	} 
	@Override
	protected String nameJavaFile() { 
		return naming()+"DDL.java";
	}

	@Override
	protected String path() {
		return path+File.separator+"ddl";
	}

	@Override
	protected String nameJavaClass() {
		return naming()+"DDL";
	} 
	
	@Override
	protected String coding() throws Exception { 
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(packaging()).append(".ddl").append(";\n\n");
		//import
		sb.append("import jws.dal.annotation.Column;").append("\n");
		sb.append("import jws.dal.annotation.GeneratedValue;").append("\n");
		sb.append("import jws.dal.annotation.GenerationType;").append("\n");
		sb.append("import jws.dal.annotation.Id;").append("\n");
		sb.append("import jws.dal.annotation.Table;").append("\n");
		sb.append("import jws.dal.common.DbType;").append("\n");
		 
		
		//comment
		sb.append("/**").append("\n");
		sb.append(" * ").append(tableInfo.getTableDesc()).append("\n");
		sb.append(" * ").append("@author auto").append("\n");
		sb.append(" * ").append("@createDate ").append(codingDate()).append("\n");
		sb.append(" **/").append("\n");
		
		//class start
		sb.append("@Table(name=").append("\"").append(tableInfo.getTableName()).append("\"").append(")").append("\n");
		sb.append("public class ").append(nameJavaClass()).append("{").append("\n");
		
		//getter setter
		for(ColumnInfo column:tableInfo.getColumnInfos()){
			if(column.isPk()){
				sb.append("\t@Id").append("\n");
			}
			if(column.isAutoIncr()){
				sb.append("\t@GeneratedValue(generationType= GenerationType.Auto)").append("\n");
			}
			String varName = varNaming(column.getColumnName());
			String dbType=null;
			String javaType="Object";
			
			switch(column.getType()){
				case java.sql.Types.BIT:
				case java.sql.Types.TINYINT:
				case java.sql.Types.SMALLINT:
				case java.sql.Types.INTEGER:
					dbType = "DbType.Int";
					javaType = "Integer";
					break;
					
				case java.sql.Types.BIGINT:
					dbType = "DbType.BigInt";
					javaType = "Long";
					break;
					
				case java.sql.Types.FLOAT:
					dbType = "DbType.Float";
					javaType = "Float";
					break;
					
				case java.sql.Types.NUMERIC:
				case java.sql.Types.DECIMAL:
				case java.sql.Types.DOUBLE:
					dbType = "DbType.Double";
					javaType = "Double";
					break;
					
				case java.sql.Types.CHAR:
					dbType = "DbType.Char";
					javaType = "Char";
					break;
					
				case java.sql.Types.VARCHAR:
				case java.sql.Types.LONGVARCHAR:
					dbType = "DbType.Varchar";
					javaType = "String";
					break;
					
				case java.sql.Types.DATE:
				case java.sql.Types.TIME:
				case java.sql.Types.TIMESTAMP:
					dbType = "DbType.DateTime";
					javaType = "Long";
					break;
					
				case java.sql.Types.BLOB:
					dbType = "DbType.Blob";
					javaType = "java.sql.Blob";
					break;
					
			/*	default:
					dbType = "DbType.Blob";
					javaType = "Object";*/
			}
			//var
			sb.append("\t@Column(name=\"").append(column.getColumnName()).append("\", type=").append(dbType).append(")").append("\n");
			sb.append("\tprivate ").append(javaType).append(" ").append(varName).append(";\n");
			
			//getter
			sb.append("\tpublic ").append(javaType).append(" ").append(getterMethodNaming(column.getColumnName())).append("() {\n");
			sb.append("\t\treturn ").append(varName).append(";\n");
			sb.append("\t}\n");
			
			//setter
			sb.append("\tpublic void ").append(setterMethodNaming(column.getColumnName())).append("(").append(javaType).append(" ").append(varName).append("){\n");
			sb.append("\t\tthis.").append(varName).append("=").append(varName).append(";\n");
			sb.append("\t}\n\n");
		}
		//Example
		sb.append("\tpublic static ").append(nameJavaClass()).append(" newExample(){").append("\n");
		sb.append("\t\t").append(nameJavaClass()).append(" object=new ").append(nameJavaClass()).append("();\n");
		for(ColumnInfo column:tableInfo.getColumnInfos()){
			sb.append("\t\tobject").append(".").append(setterMethodNaming(column.getColumnName())).append("(null);\n");
		}
		sb.append("\t\treturn object;\n");
		sb.append("\t}").append("\n");
		
		//class end
		sb.append("}\n");
		return sb.toString();
	}
	
	 
}
