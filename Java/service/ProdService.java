package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dao.PayDao;
import dao.ProdDao;
import util.CouponUtil;
import util.GradeUtil;
import util.MileageUtil;
import util.ProcedureUtil;
import util.ScanUtil;
import util.View;

public class ProdService {

	// 싱글톤 패턴 : 하나의 객체를 돌려쓰게 만들어주는 디자인 패턴
	private ProdService() {
		// private으로 다른 클래스에서 생성자를 호출하지 못하기 때문에 객체 생성을 할 수 없음 (객체가 여러 개 생길 일이 없어짐)
	}

	private static ProdService instance; // 객체를 보관할 변수

	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	public static ProdService getInstance() {
		if (instance == null) { // 객체가 생성되지 않아 변수가 비어있을 경우
			instance = new ProdService(); // 객체를 새로 생성해 리턴
		} // 객체가 이미 instance에 있으면 그대로 주면됨
		return instance; // 객체 리턴
	}
//	public static String memId = (String) MemberService.loginMember.get("MEM_ID");

	public static String outlprod = "A0001";
	public static String toplprod = "B0001";
	public static String bottomlprod = "C0001";
	public static String shoelprod = "D0001";

	public static String lprodNo;
	public static String pname;
	public static String num;

	public List<Map<String, Object>> outerlist = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> search = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> ranklist = new ArrayList<Map<String, Object>>();
	public Map<String, Object> oneprod = new HashMap<String, Object>();

	private ProdDao prodDao = ProdDao.getInstance();
	private PayDao payDao = PayDao.getInstance();
	private ProcedureUtil prcUtil = ProcedureUtil.getInstance();
	private MemberService memservice = MemberService.getInstance();

	public int ProdSearch() {// [1.검색]을 누르면 들어오는 메서드

		System.out.println(" ┌─────────────── 𝗖𝗔𝗧𝗘𝗚𝗢𝗥𝗬 ───────────────┐");
		System.out.print(" 1.𝗢𝗨𝗧𝗘𝗥 🧥   2.𝗧𝗢𝗣 👕   3.𝗕𝗢𝗧𝗧𝗢𝗠 👖   4.𝗦𝗛𝗢𝗘𝗦 👟 ");
		int input = ScanUtil.nextInt();

		if (input == 1) {
			lprodNo = outlprod;
		} else if (input == 2) {
			lprodNo = toplprod;
		} else if (input == 3) {
			lprodNo = bottomlprod;
		} else if (input == 4) {
			lprodNo = shoelprod;
		} else {
			System.out.println("   ＿人人人人人人人人人人＿");
			System.out.println("   ＞ 잘못 입력하셨습니다.＜");
			System.out.println("　 　    　┐( ∵ )┌");
			System.out.println("　  　   　 ( 　) 　");
			System.out.println("　 　     　　┘| ");
			return View.PROD_SEARCH;
		}

		return View.PROD_PBOARD;

	}

	public int ProdBoard() {// 카테고리별 상품을 출력하는 게시판 메서드

		outerlist = prodDao.selectProd(lprodNo);
		System.out.println();
		System.out.println("╔═════════════ 𝗣𝗥𝗢𝗗 𝗟𝗜𝗦𝗧 🧥 ════════════╗");
		System.out.println("          상품명            단가");
		for (Map<String, Object> board : outerlist) {
			System.out.println("    " + board.get("PROD_NAME") + ":" + board.get("PROD_COST") + "원");
		} // 상품게시판출력
		System.out.println("═══════════════════════════════════════");

		return View.PROD_SEARCHLIST;// 검색한 아우터리스트 메서드
	}

	public int searchlist() {// 검색한 상품리스트출력
		System.out.print(" 조회할 상품명을 입력하세요 ⏩ ");
		pname = ScanUtil.nextLine();// 사용자가 검색한 상품이름을 전역변수 pname에 저장

		search = prodDao.searchlist(pname, lprodNo);
		// ㄴ> 상품이름을 검색해서 나오는 데이터 출력
		System.out.println("╔══════════════════ 𝗣𝗥𝗢𝗗 𝗟𝗜𝗦𝗧 🎀 ═════════════════╗");
		System.out.println(" 상품번호\t\t상품명\t\t상품컬러\t사이즈\t가격");
		for (int i = 0; i < search.size(); i++) {
			System.out.println(search.get(i).get("PROD_NO") + "\t" + search.get(i).get("PROD_NAME") + "\t"
					+ search.get(i).get("PROD_COLOR") + "\t" + search.get(i).get("PROD_SIZE") + "\t"
					+ search.get(i).get("PROD_COST") + "\t");
		}

		System.out.println("═════════════════════════════════════════════════");
//		prodDetail(pname, num, lprodno);
		return View.PROD_DETAIL;
	}

