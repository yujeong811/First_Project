package controller;

import service.BoardService;
import service.MemberService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		/*
		 * 발표내용 : 조 소개 > 주제 소개 > 주제 선정 배경 > 메뉴 구조 > 시연 > 프로젝트 후기
		 * 발표인원 : 발표자 1명, ppt 및 시연 도우미 1명
		 * 
		 * Controller : 화면 이동
		 * Service : 화면 기능
		 * Dao : 데이터베이스 접속
		 */
		
		new Controller().start();
	}
	
	
	private MemberService memberService = MemberService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	
	private void start() {
		int view = View.HOME;
		
		while(true) {
			switch (view) {
			case View.HOME: view = home(); break;
			case View.LOGIN: view = memberService.login(); break;
			case View.JOIN: view = memberService.join(); break;
			case View.BOARD_LIST: view = boardService.boardList(); break;
			case View.BOARD_READ: view = boardService.boardRead(); break;
			case View.BOARD_INSERT: view = boardService.boardInsert(); break;
			case View.BOARD_UPDATE: view = boardService.boardUpdate(); break;
			case View.BOARD_DELETE: view = boardService.boardDelete(); break;
			}
		}
	}

	private int home() {
		System.out.print("1.로그인  2.회원가입  0.프로그램 종료>");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1: return View.LOGIN;
		case 2: return View.JOIN;
		case 0:
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
		}
		return View.HOME;
	}

}















