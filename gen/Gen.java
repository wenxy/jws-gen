import java.io.File;
import java.sql.SQLException;

import template.DDLTemplateImp;
import template.TemplateAbstract;
import Log.Log;
import database.DBInfo;
import database.TableInfo;


public class Gen {


	final class Config{
		//生成代码的表名
		public static final String tableName = "sdk_order";
		//代码生成的路径，自动加上ddl，service，dao包名
		public static final String path = "D:\\gitspace\\warthog-biz\\app\\modules\\order_test";
		//数据源连接信息
		public static final String url = "jdbc:mysql://10.10.54.27:3306/biz_01?user=warthog-biz&password=hellowarthog@20150225&useUnicode=true&characterEncoding=8859_1";
		public static final String driver = "com.mysql.jdbc.Driver";
	}
	
	public static void main(String[] args) throws SQLException {
		Log.print("Gen Hello.");
		DBInfo dbInfo = null;
		try{
			dbInfo = new DBInfo(Config.driver,Config.url);
			TableInfo tableInfo = dbInfo.tableInfo(Config.tableName);
			TemplateAbstract template = new DDLTemplateImp(tableInfo,Config.path);
			template.template();
			template.context();
			//Log.print(tableInfo.toString());
			//Log.print(code);
		}catch(Exception e){
			Log.err(e.getMessage());
		}finally{
			if( null != dbInfo){dbInfo.closeConnection();}
			Log.print("Gen finally.");
		}
	}  
}
