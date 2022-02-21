package util;

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
	
	// 데이터베이스 접속 정보
	private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String user = "MUSINSA";
	private static String password = "java";
	
	private static Connection con = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	
	// 메서드 안에서 결정할 수 없는 데이터(쿼리)가 있는 경우 파라미터로 받아야 함 
	// > 쿼리를 파라미터로 받아서 실행 후 결과를 리턴하는 메서드 생성
	
	/*
	 * // String sql :쿼리	
	 * // selectOne:조회 결과가 한 줄. 결과를 HashMap에 담아 리턴	
	 * Map<String, Object> selectOne(String sql) : 물음표 없을 때 사용
	 * Map<String, Object> selectOne(String sql, List<Object> param) : sql문 안에 물음표가 있을 때 사용
	 * 
	 * // selectList:조회 결과가 여러 줄. 결과가 여러 줄이면 HashMap을 List에 담아(List<Map<String, Object>>) 리턴
	 * List<Map<String, Object>> selectList(String sql) : 물음표 없을 때 사용
	 * List<Map<String, Object>> selectList(String sql, List<Object> param) : sql문 안에 물음표가 있을 때 사용
	 * 
	 * // select를 제외한 나머지(update, insert, delete)를 하기 위한 메서드. 영향을 받은 행의 수 리턴
	 * int update(String sql) : 물음표 없을 때 사용
	 * int update(String sql, List<Object> param) : sql문 안에 물음표가 있을 때 사용
	 */
	
	public static Map<String, Object> selectOne(String sql) {
		// 파라미터로 쿼리를 받는 메서드. 실행 결과가 한 줄이기 때문에 HashMap에 담아 리턴
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			// 연결 작업 > 커넥션 객체를 리턴받아 변수에 저장
			con = DriverManager.getConnection(url, user, password);
			
			// 문자열로 된 쿼리를 오라클에 전송하기 적합한 형태로 바꿔서 리턴
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			// 컬럼의 수 확인
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			if(rs.next()) {
				map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					// HashMap은 (컬럼명, 컬럼값)으로 이루어짐
					// metaData.getColumnName(i) : 파라미터로 인덱스를 넘겨주면 해당 컬럼명을 알 수 있음
					map.put(metaData.getColumnName(i), rs.getObject(i));
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(Exception e) {}
			if(ps != null) try {ps.close();} catch(Exception e) {}
			if(con != null) try {con.close();} catch(Exception e) {}
		}
		return map;
	}
	
	
	public static Map<String,Object> selectOne(String sql, List<Object> param) {
		// 파라미터로 쿼리를 받는 메서드. 실행 결과가 한 줄이기 때문에 HashMap에 담아 리턴
		
		// 조회 결과가 없는 경우 null이 넘어오게 하기 위해 null을 넣고 시작
		Map<String, Object> map = null;

		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql); // 쿼리를 객체로 만든 것
			
			// 물음표를 채우는 과정 (param이 있을 때만)
			for (int i = 0; i < param.size(); i++) { // for문을 돌면서 SetObject 메서드로 하나씩 채워 넣음
				ps.setObject(i + 1, param.get(i)); // 물음표의 인덱스는 1부터 시작하므로 i + 1
			}
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			// next()를 호출하기 전에는 어떤 행위도 하지 않기 때문에 무조건 한 번은 호출해야 함
			// selectOne은 한 줄이기 때문에 여러 번 반복이 필요 없어서 while문은 사용하지 않음
			// 조회된 데이터가 없는 경우 HashMap에 넣을 필요가 없어 행이 있을 때 실행하게 if문으로 생성
			if(rs.next()) {
				// Map에 null이 들어있기 때문에 HashMap 생성
				map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(Exception e) {}
			if(ps != null) try {ps.close();} catch(Exception e) {}
			if(con != null) try {con.close();} catch(Exception e) {}
		}
		return map;
	}
	
	
	public static List<Map<String, Object>> selectList(String sql){
		// 파라미터로 쿼리를 받는 메서드. 실행 결과를 ArrayList에 담아 리턴 
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql); 
			rs = ps.executeQuery();

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(Exception e) {}
			if(ps != null) try {ps.close();} catch(Exception e) {}
			if(con != null) try {con.close();} catch(Exception e) {}
		}
		return list;
	}
	
	
	public static List<Map<String, Object>> selectList(String sql, List<Object> param) {
		// 파라미터로 쿼리를 받는 메서드. 실행 결과를 ArrayList에 담아 리턴 
		// 조회 결과가 없는 경우 null 대신 사이즈가 0인 ArrayList가 넘어오게 하기 위해 ArrayList 생성
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			con = DriverManager.getConnection(url, user, password);

			// 쿼리를 파라미터로 받기 때문에 따로 작성하지 않음
			ps = con.prepareStatement(sql);

			// 파라미터로 받은 물음표에 들어갈 값이 ArrayList에 들어있음 (ArrayList는 값이 여러 개일 때 사용)
			for (int i = 0; i < param.size(); i++) { 
				ps.setObject(i + 1, param.get(i)); 
			}

			rs = ps.executeQuery();

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			// rs에서 하나씩 추출해 ArrayList에 담음
			while (rs.next()) {
				// ArrayList에 HashMap이 들어가기 때문에(List<Map<String, Object>>) HashMap 생성 후
				Map<String, Object> map = new HashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					// HashMap은 (컬럼명, 컬럼값)으로 이루어짐
					// metaData.getColumnName(i) : 파라미터로 인덱스를 넘겨주면 해당 컬럼명을 알 수 있음
					map.put(metaData.getColumnName(i), rs.getObject(i));
				} // HashMap에 한 줄의 데이터가 다 들어가면 ArrayList 에 넣음
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(Exception e) {}
			if(ps != null) try {ps.close();} catch(Exception e) {}
			if(con != null) try {con.close();} catch(Exception e) {}
		}
		return list;
	}
	
	
	public static int update(String sql) {
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			// int 타입 변수에 영향을 받은 행의 수 저장
			result = ps.executeUpdate();
			// select가 아니기 때문에 결과를 추출하는 과정이 필요 없음
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(Exception e) {}
			if(ps != null) try {ps.close();} catch(Exception e) {}
			if(con != null) try {con.close();} catch(Exception e) {}
		}
		return result;
	}
	
	
	public static int update(String sql, List<Object> param) {
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			for (int i = 0; i < param.size(); i++) { 
				ps.setObject(i + 1, param.get(i)); 
			}
			// int 타입 변수에 영향을 받은 행의 수 저장
			result = ps.executeUpdate();
			// select가 아니기 때문에 결과를 추출하는 과정이 필요 없음
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(Exception e) {}
			if(ps != null) try {ps.close();} catch(Exception e) {}
			if(con != null) try {con.close();} catch(Exception e) {}
		}
		return result; 
	}

}