	public int prodDetail() {// 선택한 상품의 상세내용 메서드

		System.out.print(" 상품의 상세내용을 보시려면 상품번호를 입력하세요 ⏩ ");
		num = ScanUtil.nextLine();// 매우 중요 기억할것! 상품번호를 입력받아 전역변수 num에 저장

		oneprod = prodDao.oneProd(num);// 메서드 oneprod로 바꾸기
		// 검색한 상품들에서 선택한 상품을 찾아야한다

		if (num.equals(String.valueOf(oneprod.get("PROD_NO")))) {
			System.out.println("╔══════════════════ 𝗣𝗥𝗢𝗗 𝗟𝗜𝗦𝗧 🎀 ═════════════════╗");
			// 선택한 상품의 상세정보 출력
			System.out.println(oneprod.get("PROD_NAME") + "\t" + oneprod.get("PROD_COST") + "\t"
					+ oneprod.get("PROD_COLOR") + "\t" + oneprod.get("PROD_SIZE") + "\t" + oneprod.get("PROD_NO"));
			System.out.println(oneprod.get("PROD_DETAIL"));
			System.out.println("═════════════════════════════════════════════════");
		}

		System.out.print(" 1.𝗕𝗨𝗬 🛍  2.𝗖𝗔𝗥𝗧 🛒  3.𝗥𝗘𝗩𝗜𝗘𝗪 📝  4.𝗤𝗻𝗔 ❓  0.𝗕𝗔𝗖𝗞 ⏩ ");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.PAY_DEORDER;// 주문 메서드
		case 2:
			return View.PROD_CART;// 장바구니
		case 3:
			return View.PROD_REVIEW_DETAIL;
		case 4:
			return View.PROD_QNA;
		case 0:
			break;
		}
		return View.PROD_SEARCHLIST;

	}

	public int ProdRanking() {// 랭킹
		System.out.println("┌─────────────── 𝗖𝗔𝗧𝗘𝗚𝗢𝗥𝗬 ───────────────┐");
		System.out.print(" 1.𝗢𝗨𝗧𝗘𝗥 🧥   2.𝗧𝗢𝗣 👕   3.𝗕𝗢𝗧𝗧𝗢𝗠 👖   4.𝗦𝗛𝗢𝗘𝗦 👟 ");
		int input = ScanUtil.nextInt();

		if (input == 1) {
			lprodNo = outlprod;
		} else if (input == 2) {
			lprodNo = toplprod;
		} else if (input == 3) {
			lprodNo = bottomlprod;
		} else if (input == 4) {
			lprodNo = shoelprod;
		} else {
			System.out.println("   ＿人人人人人人人人人人＿");
			System.out.println("   ＞ 잘못 입력하셨습니다.＜");
			System.out.println("　 　    　┐( ∵ )┌");
			System.out.println("　  　   　 ( 　) 　");
			System.out.println("　 　     　　┘| ");
			return View.PROD_HOME;
		}
		return View.PROD_RANKBOARD;
	}

	public int Rank() {

		ranklist = prodDao.rank(lprodNo);
		System.out.println();
		System.out.println("╔════════════════════ 𝗥𝗔𝗡𝗞𝗜𝗡𝗚 🥇 ═══════════════════╗");
		System.out.println(" 순위\t상품번호\t\t상품명\t\t상품가격\t사이즈");

		for (int i = 0; i < ranklist.size(); i++) {
			System.out.println("  "+ranklist.get(i).get("PRANK") + "\t" + ranklist.get(i).get("PROD_NO") + "\t"
					+ ranklist.get(i).get("PROD_NAME") + ":" + ranklist.get(i).get("PROD_COST") + "\t"
					+ ranklist.get(i).get("PROD_SIZE") + "\t");
		}
		System.out.println("════════════════════════════════════════════════════");

		System.out.print(" 1.𝗗𝗘𝗧𝗔𝗜𝗟   0.𝗕𝗔𝗖𝗞 ⏩ ");
		String a = ScanUtil.nextLine();

		if (a.equals("1")) {
			return View.PROD_DETAIL;
		} else
			return View.PROD_HOME;
	}

	public int deorderProd() {
		String memId = (String) MemberService.loginMember.get("MEM_ID");

		oneprod = prodDao.oneProd(num);

		System.out.print(" 정말 구매하시겠습니까? (𝙮/𝙣) ⏩ ");
		String yn = ScanUtil.nextLine();

		if (yn.equals("y")) {
			System.out.print(" 수량을 입력해주세요(5개 이하만 구매가능) ⏩ ");
			int qty = ScanUtil.nextInt();

			String pno = num;// 상품번호를 넣을것임
			String prod_cost = null;// 상품가격을 넣을것임(결제에 필요함)

			if (num.equals(oneprod.get("PROD_NO").toString())) {
				pno = String.valueOf(oneprod.get("PROD_NO"));// object타입을 스트링타입으로 바꿔주어 pno에 삽입
				prod_cost = String.valueOf(oneprod.get("PROD_COST"));
				// object타입을 스트링타입으로 바꾸고 int형으로 바꿔 prod_cost에 삽입
//				System.out.println(pno);
//				System.out.println(prod_cost);//test		
			}

//			System.out.println(pno);
//			System.out.println(prod_cost);//test

			prcUtil.prc(memId); // orders 테이블에 자료 인서트

			Map<String, Object> ordno = payDao.selectOrdno(memId); // 회원의 최신 주문번호를 맵으로 받아오기
//			System.out.println(ordno);
			String vOrdNoStr = String.valueOf(ordno.get("V_ORD_NO"));// 회원의 최신 주문번호를 vOrdNoStr에 저장
//			System.out.println("vOrdNoStr : " + vOrdNoStr);
//			int newordno = Integer.parseInt(vOrdNoStr); //맵으로 받아온 주문번호를 newordno에 넣기
//			System.out.println(newordno);
//			System.out.println(vOrdNoStr);
			payDao.updateDeorder(pno, vOrdNoStr, qty);// deorder테이블에 주문번호, 상품번호, 구매수량 삽입
			payDao.updateOrderPrice(vOrdNoStr);// orders테이블에 상품금액 업데이트

			prodDao.updateRank(pno); // 주문테이블 들어가면 상품들의 순위를 새로 갱신함.
			System.out.println(" 🆗 주문 완료❕\n");

			return View.PROD_UPDATERANK;
		}
		return View.MAIN;
	}

	public int updateRanking() {

		List<Map<String, Object>> lprodProd = prodDao.deordprodNo();// 카테고리별 상품목록을 데려옴(prod_no를 찾기위해)
		for (int i = 0; i < lprodProd.size(); i++) {
			prodDao.updateRank(String.valueOf(lprodProd.get(i).get("PROD_NO")));
		}
		System.out.println("  ┌───────────────┐");
		System.out.println("   순위 업데이트 완료!");
		System.out.println("  └───────────────┘");
		System.out.println("　  　ᕱ ᕱ ||");
		System.out.println("　   ( ･ω･ ||");
		System.out.println("　   /　つ Φ");
		return View.MAIN;
	}

	public List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> payList = new ArrayList<Map<String, Object>>();
