package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.BoardDao;
import dao.MemberDao;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class BoardService {

	private BoardService() {
	}

	private static BoardService instance;

	public static BoardService getInstance() {
		if (instance == null) {
			instance = new BoardService();
		}
		return instance;
	}
	
	private BoardDao boardDao = BoardDao.getInstance();
	public static Map<String, Object> selectProdNo; // 사용자가 선택한 상품번호
	
	public int memNotice() { // 공지사항
		List<Map<String, Object>> notice = BoardDao.selectnotice();
		System.out.println("╔══════════════ 𝗡𝗢𝗧𝗜𝗖𝗘 📝 ═════════════╗");
		System.out.println("  번호\t제목\t작성일");
		System.out.println("═══════════════════════════════════════");
		
		for (Map<String, Object> noticelist : notice) {
			System.out.println(noticelist.get("NOTICE_NO") + "\t" + noticelist.get("NOTICE_TITLE") + "\t"
					+ noticelist.get("NOTICE_DATE"));
		}
		System.out.println("  ═══════════════════════════════════════");
		System.out.println(" 1.𝗡𝗢𝗧𝗜𝗖𝗘 𝗦𝗘𝗔𝗥𝗖𝗛   0.𝗕𝗔𝗖𝗞 ⏩");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			noticeRead();
			break;
		}
		return 0; // 사용자 MAIN으로 돌아가기
	}

	int noticeNo;

	// 공지사항 조회하는 메서드
	public void noticeRead() {
		System.out.println(" 공지사항번호를 입력해주세요 ⏩");
		noticeNo = ScanUtil.nextInt();

		Map<String, Object> notice = BoardDao.readNotice(noticeNo);

		System.out.println("═══════════════════════════════════════");
		System.out.println(" 공지사항번호 : " + notice.get("NOTICE_NO"));
		System.out.println("    제목    : " + notice.get("NOTICE_TITLE"));
		System.out.println("    내용    : " + notice.get("NOTICE_CONTENT"));
		System.out.println("   작성일    : " + notice.get("NOTICE_DATE"));
		System.out.println("═══════════════════════════════════════");

		System.out.println(" 0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 0:
			memNotice();
			break;
		}
	}

	// QNA 시작화면
	public int prodQna() {
		System.out.println(" 1.𝗤𝗡𝗔 𝗪𝗥𝗜𝗧𝗘   2.𝗤𝗡𝗔 𝗟𝗜𝗦𝗧   0.𝗕𝗔𝗖𝗞 ⏩"); // 해당 상품에 대한 질문을 등록하고 해당 상품에 대한 질문목록을 볼 수 있음
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			choiceQna();
			break;
		case 2:
			ListQna();
			break;
		}

		return 0;
	}

	// QNA 등록
	public int choiceQna() {
		System.out.println(" 질문제목을 입력해주세요 ⏩");
		String qnaTitle = ScanUtil.nextLine();
		System.out.println(" 질문내용을 입력해주세요 ⏩");
		String qnaContent = ScanUtil.nextLine();
		String memId = (String) MemberService.qnaMember.get("MEM_ID");
		Integer prodNo = (Integer) BoardService.selectProdNo.get("PROD_NO"); // 검색한 상품의 상품번호

		int result = boardDao.qnaInsert(qnaTitle, qnaContent, memId, prodNo);

		if (0 < result) {
			System.out.println("┌───────────────┐");
			System.out.println("         질문 등록 완료!");
			System.out.println("└───────────────┘");
			System.out.println("　　ᕱ ᕱ ||");
			System.out.println("　 ( ･ω･ ||");
			System.out.println("　 /　つ Φ");
		} else {
			System.out.println("＿人人人人人人人人人人＿");
			System.out.println("  ＞ 질문 등록 실패! ＜");
			System.out.println("　　　┐( ∵ )┌");
			System.out.println("　 　 　 ( 　) 　");
			System.out.println("　　 　　┘| ");
		}
		return ListQna(); // QNA 목록으로 가기

	}

	// 질문목록출력하는 메서드
	public int ListQna() {

		Integer prodNo = (Integer) BoardService.selectProdNo.get("PROD_NO");
		List<Map<String, Object>> qnalist = boardDao.selectQnaList(prodNo);
		// 해당상품의 질문 목록을 출력하고싶음

		System.out.println();
		System.out.println("╔═════════════ 𝗤𝗡𝗔 𝗕𝗢𝗔𝗥𝗗 🔍 ════════════╗");
		System.out.println("  번호\t상품명\t\t\t제목\t작성일");
		System.out.println("═══════════════════════════════════════");
		for (Map<String, Object> qna : qnalist) {
			System.out.println(qna.get("PR_QNA_NO") + "\t" + qna.get("PROD_NAME") + "\t" + qna.get("PR_QNA_TITLE")
					+ "\t" + qna.get("PR_QNA_DATE"));
			System.out.println("═══════════════════════════════════════");
		}
		System.out.println("═══════════════════════════════════════");
		System.out.println(" 1.𝗦𝗘𝗔𝗥𝗖𝗛   0.𝗕𝗔𝗖𝗞 ⏩");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			qnaSearch();
			break; // 검색
		case 0:
			return 0; // 상품정보로 뒤로가기

		}

		return 0; // 사용자메인으로 가기

	}

	int nowQnaNo;

	// 상세질문
	public int qnaSearch() {

		int qnaNo = nowQnaNo;
		Map<String, Object> qna = boardDao.selectQna(qnaNo);

		System.out.println("═══════════════════════════════════════");
		System.out.println(" 번호\t: " + qna.get("PR_QNA_NO"));
		System.out.println(" 상품번호\t: " + qna.get("PROD_NO"));
		System.out.println(" 질문제목\t: " + qna.get("PR_QNA_TITLE"));
		System.out.println(" 질문내용\t: " + qna.get("PR_QNA_CONTENT"));
		System.out.println("═══════════════════════════════════════");

		System.out.println(" 1.𝗤𝗡𝗔 𝗘𝗗𝗜𝗧   2.𝗤𝗡𝗔 𝗗𝗘𝗟𝗘𝗧𝗘   0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.QNA_UPDATE;
		case 2:
			return View.QNA_DELETE;

		}

		return 0;

	}

	// 질문수정
	public int boardUpdate() {
		int qnaNo = nowQnaNo;
		System.out.println(" 수정할 제목 ⏩ ");
		String title = ScanUtil.nextLine();
		System.out.println(" 수정할 내용 ⏩ ");
		String content = ScanUtil.nextLine();

		int result = boardDao.updateBoard(qnaNo, title, content);

		if (0 < result) {
			System.out.println("┌───────────────┐");
			System.out.println("         질문 수정 완료!");
			System.out.println("└───────────────┘");
			System.out.println("　　ᕱ ᕱ ||");
			System.out.println("　 ( ･ω･ ||");
			System.out.println("　 /　つ Φ");
		} else {
			System.out.println("＿人人人人人人人人人人＿");
			System.out.println("＞ 질문 수정 실패! ＜");
			System.out.println("　　　┐( ∵ )┌");
			System.out.println("　 　 　 ( 　) 　");
			System.out.println("　　 　　┘| ");
		}

		return qnaSearch();
	}

	// 질문삭제
	public int boardDelete() {
		System.out.print(" 정말 삭제하시겠습니까? (𝙮/𝙣) ⏩");
		String yn = ScanUtil.nextLine();

		if (yn.equals("Y")) {
			int qnaNo = nowQnaNo;
			int result = boardDao.deleteBoard(qnaNo);

			if (0 < result) {
				System.out.println("┌───────────────┐");
				System.out.println("         질문 삭제 완료!");
				System.out.println("└───────────────┘");
				System.out.println("　　ᕱ ᕱ ||");
				System.out.println("　 ( ･ω･ ||");
				System.out.println("　 /　つ Φ");
			} else {
				System.out.println("＿人人人人人人人人人人＿");
				System.out.println("  ＞ 질문 삭제 실패! ＜");
				System.out.println("　　　┐( ∵ )┌");
				System.out.println("　 　 　 ( 　) 　");
				System.out.println("　　 　　┘| ");
			}
		}

		return 0;
	}

}
