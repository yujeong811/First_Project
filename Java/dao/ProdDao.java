package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ProdDao {

	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private ProdDao() {
		// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static ProdDao instance; // 객체를 보관할 변수

	public static ProdDao getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new ProdDao(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}
	
	public List<Map<String, Object>> selectProd(String lprodno){//카테고리별 상품 게시판
		
		String sql = "SELECT DISTINCT PROD_NAME, PROD_COST"
				+ " FROM PROD_BOARD "
				+ " WHERE LPROD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(lprodno);

		
		return JDBCUtil.selectList(sql,param);
	}

	public List<Map<String, Object>> searchlist(String pname, String lprodno) {//상품 검색 쿼리

		String sql = "SELECT PROD_NAME, PROD_COST, PROD_COLOR, PROD_SIZE, PROD_NO, PROD_DETAIL"
				+ "		FROM PROD_BOARD"
				+ "	   WHERE LPROD_NO = ?"
				+ "		 AND PROD_NAME LIKE '%'||?||'%'";
		
		List<Object> param = new ArrayList<Object>();
		param.add(lprodno);
		param.add(pname);
		
		return JDBCUtil.selectList(sql, param);
	}

	public Map<String, Object> oneProd(String prodno) {//한 상품의 상세내용보기

		String sql = "SELECT PROD_NAME, PROD_COST, PROD_COLOR, PROD_SIZE, PROD_NO, PROD_DETAIL"
				+ "          FROM PROD_BOARD"
				+ "         WHERE PROD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodno);
		
		return JDBCUtil.selectOne(sql, param);
	}

	public List<Map<String, Object>> rank(String lprodno) {//랭킹 출력

		String sql = "SELECT A.PROD_NO,"
				+ "          SUM(A.DEORDER_QTY),"
				+ "          ROW_NUMBER() OVER(ORDER BY SUM(A.DEORDER_QTY) DESC) PRANK,"
				+ "          B.LPROD_NO,"
				+ "          B.PROD_NAME,"
				+ "          B.PROD_COST,"
				+ "          B.PROD_SIZE"
				+ "     FROM DEORDER A, PROD_BOARD B"
				+ "    WHERE A.PROD_NO = B.PROD_NO"
				+ "      AND B.LPROD_NO = ?"//--상품분류코드 추가함
				+ "    GROUP BY A.PROD_NO, B.LPROD_NO, B.PROD_NAME,"
				+ "             B.PROD_COST,"
				+ "             B.PROD_SIZE";
		
		List<Object> param = new ArrayList<Object>();
		param.add(lprodno);
		
		return JDBCUtil.selectList(sql, param);
	}
	
    public int updateRank(String pno) {
		
		String sql = " UPDATE PROD_BOARD B"
				+ "       SET PROD_RANK = (SELECT DISTINCT A.PRANK"
				+ "	                   		 FROM (SELECT PROD_NO,"
				+ "			                              SUM(DEORDER_QTY),"
				+ "			                              RANK() OVER(ORDER BY SUM(DEORDER_QTY) DESC) AS PRANK"
				+ "			                         FROM DEORDER"
				+ "			                        GROUP BY PROD_NO) A, DEORDER C, PROD_BOARD D"
				+ "                         WHERE A.PROD_NO=C.PROD_NO"
				+ "			                  AND A.PROD_NO=D.PROD_NO"
				+ "                           AND A.PROD_NO = ?)"
				+ "   WHERE B.PROD_NO = (SELECT DISTINCT F.PROD_NO "
				+ "						   FROM DEORDER F, PROD_BOARD E "
				+ "					      WHERE F.PROD_NO=E.PROD_NO "
				+ "							AND F.PROD_NO = ?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(pno);
		param.add(pno);
		
		return JDBCUtil.update(sql, param);
	}
    
    public List<Map<String, Object>> deordprodNo(){
    	//pay에는 없고 deorder와 orders에만 있는 주문번호와 상품번호 출력
    	String sql = " SELECT D.ORD_NO, D.PROD_NO"
    			+ "          FROM (SELECT A.ORD_NO AS PORD_NO"
    			+ "                  FROM ORDERS A LEFT JOIN PAY B"
    			+ "			        ON A.ORD_NO = B.ORD_NO"
    			+ "			     WHERE B.ORD_NO IS NULL) C"
    			+ "         INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO";
    	
    	return JDBCUtil.selectList(sql);
    }
    
    
    
    public int updatePay(double payPrice, int inmile, String copName, String ordNono) { // pay 테이블 데이터 넣기
    	String sql = " INSERT INTO PAY(ORD_NO, PAY_PRICE, PAY_DATE, MEM_MILE, MEM_COUPON)"
    			+ "    VALUES(?,?, SYSDATE, ?,?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(ordNono);
		param.add(payPrice);
		param.add(inmile);
		param.add(copName);
		
		return JDBCUtil.update(sql, param);
    }
	
	public List<Map<String, Object>> selectOrders(String memId){
		
		String sql = "    SELECT ORD_NO"
				+ "      FROM ORDERS"
				+ "    WHERE MEM_ID = '?'";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		
		return JDBCUtil.selectList(sql, param);
	}
	
	
	public List<Map<String, Object>> selectPay(){
		
		String sql = "    SELECT ORD_NO"
				+ "      FROM PAY";
		
		
		return JDBCUtil.selectList(sql);
	}
	public List<Map<String, Object>> selectCartProd(String memId){
		//아이디별 주문에만 있는 상품의 정보
		String sql = " SELECT F.PROD_NAME, F.PROD_COST,"
				+ "        F.PROD_COLOR, F.PROD_SIZE,"
				+ "        E.PQTY, E.ORD_NO"
				+ "   FROM (SELECT D.ORD_NO,"
				+ "        D.PROD_NO,"
				+ "        D.DEORDER_QTY AS PQTY"
				+ "   FROM (SELECT A.ORD_NO AS PORD_NO"
				+ "           FROM ORDERS A LEFT JOIN PAY B"
				+ "             ON A.ORD_NO = B.ORD_NO"
				+ "          WHERE B.ORD_NO IS NULL"
				+ "            AND A.MEM_ID = ?) C "
				+ "          INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO) E"
				+ "  INNER JOIN PROD_BOARD F ON E.PROD_NO = F.PROD_NO";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.selectList(sql, param);
	}
	
	public List<Map<String, Object>> deleteCart_deord(String memId) {
		//ORDERS, DEORDER에만 있는 주문번호를 출력해서 삭제해줄 용도로 데려옴
		String sql = "SELECT D.ORD_NO"
				+ "     FROM (SELECT A.ORD_NO AS PORD_NO"
				+ "             FROM ORDERS A LEFT JOIN PAY B"
				+ "               ON A.ORD_NO = B.ORD_NO"
				+ "            WHERE B.ORD_NO IS NULL"
				+ "              AND A.MEM_ID = ?) C"
				+ "    INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.selectList(sql, param);
	}
	public int deleteCart_deorder_st(String ordNo) {
		
		String sql = "DELETE FROM DEORDER"
				+ "	   WHERE ORD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(ordNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	public int deleteCart_orders(String ordNo) {
		
		String sql = "DELETE FROM ORDERS"
				+ "	   WHERE ORD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(ordNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	public int deleteCart_deord_all(String memId) {
		
		String sql = "DELETE FROM DEORDER"
				+ "	   WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
				
		return JDBCUtil.update(sql, param);
	}
	
	public int deleteCart_orders_all(String memId) {
		
		String sql = "DELETE FROM ORDERS"
				+ "	   WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.update(sql, param);
	}
	
	public List<Map<String, Object>> selectCoupon(String memId) {
		//멤버별 보유쿠폰 조회 메서드
		String sql = "SELECT A.COUPON_NAME, B.COP_NO, B.COUPON_NO"
				+ "  FROM COUPONS A, HAVE_COUPON B"
				+ " WHERE A.COUPON_NO = B.COUPON_NO"
				+ "   AND MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.selectList(sql, param);
	}
	
	public Map<String, Object> selectMile(String memId){
		//멤버별 보유 마일리지 조회 메서드
		String sql = "SELECT MEM_MILE"
				+ "	    FROM MEMBER"
				+ "    WHERE MEM_ID = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		return JDBCUtil.selectOne(sql, param);
	}
	
	public List<Map<String, Object>> selectPay(String memId){
		//멤버의 결제하지 않은 장바구니 주문내역
		String sql = "SELECT G.EORD_NO"
				+ "          FROM (SELECT F.PROD_NAME, F.PROD_COST,F.PROD_COLOR, F.PROD_SIZE,E.PQTY, E.ORD_NO AS EORD_NO"
				+ "              FROM (SELECT D.ORD_NO,D.PROD_NO,D.DEORDER_QTY AS PQTY"
				+ "                      FROM (SELECT A.ORD_NO AS PORD_NO"
				+ "                              FROM ORDERS A "
				+ "                              LEFT JOIN PAY B ON A.ORD_NO = B.ORD_NO WHERE B.ORD_NO IS NULL"
				+ "                              AND A.MEM_ID = ?) C"
				+ "                              INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO) E"
				+ "                              INNER JOIN PROD_BOARD F ON E.PROD_NO = F.PROD_NO) G";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		return JDBCUtil.selectList(sql, param);
	}
	
	public int insertPay(String ordNo,double payPrice, int inmile, String coupNo, String paytcode) {
		//pay테이블에 구매한 주문번호에 대한 자료 삽입
		String sql = "INSERT INTO PAY(ORD_NO ,PAY_PRICE, PAY_DATE, MEM_MILE, MEM_COUPON, PAY_TCODE)"
				+ " VALUES(?, ?, SYSDATE, ?, ?, ?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(ordNo);
		param.add(payPrice);
		param.add(inmile);
		param.add(coupNo);
		param.add(paytcode);
		
		return JDBCUtil.update(sql, param);
	}
	
	public Map<String, Object> selectSpay(String memId) {//주문한 아이디의 누적금액
		
		String sql ="SELECT NVL(MEM_SPAY, 0) AS MEM_SPAY"
				+ "   FROM MEMBER"
				+ "  WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		return JDBCUtil.selectOne(sql, param);
	}
	public int updateSpay(String memId, int a) {
		
		String sql = "UPDATE MEMBER"
				+ "   SET MEM_SPAY = ?"
				+ " WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(a);
		param.add(memId);
		
		return JDBCUtil.update(sql, param);
	}
	
//	public int insertPay(double payPrice, int inmile, String coupNo, String paytcode) {
//		String sql = "INSERT INTO PAY(ORD_NO,PAY_PRICE, PAY_DATE, MEM_MILE,MEM_COUPON,PAY_TCODE)"
//				+ " VALUES((SELECT G.ORD_NO"
//				+ "          FROM (SELECT F.PROD_NAME, F.PROD_COST,F.PROD_COLOR, F.PROD_SIZE,E.PQTY, E.ORD_NO"
//				+ "              FROM (SELECT D.ORD_NO,D.PROD_NO,D.DEORDER_QTY AS PQTY"
//				+ "                      FROM (SELECT A.ORD_NO AS PORD_NO"
//				+ "                              FROM ORDERS A "
//				+ "                              LEFT JOIN PAY B ON A.ORD_NO = B.ORD_NO WHERE B.ORD_NO IS NULL AND A.MEM_ID = 'abc123') C"
//				+ "                              INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO) E"
//				+ "                              INNER JOIN PROD_BOARD F ON E.PROD_NO = F.PROD_NO) G),"
//				+ "                              ?,SYSDATE,?,?, ?)";
//		
//		List<Object> param = new ArrayList<Object>();
//		param.add(payPrice);
//		param.add(inmile);
//		param.add(coupNo);
//		param.add(paytcode);
//		
//		return JDBCUtil.update(sql, param);
//	}
	
	// 쿠폰 제거
		public int delete_coupon(String memId, String coucode) {
			
			String sql = "DELETE FROM HAVE_COUPON"
					+ "	   WHERE MEM_ID = ?"
					+ "	     AND COUPON_NO = ?";
			
			List<Object> param = new ArrayList<Object>();
			param.add(memId);
			param.add(coucode);
			
			return JDBCUtil.update(sql, param);
		}
		
		// 마일리지
		public int update_mile(String memId, String mem_mile) {
			
			String sql = "UPDATE MEMBER"
					+ "	     SET MEM_MILE = ?"
					+ "	   WHERE MEM_ID = ?";
			
			List<Object> param = new ArrayList<Object>();
			param.add(mem_mile);
			param.add(memId);
			
			return JDBCUtil.update(sql, param);
		}
		
		// 상품 > 1.검색 > 1.상품리뷰
		   public List<Map<String,Object>> prodReviewList(String prodNo) {
		         String sql = "SELECT B.REV_NO"
		               + "        , A.PROD_NAME"
		               + "        , B.REV_TITLE"
		               + "     FROM PROD_BOARD A, REVIEW B"
		               + "    WHERE A.PROD_NO = B.PROD_NO"
		               + "      AND A.PROD_NO = ?";
		         
		         List<Object> _param = new ArrayList<Object>();
		         _param.add(prodNo);
		         
		      return JDBCUtil.selectList(sql, _param);
		   }
		   
		   // 상품 > 1.검색 > 1.상품리뷰 상세히
		   public Map<String,Object> prodReviewdList(String prodNo, int revNo) {
		      String sql = "SELECT B.REV_NO"
		            + "        , C.PROD_NO"
		            + "        , A.PROD_NAME"
		            + "        , B.REV_TITLE"
		            + "        , B.REV_DATE"
		            + "        , B.REV_CONTENT"
		            + "        , B.REV_STAR"
		            + "     FROM PROD_BOARD A, REVIEW B, DEORDER C"
		            + "    WHERE A.PROD_NO = B.PROD_NO"
		            + "      AND A.PROD_NO = C.PROD_NO"
		            + "      AND A.PROD_NO = ?"
		            + "      AND B.REV_NO = ?";
		      
		      List<Object> _param = new ArrayList<Object>();
		      _param.add(prodNo);
		      _param.add(revNo);
		      
		      return JDBCUtil.selectOne(sql, _param);
		   }
		   
			// QNA 등록
			public int qnaInsert(String qnaTitle, String qnaContent, String memId, String num) {
				String sql = "INSERT INTO QNA(PR_QNA_NO," 
						+ "                    PR_QNA_TITLE,"
						+ "                    PR_QNA_CONTENT," 
						+ "                    PR_QNA_DATE,"
						+ "                    MEM_ID," 
						+ "    				   PROD_NO)"
						+ "     VALUES((SELECT NVL(MAX(PR_QNA_NO), 0) + 1 FROM QNA)," 
						+ "				 ?, ?, TO_DATE(SYSDATE)"
						+ "				, ?, ?)";

				List<Object> param = new ArrayList<Object>();
				param.add(qnaTitle);
				param.add(qnaContent);
				param.add(memId);
				param.add(num);

				return JDBCUtil.update(sql, param);
			}
			
			public List<Map<String, Object>> selectQnaList(String num) {
				String sql = "SELECT A.PR_QNA_NO," 
						+ "         B.PROD_NAME," 
						+ "         A.PR_QNA_TITLE,"
						+ "         TO_CHAR(A.PR_QNA_DATE, 'MM/DD') AS PR_QNA_DATE," 
						+ "		    A.MEM_ID,"
						+ "         A.PR_ANS_YES," 
						+ "		    B.PROD_NO" 
						+ "    FROM QNA A, PROD_BOARD B, MEMBER C"
						+ "   WHERE A.PROD_NO=B.PROD_NO" 
						+ "     AND A.MEM_ID=C.MEM_ID" 
						+ "     AND B.PROD_NO = ?";
				List<Object> param = new ArrayList<Object>();
				param.add(num);

				return JDBCUtil.selectList(sql, param);
			}


			// QNA 번호 선택후 상세보기
			public Map<String, Object> selectQna(int qnaNo, String num) {
				String sql = "SELECT A.PR_QNA_NO," 
						+ "           B.PROD_NO," 
						+ "           C.MEM_ID,"
						+ "           A.PR_QNA_TITLE," 
						+ "           A.PR_QNA_CONTENT,"
						+ "           TO_CHAR(A.PR_QNA_DATE, 'MM/DD') AS PR_QNA_DATE," 
						+ "           A.PR_ANS,"
						+ "           A.PR_DATE," 
						+ "           A.PR_ANS_YES," 
						+ "           B.PROD_NAME"
						+ "	 FROM QNA A, PROD_BOARD B, MEMBER C" 
						+ "    WHERE A.PR_QNA_NO = ?" 
						+ "      AND B.PROD_NO= ?"
						+ "      AND A.MEM_ID=C.MEM_ID";
				List<Object> param = new ArrayList<Object>();
				param.add(qnaNo);
				param.add(num);

				return JDBCUtil.selectOne(sql, param);
			}

			// QNA 수정
			public int updateQna(int qnaNo, String qnaTitle, String qnaContent) {
				String sql = "UPDATE QNA" 
						+ "		 SET PR_QNA_TITLE = ?" 
						+ "		   , PR_QNA_CONTENT = ?"
						+ "    WHERE PR_QNA_NO = ?";
				List<Object> param = new ArrayList<Object>();
				param.add(qnaTitle);
				param.add(qnaContent);
				param.add(qnaNo);

				return JDBCUtil.update(sql, param);
			}

			// QNA 삭제
			public int deleteQna(int qnaNo) {

				String sql = "DELETE FROM QNA" 
						+ "	   WHERE PR_QNA_NO = ?";
				List<Object> param = new ArrayList<Object>();

				param.add(qnaNo);

				return JDBCUtil.update(sql, param);
			}
	}

