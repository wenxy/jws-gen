package database;

import java.sql.Types;

public class ColumnInfo {

	private String columnName;
	private int type; 
	private String remarks;
	private boolean isPk;
	private boolean isAutoIncr;
	private String jwsDbType;
	private String javaType;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	} 
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public boolean isAutoIncr() {
		return isAutoIncr;
	}
	public void setAutoIncr(boolean isAutoIncr) {
		this.isAutoIncr = isAutoIncr;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	} 
	
	public String getJwsDbType() {
		return jwsDbType;
	}
	public void setJwsDbType(String jwsDbType) {
		this.jwsDbType = jwsDbType;
	}
	public String getJavaType() {
		return javaType==null?"Object":javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String toString(){
		return "[columnName->"+columnName+"][type->"+type+"][isPk->"+isPk+"][isAutoIncr->"+isAutoIncr+"][remarks->"+remarks+"]\n";
	}
	
}
