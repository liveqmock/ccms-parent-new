package com.yunat.ccms.rule.center.drl.convert;

public class LocationConverter {
	public static boolean matches(String dest, String target) {
		if (null == dest || null == target) {
			return false;
		}

		String[] destArr = dest.split(",");
		String[] targetArr = target.split(",");
		switch (targetArr.length) {
		case 2:
			if (destArr.length == 2) {
				if (destArr[1].contains(targetArr[1])) {
					return true;
				} else {
					return false;
				}
			}
			break;
		case 1:
			if (destArr.length == 2) {
				if (destArr[0].contains(targetArr[0])) {
					return true;
				} else {
					return false;
				}
			}
			break;
		default:
			break;
		}

		return false;
	}

	public static void main(String[] args) {
		String dest = "湖北,武汉";
		String target = "湖北";
		System.out.println(matches(dest, target));

		String dest1 = "湖北省,武汉";
		String target1 = "湖北";
		System.out.println(matches(dest1, target1));

		String dest2 = "湖北省,武汉";
		String target2 = "湖北省";
		System.out.println(matches(dest2, target2));

		String dest3 = "湖北,武汉";
		String target3 = "湖北,武汉";
		System.out.println(matches(dest3, target3));

		String dest4 = "湖北省,武汉市";
		String target4 = "湖北,武汉";
		System.out.println(matches(dest4, target4));

		String dest5 = "湖北省,武汉市";
		String target5 = "湖北省,武汉";
		System.out.println(matches(dest5, target5));

		String dest6 = "湖北省,武汉市";
		String target6 = "湖北省,武汉市";
		System.out.println(matches(dest6, target6));

		String dest7 = "湖北省";
		String target7 = "湖北省,武汉市";
		System.out.println(matches(dest7, target7));

		String dest8 = "湖北省";
		String target8 = "武汉市";
		System.out.println(matches(dest8, target8));

		String dest9 = "湖北省";
		String target9 = "湖北省";
		System.out.println(matches(dest9, target9));

		String dest10 = "吉林省,吉林市";
		String target10 = "吉林";
		System.out.println(matches(dest10, target10));

		String dest11 = "吉林省,长春市";
		String target11 = "吉林";
		System.out.println(matches(dest11, target11));

		String dest12 = "吉林省,长春市";
		String target12 = "吉林,吉林";
		System.out.println(matches(dest12, target12));
	}
}
