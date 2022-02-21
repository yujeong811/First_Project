package util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MileageUtil {
   private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
   private static String user = "MUSINSA";
   private static String password = "java";
   
   private static Connection con = null;
   private static PreparedStatement ps = null;
   private static ResultSet rs = null;
   private static CallableStatement cstmt = null;
   
   // 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
   private MileageUtil() {
      // private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
   }

   private static MileageUtil instance; // 객체를 보관할 변수

   public static MileageUtil getInstance() {
      if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
         instance = new MileageUtil(); // 객체를 새로 생성해 리턴
      } // 객체가 이미 instance에 있으면 그대로 주면됨
      return instance; // 객체 리턴
   }
   
   public static void mrc(String memId) {
      
      try {
         
         Class.forName("oracle.jdbc.driver.OracleDriver");
         con = DriverManager.getConnection(url, user, password);

         
          // 프로시저 호출
          CallableStatement cs = con.prepareCall("{call PROC_MILE_UPDATE(?)}");
          // 입력 파라메터
          cs.setString(1, memId);
          // 출력 파라메터
//          cs.registerOutParameter(2, java.sql.Types.VARCHAR);
          // 실행
          cs.execute();
//          System.out.println(cs.getString(2));
          cs.close();
          con.close();
      } catch(Exception e) {
          e.printStackTrace();
      }
   }

}