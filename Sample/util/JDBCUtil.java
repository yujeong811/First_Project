package util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {

	private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String user = "sem";
	private static String password = "java";
	
	private static Connection con = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	
	/*
	 * Map<String, Object> selectOne(String sql)
	 * Map<String, Object> selectOne(String sql, List<Object> param)
	 * List<Map<String, Object>> selectList(String sql)
	 * List<Map<String, Object>> selectList(String sql, List<Object> param)
	 * int update(String sql)
	 * int update(String sql, List<Object> param)
	 */
	
	public static Map<String, Object> selectOne(String sql){
		Map<String, Object> map = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			if(rs.next()) {
				map = new HashMap<String, Object>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), cast(rs.getObject(i)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return map;
	}
	
	public static Map<String, Object> selectOne(String sql, List<Object> param){
		Map<String, Object> map = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			if(rs.next()) {
				map = new HashMap<String, Object>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), cast(rs.getObject(i)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return map;
	}
	
	public static List<Map<String, Object>> selectList(String sql){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), cast(rs.getObject(i)));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return list;
	}
	
	public static List<Map<String, Object>> selectList(String sql, List<Object> param){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), cast(rs.getObject(i)));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return list;
	}
	
	public static int update(String sql) {
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return result;
	}
	
	public static int update(String sql, List<Object> param) {
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return result;
	}
	
	private static Object cast(Object value) {
		Object rtnValue = null;
		
		if(value.getClass() == BigDecimal.class) {
			BigDecimal num = (BigDecimal)value;
			
			
			rtnValue = ((BigDecimal)value).doubleValue();
		}else {
			rtnValue = value;
		}
		
		return rtnValue;
	}
	
}















