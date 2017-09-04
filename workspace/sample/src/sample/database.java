package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class database {
	//public static int node;
	//private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
    public static void storeData(){
		String url = "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false";
		String sql = "SELECT * FROM location ORDER BY id DESC LIMIT 1;";
		//SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		JavaServer sv = new JavaServer();
        //int nodeNum = sv.minIndex +1;

		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = 
					DriverManager.getConnection(url, "root","root");
			
			Statement stmt = conn.createStatement();
			//ResultSet rs = stmt.executeQuery(sql);
			//stmt.executeUpdate("insert into location (time, node) values(NOW()," + nodeNum+ ");");
			//ResultSet rs = stmt.executeQuery(sql);
			//int node = rs.getInt("node");
			//System.out.println(node);
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC �뱶�씪�씠踰� 濡쒕뱶 �삤瑜�");
		} catch (SQLException e) { 
			System.out.println("DB �뿰寃� �삤瑜�");
		} 
		
	}
	
	//database listner �벐硫� �뼱�뼥吏�? �씪�떒 �떆�룄�빐蹂댁옄
	public static void getData(){
		String url = "jdbc:mysql://localhost:3306/raspberrypi?autoReconnect=true&useSSL=false";
		String sql1 = "SELECT * FROM sensor ORDER BY id DESC LIMIT 1;";
		String sql2 = "SELECT * FROM location ORDER BY id DESC LIMIT 1;";
		Timestamp sTimeStamp = null;
		int lock = 0;
		int distance = 0;
		int gas = 0;
		
		Timestamp bTimeStamp = null;
		int node = 0;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = 
					DriverManager.getConnection(url, "root","root");
			
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery(sql1);
			ResultSet rs2 = stmt.executeQuery(sql2);
			if(rs1.next()) { 
				sTimeStamp = rs1.getTimestamp("time");
				lock = rs1.getInt("lock");
				distance = rs1.getInt("distance");
				gas = rs1.getInt("gas");
			}
			
			if(rs2.next()){
				bTimeStamp = rs2.getTimestamp("time");
				node = rs2.getInt("node");
			}
			
			
			
			if(gas == 1&&node== 1){
				//�븣由�?
			}
			
			if(distance == 1&&node== 16){
				//�븣由�? 
			}
			
			if(gas == 1 && node != 1){
				//�븣由쇰せ�븯寃�  
			}
			
			if(distance == 1 && node != 16){
				//�븣由쇰せ�븯寃� 
			}
			
			
			
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC �뱶�씪�씠踰� 濡쒕뱶 �삤瑜�");
		} catch (SQLException e) { 
			System.out.println("DB �뿰寃� �삤瑜�");
		} 
	}
}
