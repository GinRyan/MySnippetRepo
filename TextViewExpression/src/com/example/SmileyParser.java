package com.example;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.expression.R;

/**
 * 
 * 这个类来自于： <a href=
 * 'http://www.cnblogs.com/llm-android/archive/2012/01/07/2316138.html'>http://www.cnblogs.com/llm-android/archive/2012/01/07/2316138.html</a
 * >
 * 
 * @author Liang
 * 
 */
public class SmileyParser {
	private Context mContext;
	private String[] mSmileyTexts;
	private Pattern mPattern;
	private HashMap<String, Integer> mSmileyToRes;
	//95张表情图片的Id
	public static final int[] DEFAULT_SMILEY_RES_IDS = { R.drawable.f000,
			R.drawable.f001, R.drawable.f002, R.drawable.f003, R.drawable.f004,
			R.drawable.f005, R.drawable.f006, R.drawable.f007, R.drawable.f008,
			R.drawable.f009, R.drawable.f010, R.drawable.f011, R.drawable.f012,
			R.drawable.f013, R.drawable.f014, R.drawable.f015, R.drawable.f016,
			R.drawable.f017, R.drawable.f018, R.drawable.f019, R.drawable.f020,
			R.drawable.f021, R.drawable.f022, R.drawable.f023, R.drawable.f024,
			R.drawable.f025, R.drawable.f026, R.drawable.f027, R.drawable.f028,
			R.drawable.f029, R.drawable.f030, R.drawable.f031, R.drawable.f032,
			R.drawable.f033, R.drawable.f034, R.drawable.f035, R.drawable.f036,
			R.drawable.f037, R.drawable.f038, R.drawable.f039, R.drawable.f040,
			R.drawable.f041, R.drawable.f042, R.drawable.f043, R.drawable.f044,
			R.drawable.f045, R.drawable.f046, R.drawable.f047, R.drawable.f048,
			R.drawable.f049, R.drawable.f050, R.drawable.f051, R.drawable.f052,
			R.drawable.f053, R.drawable.f054, R.drawable.f055, R.drawable.f056,
			R.drawable.f057, R.drawable.f058, R.drawable.f059, R.drawable.f060,
			R.drawable.f061, R.drawable.f062, R.drawable.f063, R.drawable.f064,
			R.drawable.f065, R.drawable.f066, R.drawable.f067, R.drawable.f068,
			R.drawable.f069, R.drawable.f070, R.drawable.f071, R.drawable.f072,
			R.drawable.f073, R.drawable.f074, R.drawable.f075, R.drawable.f076,
			R.drawable.f077, R.drawable.f078, R.drawable.f079, R.drawable.f080,
			R.drawable.f081, R.drawable.f082, R.drawable.f083, R.drawable.f084,
			R.drawable.f085, R.drawable.f086, R.drawable.f087, R.drawable.f088,
			R.drawable.f089, R.drawable.f090, R.drawable.f091, R.drawable.f092,
			R.drawable.f093, R.drawable.f094 };

	public SmileyParser(Context context) {
		mContext = context;
		mSmileyTexts = mContext.getResources().getStringArray(
				DEFAULT_SMILEY_TEXTS);
		mSmileyToRes = buildSmileyToRes();
		mPattern = buildPattern();
	}

	public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;

	private HashMap<String, Integer> buildSmileyToRes() {
		if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
			// Log.w("SmileyParser", "Smiley resource ID/text mismatch");
			// 表情的数量需要和数组定义的长度一致！
			throw new IllegalStateException("Smiley resource ID/text mismatch");
		}

		HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(
				mSmileyTexts.length);
		for (int i = 0; i < mSmileyTexts.length; i++) {
			smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
		}

		return smileyToRes;
	}

	// 构建正则表达式
	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);
		patternString.append('(');
		for (String s : mSmileyTexts) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");

		return Pattern.compile(patternString.toString());
	}

	// 根据文本替换成图片
	public CharSequence replace(CharSequence text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			int resId = mSmileyToRes.get(matcher.group());
			builder.setSpan(new ImageSpan(mContext, resId), matcher.start(),
					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
}
