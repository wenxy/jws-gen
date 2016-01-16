package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import log.Log;
 

public class DBInfo {

	private Connection connection;
	private DatabaseMetaData meta;
 	
	public DBInfo(String driver,String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Class.forName(driver).newInstance();
		connection = DriverManager.getConnection(url);
		meta = connection.getMetaData(); 
	}
	
	public TableInfo tableInfo(String tableName) throws SQLException{
		TableInfo tableInfo = new TableInfo();
		ResultSet tableSet =  meta.getTables(connection.getCatalog(), "%", "%", new String[]{"TABLE"});
		while(tableSet.next()){
			if( tableSet.getString("TABLE_NAME").equals(tableName)){
				tableInfo.setTableCat(tableSet.getString("TABLE_CAT"));
				tableInfo.setTableSchem(tableSet.getString("TABLE_SCHEM"));
				tableInfo.setTableName(tableSet.getString("TABLE_NAME"));
				tableInfo.setTableDesc(tableSet.getString("REMARKS"));
				break;
			} 
		}
		tableSet.close();
		
		ResultSet pkSet =  meta.getPrimaryKeys(connection.getCatalog(), null, tableName);
		Map<String,String> pkmap = new HashMap<String,String>();
		while(pkSet.next()){
			String columnName = pkSet.getString("COLUMN_NAME");
			String seq = pkSet.getString("KEY_SEQ");
			pkmap.put(columnName, seq);
		}
		pkSet.close();
		
		ResultSet columnSet = meta.getColumns(connection.getCatalog(), "%", tableName, "%");
		while(columnSet.next()){
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setColumnName(columnSet.getString("COLUMN_NAME"));
			columnInfo.setType(columnSet.getInt("DATA_TYPE"));
			columnInfo.setRemarks(columnSet.getString("REMARKS"));
			columnInfo.setAutoIncr(columnSet.getString("IS_AUTOINCREMENT").equals("YES"));
			columnInfo.setPk(pkmap.containsKey(columnSet.getString("COLUMN_NAME")));
			
			switch(columnInfo.getType()){
			case java.sql.Types.BIT:
			case java.sql.Types.TINYINT:
			case java.sql.Types.SMALLINT:
			case java.sql.Types.INTEGER:
				columnInfo.setJwsDbType("DbType.Int");
				columnInfo.setJavaType("Integer");
				break;
				
			case java.sql.Types.BIGINT:
				columnInfo.setJwsDbType("DbType.BigInt");
				columnInfo.setJavaType("Long");
				break;
				
			case java.sql.Types.FLOAT:
				columnInfo.setJwsDbType("DbType.Float");
				columnInfo.setJavaType("Float");
				break;
				
			case java.sql.Types.NUMERIC:
			case java.sql.Types.DECIMAL:
			case java.sql.Types.DOUBLE:
				columnInfo.setJwsDbType("DbType.Double");
				columnInfo.setJavaType("Double");
				break;
				
			case java.sql.Types.CHAR:
				columnInfo.setJwsDbType("DbType.Char");
				columnInfo.setJavaType("Char");
				break;
				
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
				columnInfo.setJwsDbType("DbType.Varchar");
				columnInfo.setJavaType("String");
				break;
				
			case java.sql.Types.DATE:
			case java.sql.Types.TIME:
			case java.sql.Types.TIMESTAMP:
				columnInfo.setJwsDbType("DbType.DateTime");
				columnInfo.setJavaType("Long");
				break;
				
			case java.sql.Types.BLOB:
				columnInfo.setJwsDbType("DbType.Blob");
				columnInfo.setJavaType("java.sql.Blob");
				break;
				
		/*	default:
				dbType = "DbType.Blob";
				javaType = "Object";*/
		} 			
			tableInfo.getColumnInfos().add(columnInfo);
		}
		columnSet.close();
		
		return tableInfo;
	} 
	
	public void closeConnection(){
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				Log.err(e.getMessage());
			}
		}
	}
}
