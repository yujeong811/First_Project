package controller;

import dao.ProdDao;
import service.AdminService;
import service.BoardService;
import service.MemberService;
//import service.PayService;
import service.ProdService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		/*
		 * Controller : 화면 이동 (화면을 이동하고 싶을 때 컨트롤러를 통해서 다른 화면 호출
		 *  하나의 기능을 수행하고 그 화면에서 바로 호출을 하는 것이 아닌 
		 *  기능을 수행 후 컨트롤러로 돌아가 컨트롤러에서 다시 다른 서비스를 호출)
		 * Service : 화면 기능(각각의 기능이 Service), 화면에 나타나는 기능으로 각각의 메서드로 구현함
		 * Dao : 데이터베이스 접속(데이터베이스에 접근해야 할 때 Dao 사용)
		 */
		
		new Controller().start();
	}
	
	private MemberService memberService = MemberService.getInstance(); // 멤버서비스의 객체
	private BoardService boardService = BoardService.getInstance(); // 보드서비스의 객체
	private ProdService prodService = ProdService.getInstance();
//	private PayService payService = PayService.getInstance();
	private AdminService adminService = AdminService.getInstance();
	
	
	
	private void start() {
		// 각 화면을 이동시켜주는 메서드
		int view = View.HOME; // 사용자가 첫 화면에서 로그인, 회원가입을 선택하는 화면

		while(true) { // 이 구조를 숙지할것
			// 홈화면부터 시작. 초기값 view = 0임
			switch (view) { // view 값에 따라 해당하는 화면 실행
			case View.HOME: view = home(); break; // home에서는 다음 실행할 화면을 리턴 (리턴되는 값에 따라 계속 바뀜)
			case View.LOGIN: view = memberService.login(); break;
			case View.JOIN: view = memberService.join(); break;
			case View.MAIN: view = main(); break;
			case View.LOGOUT: view = memberService.logout(); break;
			case View.MY_PAGE: view = myPage(); break;
			case View.MY_INFO: view = memberService.myInfo(); break;
			case View.INFO_UPDATE: view = memberService.infoUpdate(); break;
			case View.MY_GRADE: view = memberService.myGrade(); break;
			case View.MY_MILE: view = memberService.myMile(); break;
			case View.MY_COUPON: view = memberService.myCoupon(); break;
			case View.MY_ORDERLIST: view = memberService.payList(); break;
			case View.DE_ORDERLIST: view = memberService.deOrderList(); break;
			case View.REVIEW: view = memberService.review(); break;
			case View.MY_REVIEW: view = memberService.myReview(); break;
			case View.MY_REVIEW_DETAIL: view = memberService.myReview_detail(); break;
			case View.MY_REVIEW_UPDATE: view = memberService.myReview_update(); break;
			case View.MY_REVIEW_DELETE: view = memberService.myReview_delete(); break;
			case View.MY_REVIEW_DDETAIL: view = memberService.myReview_ddetail(); break;
			case View.MY_NOTICE: view = boardService.memNotice(); break;
			case View.MY_QNA: view = boardService.prodQna(); break;
			case View.QNA_UPDATE: view = boardService.boardUpdate(); break;
			case View.QNA_DELETE: view = boardService.boardDelete(); break;
			
			//상품 연결
			case View.PROD_HOME: view = prodHome(); break;
			case View.PROD_SEARCH: view = prodService.ProdSearch(); break;
			case View.PROD_RANKING: view = prodService.ProdRanking(); break;
			case View.PROD_PBOARD: view = prodService.ProdBoard(); break;
			case View.PROD_SEARCHLIST: view = prodService.searchlist(); break;
			case View.PROD_DETAIL: view = prodService.prodDetail(); break;
			case View.PROD_RANKBOARD: view = prodService.Rank(); break;
			case View.PAY_DEORDER: view = prodService.deorderProd(); break;			
			case View.PROD_CART: view = prodService.orderCart(); break;
			case View.PROD_UPDATERANK: view = prodService.updateRanking(); break;			
			case View.CART_DELETE: view = prodService.delCart(); break;
			case View.CART_DELETE_ALL: view = prodService.delCart_ALL(); break;
			case View.PAY_CARD: view = prodService.cardPay(); break;
			case View.PAY_CASH: view = prodService.cashPay(); break;
			case View.PAYMENT: view = prodService.payment(); break;
			case View.PAY_REFUND: view = memberService.payRefund(); break;
			
			//상품 QNA
			case View.PROD_QNA: view = prodService.prodQna(); break;
			case View.PROD_QNA_CHOICE: view = prodService.choiceQna(); break;
			case View.PROD_QNA_LIST : view = prodService.listQna(); break;
			case View.PROD_QNA_SEARCH: view = prodService.qnaSearch(); break;
			case View.PROD_QNA_UPDATE: view = prodService.qnaUpdate(); break;
			case View.PROD_QNA_DELETE: view = prodService.qnaDelete(); break;
			case View.PROD_REVIEW_DETAIL: view = prodService.prodReview_detail(); break;
			case View.PROD_REVIEW_DDETAIL: view = prodService.prodReview_ddetail(); break;
			
			//관리자
			case View.ADMIN_LOGIN: view = adminService.admin_login(); break;
			case View.ADMIN_MAIN: view = admin_main(); break;
			case View.ADMIN_MEMREAD: view = adminService.admin_memread(); break;
			case View.ADMIN_MEMEVENT: view = adminService.admin_memevent(); break;
			case View.ADMIN_MEMREADDE: view = adminService.admin_memreadde(); break;
			case View.ADMIN_PMANAGE: view = adminService.admin_prod(); break;
			case View.ADMIN_NOTICE: view = adminService.admin_notice_insert(); break;
			case View.ADMIN_NOTICELIST: view = adminService.admin_notice_List(); break;
			case View.ADMIN_QNA: view = adminService.admin_qna(); break;
			case View.ADMIN_NOTICELIST_DETAIL: view = adminService.admin_noticeList_detail(); break;
			case View.ADMIN_NOTICELIST_UPDATE: view = adminService.admin_notice_update(); break;
			case View.ADMIN_NOTICELIST_DELETE: view = adminService.admin_notice_delete(); break;
//			case View.ADMIN_PROD_SELECT: view = adminService.admin_prod_lprod_select(); break;
			case View.ADMIN_PROD_INSERT: view = adminService.admin_prod_lprod_insert(); break;
			case View.ADMIN_PROD_INSERT_OUTER: view = adminService.admin_prod_insert_outer(); break;
			case View.ADMIN_PROD_INSERT_TOP: view = adminService.admin_prod_insert_top(); break;
			case View.ADMIN_PROD_INSERT_BOTTOM: view = adminService.admin_prod_insert_bottom(); break;
			case View.ADMIN_PROD_INSERT_SHOES: view = adminService.admin_prod_insert_shoes(); break;
			case View.ADMIN_PROD_OUTER: view = adminService.admin_prod_outer(); break;
			case View.ADMIN_PROD_TOP: view = adminService.admin_prod_top(); break;
			case View.ADMIN_PROD_BOTTOM: view = adminService.admin_prod_bottom(); break;
			case View.ADMIN_PROD_SHOES: view = adminService.admin_prod_shoes(); break;
//			case View.ADMIN_SEARCH: view = adminService.adminProdBoard(); break;
			case View.ADMIN_PROD_UPDATE: view = adminService.admin_prod_lprod_update(); break;
			case View.ADMIN_PROD_UPDATE_OUTER: view = adminService.admin_prod_update_outer(); break;
			case View.ADMIN_PROD_UPDATE_TOP: view = adminService.admin_prod_update_top(); break;
			case View.ADMIN_PROD_UPDATE_BOTTOM: view = adminService.admin_prod_update_bottom(); break;
			case View.ADMIN_PROD_UPDATE_SHOES: view = adminService.admin_prod_update_shoes(); break;
			case View.ADMIN_PROD_DELETE: view = adminService.admin_prod_lprod_delete(); break;
			case View.ADMIN_PROD_DELETE_OUTER: view = adminService.admin_prod_delete_outer(); break;
			case View.ADMIN_PROD_DELETE_TOP: view = adminService.admin_prod_delete_top(); break;
			case View.ADMIN_PROD_DELETE_BOTTOM: view = adminService.admin_prod_delete_bottom(); break;
			case View.ADMIN_PROD_DELETE_SHOES: view = adminService.admin_prod_delete_shoes(); break;
//			case View.ADMIN_PROD_SELECT: view = adminService.admin-(); break;
			case View.ADMIN_GRADE: view = adminService.admin_grade(); break;
//			case View.ADMIN_MILE: view = adminService.admin_mile(); break;
			
			//관리자 QNA
			case View.ADMIN_QNA_SEARCH: view = adminService.adminqnaSearch(); break;
			case View.ADMIN_QNA_REPLY: view = adminService.adminQnaReply(); break;
			case View.ADMIN_QNA_UPDATE: view = adminService.adminQnaUpdate(); break;
			}
		}
	}

	// HOME
	private int home() {
		System.out.println("   ┌─────────┐");
		System.out.println("   │         │");
	    System.out.println("   │ MUSINSA │");
		System.out.println("   │         │");
		System.out.println("   └─────────┘");
		System.out.println("𝐎𝐍𝐋𝐈𝐍𝐄 𝐅𝐀𝐒𝐇𝐈𝐎𝐍 𝐒𝐓𝐎𝐑𝐄👕 ");
		System.out.println();
		System.out.print(" 1.𝗟𝗢𝗚𝗜𝗡      2.𝗦𝗜𝗚𝗡 𝗨𝗣     3.𝗔𝗗𝗠𝗜𝗡     0.𝗘𝗡𝗗 ⏩ ");
		int input = ScanUtil.nextInt();
		
		switch(input) {
		// 직접 호출하는 것이 아닌 리턴. while문으로 돌아가서 실행됨
		// (switch 안에서는 메서드를 직접 호출하지 말고 리턴할 것)
		case 1: return View.LOGIN;
		case 2: return View.JOIN;
		case 3: return View.ADMIN_LOGIN;
		case 0:
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
		}
		// 사용자가 다른 숫자를 입력했을 경우를 대비해
		return View.HOME;
	}
	
	// ADMIN_MAIN
	private int admin_main() { 
			System.out.print(" 1.𝗠𝗘𝗠𝗕𝗘𝗥 𝗜𝗡𝗙𝗢  2.𝗣𝗥𝗢𝗗 𝗠𝗔𝗡𝗔𝗚𝗘  3.𝗡𝗢𝗧𝗜𝗖𝗘  4.𝗤𝗻𝗔  0.𝗘𝗫𝗜𝗧 ⏩ ");
			int input = ScanUtil.nextInt();

			switch (input) {
			case 1: return View.ADMIN_MEMREAD;
			case 2: return View.ADMIN_PMANAGE;
			case 3: return View.ADMIN_NOTICELIST;
			case 4: return View.ADMIN_QNA;
			}
			return View.HOME;
		}
	
	// MAIN
	private int main() {
		System.out.print(" 1.𝗣𝗥𝗢𝗗 🧥   2.𝗠𝗬 𝗣𝗔𝗚𝗘 👨‍🦲   3.𝗡𝗢𝗧𝗜𝗖𝗘 📝   4.𝗟𝗢𝗚𝗢𝗨𝗧 ➰   0.𝗘𝗡𝗗 ⏩ ");
		int input = ScanUtil.nextInt();
		System.out.println("•·······················•·······················•");
		switch (input) {
		case 1: return View.PROD_HOME;
		case 2: return View.MY_PAGE;
		case 3: return View.MY_NOTICE;
		case 4: return View.LOGOUT;
		case 0:
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
		}
		return View.HOME;
	}
	
	// 1. PROD_HOME
	private int prodHome() {

//		List<Map<String, Object>> boardList = BoardDao.selectBoardList(); // 게시판 목록 불러오기
//		System.out.println("==============================");
//		System.out.println("번호\t제목\t작성자\t작성일");
//		System.out.println("------------------------------");
//		for(Map<String, Object> board : boardList) {
//			System.out.println(board.get("BOARD_NO")
//					+ "\t" + board.get("TITLE")
//					+ "\t" + board.get("MEM_NAME")
//					+ "\t" + board.get("REG_DATE"));
//		}
		
		System.out.print(" 1.𝗦𝗘𝗔𝗥𝗖𝗛 🔍   2.𝗥𝗔𝗡𝗞𝗜𝗡𝗚 🥇   3.𝗖𝗔𝗥𝗧 🛒    0.𝗕𝗔𝗖𝗞 ⏩ ");
		int input = ScanUtil.nextInt();
		System.out.println("•·······················•·······················•");
		System.out.println();
		
		while(true) {
			switch (input) {
			case 1:
				return View.PROD_SEARCH;//검색
			case 2:
				return View.PROD_RANKING;//랭킹
			case 3: 
				return View.PROD_CART;//장바구니
			case 0:
				return View.MAIN;//로그인된 홈 
				}
		}
	}
	
	// 2. MY_PAGE
	private int myPage() {
		System.out.print(" 1.𝗠𝗬 𝗜𝗡𝗙𝗢‍ 👨‍🦲  2.𝗚𝗥𝗔𝗗𝗘 💎  3.𝗢𝗥𝗗𝗘𝗥𝗟𝗜𝗦𝗧 🧾  4.𝗥𝗘𝗩𝗜𝗘𝗪 📝  0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		System.out.println("•·······················•·······················•");
		
		switch (input) { // 메서드 호출하지 않고 return 하기
		case 1: return View.MY_INFO; 
		case 2: return View.MY_GRADE;
		case 3: return View.MY_ORDERLIST;
		case 4: return View.MY_REVIEW;
		case 0: return View.MAIN;
		}
		return View.HOME;
	}
	//𝗥𝗘𝗖𝗘𝗜𝗣𝗧 𝗥𝗘𝗩𝗜𝗘𝗪 𝗪𝗥𝗜𝗧𝗘 𝗥𝗘𝗙𝗨𝗡𝗗
	//🆗 🆖 ❕ ❔ ✏️(𝙮/𝙣)❔ ✏️(𝙮/𝙣) 1.𝗘𝗗𝗜𝗧 📝   2.𝗗𝗘𝗟𝗘𝗧𝗘 ✂️   0.𝗕𝗔𝗖𝗞 ⏩
	//System.out.println("════════════════════════════════════════════════");
	//System.out.println("═════════════════[ 𝗠𝗬 𝗖𝗢𝗨𝗣𝗢𝗡 ]═════════════════");
	
	

}
