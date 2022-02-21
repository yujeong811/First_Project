package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AdminDao {

	// Dao : 데이터에 접근하기 위한 객체
	// 같은 내용(insertMember)을 다른 화면에서도 사용할 수 있기 때문에 db에 접속하는 내용을 다른 클래스로 생성

	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private AdminDao() {
			// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static AdminDao instance; // 객체를 보관할 변수

	public static AdminDao getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new AdminDao(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}
	
	
	// 관리자 조회 (등록된 정보와 일치하는지 확인 후 로그인) 
	public Map<String, Object> selectAdmin(String adm_id, String adm_pass){ 
		// 로그인
		String sql = "SELECT ADM_ID"
				+ "        , ADM_PASS"
				+ "     FROM ADMIN"
				+ "    WHERE ADM_ID = ?"
				+ "      AND ADM_PASS = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(adm_id);
		param.add(adm_pass);
		
		return JDBCUtil.selectOne(sql, param);
	}
	
	// 관리자 > 멤버 조회 
		public List<Map<String, Object>> selectMemberRead(){ 
			String sql = "SELECT MEM_ID"
					+ "        , MEM_NAME"
					+ "     FROM MEMBER";
			List<Object> param = new ArrayList<Object>();
			
			return JDBCUtil.selectList(sql);
		}

		
		// 관리자 > 멤버 조회 > 멤버 상세보기
		public Map<String, Object> selectMemberReadde(String memId) {
			String sql = "SELECT *"
					+ "     FROM MEMBER"
					+ "    WHERE MEM_ID = ?";
			
			List<Object> _param = new ArrayList<Object>();
			_param.add(memId);
			
			return JDBCUtil.selectOne(sql, _param);
		}
	
	// 상품 등록 (아우터) --> 관리자
	public int insert_prod_outer(String prodColor, String prodName, String prodDetail, int prodCost, String prodSize) {
		String sql = " INSERT INTO PROD_BOARD(PROD_NO, PROD_COLOR, PROD_NAME, PROD_DETAIL, PROD_COST, PROD_SIZE, LPROD_NO) "
				+ "   VALUES((SELECT NVL(MAX(PROD_NO), 0) + 1 FROM PROD_BOARD WHERE SUBSTR(PROD_NO,1,1) = '1'), ?, ?, ?, ? ,?, 'A0001')";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodColor);
		param.add(prodName);
		param.add(prodDetail);
		param.add(prodCost);
		param.add(prodSize);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 등록 (상의) --> 관리자
	public int insert_prod_top(String prodColor, String prodName, String prodDetail, int prodCost, String prodSize) {
		String sql = " INSERT INTO PROD_BOARD(PROD_NO, PROD_COLOR, PROD_NAME, PROD_DETAIL, PROD_COST, PROD_SIZE, LPROD_NO) "
				+ "   VALUES((SELECT NVL(MAX(PROD_NO), 0) + 1 FROM PROD_BOARD WHERE SUBSTR(PROD_NO,1,1) = '2'), ?, ?, ?, ? ,?, 'B0001')";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodColor);
		param.add(prodName);
		param.add(prodDetail);
		param.add(prodCost);
		param.add(prodSize);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 등록 (하의) --> 관리자
	public int insert_prod_bottom(String prodColor, String prodName, String prodDetail, int prodCost, String prodSize) {
		String sql = " INSERT INTO PROD_BOARD(PROD_NO, PROD_COLOR, PROD_NAME, PROD_DETAIL, PROD_COST, PROD_SIZE, LPROD_NO) "
				+ "   VALUES((SELECT NVL(MAX(PROD_NO), 0) + 1 FROM PROD_BOARD WHERE SUBSTR(PROD_NO,1,1) = '3'), ?, ?, ?, ? ,?, 'C0001')";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodColor);
		param.add(prodName);
		param.add(prodDetail);
		param.add(prodCost);
		param.add(prodSize);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 등록 (신발) --> 관리자
	public int insert_prod_shoes(String prodColor, String prodName, String prodDetail, int prodCost, String prodSize) {
		String sql = " INSERT INTO PROD_BOARD(PROD_NO, PROD_COLOR, PROD_NAME, PROD_DETAIL, PROD_COST, PROD_SIZE, LPROD_NO) "
				+ "   VALUES((SELECT NVL(MAX(PROD_NO), 0) + 1 FROM PROD_BOARD WHERE SUBSTR(PROD_NO,1,1) = '4'), ?, ?, ?, ? ,?, 'D0001')";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodColor);
		param.add(prodName);
		param.add(prodDetail);
		param.add(prodCost);
		param.add(prodSize);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 수정 (아우터)
	public int update_prod_outer(int prodCost, String prodNo) {
		String sql = " UPDATE PROD_BOARD SET PROD_COST = ? WHERE PROD_NO = ? AND LPROD_NO = 'A0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodCost);
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 수정 (상의)
	public int update_prod_top(int prodCost, String prodNo) {
		String sql = " UPDATE PROD_BOARD SET PROD_COST = ? WHERE PROD_NO = ? AND LPROD_NO = 'B0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodCost);
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 수정 (하의)
	public int update_prod_bottom(int prodCost, String prodNo) {
		String sql = " UPDATE PROD_BOARD SET PROD_COST = ? WHERE PROD_NO = ? AND LPROD_NO = 'C0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodCost);
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 수정 (신발)
	public int update_prod_shoes(int prodCost, String prodNo) {
		String sql = " UPDATE PROD_BOARD SET PROD_COST = ? WHERE PROD_NO = ? AND LPROD_NO = 'D0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodCost);
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 삭제 (아우터)
	public int delete_prod_outer(String prodNo) {
		String sql = " DELETE FROM PROD_BOARD WHERE PROD_NO = ? AND LPROD_NO = 'A0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 삭제 (아우터)
	public int delete_prod_top(String prodNo) {
		String sql = " DELETE FROM PROD_BOARD WHERE PROD_NO = ? AND LPROD_NO = 'B0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 삭제 (아우터)
	public int delete_prod_bottom(String prodNo) {
		String sql = " DELETE FROM PROD_BOARD WHERE PROD_NO = ? AND LPROD_NO = 'C0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 상품 삭제 (아우터)
	public int delete_prod_shoes(String prodNo) {
		String sql = " DELETE FROM PROD_BOARD WHERE PROD_NO = ? AND LPROD_NO = 'D0001' ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(prodNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	public List<Map<String, Object>> adminselectOuter(){//아우터 상품 목록
		
		String sql = "SELECT * "
				+ " FROM PROD_BOARD "
				+ " WHERE LPROD_NO = 'A0001'";
	
		return JDBCUtil.selectList(sql);
	}
	
	public List<Map<String, Object>> adminselectTop(){//상의 상품 목록
		
		String sql = "SELECT * "
				+ " FROM PROD_BOARD "
				+ " WHERE LPROD_NO = 'B0001'";
	
		return JDBCUtil.selectList(sql);
	}
	
	public List<Map<String, Object>> adminselectBottom(){//하의 상품 목록
		
		String sql = "SELECT * "
				+ " FROM PROD_BOARD "
				+ " WHERE LPROD_NO = 'C0001'";
	
		return JDBCUtil.selectList(sql);
	}
	
	public List<Map<String, Object>> adminselectShoes(){//신발 상품 목록
		
		String sql = "SELECT * "
				+ " FROM PROD_BOARD "
				+ " WHERE LPROD_NO = 'D0001'";
	
		return JDBCUtil.selectList(sql);
	}

	// 공지사항 등록 --> 관리자
	public int insertnotice(String title, String content, String admId) {
		String sql = " INSERT INTO NOTICE(NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, NOTICE_DATE, ADM_ID)"
				   + " VALUES((SELECT NVL(MAX(NOTICE_NO), 0) + 1 FROM NOTICE), ?, ?, SYSDATE, ?)";

		List<Object> param = new ArrayList<Object>();
		param.add(title);
		param.add(content);
		param.add(admId);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 공지사항 조회 --> 관리자
	public static List<Map<String, Object>> admin_selectnotice(String admId) {
		String sql = "SELECT NOTICE_NO,NOTICE_TITLE,NOTICE_DATE,ADM_ID "
				+ "     FROM NOTICE "
				+ "    WHERE ADM_ID = ?";

		List<Object> param = new ArrayList<Object>();
		param.add(admId);
		
		return JDBCUtil.selectList(sql, param);
	}
	
	// 공지사항번호 입력후 조회
	public static Map<String, Object> admin_readnotice(String admId, int noticeNo) { 
			
		String sql = "SELECT NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, NOTICE_DATE, ADM_ID"
					+ "		FROM NOTICE "
			     	+ " WHERE ADM_ID = ? "
				   	+ "	 AND NOTICE_NO = ? ";
			
		List<Object> param = new ArrayList<Object>();
		param.add(admId);
		param.add(noticeNo);
			
		return JDBCUtil.selectOne(sql, param);
	}
	
	// 공지사항 삭제 --> 관리자
	public int deletenotice(String admId, int noticeNo) {
		String sql = " DELETE FROM NOTICE "
				+ "     WHERE ADM_ID = ?"
				+ "       AND NOTICE_NO = ?";

		List<Object> param = new ArrayList<Object>();
		param.add(admId);
		param.add(noticeNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	public int updatenotice(String title, String content, String admId, int noticeNo) {
		String sql = " UPDATE NOTICE"
				+ "       SET NOTICE_TITLE = ?, "
				+ "           NOTICE_CONTENT = ?, "
				+ "           ADM_ID = ?, "
				+ "           NOTICE_NO = ? ";

		List<Object> param = new ArrayList<Object>();
		param.add(title);
		param.add(content);
		param.add(admId);
		param.add(noticeNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	// 관리자:QNA선택 -> 전체 QNA 리스트 출력
		public static List<Map<String, Object>> selectQnaList() {
			String sql = "SELECT A.PR_QNA_NO,"
			         + "         B.PROD_NAME,"
			         + "         A.PR_QNA_TITLE,"
			         + "         TO_CHAR(A.PR_QNA_DATE, 'MM/DD') AS PR_QNA_DATE,"
			         + "		 A.MEM_ID,"
			         + "         A.PR_ANS_YES"
			         + "    FROM QNA A, PROD_BOARD B, MEMBER C"
			         + "   WHERE A.PROD_NO=B.PROD_NO"
			         + "     AND A.MEM_ID=C.MEM_ID";
			
			return JDBCUtil.selectList(sql);
		}

		// QNA번호 선택->글 상세보기
		public static Map<String, Object> selectQna(int qnaNo) {
			String sql = "SELECT A.PR_QNA_NO,"
					+ "          B.PROD_NO,"
					+ "          C.MEM_ID,"
					+ " 		 A.PR_QNA_TITLE,"
					+ "			 A.PR_QNA_CONTENT,"
					+ "          TO_CHAR(A.PR_QNA_DATE, 'MM/DD') AS PR_QNA_DATE,"
					+ "          A.PR_ANS,"
					+ "          A.PR_DATE,"
					+ "			 A.PR_ANS_YES"
					+ "		FROM QNA A, PROD_BOARD B, MEMBER C"
					+ "	   WHERE A.PR_QNA_NO = ?"
					+ "      AND A.PROD_NO=B.PROD_NO"
					+ "		 AND A.MEM_ID=C.MEM_ID";
			List<Object> param = new ArrayList<Object>();
			param.add(qnaNo);
			
			return JDBCUtil.selectOne(sql, param);
		}


		// 답글등록
		public int adminQnaReply(int qnaNo, String replyContent, String qnaAnsYes) {
			
			String sql = "UPDATE QNA"
					+ "      SET PR_ANS = ?, PR_DATE = SYSDATE, PR_ANS_YES = ?"
					+ " WHERE PR_QNA_NO = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(replyContent);
			param.add(qnaAnsYes);
			param.add(qnaNo);
			
			return JDBCUtil.update(sql, param);
		}


		// 답글 수정
		public int adminQnaUpdate(int qnaNo, String replyContent) {
			String sql = "UPDATE QNA"
					+ " SET PR_ANS = ?, PR_DATE = SYSDATE"
					+ " WHERE PR_QNA_NO = ?";
			
			List<Object> param = new ArrayList<Object>();
			param.add(replyContent);
			param.add(qnaNo);
				      
			return JDBCUtil.update(sql, param);
		}


	// 회원 아이디별 금액 합계 조회
	public List<Map<String, Object>> selectMemevent() {
		String sql = "SELECT MEM_ID, MEM_SPAY" 
				+ "     FROM MEMBER";
	    List<Object> param = new ArrayList<Object>();
	    
	    return JDBCUtil.selectList(sql);
	}

	// 변경된 등급 조회 
	public List<Map<String, Object>> selectGrade() {
		String sql = " SELECT MEM_ID, MEM_GRADE"
				+ "      FROM MEMBER";
		return JDBCUtil.selectList(sql);
	}
	
	// 모든 회원 조회
	public List<Map<String, Object>> selectMemberAll() {
				String sql = "SELECT MEM_ID"
						+ "     FROM MEMBER";
				
				return JDBCUtil.selectList(sql);
			}

	//보유쿠폰 테이블 조회
	public List<Map<String, Object>> selectHaveCop() {
		String sql = "SELECT MEM_ID"
				+ "     FROM HAVE_COUPON";
		
		return JDBCUtil.selectList(sql);
	}

	// 사용 기한이 지난 쿠폰 삭제
	public int deleteCoupon() {
		String sql = " DELETE FROM HAVE_COUPON "
				+ "     WHERE COP_DATE < SYSDATE";
		List<Object> param = new ArrayList<Object>();
		
		return JDBCUtil.update(sql);
	}

//	// 적립금 부여(구매금액에 따라 해당 회원에게 적립금 지급)
//	public int updateMile(String memMile, String memId) {
//		String sql = " UPDATE MEMBER"
//				+ "       SET MEM_MILE = ?"
//				+ "     WHERE MEM_ID = ?";
//
//		List<Object> param = new ArrayList<Object>();
//		param.add(memMile);
//		param.add(memId);
//		
//		return JDBCUtil.update(sql, param);
//	}

	// 회원의 기존 적립금 조회
	public Map<String, Object> selectMile(String memId) {
		String sql = "SELECT MEM_MILE, MEM_ID" 
				+ "     FROM MEMBER"
				+ "     WHERE MEM_ID = ?";
	    List<Object> param = new ArrayList<Object>();
	    param.add(memId);
	    
	    return JDBCUtil.selectOne(sql, param);
	}
	}