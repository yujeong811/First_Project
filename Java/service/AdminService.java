package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AdminDao;
import dao.BoardDao;
import dao.MemberDao;
import dao.ProdDao;
import util.CouponUtil;
import util.GradeUtil;
import util.ScanUtil;
import util.View;

public class AdminService {

	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private AdminService() {
		// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static AdminService instance; // 객체를 보관할 변수

	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	public static AdminService getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new AdminService(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}
	private AdminDao adminDao = AdminDao.getInstance();
	private ProdDao prodDao = ProdDao.getInstance();
	private MemberDao memberDao = MemberDao.getInstance();
	public static Map<String, Object> loginAdmin;
	public static int noticeAdmin;
	
	public static String outlprod = "A0001";
	public static String toplprod = "B0001";
	public static String bottomlprod = "C0001";
	public static String shoelprod = "D0001";
	public static String lprodNo;
	public static String pname;
	public static String num;
	
	// 로그인 메서드
	public int admin_login() {
		System.out.println("╔═════════════════════[ 𝗔𝗗𝗠𝗜𝗡 ]═════════════════════╗");
		System.out.print("[ 𝗜𝗗 ] ➜");
		String adm_id = ScanUtil.nextLine();
		System.out.print("[ 𝗣𝗔𝗦𝗦𝗪𝗢𝗥𝗗 ] ➜");
		String adm_pass = ScanUtil.nextLine();

		// 멤버테이블에서 입력받은 아이디, 비번과 일치하는 사람을 찾으면 로그인
		// 사용자가 입력한 아이디, 비번을 가지고 selectMember를 실행한 값을 리턴받아 member에 저장
		Map<String, Object> admin = adminDao.selectAdmin(adm_id, adm_pass);

		if (admin == null) {
			System.out.println("      ∧__∧");
			System.out.println("     ( ｀Д´）");
			System.out.println("    (っ▄︻▇〓┳═☆  로그인 실패❕");
			System.out.println("     /　　 )");
			System.out.println("    ( /￣∪");
		} else {
			System.out.println("  　 ♡ ♡ ᕬ ᕬ ♡ ♡");
			System.out.println(" 　 + ♡ ( ⌯′-′⌯) ♡ +");
			System.out.println(" ┏━♡━ U U━━♡━┓");
			System.out.println("  ♡　 관리자 로그인 성공　 ♡ ");
			System.out.println(" ┗━♡━━━━━♡━┛");
			System.out.println();
			loginAdmin = admin; // 로그인 상태이기 때문에 로그인한 관리자 정보 담아두기
			// (글 작성이나 수정 시 자신의 아이디에 맞는 것을 가져와야 하기 때문에 그때 사용하기 위해 담아두는 것)
			return View.ADMIN_MAIN; // 로그인에 성공하면 게시판이 나오게 설정
		}

		return View.ADMIN_LOGIN; // 로그인에 실패하면 다시 로그인을 할 수 있게 로그인 화면을 리턴
	}

	// 1. 회원정보열람
	String mid;

	public int admin_memread() {
		List<Map<String, Object>> memberlist = adminDao.selectMemberRead();

		System.out.println("╔═════════[ 𝗠𝗘𝗠𝗕𝗘𝗥 𝗜𝗡𝗙𝗢 ]═════════╗");
		System.out.println("  𝗠𝗘𝗠𝗕𝗘𝗥 𝗜𝗗 \t\t 𝗡𝗔𝗠𝗘");
		for (Map<String, Object> mem : memberlist) {
			System.out.println("  " + mem.get("MEM_ID") + " \t\t " + mem.get("MEM_NAME"));
			}
		System.out.println("╚════════════════════════════════╝");

		System.out.print("조회하고 싶은 회원의 아이디를 입력하세요 ➜ ");
		mid = ScanUtil.nextLine();
		return admin_memreadde();
	}

	// 1. 회원정보열람 > 한명상세보기
	public int admin_memreadde() {
		Map<String, Object> memberfind = adminDao.selectMemberReadde(mid);

		if (mid.equals(memberfind.get("MEM_ID").toString())) {
			System.out.println("┌────────── ༻❁༺ ──────────┐");
			System.out.println(" 𝗡𝗔𝗠𝗘\t: " + memberfind.get("MEM_NAME"));
			System.out.println(" 𝗜𝗗\t: " + memberfind.get("MEM_ID"));
			System.out.println(" 𝗣𝗔𝗦𝗦\t: " + memberfind.get("MEM_PASS"));
			System.out.println(" 𝗔𝗗𝗗\t: " + memberfind.get("MEM_ADDR"));
			System.out.println(" 𝗕𝗜𝗥𝗧𝗛\t: " + format.format(memberfind.get("MEM_BIR")));
			System.out.println(" 𝗚𝗥𝗔𝗗𝗘\t: " + memberfind.get("MEM_GRADE"));
			System.out.println(" 𝗠𝗜𝗟𝗘\t: " + memberfind.get("MEM_MILE"));
			System.out.println(" 𝗕𝗔𝗡𝗞\t: " + memberfind.get("MEM_BANK"));
			System.out.println(" 𝗔𝗖𝗖\t: " + memberfind.get("MEM_ACC"));
			System.out.println("└────────── ༻❁༺ ──────────┘");
		}
		return View.ADMIN_MAIN;
	}
	
	// 3. 상품관리 
	public int admin_prod() {
		System.out.println("┌─[𝗣𝗥𝗢𝗗𝗨𝗖𝗧]──────────────────────────────────┐");
		System.out.println(" 1. 상품등록   2. 상품수정   3. 상품삭제   0. 뒤로가기");
		System.out.println("└───────────────────────────────────────────┘");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.ADMIN_PROD_INSERT;
		case 2:
			return View.ADMIN_PROD_UPDATE;
		case 3:
			return View.ADMIN_PROD_DELETE;	
		case 0:
			return View.ADMIN_MAIN;
		}
		return View.ADMIN_MAIN;
	}
	
