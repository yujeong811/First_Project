package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.BoardDao;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class BoardService {
	
	private BoardService() {
		
	}
	private static BoardService instance;
	public static BoardService getInstance() {
		if(instance == null) {
			instance = new BoardService();
		}
		return instance;
	}
	
	
	private BoardDao boardDao = BoardDao.getInstance();
	
	public int boardList() {
		List<Map<String,Object>> boardList = boardDao.selectBoardList();
		
		System.out.println("=============================");
		System.out.println("번호\t제목\t작성자\t작성일");
		System.out.println("-----------------------------");
		for(Map<String, Object> board : boardList) {
			System.out.println(board.get("BOARD_NO")
					+ "\t" + board.get("TITLE")
					+ "\t" + board.get("MEM_NAME")
					+ "\t" + board.get("REG_DATE"));
		}
		System.out.println("=============================");
		System.out.println("1.조회  2.등록  0.로그아웃>");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.print("게시글 번호>");
			currentBoardNo = ScanUtil.nextInt();
			return View.BOARD_READ;
		case 2:
			return View.BOARD_INSERT;
		case 0:
			MemberService.loginMember = null;
			return View.HOME;
		}
		
		return View.BOARD_LIST;
	}

	int currentBoardNo;
	
	public int boardRead() {
		int boardNo = currentBoardNo;
		Map<String, Object> board = boardDao.selectBoard(boardNo);
		
		System.out.println("----------------------------------");
		System.out.println("번호\t: " + board.get("BOARD_NO"));
		System.out.println("작성자\t: " + board.get("MEM_NAME"));
		System.out.println("작성일\t: " + board.get("REG_DATE"));
		System.out.println("제목\t: " + board.get("TITLE"));
		System.out.println("내용\t: " + board.get("CONTENT"));
		System.out.println("----------------------------------");
		
		System.out.print("1.수정  2.삭제  0.목록>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.BOARD_UPDATE;
		case 2: return View.BOARD_DELETE;
		case 0: return View.BOARD_LIST;
		}
		
		return View.BOARD_READ;
	}

	public int boardInsert() {
		System.out.print("제목>");
		String title = ScanUtil.nextLine();
		System.out.print("내용>");
		String content = ScanUtil.nextLine();
		String memId = (String) MemberService.loginMember.get("MEM_ID");
		
		int result = boardDao.insertBoard(title, content, memId);
		
		if(0 < result) {
			System.out.println("게시글 등록 성공");
			Map<String, Object> board = boardDao.selectNewBoard();
			currentBoardNo = (int)((double) board.get("BOARD_NO"));
		}else {
			System.out.println("게시글 등록 실패");
		}
		
		return View.BOARD_READ;
	}

	public int boardUpdate() {
		int boardNo = currentBoardNo;
		System.out.print("제목>");
		String title = ScanUtil.nextLine();
		System.out.print("내용>");
		String content = ScanUtil.nextLine();
		
		int result = boardDao.updateBoard(boardNo, title, content);
		
		if(0 < result) {
			System.out.println("게시글 수정 성공");
		}else {
			System.out.println("게시글 수정 실패");
		}
		
		return View.BOARD_READ;
	}

	public int boardDelete() {
		System.out.print("정말 삭제하시겠습니까?>");
		String yn = ScanUtil.nextLine();
		
		if(yn.equals("Y")) {
			int boardNo = currentBoardNo;
			int result = boardDao.deleteBoard(boardNo);
			
			if(0 < result) {
				System.out.println("게시글 삭제 성공");
			}else {
				System.out.println("게시글 삭제 실패");
			}
		}
		
		return View.BOARD_LIST;
	}
	
}
















