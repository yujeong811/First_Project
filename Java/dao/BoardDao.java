package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import util.ScanUtil;

public class BoardDao {

	private BoardDao() {
	}

	private static BoardDao instance;
	public static BoardDao getInstance() {
		if (instance == null) { 
			instance = new BoardDao(); 
		} 
		return instance; 
	}

	// 공지사항번호 입력후 조회
	public static Map<String, Object> readNotice(int noticeNo) { 
			
		String sql = "SELECT NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, NOTICE_DATE"
					+ "		FROM NOTICE"
					+ "	   WHERE NOTICE_NO = ?";
			
		List<Object> param = new ArrayList<Object>();
		param.add(noticeNo);
			
		return JDBCUtil.selectOne(sql, param);
	}
	
	public static List<Map<String, Object>> selectnotice() {
		String sql = "SELECT NOTICE_NO, NOTICE_TITLE,NOTICE_DATE FROM NOTICE";

		return JDBCUtil.selectList(sql);
	}

	// QNA 등록	
	public int qnaInsert(String qnaTitle, String qnaContent, String memId, Integer prodNo) {  
		String sql =  "INSERT INTO QNA(PR_QNA_NO,"
				+ "                    PR_QNA_TITLE,"
				+ "                    PR_QNA_CONTENT,"
				+ "                    PR_QNA_DATE,"
				+ "                    MEM_ID,"
				+ "    				   PROD_NO"
				+ "     VALUES((SELECT NVL(MAX(PR_QNA_NO), 0) + 1 FROM QNA),"
				+ "				 ?, ?, SYSDATE"
				+ "				, ?, ?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(qnaTitle);
		param.add(qnaContent);
		param.add(memId);
		param.add(prodNo);

		return JDBCUtil.update(sql, param);
	}


	public List<Map<String, Object>> selectQnaList(int prodNo) {
		String sql = "SELECT A.PR_QNA_NO,"
		         + "         B.PROD_NAME,"
		         + "         A.PR_QNA_TITLE,"
		         + "         TO_CHAR(A.PR_QNA_DATE, 'MM/DD') AS PR_QNA_DATE"
		         + "    FROM QNA A, PROD_BOARD B"
		         + "   WHERE A.PROD_NO=B.PROD_NO"
		         + "     AND A.PROD_NO= ?";
		List<Object> param = new ArrayList<Object>();
		param.add(prodNo);

		return JDBCUtil.selectList(sql);
	}

	// QNA 번호 선택후 상세보기
	public Map<String, Object> selectQna(int qnaNo) {  
		String sql = "SELECT PR_QNA_NO,"
				+ " PR_QNA_TITLE,"
				+ " PR_QNA_CONTENT,"
				+ " TO_CHAR(PR_QNA_DATE, 'MM/DD') AS PR_QNA_DATE"
				+ "		FROM QNA"
				+ "	   WHERE PR_QNA_NO = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(qnaNo);
		
		return JDBCUtil.selectOne(sql, param);
	}

	// QNA 수정
	public int updateBoard(int qnaNo, String title, String content) { 
		String sql = "UPDATE QNA"
				+ "		 SET PR_QNA_TITLE = ?"
				+ "		   , PR_QNA_CONTENT = ?"
				+ "    WHERE PR_QNA_NO = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(title);
		param.add(content);
		param.add(qnaNo);
		
		return JDBCUtil.update(sql, param);
	}

	// QNA 삭제
	public int deleteBoard(int qnaNo) {  
		
		String sql = "DELETE FROM QNA"
				+ "	   WHERE PR_QNA_NO = ?";
		List<Object> param = new ArrayList<Object>();

		param.add(qnaNo);

		return JDBCUtil.update(sql, param);
	}


}