//	public List<Map<String, Object>> orderProdList = new ArrayList<Map<String,Object>>();

	public int orderCart() {
		// 아직 결제하지 않은 상품들의 주문서를 출력해주기
//		orderList = prodDao.selectOrders(memId);//orders테이블의 ord_no를 모두 받아옴
		System.out.println("╔═════════════════════ 𝗖𝗔𝗥𝗧 𝗟𝗜𝗦𝗧 🛒 ════════════════════╗");
		System.out.println(" 주문번호\t\t상품명\t\t상품가격\t\t색상\t사이즈\t개수");

		String memId = (String) MemberService.loginMember.get("MEM_ID");

//		payList = prodDao.selectPay();//pay테이블의 ord_no를 모두 받아옴
		List<Map<String, Object>> orderProdList = prodDao.selectCartProd(memId);// 아이디별 주문에 있는 상품의 정보와 ord_no를 받아옴

		for (int i = 0; i < orderProdList.size(); i++) {
			System.out.println(orderProdList.get(i).get("ORD_NO") + "\t" + orderProdList.get(i).get("PROD_NAME") + "\t"
					+ orderProdList.get(i).get("PROD_COST") + "\t" + orderProdList.get(i).get("PROD_COLOR") + "\t"
					+ orderProdList.get(i).get("PROD_SIZE") + "\t" + orderProdList.get(i).get("PQTY") + "\t");

		}
		System.out.println("═══════════════════════════════════════════════════════");

		System.out.print(" 1.𝗗𝗘𝗟𝗘𝗧𝗘 𝗢𝗣𝗧𝗜𝗢𝗡   2.𝗗𝗘𝗟𝗘𝗧𝗘 𝗔𝗟𝗟   3.𝗕𝗨𝗬   0.𝗕𝗔𝗖𝗞 ⏩ ");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.CART_DELETE;

		case 2:
			return View.CART_DELETE_ALL;

		case 3:
			return View.PAYMENT;

		}

		return View.PROD_HOME;
	}

	public int delCart() {// 장바구니에서 상품 선택 삭제
		String memId = (String) MemberService.loginMember.get("MEM_ID");

		System.out.println(" 삭제하실 상품의 주문번호를 입력해주세요 ⏩ ");
		String ordNo = ScanUtil.nextLine();
		List<Map<String, Object>> a = prodDao.deleteCart_deord(memId);

		for (int i = 0; i < a.size(); i++) {
			if (String.valueOf(a.get(i).get("ORD_NO")).contains(ordNo)) {
				int result = prodDao.deleteCart_deorder_st(ordNo);
				int res2 = prodDao.deleteCart_orders(ordNo);

				if (0 < result && 0 < res2) {
					System.out.println("  ┌───────────────┐");
					System.out.println("   장바구니 삭제 완료!");
					System.out.println("  └───────────────┘");
					System.out.println("　  　ᕱ ᕱ ||");
					System.out.println("　   ( ･ω･ ||");
					System.out.println("　   /　つ Φ");
				}
			}
		}
		return View.PROD_CART;
	}

	public int delCart_ALL() {// 장바구니에서 상품 전체 삭제

		String memId = (String) MemberService.loginMember.get("MEM_ID");

		System.out.println(" 정말 삭제하시겠습니까? (𝘆/𝗻) ⏩ ");
		String yn = ScanUtil.nextLine();

		if (yn.equals("y")) {
			List<Map<String, Object>> a = prodDao.deleteCart_deord(memId);
			for (int i = 0; i < a.size(); i++) {
				String delordNo = String.valueOf(a.get(i).get("ORD_NO"));
				prodDao.deleteCart_deorder_st(delordNo);
				prodDao.deleteCart_orders(delordNo);
			}
		}
		System.out.println("  ┌───────────────┐");
		System.out.println("     모두 삭제 완료!");
		System.out.println("  └───────────────┘");
		System.out.println("　  　ᕱ ᕱ ||");
		System.out.println("　   ( ･ω･ ||");
		System.out.println("　   /　つ Φ");

		return View.PROD_CART;
	}

	public int payment() {// 전체 구매 메서드
		String memId = (String) MemberService.loginMember.get("MEM_ID");

		/*
		 * 결제메서드 - 적립금 할인 - 쿠폰 할인 - 할인된 가격, 사용 쿠폰, 사용 적립금, 주문번호(결제번호), 결제일자, 결제 수단 삽입하기
		 */
		List<Map<String, Object>> coup = prodDao.selectCoupon(memId);// 보유쿠폰 데이터 가져오기
		Map<String, Object> mile = prodDao.selectMile(memId);// 보유 적립금 데이터 가져오기
		List<Map<String, Object>> orderProdList = payDao.getOrderPrice(memId); // ORDERS에만 있는 주문번호, 총금액
		String coupNo = null;// 쿠폰번호 받을거
		int inmile = 0;// 사용 적립금 받을거
		double payPrice = 0;// 총결제금액 넣을거
		String coucode = null;
		
		orderList = prodDao.selectCartProd(memId);

		System.out.print(" 구매하실 주문번호를 입력하세요 ⏩ ");
		String memselordNo = ScanUtil.nextLine();
//		System.out.println(orderList);

		for (int i = 0; i < orderList.size(); i++) {
			if (memselordNo.equals(String.valueOf(orderList.get(i).get("ORD_NO")))) {
				
				System.out.println(" 쿠폰을 사용하세요❕\n");
				System.out.println("╔═══════════ 𝗖𝗢𝗨𝗣𝗢𝗡 𝗟𝗜𝗦𝗧 🎁 ══════════╗");
				System.out.println(" 쿠폰번호\t쿠폰명");
				for (int j = 0; j < coup.size(); j++) {
					System.out.println(coup.get(j).get("COUPON_NO") + "\t" + coup.get(j).get("COUPON_NAME"));
				}
				System.out.println("═════════════════════════════════════");
				System.out.print(" 쿠폰을 사용하시겠습니까? (𝘆/𝗻) ⏩ ");
				String yn = ScanUtil.nextLine();
				if (yn.equals("y")) {
					System.out.print(" 쿠폰을 사용하시려면 쿠폰 번호를 입력해주세요 ⏩ ");
					coupNo = ScanUtil.nextLine();// 사용할 쿠폰 번호를 입력받음
					coucode = coupNo;
				}
				System.out.println("적립금을 사용하세요❕❕\n");
				System.out.println("╔═══════════ 𝗠𝗜𝗟𝗘𝗔𝗚𝗘 💰 ══════════╗");
				System.out.println(mile.get("MEM_MILE"));
				System.out.println("═════════════════════════════════");
				System.out.print(" 적립금을 사용하시겠습니까? (𝘆/𝗻) ⏩");
				yn = ScanUtil.nextLine();
				if (yn.equals("y")) {
					System.out.print(" 사용하실 적립금을 입력해주세요 ⏩ ");
					inmile = ScanUtil.nextInt();// 사용할 적립금을 입력받음
				}

				for (int q = 0; q < orderProdList.size(); q++) {// 반복문 돌면서 총결제할 금액 넣기
					payPrice += Double.parseDouble(orderProdList.get(q).get("PORD_PRICE").toString());
				}
				System.out.println(" 총 금액 : " + Math.round(payPrice * 100) / 100 + "원");

				int mem_mile = Integer.parseInt(mile.get("MEM_MILE").toString());

				if (coupNo != null && inmile != 0) { // 쿠폰이랑 마일리지 둘다 할때
					if (coupNo.equals("A001")) {// 쿠폰적용
						payPrice = payPrice * 0.99;
						payPrice = payPrice - inmile;
						coupNo = "MEMBER";
					} else if (coupNo.equals("B001")) {
						payPrice = payPrice * 0.95;
						payPrice = payPrice - inmile;
						coupNo = "BRONZE";
					} else if (coupNo.equals("C001")) {
						payPrice = payPrice * 0.90;
						payPrice = payPrice - inmile;
						coupNo = "SILVER";
					} else if (coupNo.equals("D001")) {
						payPrice = payPrice * 0.85;
						payPrice = payPrice - inmile;
						coupNo = "GOLD";
					}
					System.out.println(" 최종결제금액 : " + Math.round(payPrice * 100) / 100 + "원");
					prodDao.delete_coupon(memId, coucode);
					mem_mile = mem_mile - inmile;
					prodDao.update_mile(memId, String.valueOf(mem_mile));

				} else if (coupNo != null && inmile == 0) {
					if (coupNo.equals("A001")) {// 쿠폰적용
						payPrice = payPrice * 0.99;
						coupNo = "MEMBER";
					} else if (coupNo.equals("B001")) {
						payPrice = payPrice * 0.95;
						coupNo = "BRONZE";
					} else if (coupNo.equals("C001")) {
						payPrice = payPrice * 0.90;
						coupNo = "SILVER";
					} else if (coupNo.equals("D001")) {
						payPrice = payPrice * 0.85;
						coupNo = "GOLD";
					}
					System.out.println(" 최종결제금액 : " + Math.round(payPrice * 100) / 100 + "원");
					prodDao.delete_coupon(memId, coucode);

				} else if (inmile != 0 && coupNo == null) { // 마일리지만 할때
					payPrice -= inmile;
					System.out.println(" 최종결제금액 : " + Math.round(payPrice * 100) / 100 + "원");
					mem_mile = mem_mile - inmile;
					prodDao.update_mile(memId, String.valueOf(mem_mile));
				}

				// 입력받은게 끝나면 pay테이블로 자료 insert해야함!!!!!!!!!!!!
				String paytcode = null;
				System.out.print(" 결제 수단을 선택해주세요.\n[1.𝗖𝗔𝗥𝗗 💳   2.𝗔𝗖𝗖𝗢𝗨𝗡𝗧💵]  ⏩ ");
				int input = ScanUtil.nextInt();
				if (input == 1) {
					paytcode = "A001";
				} else if (input == 2) {
					paytcode = "B001";
				}

				prodDao.insertPay(memselordNo, payPrice, inmile, coupNo, paytcode);
				// pay테이블에 자료 구매한 자료 삽입

				Map<String, Object> spay = prodDao.selectSpay(memId);// 주문한 아이디의 누적금액 받아옴
				int a = Integer.valueOf(spay.get("MEM_SPAY").toString()) + (int) payPrice;
				// 여기서 안걸림!
				int result = prodDao.updateSpay(memId, a);

				switch (input) {
				case 1:
					return View.PAY_CARD;// 1.카드
				case 2:
					return View.PAY_CASH;// 2.무통장입금
				}
			}

		}
		return View.PROD_CART;
	}

	public int cardPay() {
		boolean flag = true;

		do {
			flag = true;
			System.out.print(" 카드번호를 입력해주세요 ⏩ ");
			String mem_card = ScanUtil.nextLine();
			System.out.println();

			String cardno = "^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}$";

			if (Pattern.matches(cardno, mem_card)) {
				System.out.println("  ┌───────────────┐");
				System.out.println("      결제 완료!");
				System.out.println("  └───────────────┘");
				System.out.println("　  　ᕱ ᕱ ||");
				System.out.println("　   ( ･ω･ ||");
				System.out.println("　   /　つ Φ");
				String a = (String) MemberService.loginMember.get("MEM_ID");
				MileageUtil.mrc(a);
				CouponUtil.crc(a);
				GradeUtil.grc(a);
				
				return View.PROD_HOME;
			} else {
				System.out.println("   ＿人人人人人人人人人人＿");
				System.out.println("   ＞ 다시 입력해주세요!＜");
				System.out.println("　　  　┐( ∵ )┌");
				System.out.println("　  　   ( 　) 　");
				System.out.println("　　   　　┘| ");
				flag = false;
			}
		} while (!flag);
		return View.PROD_HOME;
	}

	public int cashPay() {
		boolean flag = true;
		String mem_acc;

		do {
			flag = true;
			System.out.print(" 계좌번호(10~15자리 이내 숫자만 입력) ⏩ ");
			mem_acc = ScanUtil.nextLine();
			System.out.println();

			String str = mem_acc;
			String regex = "^[0-9]{10,15}$";

			if (Pattern.matches(regex, str)) {
				System.out.println("  ┌───────────────┐");
				System.out.println("      결제 완료!");
				System.out.println("  └───────────────┘");
				System.out.println("　  　ᕱ ᕱ ||");
				System.out.println("　   ( ･ω･ ||");
				System.out.println("　   /　つ Φ");
//				System.out.println(mem_acc);
				String a = (String) MemberService.loginMember.get("MEM_ID");
				MileageUtil.mrc(a);
				CouponUtil.crc(a);
				GradeUtil.grc(a);
				
				return View.PROD_HOME;
			} else {
				System.out.println("   ＿人人人人人人人人人人＿");
				System.out.println("   ＞ 다시 입력해주세요!＜");
				System.out.println("　　  　┐( ∵ )┌");
				System.out.println("　  　   ( 　) 　");
				System.out.println("　　   　　┘| ");
				flag = false;
			}
		} while (!flag);
		return View.PROD_HOME;

	}

	// QNA
	public int prodQna() {
		System.out.print(" 1.𝗤𝗨𝗘𝗦𝗧𝗜𝗢𝗡  2.𝗤𝗻𝗔 𝗟𝗜𝗦𝗧  0.𝗕𝗔𝗖𝗞 ⏩ ");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.PROD_QNA_CHOICE;
		case 2:
			return View.PROD_QNA_LIST;
		}
		return View.PROD_HOME;
	}

	// QNA 질문등록
	public int choiceQna() {
		System.out.println(" [ 상품🛍 / 배송📦 / 결제💳 / 기타📃 ]");
		System.out.print(" 분류명을 입력해주세요 ⏩ ");
		String qnaTitle = ScanUtil.nextLine();
		System.out.print(" 질문내용을 입력해주세요 ⏩ ");
		String qnaContent = ScanUtil.nextLine();
		String memId = (String) MemberService.loginMember.get("MEM_ID");

		int result = prodDao.qnaInsert(qnaTitle, qnaContent, memId, num);

		if (0 < result) {
			System.out.println("  ┌───────────────┐");
			System.out.println("    질문 등록 성공!");
			System.out.println("  └───────────────┘");
			System.out.println("　  　ᕱ ᕱ ||");
			System.out.println("　   ( ･ω･ ||");
			System.out.println("　   /　つ Φ");
		} else {
			System.out.println("   ＿人人人人人人人人人＿");
			System.out.println("    ＞질문 등록 실패!＜");
			System.out.println("　  　　┐( ∵ )┌");
			System.out.println("　   　  ( 　) 　");
			System.out.println("　　   　　┘| ");
		}

		return View.PROD_QNA_LIST;
	}

	// 해당상품의 QNA목록
	public int listQna() {

		List<Map<String, Object>> qnalist = prodDao.selectQnaList(num);

		System.out.println();
		System.out.println("╔═════════════ 𝗤𝗻𝗔 𝗟𝗜𝗦𝗧 ❓ ════════════╗");
		System.out.println(" 번호 - 상품명\t  분류명\t작성자아이디\t작성일\t답변여부");
		for (Map<String, Object> qna : qnalist) {
			System.out.print(qna.get("PR_QNA_NO") + " - " + qna.get("PROD_NAME") + "\t" + qna.get("PR_QNA_TITLE") + "\t"
					+ qna.get("MEM_ID") + "\t\t" + qna.get("PR_QNA_DATE") + "\t");
			if (qna.get("PR_ANS_YES") == null) {
				System.out.println("X");
			} else {
				System.out.println("O");
			}

			System.out.println();
			System.out.println("═══════════════════════════════════════");
		}

		System.out.print(" 1.질문번호입력  2.질문등록  0.𝗛𝗢𝗠𝗘 ⏩ ");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1: // QNA 번호선택
			System.out.print(" 질문번호를 입력해주세요 ⏩ ");
			nowQnaNo = ScanUtil.nextInt();
			return View.PROD_QNA_SEARCH;
		case 2:
			return View.PROD_QNA_CHOICE;
		}

		return View.PROD_HOME; // 상품정보로 뒤로가기
	}

	int nowQnaNo;

	public int qnaSearch() {
		int qnaNo = nowQnaNo;
		Map<String, Object> qna = prodDao.selectQna(qnaNo, num);

		if (qna.get("PR_ANS") == null) { // 답글이 없는 상태

			System.out.println("\n═══════════════════════════════════════");
			System.out.println(" 번호\t: " + qna.get("PR_QNA_NO"));
			System.out.println(" 상품번호\t: " + qna.get("PROD_NO"));
			System.out.println(" 아이디\t: " + qna.get("MEM_ID"));
			System.out.println(" 분류명\t: " + qna.get("PR_QNA_TITLE"));
			System.out.println(" 질문내용\t: " + qna.get("PR_QNA_CONTENT"));
			System.out.println(" 작성일\t: " + qna.get("PR_QNA_DATE"));
			System.out.println("═══════════════════════════════════════");

			System.out.print(" 1.𝗨𝗣𝗗𝗔𝗧𝗘   2.𝗗𝗘𝗟𝗘𝗧𝗘   0.𝗕𝗔𝗖𝗞 ⏩ ");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.PROD_QNA_UPDATE;
			case 2:
				return View.PROD_QNA_DELETE;

			}
		} else {

			System.out.println("\n═══════════════════════════════════════");
			System.out.println(" 번호\t: " + qna.get("PR_QNA_NO"));
			System.out.println(" 상품번호\t: " + qna.get("PROD_NO"));
			System.out.println(" 아이디\t: " + qna.get("MEM_ID"));
			System.out.println(" 분류명\t: " + qna.get("PR_QNA_TITLE"));
			System.out.println(" 질문내용\t: " + qna.get("PR_QNA_CONTENT"));
			System.out.println(" 작성일\t: " + qna.get("PR_QNA_DATE"));
			System.out.println("═══════════════════════════════════════");
			System.out.println(" 답글내용\t : " + qna.get("PR_ANS"));
			System.out.println(" 답글날짜\t : " + qna.get("PR_DATE"));
			System.out.println("═══════════════════════════════════════");

			System.out.print(" 1.𝗨𝗣𝗗𝗔𝗧𝗘   2.𝗗𝗘𝗟𝗘𝗧𝗘   0.𝗕𝗔𝗖𝗞 ⏩ ");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.PROD_QNA_UPDATE;
			case 2:
				return View.PROD_QNA_DELETE;
			}

		}
		return View.PROD_QNA_LIST;
	}

	public int qnaUpdate() {

		int qnaNo = nowQnaNo;
		System.out.println(" [ 상품🛍 / 배송📦 / 결제💳 / 기타📃 ]");
		System.out.print(" 수정할 분류명을 입력해주세요 ⏩  ");
		String qnaTitle = ScanUtil.nextLine();
		System.out.print(" 수정할 질문내용을 입력해주세요 ⏩  ");
		String qnaContent = ScanUtil.nextLine();

		int result = prodDao.updateQna(qnaNo, qnaTitle, qnaContent);
		System.out.println();

		if (0 < result) {
			System.out.println("  ┌───────────────┐");
			System.out.println("    질문 수정 성공!");
			System.out.println("  └───────────────┘");
			System.out.println("　  　ᕱ ᕱ ||");
			System.out.println("　   ( ･ω･ ||");
			System.out.println("　   /　つ Φ");
		} else {
			System.out.println("  ＿人人人人人人人人人＿");
			System.out.println("   ＞질문 수정 실패!＜");
			System.out.println("　  　　┐( ∵ )┌");
			System.out.println("　   　  ( 　) 　");
			System.out.println("　  　 　　┘| ");
		}  

		return View.PROD_QNA_SEARCH;
	}

	public int qnaDelete() {
		System.out.print(" 정말 삭제하시겠습니까? (y/n) ⏩ ");
		String yn = ScanUtil.nextLine();
		System.out.println();

		if (yn.equals("y")) {
			int qnaNo = nowQnaNo;
			int result = prodDao.deleteQna(qnaNo);

			if (0 < result) {
				System.out.println("  ┌───────────────┐");
				System.out.println("     질문 삭제 성공!");
				System.out.println("  └───────────────┘");
				System.out.println("  　　ᕱ ᕱ ||");
				System.out.println("　   ( ･ω･ ||");
				System.out.println("  　 /　つ Φ");
			} else {
				System.out.println("  ＿人人人人人人人人人＿");
				System.out.println("   ＞질문 삭제 실패!＜");
				System.out.println("　  　　┐( ∵ )┌");
				System.out.println("　    　 ( 　) 　");
				System.out.println("　　   　　┘| ");
			}
		}
		return View.PROD_QNA_LIST;
	}

	// 상품 > 1.검색 > 1.상품리뷰
	public int prodReview_detail() {
//	       Integer prodNo = (Integer) ProdService.prodNumber.get("PROD_NO");
		List<Map<String, Object>> preview = prodDao.prodReviewList(num);

		System.out.println("╔═════════════ 𝗥𝗘𝗩𝗜𝗘𝗪 𝗟𝗜𝗦𝗧 💯 ════════════╗");
		for (int i = preview.size() - 1; i >= 0; i--) {
			System.out.println(" 리뷰번호\t상품명\t리뷰제목");
			System.out.println(preview.get(i).get("REV_NO") + "\t" + preview.get(i).get("PROD_NAME") + "\t"
					+ preview.get(i).get("REV_TITLE"));
			System.out.println("═══════════════════════════════════════");
		}

		System.out.println(" 1.𝗗𝗘𝗧𝗔𝗜𝗟    0.𝗕𝗔𝗖𝗞 ⏩ ");
		int input2 = ScanUtil.nextInt();

		switch (input2) {
		case 1:
			return View.PROD_REVIEW_DDETAIL;
		case 0:
			return View.PROD_SEARCH;
		}

		return View.MAIN;
	}

	// 상품 > 1.검색 > 1.상품리뷰 상세히
	public int prodReview_ddetail() {
		System.out.println(" 자세히 볼 리뷰 번호 입력 ⏩ ");
		int revNo = ScanUtil.nextInt();

		Map<String, Object> preview = prodDao.prodReviewdList(num, revNo);
		System.out.println();
		System.out.println("╔═════════════ 𝗥𝗘𝗩𝗜𝗘𝗪 💯 ════════════╗");
		System.out.println(" 리뷰번호\t : " + preview.get("REV_NO"));
		System.out.println(" 상품번호\t : " + preview.get("PROD_NO"));
		System.out.println(" 상품명\t : " + preview.get("PROD_NAME"));
		System.out.println(" 리뷰제목\t : " + preview.get("REV_TITLE"));
		System.out.println(" 리뷰내용\t : " + preview.get("REV_CONTENT"));
		System.out.print(" 별점\t : " + preview.get("REV_STAR") + "\t");
		// 오브젝트 타입을 int로 형변환하려면 toString으로 문자열타입으로 만들어준뒤 Integer.parseInt를 써줘야함
		int star = Integer.parseInt(preview.get("REV_STAR").toString());
		for (int i = 0; i < star; i++) {
			System.out.print("*");
		}
		System.out.println();
		System.out.println(" 작성일자\t : " + format.format(preview.get("REV_DATE")));
		System.out.println("═══════════════════════════════════════");

		System.out.println("1.𝗦𝗘𝗔𝗥𝗖𝗛 🔍   2.𝗥𝗔𝗡𝗞𝗜𝗡𝗚 🥇   0.𝗛𝗢𝗠𝗘 ⏩ ");
		int input2 = ScanUtil.nextInt();

		switch (input2) {
		case 1:
			return View.PROD_SEARCH;// 검색
		case 2:
			return View.PROD_RANKING;// 랭킹
		case 0:
			return View.PROD_HOME;// 상품페이지
		}

		return View.HOME;
	}

}
