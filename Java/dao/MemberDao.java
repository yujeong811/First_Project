package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MemberDao { 
	// Dao : 데이터에 접근하기 위한 객체
	// 같은 내용(insertMember)을 다른 화면에서도 사용할 수 있기 때문에 db에 접속하는 내용을 다른 클래스로 생성
	
	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private MemberDao() {
		// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static MemberDao instance; // 객체를 보관할 변수

	public static MemberDao getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new MemberDao(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}

	// 멤버 등록 (회원가입)
	public int insertMember(Map<String, Object> param) {
		String sql = "	INSERT INTO MEMBER" + " 	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
		List<Object> _param = new ArrayList<Object>();
		_param.add(param.get("MEM_ID"));
		_param.add(param.get("MEM_NAME"));
		_param.add(param.get("MEM_PASS"));
		_param.add(param.get("MEM_ADDR"));
		_param.add(param.get("MEM_BIR"));
		_param.add(param.get("MEM_GRADE"));
		_param.add(param.get("MEM_MILE"));
		_param.add(param.get("MEM_BANK"));
		_param.add(param.get("MEM_ACC"));

		return JDBCUtil.update(sql, _param); // 행의 수 리턴
	}
	
	public int insertHaveCoupon(Map<String, Object> param) {
		String sql = "   INSERT INTO HAVE_COUPON" + "    VALUES ((SELECT NVL(MAX(COP_NO), 0) + 1 FROM HAVE_COUPON), SYSDATE, ?, 'A001')";
		List<Object> _param = new ArrayList<Object>();
		_param.add(param.get("MEM_ID"));

		return JDBCUtil.update(sql, _param); // 행의 수 리턴
	}
	
	// 멤버 조회 (등록된 정보와 일치하는 회원인지 확인 후 로그인) 
	public Map<String, Object> selectMember(String mem_mid, String mem_mpass){ 
		// 로그인
		String sql = "SELECT MEM_ID"
				+ "        , MEM_PASS"
				+ "     FROM MEMBER"
				+ "    WHERE MEM_ID = ?"
				+ "      AND MEM_PASS = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(mem_mid);
		param.add(mem_mpass);
		
		return JDBCUtil.selectOne(sql, param);
	}
	
	// 아이디 중복 확인
	public Map<String, Object> selectMember_id(String mem_mid){
		String sql = "SELECT MEM_ID"
				+ "     FROM MEMBER"
				+ "    WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(mem_mid);
		
		return JDBCUtil.selectOne(sql, param); // 조회 결과가 한 줄이기 때문에 selectOne 사용	
	}
	
	 // 내 정보 보기
	public Map<String, Object> selectMemberList(String memId) {
		String sql = "SELECT MEM_NAME"
				+ "        , MEM_ID"
				+ "        , MEM_PASS"
				+ "        , MEM_BIR"
				+ "        , MEM_ADDR"
				+ "        , MEM_BANK"
				+ "        , MEM_ACC"
				+ "        , MEM_MILE"
				+ "        , MEM_GRADE"
				+ "     FROM MEMBER"
				+ "    WHERE MEM_ID = ?";
		
		List<Object> _param = new ArrayList<Object>();
		_param.add(memId);
		
		return JDBCUtil.selectOne(sql, _param);
	}
	
	// 내 정보 수정
	public int updateBoard(String addr, String bank, Integer acc, String memId) { 
		String sql = "UPDATE MEMBER"
				+ "		 SET MEM_ADDR = ?"
				+ "		   , MEM_BANK = ?"
				+ "        , MEM_ACC = ?"
				+ "	   WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(addr);
		param.add(bank);
		param.add(acc);
		param.add(memId);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 내 정보 > 쿠폰 보기
	public Map<String, Object> selectCoupon(String memId) { 
		String sql = "SELECT A.COUPON_NO"
				+ "        , A.MEM_ID"
				+ "        , B.COUPON_NAME"
				+ "        , A.COP_DATE"
				+ "     FROM HAVE_COUPON A, COUPONS B"
				+ "    WHERE A.COUPON_NO = B.COUPON_NO"
				+ "      AND A.MEM_ID = ?";

		List<Object> _param = new ArrayList<Object>();
		_param.add(memId);
		
		return JDBCUtil.selectOne(sql, _param);
	}
	
	// 내 주문내역목록 보기
	public List<Map<String, Object>> selectOrderList(String memId) { // 내 주문내역목록 보기
		String sql = "SELECT A.ORD_NO, "
				+ "          B.ORD_DATE, "
				+ "          D.MEM_ID, "
				+ "          C.PROD_NAME, "
				+ "          C.PROD_COST,"
				+ "          A.DEORDER_QTY "
				+ "     FROM DEORDER A, ORDERS B, PROD_BOARD C, MEMBER D "
				+ "    WHERE A.ORD_NO = B.ORD_NO "
				+ "      AND A.PROD_NO = C.PROD_NO "
				+ "      AND B.MEM_ID = D.MEM_ID "
				+ "      AND B.MEM_ID = ?";
		
		List<Object> _param = new ArrayList<Object>();
		_param.add(memId);
		
		return JDBCUtil.selectList(sql, _param);
	}
	
	// 내주문내역 상세보기
	public List<Map<String, Object>> selectdeOrderList(String memId, String orderdeNo) { 
		String sql = "SELECT A.ORD_NO, "
				+ "          B.ORD_DATE, "
				+ "          D.MEM_ID, "
				+ "          C.PROD_NAME,"
				+ "          C.PROD_COLOR, "
				+ "          C.PROD_SIZE, "
				+ "          A.DEORDER_QTY, "
				+ "          C.PROD_COST, "
				+ "          C.PROD_NO "
				+ "     FROM DEORDER A, ORDERS B, PROD_BOARD C, MEMBER D "
				+ "    WHERE A.ORD_NO = B.ORD_NO "
				+ "      AND A.PROD_NO = C.PROD_NO "
				+ "      AND B.MEM_ID = D.MEM_ID "
				+ "      AND B.MEM_ID = ?"
				+ "      AND B.ORD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(orderdeNo);
		
		return JDBCUtil.selectList(sql, param);
	}
	
	
	// 주문내역 > 1.리뷰 > 1.리뷰 작성
	//Integer revNo, String revTitle, String revContent, Integer revStar
	public int insertReview(String revTitle, String revContent, Integer revStar, String memId, String prodNo) {
		String sql = "INSERT INTO REVIEW"
				+ " 	VALUES ((SELECT NVL(MAX(REV_NO), 0) + 1	 FROM REVIEW), ?, ?, SYSDATE, ?, ?, ?)";
		List<Object> param = new ArrayList<Object>();
		param.add(revTitle);
		param.add(revContent);
		param.add(revStar);
		param.add(memId);
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param); // 행의 수 리턴
	}

	// 마이페이지 > 4.내가쓴리뷰 > 1.리뷰 목록보기
	public List<Map<String,Object>> selectReviewList(String memId) {
			String sql = "SELECT B.REV_NO"
					+ "        , D.MEM_ID"
					+ "        , C.PROD_NO"
					+ "        , A.PROD_NAME"
					+ "        , B.REV_TITLE"
					+ "        , B.REV_DATE"
					+ "     FROM PROD_BOARD A, REVIEW B, DEORDER C, MEMBER D"
					+ "    WHERE B.MEM_ID = D.MEM_ID"
					+ "      AND A.PROD_NO = B.PROD_NO"
					+ "      AND A.PROD_NO = C.PROD_NO"
					+ "      AND D.MEM_ID = ?";
			
			List<Object> _param = new ArrayList<Object>();
			_param.add(memId);
			
		return JDBCUtil.selectList(sql, _param);
	}
	
	// 마이페이지 > 4.내가쓴리뷰 > 1.리뷰 목록보기 > 1.상세보기
	public Map<String,Object> selectReviewdList(String memId, int revNo) {
		String sql = "SELECT B.REV_NO"
				+ "        , D.MEM_ID"
				+ "        , C.PROD_NO"
				+ "        , A.PROD_NAME"
				+ "        , B.REV_TITLE"
				+ "        , B.REV_DATE"
				+ "        , B.REV_CONTENT"
				+ "        , B.REV_STAR"
				+ "     FROM PROD_BOARD A, REVIEW B, DEORDER C, MEMBER D"
				+ "    WHERE B.MEM_ID = D.MEM_ID"
				+ "      AND A.PROD_NO = B.PROD_NO"
				+ "      AND A.PROD_NO = C.PROD_NO"
				+ "      AND D.MEM_ID = ?"
				+ "      AND B.REV_NO = ?";
		
		List<Object> _param = new ArrayList<Object>();
		_param.add(memId);
		_param.add(revNo);
		
		return JDBCUtil.selectOne(sql, _param);
	}
	
	
	// 마이페이지 > 4.내가쓴리뷰 > 1.리뷰 목록보기 > 1.상세보기 > 1.수정)
	public int updateReview(String revTitle, String revContent, Integer revStar, String memId) {
		String sql = "UPDATE REVIEW SET REV_TITLE = ? , "
				+ "                     REV_CONTENT = ? , "
				+ "                     REV_STAR = ? "
				+ "   WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(revTitle);
		param.add(revContent);
		param.add(revStar);
//		param.add(cur_RevNo);
		param.add(memId);
		
		return JDBCUtil.update(sql, param); // 행의 수 리턴
	}

	// 마이페이지 > 4.내가쓴리뷰 > 1.상세보기 > 2.삭제
	public int deleteReview(String memId) {
		String sql = "DELETE FROM REVIEW"
				+ "      WHERE MEM_ID = ? ";
		List<Object> param = new ArrayList<Object>();
//		param.add(cur_RevNo);
		param.add(memId);
		
		return JDBCUtil.update(sql, param); // 행의 수 리턴
	}
	
	public static List<Map<String, Object>> intersect() {
		String sql = " SELECT ORD_NO "
				+ "      FROM ORDERS "
			+ "     INTERSECT "
			+ "        SELECT ORD_NO "
			+ "          FROM PAY ";
		
		return JDBCUtil.selectList(sql);
	}
//	public static List<Map<String, Object>> selectordNo(String memId) {
//		String sql = " SELECT E.CONO , "
//		        + "           E.CADA , "
//		        + "           E.DPNO , "
//		        + "           F.PROD_NAME , "
//		        + "           F.PROD_COLOR , "
//		        + "           F.PROD_SIZE , "
//		        + "           E.DDQTY , "
//		        + "           F.PROD_COST , "
//		        + "           E.CBPR , "
//		        + "           E.CAPR , "
//		        + "           E.CBMID  "
//		        + "      FROM (SELECT D.PROD_NO AS DPNO, "
//		        + "                   D.DEORDER_QTY AS DDQTY, "
//		        + "                   C.AONO AS CONO, "
//		        + "                   C.APR AS CAPR, "
//		        + "                   C.BPR AS CBPR, "
//		        + "                   C.ADA AS CADA, "
//		        + "                   C.BMID AS CBMID "
//		        + "              FROM (SELECT A.ORD_NO AS AONO, "
//		        + "                           A.PAY_PRICE AS APR, "
//		        + "                           B.ORD_PRICE AS BPR, "
//		        + "                           A.PAY_DATE AS ADA, " 
//		        + "                           B.MEM_ID AS BMID "
//		        + "                      FROM PAY A, ORDERS B  "
//		        + "                     WHERE A.ORD_NO=B.ORD_NO "
//		        + "                       AND B.MEM_ID = ?) C,  "
//		        + "                       DEORDER D "
//		        + "             WHERE D.ORD_NO=C.AONO) E, PROD_BOARD F "   
//	            + "     WHERE F.PROD_NO=E.DPNO "
//	            + "     ORDER BY E.CONO ";     
//		
//		List<Object> param = new ArrayList<Object>();
//		param.add(memId);
//	
//		return JDBCUtil.selectList(sql, param);
//	}
	
	public static List<Map<String, Object>> selectprodNo(String memId, String orderdeNo) { // 주문내역 상세목록
		String sql = "  SELECT A.PAY_DATE ,"
				+ "            A.ORD_NO, "
				+ "            B.PROD_NO, "
				+ "            B.PROD_NAME ,"
				+ "            B.PROD_COLOR ,"
				+ "            B.PROD_SIZE , "
				+ "            C.DEORDER_QTY ,"
				+ "            B.PROD_COST"
				+ "      FROM PAY A, PROD_BOARD B, DEORDER C, ORDERS D "
				+ "     WHERE A.ORD_NO = D.ORD_NO "
				+ "       AND D.ORD_NO = C.ORD_NO "
				+ "       AND C.PROD_NO = B.PROD_NO "
				+ "       AND D.MEM_ID = ? "
				+ "       AND A.ORD_NO = ? ";     
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(orderdeNo);
	
		return JDBCUtil.selectList(sql, param);
	}
	
	public static List<Map<String, Object>> selectordPrice(String memId) { // 주문내역 목록
		String sql = " SELECT C.ONO, C.PPRI, C.OPRI, C.PDATE"
				+ "   FROM (SELECT A.ORD_NO AS ONO, A.PAY_PRICE AS PPRI,"
				+ "                B.ORD_PRICE AS OPRI, "
				+ "                A.PAY_DATE AS PDATE"
				+ "           FROM PAY A, ORDERS B  "
				+ "          WHERE A.ORD_NO=B.ORD_NO"
				+ "            AND B.MEM_ID = ? "
				+ "          ORDER BY A.ORD_NO) C LEFT JOIN REFUND D"
				+ "          ON C.ONO = D.ORD_NO"
				+ "          WHERE D.ORD_NO IS NULL";     
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
	
		return JDBCUtil.selectList(sql, param);
	}
	
	public static List<Map<String, Object>> selectordPay(String memId, String orderdeNo) { // 주문내역 목록
		String sql = "  SELECT A.ORD_NO , "
				+ "            A.PAY_PRICE , "
				+ "	           B.ORD_PRICE , "
				+ "            A.PAY_DATE , "
				+ "            A.MEM_MILE, "
				+ "            A.MEM_COUPON "
				+ "       FROM PAY A, ORDERS B"
				+ "      WHERE A.ORD_NO=B.ORD_NO "
				+ "	       AND B.MEM_ID = ? "
				+ "        AND A.ORD_NO = ? ";     
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(orderdeNo);
	
		return JDBCUtil.selectList(sql, param);
	}
	
	public Map<String, Object> selectRefund(String ordNo){
		
		String sql = "SELECT PAY_PRICE, MEM_MILE, PAY_TCODE"
				+ "	    FROM PAY"
				+ "	   WHERE ORD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(ordNo);
		
		return JDBCUtil.selectOne(sql,param);
	}
	
	public Map<String, Object> selectMem(String memId){
		
		String sql = "SELECT NVL(MEM_SPAY, 0) AS MEM_SPAY, NVL(MEM_MILE, 0) AS MEM_MILE"
				+ "	    FROM MEMBER"
				+ "	   WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		return JDBCUtil.selectOne(sql, param);
	}
	
	public int updateMemRefund(int memMile, int memSpay, String memId){
		//보유쿠폰 가져오기
		String sql = "UPDATE MEMBER"
				+ "		 SET MEM_MILE = ?,"
				+ "		 MEM_SPAY = ?"
				+ "		WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memMile);
		param.add(memSpay);
		param.add(memId);
		return JDBCUtil.update(sql, param);
	}
	
	
	public int insertRefund(String rdordNo, String payTcode) {
		
		String sql = "INSERT INTO REFUND"
				+ "		VALUES(?, SYSDATE, ?)";
		List<Object> param = new ArrayList<Object>();
		param.add(rdordNo);
		param.add(payTcode);
		
		return JDBCUtil.update(sql, param);
		
	}
	
}