	public int admin_prod_lprod_insert() {
		System.out.println("┌─[𝗖𝗔𝗧𝗘𝗚𝗢𝗥𝗬]─────────────────────────────────┐");
		System.out.println(" 1. 𝗢𝗨𝗧𝗘𝗥    2. 𝗧𝗢𝗣     3. 𝗕𝗢𝗧𝗧𝗢𝗠     4. 𝗦𝗛𝗢𝗘𝗦");
		System.out.println("└───────────────────────────────────────────┘");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.ADMIN_PROD_INSERT_OUTER;
		case 2:
			return View.ADMIN_PROD_INSERT_TOP;
		case 3:
			return View.ADMIN_PROD_INSERT_BOTTOM;	
		case 4:
			return View.ADMIN_PROD_INSERT_SHOES;
		}
		return View.ADMIN_PMANAGE;
	}
	
	// 3. 상품관리 -- > 등록(아우터)
	public int admin_prod_insert_outer() {
		System.out.print("𝗖𝗢𝗟𝗢𝗥 ➜ ");
		String prodColor = ScanUtil.nextLine();
		System.out.print("𝗡𝗔𝗠𝗘 ➜ ");
		String prodName = ScanUtil.nextLine();
		System.out.print("𝗗𝗘𝗧𝗔𝗜𝗟 ➜ ");
		String prodDetail = ScanUtil.nextLine();
		System.out.print("𝗣𝗥𝗜𝗖𝗘 ➜ ");
		int prodCost = ScanUtil.nextInt();
		System.out.print("𝗦𝗜𝗭𝗘 ➜ ");
		String prodSize = ScanUtil.nextLine();
	
		int result = adminDao.insert_prod_outer(prodColor, prodName, prodDetail, prodCost, prodSize);
		
		if (0 < result) {
			System.out.println("상품 등록 성공!");
		} else {
			System.out.println("상품 등록 실패!");
		}
		
		System.out.println("𝟭.𝗟𝗜𝗦𝗧   𝟬.𝗕𝗔𝗖𝗞");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			return View.ADMIN_PROD_OUTER;
		case 0:
			return View.ADMIN_PMANAGE;
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_insert_top() {
		System.out.print("𝗖𝗢𝗟𝗢𝗥 ➜ ");
		String prodColor = ScanUtil.nextLine();
		System.out.print("𝗡𝗔𝗠𝗘 ➜ ");
		String prodName = ScanUtil.nextLine();
		System.out.print("𝗗𝗘𝗧𝗔𝗜𝗟 ➜ ");
		String prodDetail = ScanUtil.nextLine();
		System.out.print("𝗣𝗥𝗜𝗖𝗘 ➜ ");
		int prodCost = ScanUtil.nextInt();
		System.out.print("𝗦𝗜𝗭𝗘 ➜ ");
		String prodSize = ScanUtil.nextLine();
	
		int result = adminDao.insert_prod_top(prodColor, prodName, prodDetail, prodCost, prodSize);
		
		if (0 < result) {
			System.out.println("상품 등록 성공!");
		} else {
			System.out.println("상품 등록 실패!");
		}		
		
		System.out.println(" 𝟭.𝗟𝗜𝗦𝗧   𝟬.𝗕𝗔𝗖𝗞");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			return View.ADMIN_PROD_TOP;
		case 0:
			return View.ADMIN_PMANAGE;
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_insert_bottom() {
		System.out.print("𝗖𝗢𝗟𝗢𝗥 ➜ ");
		String prodColor = ScanUtil.nextLine();
		System.out.print("𝗡𝗔𝗠𝗘 ➜ ");
		String prodName = ScanUtil.nextLine();
		System.out.print("𝗗𝗘𝗧𝗔𝗜𝗟 ➜ ");
		String prodDetail = ScanUtil.nextLine();
		System.out.print("𝗣𝗥𝗜𝗖𝗘 ➜ ");
		int prodCost = ScanUtil.nextInt();
		System.out.print("𝗦𝗜𝗭𝗘 ➜ ");
		String prodSize = ScanUtil.nextLine();
	
		int result = adminDao.insert_prod_bottom(prodColor, prodName, prodDetail, prodCost, prodSize);
		
		if (0 < result) {
			System.out.println("상품 등록 성공!");
		} else {
			System.out.println("상품 등록 실패!");
		}
		
		System.out.println(" 𝟭.𝗟𝗜𝗦𝗧   𝟬.𝗕𝗔𝗖𝗞");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			return View.ADMIN_PROD_BOTTOM;
		case 0:
			return View.ADMIN_PMANAGE;
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_insert_shoes() {
		System.out.print("𝗖𝗢𝗟𝗢𝗥 ➜ ");
		String prodColor = ScanUtil.nextLine();
		System.out.print("𝗡𝗔𝗠𝗘 ➜ ");
		String prodName = ScanUtil.nextLine();
		System.out.print("𝗗𝗘𝗧𝗔𝗜𝗟 ➜ ");
		String prodDetail = ScanUtil.nextLine();
		System.out.print("𝗣𝗥𝗜𝗖𝗘 ➜ ");
		int prodCost = ScanUtil.nextInt();
		System.out.print("𝗦𝗜𝗭𝗘 ➜ ");
		String prodSize = ScanUtil.nextLine();
	
		int result = adminDao.insert_prod_shoes(prodColor, prodName, prodDetail, prodCost, prodSize);
		
		if (0 < result) {
			System.out.println("상품 등록 성공!");
		} else {
			System.out.println("상품 등록 실패!");
		}
		
		System.out.println(" 𝟭.𝗟𝗜𝗦𝗧   𝟬.𝗕𝗔𝗖𝗞");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			return View.ADMIN_PROD_SHOES;
		case 0:
			return View.ADMIN_PMANAGE;
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_lprod_update() {
		System.out.println("┌─[𝗖𝗔𝗧𝗘𝗚𝗢𝗥𝗬]─────────────────────────────────┐");
		System.out.println(" 1. 𝗢𝗨𝗧𝗘𝗥    2. 𝗧𝗢𝗣     3. 𝗕𝗢𝗧𝗧𝗢𝗠     4. 𝗦𝗛𝗢𝗘𝗦");
		System.out.println("└───────────────────────────────────────────┘");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.ADMIN_PROD_UPDATE_OUTER;
		case 2:
			return View.ADMIN_PROD_UPDATE_TOP;
		case 3:
			return View.ADMIN_PROD_UPDATE_BOTTOM;	
		case 4:
			return View.ADMIN_PROD_UPDATE_SHOES;
		}
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_update_outer() {
		admin_prod_outer();
		
		System.out.print("수정하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
		System.out.print("상품 가격 ➜ ");
		int prodCost = ScanUtil.nextInt();
	
		int result = adminDao.update_prod_outer(prodCost, prodNo);
		
		if (0 < result) {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}	
		
		return View.ADMIN_PMANAGE;
	}
		
	public int admin_prod_update_top() {
		admin_prod_top();
		
		System.out.print("수정하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
		System.out.print("상품 가격 ➜ ");
		int prodCost = ScanUtil.nextInt();
	
		int result = adminDao.update_prod_top(prodCost, prodNo);
		
		if (0 < result) {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}

		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_update_bottom() {
		admin_prod_bottom();
		
		System.out.print("수정하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
		System.out.print("상품 가격 ➜ ");
		int prodCost = ScanUtil.nextInt();
	
		int result = adminDao.update_prod_bottom(prodCost, prodNo);
		
		if (0 < result) {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_update_shoes() {
		admin_prod_shoes();
		
		System.out.print("수정할 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
		System.out.print("수정할 상품 가격 ➜ ");
		int prodCost = ScanUtil.nextInt();
	
		int result = adminDao.update_prod_shoes(prodCost, prodNo);
		
		if (0 < result) {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_lprod_delete() {
		System.out.println("┌─[𝗖𝗔𝗧𝗘𝗚𝗢𝗥𝗬]─────────────────────────────────┐");
		System.out.println(" 1. 𝗢𝗨𝗧𝗘𝗥    2. 𝗧𝗢𝗣     3. 𝗕𝗢𝗧𝗧𝗢𝗠     4. 𝗦𝗛𝗢𝗘𝗦");
		System.out.println("└───────────────────────────────────────────┘");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.ADMIN_PROD_DELETE_OUTER;
		case 2:
			return View.ADMIN_PROD_DELETE_TOP;
		case 3:
			return View.ADMIN_PROD_DELETE_BOTTOM;	
		case 4:
			return View.ADMIN_PROD_DELETE_SHOES;
		}
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_delete_outer() {
		admin_prod_outer();
		
		System.out.print("삭제하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
	
		int result = adminDao.delete_prod_outer(prodNo);
		
		if (0 < result) {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_delete_top() {
		admin_prod_top();
		
		System.out.print("삭제하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
	
		int result = adminDao.delete_prod_top(prodNo);
		
		if (0 < result) {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_delete_bottom() {
		admin_prod_bottom();
		
		System.out.print("삭제하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
	
		int result = adminDao.delete_prod_bottom(prodNo);
		
		if (0 < result) {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_delete_shoes() {
		admin_prod_shoes();
		
		System.out.print("삭제하고 싶은 상품번호 ➜ ");
		String prodNo = ScanUtil.nextLine();
	
		int result = adminDao.delete_prod_shoes(prodNo);
		
		if (0 < result) {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗗𝗘𝗟𝗘𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_outer() {
		List<Map<String, Object>> outer = adminDao.adminselectOuter();
		
		System.out.println("┌───────────────────────────────────────────┐");
		for(Map<String, Object> board : outer) {
			System.out.println(board.get("PROD_NO")
					+ ". " + board.get("PROD_NAME") + ". " + board.get("PROD_COLOR")
					+ ". " + board.get("PROD_SIZE") + ". " + board.get("PROD_COST"));
		}//상품게시판출력
		System.out.println("└───────────────────────────────────────────┘");
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_top() {
		List<Map<String, Object>> top = adminDao.adminselectTop();
		
		System.out.println("┌───────────────────────────────────────────┐");
		for(Map<String, Object> board : top) {
			System.out.println(board.get("PROD_NO")
					+ ". " + board.get("PROD_NAME") + ". " + board.get("PROD_COLOR")
					+ ". " + board.get("PROD_SIZE") + ". " + board.get("PROD_COST"));
		}//상품게시판출력
		System.out.println("└───────────────────────────────────────────┘");
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_bottom() {
		List<Map<String, Object>> bottom = adminDao.adminselectBottom();
		
		System.out.println("┌───────────────────────────────────────────┐");
		for(Map<String, Object> board : bottom) {
			System.out.println(board.get("PROD_NO")
					+ ". " + board.get("PROD_NAME") + ". " + board.get("PROD_COLOR")
					+ ". " + board.get("PROD_SIZE") + ". " + board.get("PROD_COST"));
		}//상품게시판출력
		System.out.println("└───────────────────────────────────────────┘");
		
		return View.ADMIN_PMANAGE;
	}
	
	public int admin_prod_shoes() {
		List<Map<String, Object>> shoes = adminDao.adminselectShoes();
		
		System.out.println("┌───────────────────────────────────────────┐");
		for(Map<String, Object> board : shoes) {
			System.out.println(board.get("PROD_NO")
					+ ". " + board.get("PROD_NAME") + ". " + board.get("PROD_COLOR")
					+ ". " + board.get("PROD_SIZE") + ". " + board.get("PROD_COST"));
		}//상품게시판출력
		System.out.println("└───────────────────────────────────────────┘");
		
		return View.ADMIN_PMANAGE;
	}
	
	// 4. 공지사항 
	public int admin_notice_List() {

		String admId = (String) AdminService.loginAdmin.get("ADM_ID");

		List<Map<String, Object>> adminnotice = adminDao.admin_selectnotice(admId);
		System.out.println("┌─[𝗡𝗢𝗧𝗜𝗖𝗘]───────────────────────────────────┐");
		for (Map<String, Object> notice : adminnotice) {
			System.out.println(notice.get("NOTICE_NO") + "\t" + notice.get("NOTICE_TITLE") + "\t"
					+ format.format(notice.get("NOTICE_DATE")));
		}
		System.out.println("└───────────────────────────────────────────┘");
		System.out.print(" 𝟭.𝗦𝗘𝗔𝗥𝗖𝗛   𝟮.𝗜𝗡𝗦𝗘𝗥𝗧   𝟬.𝗕𝗔𝗖𝗞 ➜ ");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.ADMIN_NOTICELIST_DETAIL;
		case 2:
			return View.ADMIN_NOTICE;
		case 0:
			return View.ADMIN_MAIN;
		}
		return View.MAIN;
	}

	public int admin_noticeList_detail() { // 공지사항 자세히 보기

		String admId = (String) AdminService.loginAdmin.get("ADM_ID");

		System.out.println("공지사항번호 ➜ ");
		int noticeNo = ScanUtil.nextInt();
		noticeAdmin = noticeNo;

		Map<String, Object> notice = adminDao.admin_readnotice(admId, noticeNo);

		System.out.println("┌─[" + notice.get("NOTICE_NO") + "]────────────────────────────────────┐");
		System.out.println("  𝗧𝗜𝗧𝗟𝗘\t: " + notice.get("NOTICE_TITLE"));
		System.out.println("  𝗖𝗢𝗡𝗧𝗘𝗡𝗧\t: " + notice.get("NOTICE_CONTENT"));
		System.out.println("  𝗖𝗢𝗡𝗧𝗘𝗡𝗧\t: " + format.format(notice.get("NOTICE_DATE")));
		System.out.println("└───────────────────────────────────────────┘");
		System.out.print(" 𝟭.수정   𝟮.삭제   𝟬.뒤로가기 ➜ ");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.ADMIN_NOTICELIST_UPDATE;
		case 2:
			return View.ADMIN_NOTICELIST_DELETE;
		case 0:
			return View.ADMIN_MAIN;
		}
		return View.ADMIN_MAIN;
	}

	public int admin_notice_insert() { // 공지사항 등록

		String admId = (String) AdminService.loginAdmin.get("ADM_ID");

		System.out.print("𝗡𝗢𝗧𝗜𝗖𝗘 𝗧𝗜𝗧𝗟𝗘 ➜ ");
		String title = ScanUtil.nextLine();
		System.out.print("𝗡𝗢𝗧𝗜𝗖𝗘 𝗖𝗢𝗡𝗧𝗘𝗡𝗧 ➜ ");
		String content = ScanUtil.nextLine();

		int result = adminDao.insertnotice(title, content, admId);

		if (0 < result) {
			System.out.println("𝗡𝗢𝗧𝗜𝗖𝗘 𝗜𝗡𝗦𝗘𝗥𝗧 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗡𝗢𝗧𝗜𝗖𝗘 𝗜𝗡𝗦𝗘𝗥𝗧 𝗙𝗔𝗜𝗟...");
		}

		System.out.print(" 𝟬.뒤로가기 ➜ ");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 0:
			return View.ADMIN_NOTICELIST;
		}

		return View.ADMIN_MAIN;
	}

	public int admin_notice_delete() { // 공지사항 삭제

		String admId = (String) AdminService.loginAdmin.get("ADM_ID");
		System.out.println("공지사항번호 ➜ ");
		int noticeNo = noticeAdmin;

		int result = adminDao.deletenotice(admId, noticeNo);

		if (0 < result) {
			System.out.println("𝗡𝗢𝗧𝗜𝗖𝗘 𝗗𝗘𝗟𝗘𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗡𝗢𝗧𝗜𝗖𝗘 𝗗𝗘𝗟𝗘𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}

		System.out.print(" 𝟬.뒤로가기 ➜ ");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 0:
			return View.ADMIN_NOTICELIST;
		}

		return View.ADMIN_MAIN;
	}

	public int admin_notice_update() { // 공지사항 수정
		System.out.print("수정할 제목 ➜ ");
		String title = ScanUtil.nextLine();
		System.out.print("수정할 내용 ➜ ");
		String content = ScanUtil.nextLine();
		String admId = (String) AdminService.loginAdmin.get("ADM_ID");
		int noticeNo = noticeAdmin;

		int result = adminDao.updatenotice(title, content, admId, noticeNo);

		if (0 < result) {
			System.out.println("𝗡𝗢𝗧𝗜𝗖𝗘 𝗨𝗣𝗗𝗔𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗡𝗢𝗧𝗜𝗖𝗘 𝗨𝗣𝗗𝗔𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}

		System.out.print(" 𝟬.뒤로가기 ➜ ");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 0:
			return View.ADMIN_NOTICELIST;
		}

		return View.ADMIN_MAIN;
	}

	// 관리자:QNA선택 -> QNA 전체리스트 출력
	public int admin_qna() {
		List<Map<String, Object>> qnalist = AdminDao.selectQnaList();

		System.out.println();
		System.out.println("┌─[𝗤&𝗔]───────────────────────────────────────┐");
		System.out.println(" 번호.  상품명\t\t작성일\t\t답변여부");
		for (Map<String, Object> qna : qnalist) {
			System.out.print("  "+qna.get("PR_QNA_NO") + ". (" + qna.get("PR_QNA_TITLE") + ")" + qna.get("PROD_NAME") 
					+ "\t" + qna.get("PR_QNA_DATE") + " \t         ");
			if (qna.get("PR_ANS_YES") == null) {
				System.out.println("X");
			} else {
				System.out.println("O");
			}

			System.out.println();
		}
		System.out.println("└─────────────────────────────────────────────┘");

		System.out.println(" 𝟭.질문번호입력  𝟬.뒤로가기 ➜");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1: // QNA 번호선택
			System.out.print("질문번호 ➜");
			nowQnaNo = ScanUtil.nextInt();
			return View.ADMIN_QNA_SEARCH;
		}

		return View.ADMIN_MAIN; // 상품정보로 뒤로가기
	}

	int nowQnaNo;

	public int adminqnaSearch() { // PR_ANS가 NULL이면 빼고 출력, NULL이 아니면 전체 출력

		int qnaNo = nowQnaNo;
		Map<String, Object> qna = AdminDao.selectQna(qnaNo);

		if (qna.get("PR_ANS") == null) { // 답글이 없는 상태

			System.out.println("┌─["+qna.get("PR_QNA_TITLE")+"]────────────────────────────────────┐");
			System.out.println(" 𝗚𝗥𝗢𝗨𝗣\t: " + qna.get("PR_QNA_TITLE"));
			System.out.println(" 𝗣𝗥𝗢𝗗𝗡𝗢\t: " + qna.get("PROD_NO"));
			System.out.println(" 𝗜𝗗\t: " + qna.get("MEM_ID"));
			System.out.println(" 𝗖𝗢𝗡𝗧𝗘𝗡𝗧\t: " + qna.get("PR_QNA_CONTENT"));
			System.out.println(" 𝗗𝗔𝗧𝗘\t: " + qna.get("PR_QNA_DATE"));
			System.out.println("└───────────────────────────────────────────┘");

			System.out.println(" 𝟭.답글달기  𝟬.뒤로가기 ➜");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.ADMIN_QNA_REPLY;

			}
		} else {
			
			System.out.println("┌─["+qna.get("PR_QNA_TITLE")+"]────────────────────────────────────┐");
			System.out.println(" 𝗚𝗥𝗢𝗨𝗣\t: " + qna.get("PR_QNA_TITLE"));
			System.out.println(" 𝗣𝗥𝗢𝗗𝗡𝗢\t: " + qna.get("PROD_NO"));
			System.out.println(" 𝗜𝗗\t: " + qna.get("MEM_ID"));
			System.out.println(" 𝗖𝗢𝗡𝗧𝗘𝗡𝗧\t: " + qna.get("PR_QNA_CONTENT"));
			System.out.println(" 𝗗𝗔𝗧𝗘\t: " + qna.get("PR_QNA_DATE"));
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - ");
			System.out.println(" 𝗔𝗡𝗦𝗪𝗘𝗥\t : " + qna.get("PR_ANS"));
			System.out.println(" 𝗔𝗡𝗦𝗗𝗔𝗧𝗘\t : " + qna.get("PR_DATE"));
			System.out.println("└───────────────────────────────────────────┘");

			System.out.println(" 𝟭.답글달기  𝟬.뒤로가기 ➜");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.ADMIN_QNA_UPDATE;
			}

		}

		return View.ADMIN_QNA;
	}

	// 답글달기
	public int adminQnaReply() {
		int qnaNo = nowQnaNo;
		System.out.print(" 답글내용 입력 ➜ ");
		String replyContent = ScanUtil.nextLine();
		System.out.print("답글여부(O/X) ➜ ");
		String qnaAnsYes = ScanUtil.nextLine();

		int result = adminDao.adminQnaReply(qnaNo, replyContent, qnaAnsYes);
		return View.ADMIN_QNA_SEARCH;
	}

	// 답글 수정
	public int adminQnaUpdate() {
		int qnaNo = nowQnaNo;
		System.out.print(" 수정답변 입력 ➜ ");
		String replyContent = ScanUtil.nextLine();

		int result = adminDao.adminQnaUpdate(qnaNo, replyContent);

		if (0 < result) {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗦𝗨𝗖𝗖𝗘𝗦𝗦!");
		} else {
			System.out.println("𝗨𝗣𝗗𝗔𝗧𝗘 𝗙𝗔𝗜𝗟...");
		}
		return View.ADMIN_QNA_SEARCH;
	}

	List<Map<String, Object>> memberlist = adminDao.selectMemevent();

	// 2. 회원이벤트
	public int admin_memevent() {
		System.out.println("┌─[𝗠𝗘𝗠𝗕𝗘𝗥]───────────────────────────────────┐");
		System.out.println("  𝗠𝗘𝗠𝗕𝗘𝗥 𝗜𝗗\t\t𝗧𝗢𝗧𝗔𝗟 𝗣𝗥𝗜𝗖𝗘");
		for (Map<String, Object> mem : memberlist) {
			System.out.println("  "+mem.get("MEM_ID") + "\t\t" + mem.get("MEM_SPAY")+"원");
		}
		System.out.println("└────────────────────────────────────────────┘");

		System.out.println("1.등급/쿠폰관리  0.뒤로가기");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.ADMIN_GRADE;
		}
		return View.ADMIN_MAIN;
	}

	// 2. 회원이벤트 > 1.등급/쿠폰 부여
		public int admin_grade() {
			String a = null;
			
			List<Map<String, Object>> memberlist = adminDao.selectMemevent();
			for (Map<String, Object> mem : memberlist) {
				a = String.valueOf(mem.get("MEM_ID"));
				GradeUtil.grc(a);
			}
			
			List<Map<String, Object>> memberAll = adminDao.selectMemberAll(); //모든 회원 아이디
			List<Map<String, Object>> memberHaveCop = adminDao.selectHaveCop(); //쿠폰을 가진 회원 아이디
			
			for (int i = 0; i < memberAll.size(); i++) {
				for(int j = 0; j < memberHaveCop.size(); j++) {
					if(memberAll.get(i).get("MEM_ID") != memberHaveCop.get(j).get("MEM_ID")) {
						CouponUtil.crc(String.valueOf(memberAll.get(i).get("MEM_ID")));
					}
				}
			}

			List<Map<String, Object>> memberGradelist = adminDao.selectGrade();
			System.out.println("\n┌────────────────────────────────────┐");
			System.out.println("  𝗠𝗘𝗠𝗕𝗘𝗥 𝗜𝗗\t\t𝗚𝗥𝗔𝗗𝗘");
			for (Map<String, Object> mem : memberGradelist) {
				System.out.println("  "+mem.get("MEM_ID") + "\t\t" + mem.get("MEM_GRADE"));
			}
			System.out.println("└────────────────────────────────────┘");
			System.out.println("등급 변경과 쿠폰 지급 완료!\n");

			int result = adminDao.deleteCoupon();
			if (0 < result) {
				System.out.println("사용기한이 지난 쿠폰 삭제완료...");
			}
			System.out.println();

			return View.ADMIN_MEMEVENT;
		}

	// 2. 회원이벤트 > 2.적립금 부여
//	public int admin_mile() {
//		for (Map<String, Object> mem : memberlist) {
//			int memSpay = Integer.parseInt(mem.get("MEM_SPAY").toString());
//			if (memSpay >= 100000) {
//				int mile1 = memSpay / 100000;
//				int mile2 = mile1 * 1000;
//				Map<String, Object> memberMile = adminDao.selectMile((String) mem.get("MEM_ID"));
//				String memId = String.valueOf(memberMile.get("MEM_ID"));
//				int mileBefore = Integer.parseInt(memberMile.get("MEM_MILE").toString());
//
//				int memMile = mileBefore + mile2;
//				int result = adminDao.updateMile(Integer.toString(memMile), memId);
//
//				if (0 < result) {
//					System.out.println("\n" + mem.get("MEM_ID") + " / 변경된 적립금 : " + memberMile.get("MEM_MILE"));
//					System.out.println("-----------------------------------");
//				}
//			}
//			memSpay = 0;
//		}
//
//		return View.ADMIN_MAIN;
//	}

}
