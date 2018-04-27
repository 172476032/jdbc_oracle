package javaToOracle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONObject;


public class GetOracleData {
	public static void main(String[] args) throws SQLException {
		 resultToJson ();
	}
	public static void resultToJson () throws SQLException{
		JSONObject jb = new JSONObject();
		String  sql = "select * from  tabbaseperson where chvid = 10829";
	    try{
	    	ResultSet rSet = OracleHelper.query(sql);
		    ResultSetMetaData md = rSet.getMetaData(); //获得结果集结构信息,元数据  
	        int columnCount = md.getColumnCount();   //获得列数   
	        while (rSet.next()) {  
	            for (int i = 1; i <= columnCount; i++) {
	            	jb.put(md.getColumnName(i), rSet.getObject(i));	            
	           }
	            System.out.println(jb);
	        } 
	    }catch(Exception ex){
	    	
	    }
	    finally {
			
		}
		
	 }
}
 
