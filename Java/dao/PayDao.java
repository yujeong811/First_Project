package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.MemberService;
import util.JDBCUtil;

public class PayDao {

	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private PayDao() {
		// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static PayDao instance; // 객체를 보관할 변수

	public static PayDao getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new PayDao(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}
	
	private MemberService memservice = MemberService.getInstance();	
	
//	public int updateOrder(String memId) {
//	//orders 테이블에 price를 제외한 ord_no, ord_date, mem_id를 업데이트 
//	String sql = "CallabeStatement cstmt = con.prepareCall(exec proc_create_ordno(?))";
//	
//	List<Object> param = new ArrayList<Object>();
//	param.add(memId);
//		
//		return JDBCUtil.update(sql, param);
//	}
//	

	
	public Map<String, Object> selectOrdno(String memId){//사용자의 최신 주문번호 가져오기
		
		String sql = "SELECT MAX(ORD_NO) AS V_ORD_NO"
				+ "     FROM ORDERS"
				+ "    WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.selectOne(sql, param);
	}
	
	public int updateDeorder(String pno, String newordno, int qty) {

		String sql =  "INSERT INTO DEORDER"
				+ "     VALUES((SELECT PROD_NO"
				+ "      FROM PROD_BOARD"
				+ "     WHERE PROD_NO = ?),"
				+ "     (SELECT ORD_NO"
				+ "      FROM ORDERS"
				+ "     WHERE ORD_NO = ?), ?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(pno);
		param.add(newordno);
		param.add(qty);
		
		return JDBCUtil.update(sql, param);
	}
	
   public int updateOrderPrice(String orderNo) {//orders테이블에 총상품금액 넣기
		      String sql = "  UPDATE ORDERS"
		      		+ "     SET ORD_PRICE = "
		      		+ "        (SELECT D.PSUM "
		      		+ "           FROM (SELECT A.ORD_NO AS AOID,"
		      		+ "                        SUM(C.PROD_COST*B.DEORDER_QTY) AS PSUM"
		      		+ "                   FROM ORDERS A, DEORDER B, PROD_BOARD C "
		      		+ "                  WHERE A.ORD_NO=B.ORD_NO "
		      		+ "                    AND B.PROD_NO=C.PROD_NO "
		      		+ "                    AND A.ORD_NO= ? "
		      		+ "                  GROUP BY A.ORD_NO) D) "
		      		+ "   WHERE ORD_NO = ?";
		      List<Object> param = new ArrayList<Object>();
		      param.add(orderNo);
		      param.add(orderNo);
		      
		      return JDBCUtil.update(sql, param); // 행의 수 리턴
		   }
   
   public List<Map<String, Object>> getOrderPrice(String memId) {//ORDERS에만 있는 주문번호, 총금액을 출력
	      String sql = "SELECT A.ORD_NO AS PORD_NO,"
	      		+ "                 A.ORD_PRICE AS PORD_PRICE"
	      		+ "           FROM ORDERS A LEFT JOIN PAY B"
	      		+ "             ON A.ORD_NO = B.ORD_NO"
	      		+ "          WHERE B.ORD_NO IS NULL"
	      		+ "            AND A.MEM_ID = ?";
	      List<Object> param = new ArrayList<Object>();
	      param.add(memId);
	      
	      return JDBCUtil.selectList(sql, param); // 행의 수 리턴
	   }
   
}

