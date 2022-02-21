package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dao.MemberDao;
import util.CouponUtil;
import util.GradeUtil;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class MemberService {

	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private MemberService() {
		// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static MemberService instance; // 객체를 보관할 변수
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	public static MemberService getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new MemberService(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}
	
	public static Map<String, Object> loginMember; // 로그인된 사용자 정보 저장

	private MemberDao memberDao = MemberDao.getInstance(); // 멤버다오의 객체
	
	public static Map<String, Object> qnaMember; // 질문작성한 사용자 정보 저장
	
	// 회원가입
	public int join() {
		// 등급, 마일리지는 관리자가 부여
		System.out.println("╔════════════════ 𝗝𝗢𝗜𝗡 ➰ ════════════════╗");
		boolean flag;
		
		// 아이디 중복 확인, 유효성 검사
		String mem_id;
		do {
			do {
				flag = true;
				System.out.print(" 𝕀𝔻 (5~20자의 영문 소문자, 숫자) ⏩ ");
				mem_id = ScanUtil.nextLine();
				
				String str = mem_id; 
				String regex = "^\\w{5,20}$";
				
				if(Pattern.matches(regex, str)) {
					System.out.println(" 🆗 아이디 유효성 검사 확인❕\n");
				} else {            
					System.out.println(" 🆖 형식에 맞지 않습니다. 다시 입력해주세요❕❕\n");
					flag = false;
				}
			}while (!flag);
			
		}while (!idCheck(mem_id));
		System.out.print(" ℕ𝔸𝕄𝔼 ⏩ ");
		String mem_name = ScanUtil.nextLine();

		// 비밀번호 확인, 유효성 검사
		String mem_pass;
		String mem_pass1;
		do {
			do {
				flag = true;
				System.out.print(" ℙ𝔸𝕊𝕊𝕎𝕆ℝ𝔻 (5~20자의 영문 소문자, 숫자) ⏩ ");
				mem_pass = ScanUtil.nextLine();
				
				String str = mem_pass; 
				String regex = "^\\w{5,20}$";
				
				if(Pattern.matches(regex, str)) {
					System.out.println(" 🆗 비밀번호 유효성 검사 확인❕\n");
				} else {            
					System.out.println(" 🆖 형식에 맞지 않습니다. 다시 입력해주세요❕❕\n");
					flag = false;
				}
			}while (!flag);
			
			System.out.print(" ℙ𝔸𝕊𝕊𝕎𝕆ℝ𝔻 (재입력) ⏩ ");
			mem_pass1 = ScanUtil.nextLine();

			if (mem_pass.equals(mem_pass1)) {
				System.out.println(" 🆗 비밀번호가 확인❕\n");
			} else {
				System.out.println(" 🆖 비밀번호를 다시 입력해주세요❕❕\n");
			}
		} while (!(mem_pass.equals(mem_pass1)));
		
		System.out.print(" 𝔸𝔻𝔻ℝ𝔼𝕊𝕊 ⏩"); 
		String mem_addr = ScanUtil.nextLine();
		
		// 생년월일 유효성 검사
		String mem_bir;
		do {
        	flag = true;
        	System.out.print(" 𝔹𝕀ℝ𝕋ℍ (0000/00/00) ⏩ "); 
        	mem_bir = ScanUtil.nextLine();
        	
        	String str = mem_bir; 
        	String regex = "^[0-9]{4}/[0-9]{2}/[0-9]{2}$";
        	
        	if(Pattern.matches(regex, str)) {
        		System.out.println(" 🆗 생년월일 확인되었습니다❕\n");
//        		System.out.println(mem_bir);
        	} else {            
        		System.out.println(" 🆖 형식에 맞지 않습니다. 다시 입력해주세요❕❕\n");
        		flag = false;
        	}
        }while (!flag);
		
        System.out.print(" 𝔹𝔸ℕ𝕂ℕ𝔸𝕄𝔼 ⏩ ");
        String mem_bank = ScanUtil.nextLine();
        
        // 계좌번호 유효성 검사
        String mem_acc;
        do {
        	flag = true;
        	System.out.print(" 𝔸ℂℂ𝕆𝕌ℕ𝕋 ℕ𝕌𝕄𝔹𝔼ℝ (10~15자리 이내 숫자만 입력) ⏩ ");
        	mem_acc = ScanUtil.nextLine();
        	
        	String str = mem_acc; 
        	String regex = "^[0-9]{10,15}$";
    		
        	if(Pattern.matches(regex, str)) {
        		System.out.println(" 🆗 계좌번호 확인되었습니다❕\n");
//        		System.out.println(mem_acc);
        	} else {            
        		System.out.println(" 🆖 형식에 맞지 않습니다. 다시 입력해주세요❕❕\n");
        		flag = false;
        	}
        }while (!flag);
        
        
        Map<String, Object> param = new HashMap<String, Object>();
		param.put("MEM_ID", mem_id);
		param.put("MEM_NAME", mem_name);
		param.put("MEM_PASS", mem_pass);
		param.put("MEM_ADDR", mem_addr);
		param.put("MEM_BIR", mem_bir);
		param.put("MEM_GRADE", "MEMBER");
		param.put("MEM_MILE", 1000);
		param.put("MEM_BANK", mem_bank);
		param.put("MEM_ACC", mem_acc);

//		CouponUtil.crc(mem_id);
		
		int result = memberDao.insertMember(param); // 영향을 받은 행의 수 리턴받음 
		// 같은 내용(insertMember)을 다른 화면에서도 사용할 수 있기 때문에 db에 접속하는 내용을 다른 클래스(Dao)로 별도 생성

		if (0 < result) {
			System.out.println(" 🆗 회원가입 성공❕\n");
		} else {
			System.out.println(" 🆖 회원가입 실패❕❕\n");
		}
		
		Map<String, Object> param2 = new HashMap<String, Object>();
	      param2.put("MEM_ID", mem_id);
//	      memberDao.insertHaveCoupon(param2);
	      int res = memberDao.insertHaveCoupon(param2); // 영향을 받은 행의 수 리턴받음 
	      // 같은 내용(insertMember)을 다른 화면에서도 사용할 수 있기 때문에 db에 접속하는 내용을 다른 클래스(Dao)로 별도 생성
	      
	      if (0 < res) {
	         System.out.println(" 🆗 쿠폰지급 완료❕\n");
	      } else {
	         System.out.println(" 🆖 쿠폰지급 실패❕❕\n");
	      }
//	      CouponUtil.crc(mem_id);


		return View.HOME;
	
	}
           
	// 아이디 중복 확인 메서드
	public boolean idCheck(String mem_id) {
		boolean check = true;
		
		Map<String, Object> member = memberDao.selectMember_id(mem_id);
		
		if (member != null) {
			System.out.println(" 🆖 아이디가 중복되므로 다시 입력해주세요❕❕\n");
			check = false;
		}else {
			System.out.println(" 🆗 아이디 중복 확인 통과❕\n");
		}
		return check;
	}
	
	// 로그인 메서드
	public int login() {
		System.out.println();
		System.out.println("╔════════════════ 𝗟𝗢𝗚𝗜𝗡 ✅ ═════════════╗");
		System.out.print("           𝕀𝔻     ⏩ ");
		String mem_mid = ScanUtil.nextLine();
		System.out.print("        ℙ𝔸𝕊𝕊𝕎𝕆ℝ𝔻 ⏩ ");
		String mem_mpass = ScanUtil.nextLine();
		System.out.println("════════════════════════════════════════");
		
		// 멤버테이블에서 입력받은 아이디, 비번과 일치하는 사람을 찾으면 로그인
		// 사용자가 입력한 아이디, 비번을 가지고 selectMember를 실행한 값을 리턴받아 member에 저장
		Map<String, Object> member = memberDao.selectMember(mem_mid, mem_mpass);
		
		if(member == null) {
			System.out.println("      ∧__∧");
			System.out.println("     ( ｀Д´）");
			System.out.println("    (っ▄︻▇〓┳═☆  로그인 실패❕");
			System.out.println("     /　　 )");
			System.out.println("    ( /￣∪");

		} else {
			System.out.println("  　 ♡ ♡ ᕬ ᕬ ♡ ♡");
			System.out.println(" 　 + ♡ ( ⌯′-′⌯) ♡ +");
			System.out.println(" ┏━♡━ U U━━♡━┓");
			System.out.println("  ♡　    로그인 성공　  ♡ ");
			System.out.println(" ┗━♡━━━━━♡━┛");
			System.out.println();
			loginMember = member; // 로그인 상태이기 때문에 로그인한 사용자정보 담아두기
			// (글 작성이나 수정 시 자신의 아이디에 맞는 것을 가져와야 하기 때문에 그때 사용하기 위해 담아두는 것)
			return View.MAIN; // 로그인에 성공하면 게시판이 나오게 설정
		}
		
		return View.LOGIN; // 로그인에 실패하면 다시 로그인을 할 수 있게 로그인 화면을 리턴
	}
	
	// 로그아웃
	public int logout() {
		System.out.print(" 로그아웃 하시겠습니까? (𝙮/𝙣) ⏩");
		String yn = ScanUtil.nextLine();

		if (yn.equals("y")) {
			loginMember = null;
			return View.HOME;
		} else if (yn.equals("n")){
			return View.MAIN;
		}
		
		return View.HOME;
	}

	// 내 정보 조회
    public int myInfo() {
		
		String memId = (String) MemberService.loginMember.get("MEM_ID");

		Map<String, Object> member = memberDao.selectMemberList(memId);
		//𝗡𝗔𝗠𝗘 𝗔𝗗𝗗𝗥𝗘𝗦𝗦 𝗕𝗜𝗥𝗧𝗛 𝗔𝗖𝗖𝗢𝗨𝗡𝗧𝗡𝗨𝗠𝗕𝗘𝗥 𝗕𝗔𝗡𝗞𝗡𝗔𝗠𝗘 𝗜𝗗 𝗣𝗔𝗦𝗦𝗪𝗢𝗥𝗗 𝗘𝗗𝗜𝗧 𝗠𝗬 𝗜𝗡𝗙𝗢
		System.out.print("           𝕀𝔻     ⏩ ");
		String mem_mid = ScanUtil.nextLine();
		System.out.print("        ℙ𝔸𝕊𝕊𝕎𝕆ℝ𝔻 ⏩ ");
		String mem_mpass = ScanUtil.nextLine();
		System.out.println();
		System.out.println("╔════════════════ 𝗠𝗬 𝗜𝗡𝗙𝗢 👨‍🦲 ════════════════╗");
		System.out.println("   𝗡𝗔𝗠𝗘\t\t : " + member.get("MEM_NAME"));
		System.out.println("   𝗜𝗗\t\t : " + member.get("MEM_ID"));
		System.out.println("   𝗣𝗔𝗦𝗦𝗪𝗢𝗥𝗗\t : " + member.get("MEM_PASS"));
		System.out.println("   𝗕𝗜𝗥𝗧𝗛\t\t : " + format.format(member.get("MEM_BIR")));
		System.out.println("   𝗔𝗗𝗗𝗥𝗘𝗦𝗦\t\t : " + member.get("MEM_ADDR"));
		System.out.println("   𝗕𝗔𝗡𝗞𝗡𝗔𝗠𝗘\t : " + member.get("MEM_BANK"));
		System.out.println("   𝗔𝗖𝗖𝗢𝗨𝗡𝗧 𝗡𝗨𝗠𝗕𝗘𝗥\t : " + member.get("MEM_ACC"));
		System.out.println("════════════════════════════════════════════");
		
		System.out.print(" 1.𝗨𝗣𝗗𝗔𝗧𝗘   0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		System.out.println("•·······················•·······················•");
		switch (input) {
		case 1: return View.INFO_UPDATE;
		case 0: return View.MY_PAGE;
		}	
		
		return View.MAIN;
	}
    
    // 내 정보 수정
    public int infoUpdate() { 
		System.out.print(" 𝗔𝗗𝗗𝗥𝗘𝗦𝗦         ⏩ ");
		String addr = ScanUtil.nextLine();
		System.out.print(" 𝗕𝗔𝗡𝗞𝗡𝗔𝗠𝗘       ⏩ ");
		String bank = ScanUtil.nextLine();
		System.out.print(" 𝗔𝗖𝗖𝗢𝗨𝗡𝗧 𝗡𝗨𝗠𝗕𝗘𝗥 ⏩ ");
		Integer acc = ScanUtil.nextInt();
		String memId = (String) MemberService.loginMember.get("MEM_ID");
		
		int result = memberDao.updateBoard(addr, bank, acc, memId);
		
		if (0 < result) {
			System.out.println(" ┌───────────────┐");
			System.out.println("  내 정보 수정 완료!");
			System.out.println(" └───────────────┘");
			System.out.println("　 　ᕱ ᕱ ||");
			System.out.println("　  ( ･ω･ ||");
			System.out.println("　  /　つ Φ");
		} else {
			System.out.println(" ＿人人人人人人人人人人＿");
			System.out.println(" ＞ 내 정보 수정 실패! ＜");
			System.out.println("　　 　┐( ∵ )┌");
			System.out.println("　 　　 ( 　) 　");
			System.out.println("　　 　　┘| ");
		}
		
		return View.MY_INFO;
	}
    
    
    // 내 정보 > 등급 조회
    public int myGrade() {
		
    	String memId = (String) MemberService.loginMember.get("MEM_ID");

		Map<String, Object> member = memberDao.selectMemberList(memId);
		
		System.out.println("╔════════════════ 𝗠𝗬 𝗚𝗥𝗔𝗗𝗘 💎 ════════════════╗");
		System.out.println("\t       " + member.get("MEM_GRADE"));
		System.out.println("═══════════════════════════════════════════════");
		System.out.println("   💎 𝗠𝗘𝗠𝗕𝗘𝗥 : 처음 가입시 부여되는 등급");
		System.out.println("   💎 𝗕𝗥𝗢𝗡𝗭𝗘 : 총 결제액이 200,000원 이상일 경우");
		System.out.println("   💎 𝗦𝗜𝗟𝗩𝗘𝗥 :  총 결제액이 500,000원 이상일 경우");
		System.out.println("   💎 𝗚𝗢𝗟𝗗 : 총 결제액이 1,000,000원 이상일 경우");
		System.out.println("══════════════════════════════════════════════");
		System.out.print(" 1.𝗠𝗜𝗟𝗘𝗔𝗚𝗘 💰   2.𝗖𝗢𝗨𝗣𝗢𝗡 🏷   0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.MY_MILE;
		case 2: return View.MY_COUPON;
		case 0: return View.MY_PAGE;
		}	
		
		return View.MAIN;
	}
    
    // 내 정보 > 적립금 조회
    public int myMile() {
		
    	String memId = (String) MemberService.loginMember.get("MEM_ID");

		Map<String, Object> member = memberDao.selectMemberList(memId);
		
		System.out.println("╔═══════════════ 𝗠𝗬 𝗠𝗜𝗟𝗘𝗔𝗚𝗘 💰 ═══════════════╗");
		System.out.println("\t       " + member.get("MEM_MILE") + "원");
		System.out.println("═════════════════════════════════════════════");
		System.out.println("       💰 결제액 10만원당 1000원씩 적립");
		System.out.println("═════════════════════════════════════════════");
		
		System.out.print(" 1.𝗖𝗢𝗨𝗣𝗢𝗡   0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.MY_COUPON;
		case 0: return View.MY_PAGE;
		}	
		
		return View.MAIN;
	}
    
    // 내 정보 > 쿠폰 조회
    public int myCoupon() {
		
    	String memId = (String) MemberService.loginMember.get("MEM_ID");

		Map<String, Object> coupon = memberDao.selectCoupon(memId);
		System.out.println("╔═══════════════ 𝗠𝗬 𝗖𝗢𝗨𝗣𝗢𝗡 🏷 ═══════════════╗");
		if (coupon != null) {
		System.out.println(" 쿠폰사용기한 : " + format.format(coupon.get("COP_DATE")));
		if (coupon.get("COUPON_NAME").equals("MEMBER COUPON")) {
			System.out.println(" 쿠폰이름\t : " + coupon.get("COUPON_NAME") + " 가입축하 1% 쿠폰");
		} else if (coupon.get("COUPON_NAME").equals("BRONZE COUPON")) {
			System.out.println(" 쿠폰이름\t : " + coupon.get("COUPON_NAME") + " 결제액의 5% 쿠폰"); 
		} else if (coupon.get("COUPON_NAME").equals("SILVER COUPON")) {
			System.out.println(" 쿠폰이름\t : " + coupon.get("COUPON_NAME") + " 결제액의 10% 쿠폰");
		} else if (coupon.get("COUPON_NAME").equals("GOLD COUPON")) {
			System.out.println(" 쿠폰이름\t : " + coupon.get("COUPON_NAME") + " 결제액의 15% 쿠폰");
		}
		} else if (coupon == null) {
			System.out.println("          보유한 쿠폰이 없습니다!");
		}
		
		System.out.println("═══════════════════════════════════════════");
		System.out.println("   💎 𝗠𝗘𝗠𝗕𝗘𝗥 : 결제액의 1% 쿠폰");
		System.out.println("   💎 𝗕𝗥𝗢𝗡𝗭𝗘 : 결제액의 5% 쿠폰");
		System.out.println("   💎 𝗦𝗜𝗟𝗩𝗘𝗥 : 결제액의 10% 쿠폰");
		System.out.println("   💎 𝗚𝗢𝗟𝗗 : 결제액의 15% 쿠폰");
		System.out.println("═══════════════════════════════════════════");

		
		System.out.print(" 1.𝗠𝗜𝗟𝗘𝗔𝗚𝗘   0.𝗕𝗔𝗖𝗞 ⏩");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.MY_MILE;
		case 0: return View.MY_PAGE;
		}	
		
		return View.MAIN;
	}

    
//    List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
    // 주문 내역
//    public int myOrderList() { 
//    	String memId = (String) MemberService.loginMember.get("MEM_ID");
//
//		orderList = memberDao.selectOrderList(memId);
//		
//		System.out.println("----------------------------- 주문 내역 --------------------------");
//		System.out.println("  주문번호\t          주문날짜\t          상품이름\t       수량        금액");
//		System.out.println("---------------------------------------------------------------");
//		for(int i = orderList.size() - 1; i >= 0; i--) {
//	    	Map<String, Object> order = orderList.get(i);
//	    	System.out.println(order.get("ORD_NO")+"\t"+format.format(order.get("ORD_DATE"))+"\t"+order.get("PROD_NAME")
//	    	+"\t"+order.get("DEORDER_QTY")+"\t"+order.get("PROD_COST")+"원");
//	    }
//		System.out.println("---------------------------------------------------------------");
//		
//		System.out.print("1. 상세보기   0. 뒤로가기>"); 
//		int input = ScanUtil.nextInt();
//		switch (input) {
//		case 1: return View.DE_ORDERLIST;
//		case 0: return View.MY_PAGE;
//		}	
//		return View.MAIN;  	
//    }
    
    // 주문 내역 상세보기
//	public int deOrderList() { 
//		String memId = (String) MemberService.loginMember.get("MEM_ID");
//
//		System.out.print("조회하고 싶은 주문번호>");
//		String orderdeNo = ScanUtil.nextLine(); // 수정
//
//		List<Map<String, Object>> orderdeList = memberDao.selectdeOrderList(memId, orderdeNo);
////		int result = memberDao.updateOrder(memId, orderdeNo);
//
//		System.out.println("----------------- 주문서 ------------------");
//		for (int i = orderdeList.size() - 1; i >= 0; i--) {
//			Map<String, Object> orderde = orderdeList.get(i);
//			System.out.println("상품번호\t: " + orderde.get("PROD_NO"));
//			System.out.println("주문날짜\t: " + format.format(orderde.get("ORD_DATE")));
//			System.out.println("상품이름\t: " + orderde.get("PROD_NAME"));
//			System.out.println("상품색상\t: " + orderde.get("PROD_COLOR"));
//			System.out.println("사이즈\t: " + orderde.get("PROD_SIZE"));
//			System.out.println("수량\t: " + orderde.get("DEORDER_QTY"));
//			System.out.println("금액\t: " + orderde.get("PROD_COST") + "원");
//			System.out.println("-----------------------------------------");
//		}
//				
//		System.out.print("1. 리뷰작성    2. 환불    0. 뒤로가기>"); // 추가
//		int input = ScanUtil.nextInt();
//		switch (input) {
//		case 1:
//			return View.REVIEW;
//		case 2: 
////			return View.
//		case 0:
//			return View.MY_ORDERLIST;
//		}
//		return View.MAIN;
//	}
    
    
    // 주문내역 > 1.리뷰 > 1.리뷰 작성
	public int review() {
		String memId = (String) MemberService.loginMember.get("MEM_ID");
		System.out.print(" 리뷰를 작성할 상품번호 입력 ⏩");
		String prodNo = ScanUtil.nextLine();
		System.out.print(" 리뷰제목 ⏩");
		String revTitle = ScanUtil.nextLine();
		System.out.print(" 리뷰내용 ⏩");
		String revContent = ScanUtil.nextLine();
		System.out.print(" 별점    ⏩");
		Integer revStar = ScanUtil.nextInt();
		
		int result = memberDao.insertReview(revTitle, revContent, revStar, memId, prodNo);
		
		if(0 < result) {
			System.out.println(" ┌───────────────┐");
			System.out.println("   리뷰 등록 완료!");
			System.out.println(" └───────────────┘");
			System.out.println("　 　ᕱ ᕱ ||");
			System.out.println("　  ( ･ω･ ||");
			System.out.println("　  /　つ Φ");
		}else {
			System.out.println(" ＿人人人人人人人人人人＿");
			System.out.println("  ＞ 리뷰 등록 실패! ＜");
			System.out.println("　　 　┐( ∵ )┌");
			System.out.println("　 　　 ( 　) 　");
			System.out.println("　　  　┘| ");
		}
		
		return View.MY_PAGE;
	}  
	
	
	// 마이페이지 > 4.내가쓴리뷰 (1.상세보기  0.뒤로가기)
	public int myReview() {
//		String memId = (String) MemberService.loginMember.get("MEM_ID");

		System.out.println("╔═══════════════ 𝗠𝗬 𝗥𝗘𝗩𝗜𝗘𝗪📝 ═══════════════╗");
		System.out.println("     1.𝗥𝗘𝗩𝗜𝗘𝗪 𝗟𝗜𝗦𝗧 📄  0.𝗕𝗔𝗖𝗞 ⏩");
		System.out.println("════════════════════════════════════════════");
		
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.MY_REVIEW_DETAIL;
		case 0: return View.MY_PAGE;
		}
		
		return View.MAIN;
	}
	
	// 마이페이지 > 4.내가쓴리뷰 > 1. 리뷰 목록보기
	public int myReview_detail() {
		
		String memId = (String) MemberService.loginMember.get("MEM_ID");
//		Integer revNo = (Integer) MemberService.currentRevNo.get("REV_NO");
		List<Map<String, Object>> review = memberDao.selectReviewList(memId);
		
		System.out.println("╔═══════════════ 𝗠𝗬 𝗥𝗘𝗩𝗜𝗘𝗪📝 ═══════════════╗");
		for(int i = review.size() - 1; i >= 0; i--) {
//			System.out.println("-------------------------------------");
			System.out.println("  리뷰번호\t상품명\t리뷰제목");
			System.out.println(review.get(i).get("REV_NO")+"\t"+review.get(i).get("PROD_NAME")+"\t"+review.get(i).get("REV_TITLE"));
		}
		System.out.println("═══════════════════════════════════════════");
		
		
		System.out.println(" 1.𝗗𝗘𝗧𝗔𝗜𝗟   0.𝗕𝗔𝗖𝗞 ⏩");
		int input2 = ScanUtil.nextInt();
		
		switch (input2) {
		case 1: 
			return View.MY_REVIEW_DDETAIL;
		case 0: 
			return View.MY_PAGE;
		}
		
		return View.MAIN;
	}
	
	// 마이페이지 > 4.내가쓴리뷰 > 1. 리뷰 목록보기 > 1.상세보기
	public int myReview_ddetail() {
		
		String memId = (String) MemberService.loginMember.get("MEM_ID");
		
		System.out.print(" 자세히 볼 리뷰 번호 입력 ⏩");
		int revNo = ScanUtil.nextInt();
		
		Map<String, Object> review = memberDao.selectReviewdList(memId, revNo);
		
			System.out.println("╔═════════════ 𝗥𝗘𝗩𝗜𝗘𝗪 𝗗𝗘𝗧𝗔𝗜𝗟 📄 ════════════╗");
			System.out.println(" 리뷰번호\t : " + review.get("REV_NO"));
			System.out.println(" 상품번호\t : " + review.get("PROD_NO"));
			System.out.println(" 상품명\t : " + review.get("PROD_NAME"));
			System.out.println(" 리뷰제목\t : " + review.get("REV_TITLE"));
			System.out.println(" 리뷰내용\t : " + review.get("REV_CONTENT"));
			System.out.print(" 별점\t : " + review.get("REV_STAR") + "\t");
			//오브젝트 타입을 int로 형변환하려면 toString으로 문자열타입으로 만들어준뒤 Integer.parseInt를 써줘야함
			int star = Integer.parseInt(review.get("REV_STAR").toString());
			for(int i = 0; i < star; i++) {
				System.out.print("*");
			}System.out.println();
			System.out.println(" 작성일자\t : " + format.format(review.get("REV_DATE")));
			System.out.println("═════════════════════════════════════════");
			
		System.out.print(" 1.𝗨𝗣𝗗𝗔𝗧𝗘   2.𝗗𝗘𝗟𝗘𝗧𝗘   0.𝗕𝗔𝗖𝗞 ⏩");
		int input2 = ScanUtil.nextInt();
		
		switch (input2) {
		case 1: 
			return View.MY_REVIEW_UPDATE;
		case 2: 
			return View.MY_REVIEW_DELETE;
		case 0: 
			return View.MY_REVIEW_DETAIL;
		}
		
		return View.MY_PAGE;
	}
	
	// 마이페이지 > 4.내가쓴리뷰 > 1.수정
	public int myReview_update() {
		String memId = (String) MemberService.loginMember.get("MEM_ID");
		
//		System.out.print("수정하려는 리뷰의 번호를 입력하세요.>");
//		Integer cur_RevNo = ScanUtil.nextInt();
		
		System.out.print(" 리뷰제목 ⏩");
		String revTitle = ScanUtil.nextLine();
		System.out.print(" 리뷰내용 ⏩");
		String revContent = ScanUtil.nextLine();
		System.out.print(" 별점    ⏩");
		Integer revStar = ScanUtil.nextInt();
		
		int result = memberDao.updateReview(revTitle, revContent, revStar, memId);
		
		if (0 < result) {
			System.out.println("  ┌───────────────┐");
			System.out.println("    리뷰 수정 완료!");
			System.out.println("  └───────────────┘");
			System.out.println("　  　ᕱ ᕱ ||");
			System.out.println("　   ( ･ω･ ||");
			System.out.println("　   /　つ Φ");
		} else {
			System.out.println("  ＿人人人人人人人人人人＿");
			System.out.println("   ＞ 리뷰 수정 실패! ＜");
			System.out.println("　　  　┐( ∵ )┌");
			System.out.println("　 　 　 ( 　) 　");
			System.out.println("　　   　　┘| ");
		}
		
		return View.MY_REVIEW;
	}
	
	// 마이페이지 > 4.내가쓴리뷰 > 2.삭제
	public int myReview_delete() {
		String memId = (String) MemberService.loginMember.get("MEM_ID");
//		Map<String, Object> member = memberDao.deleteReview(memId);
		
//		System.out.print("삭제하려는 리뷰의 번호를 입력하세요.>");
//		Integer cur_RevNo = ScanUtil.nextInt();
		
		System.out.print(" 정말 삭제하시겠습니까? (𝙮/𝙣) ⏩");
		String yn = ScanUtil.nextLine();
		
		if (yn.equals("y")) {
			int result = memberDao.deleteReview(memId);
			if (0 < result) {
				System.out.println(" ┌───────────────┐");
				System.out.println("   리뷰 삭제 완료!");
				System.out.println(" └───────────────┘");
				System.out.println("　 　ᕱ ᕱ ||");
				System.out.println("　  ( ･ω･ ||");
				System.out.println("　  /　つ Φ");
			} else {
				System.out.println("  ＿人人人人人人人人人人＿");
				System.out.println("   ＞ 리뷰 삭제 실패! ＜");
				System.out.println("　　  　┐( ∵ )┌");
				System.out.println("　 　 　 ( 　) 　");
				System.out.println("　　   　　┘| ");
			}
		}
		return View.MY_REVIEW;
	}
	
	public int payList() {
		String memId = (String) MemberService.loginMember.get("MEM_ID");
		
//		List<Map<String, Object>> payList = memberDao.selectordNo(memId);
		List<Map<String, Object>> priceList = memberDao.selectordPrice(memId);
		
		System.out.println("═══════════════════════════════════════════");
		System.out.println("  결제번호           결제일           총금액        총결제액");
		System.out.println("═══════════════════════════════════════════");
		for(Map<String, Object> price:priceList) {
			System.out.println(price.get("ONO") + "\t" + format.format(price.get("PDATE")) + "\t" +
		                       price.get("PPRI")  + "원\t     " + price.get("OPRI") + "원");
			System.out.println("═══════════════════════════════════════════");
		}
		
		System.out.print(" 1.𝗥𝗘𝗖𝗘𝗜𝗣𝗧   0.𝗕𝗔𝗖𝗞 ⏩");
		int input2 = ScanUtil.nextInt();
		
		switch (input2) {
		case 1: 
			return View.DE_ORDERLIST;
		case 0: 
			return View.MY_PAGE;
		}
	
		return View.MAIN;
	}
	
	public int deOrderList() { 
		String memId = (String) MemberService.loginMember.get("MEM_ID");

		System.out.print(" 조회하고 싶은 결제번호 ⏩");
		String orderdeNo = ScanUtil.nextLine(); // 수정

		List<Map<String, Object>> payList = memberDao.selectprodNo(memId, orderdeNo);
		List<Map<String, Object>> priceList = memberDao.selectordPay(memId, orderdeNo);
		System.out.println("  ╔══════════════ 𝗥𝗘𝗖𝗘𝗜𝗣𝗧 🧾 ═════════════╗");
		for (int i = payList.size() - 1; i >= 0; i--) {
			Map<String, Object> orderde = payList.get(i);
			System.out.println(" 상품번호\t: " + orderde.get("PROD_NO"));
			System.out.println(" 주문날짜\t: " + orderde.get("PAY_DATE"));
			System.out.println(" 상품이름\t: " + orderde.get("PROD_NAME"));
			System.out.println(" 상품색상\t: " + orderde.get("PROD_COLOR"));
			System.out.println(" 사이즈\t: " + orderde.get("PROD_SIZE"));
			System.out.println(" 수량\t: " + orderde.get("DEORDER_QTY"));
			System.out.println(" 금액\t: " + orderde.get("PROD_COST") + "원");
			System.out.println("  ═════════════════════════════════════════");
		}
		
		for(Map<String, Object> price:priceList) {
		   if(price.get("MEM_COUPON") != null) {
			System.out.println(" 총금액\t: " + price.get("ORD_PRICE") + "원");
			if (price.get("MEM_COUPON").equals("MEMBER")) {
				System.out.println(" 쿠폰사용\t: " + price.get("MEM_COUPON") + " 가입축하 1% 쿠폰");
			} else if (price.get("MEM_COUPON").equals("BRONZE")) {
				System.out.println(" 쿠폰사용\t: " + price.get("MEM_COUPON") + " 결제액의 5% 쿠폰"); 
			} else if (price.get("MEM_COUPON").equals("SILVER")) {
				System.out.println(" 쿠폰사용\t: " + price.get("MEM_COUPON") + " 결제액의 10% 쿠폰");
			} else if (price.get("MEM_COUPON").equals("GOLD")) {
				System.out.println(" 쿠폰사용\t: " + price.get("MEM_COUPON") + " 결제액의 15% 쿠폰");
			}
			System.out.println(" 적립금사용\t: " + (price.get("MEM_MILE")) + "원");
			System.out.println(" 총결제액\t: " + price.get("PAY_PRICE") + "원");
			System.out.println("═════════════════════════════════════════");
		   } else if(price.get("MEM_COUPON") == null) { // 쿠폰 or 적립금 사용 안할때
			  System.out.println(" 적립금사용\t: " + (price.get("MEM_MILE")) + "원");
			  System.out.println(" 총결제액\t: " + price.get("PAY_PRICE") + "원");
			  System.out.println("═════════════════════════════════════════"); 
		   }
		}
		   
		
		System.out.print(" 1.𝗥𝗘𝗩𝗜𝗘𝗪 𝗨𝗣𝗗𝗔𝗧𝗘   2.𝗥𝗘𝗙𝗨𝗡𝗗   0.𝗕𝗔𝗖𝗞 ⏩"); // 추가
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.REVIEW;
		case 2: 
			return View.PAY_REFUND;
		case 0:
			return View.MY_ORDERLIST;
		}
		return View.MAIN;
	}
	
	public int payRefund() {
		String memId = (String) MemberService.loginMember.get("MEM_ID");


		System.out.print(" 환불하실 결제번호를 입력하세요 ⏩");
		String payOrdNo = ScanUtil.nextLine();
		//결제내역에서 선택한 내역 받아오기
		Map<String, Object> re = memberDao.selectRefund(payOrdNo);
		System.out.println(" 환불 금액   : " + re.get("PAY_PRICE"));
		System.out.println(" 환불 적립금 : " + re.get("MEM_MILE"));
		
		System.out.print(" 정말 환불 하시겠습니까? (𝙮/𝙣) ⏩");
		String yn = ScanUtil.nextLine();
		
		Map<String, Object> mem = memberDao.selectMem(memId); //로그인 멤버 모든 정보 
//		List<Map<String, Object>> memCoup = memberDao.myCouponList(memId);//로그인한 멤버의 보유쿠폰
		
		int zz = Integer.parseInt(mem.get("MEM_SPAY").toString());
		int yy = Integer.parseInt(re.get("PAY_PRICE").toString());
		int memm = Integer.parseInt(mem.get("MEM_MILE").toString()); //결제 후 결제 금액에 따라서 적립된 적립금
		int rem = Integer.parseInt(re.get("MEM_MILE").toString()); //사용된 적립금
		
		if(yn.equals("y")) {
			//멤버의 누적금액, 마일리지, 쿠폰 되돌리기
		    int reSpay = zz - yy;
		    memm = 0;
			int reMile = memm + rem;
//			GradeUtil.grc(String.valueOf(mem.get("MEM_ID")));
//			CouponUtil.crc(String.valueOf(mem.get("MEM_ID")));
			memberDao.updateMemRefund(reMile, reSpay, memId);
			
		}		
		
		String d = String.valueOf(re.get("PAY_TCODE"));
		
		int daa = memberDao.insertRefund(payOrdNo, d); //환불테이블 인서트
		
		System.out.println("  ┌───────────────┐");
		System.out.println("      환불 완료 !");
		System.out.println("  └───────────────┘");
		System.out.println("　  　ᕱ ᕱ ||");
		System.out.println("　   ( ･ω･ ||");
		System.out.println("　   /　つ Φ");
		
		return View.PROD_HOME;
	}

}
