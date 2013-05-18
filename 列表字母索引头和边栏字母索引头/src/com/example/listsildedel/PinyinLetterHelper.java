package com.example.listsildedel;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinyinLetterHelper {
	/**
	 * 获取人名的拼音首字母并且大写化
	 * 
	 * @param name
	 *            人名字符串，允许开头包含英文数字和下划线
	 * @return
	 */
	public static String getPinyinFirstLetter(String name) {
		String firstletter = "#";
		if (name != null) {
			// 转换前不管是中文还是英文直接取开头字符
			char initial = name.charAt(0);
			System.out.print(initial + "  ---From:  " + name);
			if (!((initial >= 'A' && initial <= 'Z')
					|| (initial >= 'a' && initial <= 'z')
					|| (initial >= '0' && initial <= '9') || initial == '_')) {
				firstletter = (PinyinHelper.toHanyuPinyinStringArray(initial))[0];
			} else {
				firstletter = initial + "";
			}

			firstletter = firstletter.toUpperCase();
			System.out.print("  -- " + firstletter);
			char[] firstChar = new char[1];
			firstChar[0] = firstletter.charAt(0);
			firstletter = new String(firstChar);
			System.out.println("    --" + firstletter);
		}
		return firstletter;
	}
}
