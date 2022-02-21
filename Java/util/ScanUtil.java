package util;

import java.util.Scanner;

public class ScanUtil {

	// 스캐너를 사용하려면 스캐너 객체가 필요
	private static Scanner s = new Scanner(System.in);

	public static String nextLine() {
		return s.nextLine();
	}

	public static int nextInt() {
		int input = 0;

		try {
			input = Integer.parseInt(s.nextLine());
		} catch (Exception e) {
			System.out.print("잘못 입력하셨습니다. 다시 입력해주세요.> ");
			input = nextInt(); // 재귀호출 (메서드 내에서 자기 자신 호출)
		}

		return input;
	}

}
