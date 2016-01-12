package database;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {
	
	private String tableCat;
	private String tableName;
	private String tableSchem;
	private String tableDesc;
	private List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
	
	public String getTableCat() {
		return tableCat;
	}
	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableSchem() {
		return tableSchem;
	}
	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}
	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	} 
	
	public String getTableDesc() {
		return tableDesc==null?"":tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[tableCat->").append(tableCat).append("][tabelName->").append(tableName).append("][tableSchem->").append(tableSchem).append("]");
		for(ColumnInfo c :columnInfos ){
			sb.append(c);
		}
		return sb.toString();
	}
	
	
}
