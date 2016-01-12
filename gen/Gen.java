import java.io.File;
import java.sql.SQLException;

import template.DDLTemplateImp;
import template.TemplateAbstract;
import Log.Log;
import database.DBInfo;
import database.TableInfo;


public class Gen {

	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://10.10.54.27:3306/biz_01?user=warthog-biz&password=hellowarthog@20150225&useUnicode=true&characterEncoding=8859_1";
	
	public static void main(String[] args) throws SQLException {
		Log.print("Gen Hello.");
		DBInfo dbInfo = null;
		try{
			dbInfo = new DBInfo(driver,url);
			TableInfo tableInfo = dbInfo.tableInfo("sdk_order");
			TemplateAbstract template = new DDLTemplateImp(tableInfo,"D:\\gitspace\\warthog-biz\\app\\modules\\order_test");
			String code = template.template();
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
