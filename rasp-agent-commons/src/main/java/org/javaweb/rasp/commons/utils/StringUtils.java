package org.javaweb.rasp.commons.utils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.list;
import static org.apache.commons.lang3.StringUtils.*;

public class StringUtils {

	private static final String[] EMPTY_STRING_ARRAY = {};

	/**
	 * 字符串转小写，如果字符串中不包含大写字母不会调用toLowerCase方法
	 *
	 * @param str 字符串
	 * @return 转换成小写后的字符串
	 */
	public static String toLowerCase(String str) {
		if (str != null) {
			char[] chars = null;

			for (int i = 0; i < str.length(); i++) {
				int c   = str.charAt(i);
				int chr = toLowerCase(c);

				// 比较转小写后ASCII是否有变化
				if (c != chr) {
					if (chars == null) {
						chars = str.toCharArray();
					}

					chars[i] = (char) chr;
				}
			}

			if (chars != null) {
				return new String(chars);
			}
		}

		return str;
	}

	/**
	 * ASCII大写字母转小写
	 *
	 * @param ascii 字符
	 * @return 转换后的小写字母
	 */
	public static int toLowerCase(int ascii) {
		if (ascii >= 'A' && ascii <= 'Z') {
			// ('a' - 'A') = 32
			return ascii + 32;
		}

		return ascii;
	}

	/**
	 * 获取JDK文件默认编码
	 */
	private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");

	public static String replaceAll(String text, String[] searchList, String[] replacementList) {
		return replaceEachRepeatedly(text, searchList, replacementList);
	}

	public static String replace(String text, String searchList, String replacementList) {
		return org.apache.commons.lang3.StringUtils.replace(text, searchList, replacementList);
	}

	public static boolean startWithIgnoreCase(String str, String prefix) {
		return startsWithIgnoreCase(str, prefix);
	}

	public static boolean equalIgnoreCase(String str, String prefix) {
		return equalsIgnoreCase(str, prefix);
	}

	public static boolean endWithIgnoreCase(String str, String prefix) {
		return endsWithIgnoreCase(str, prefix);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * unicode转字符串
	 *
	 * @param content unicode字符串
	 * @return 转码后的字符串
	 */
	public static String ascii2Native(String content) {
		if (isNotEmpty(content)) {
			List<String> asciiList = new ArrayList<String>();

			Matcher matcher = Pattern.compile("\\\\u[0-9a-fA-F]{4}").matcher(content);
			while (matcher.find()) {
				asciiList.add(matcher.group());
			}

			for (int i = 0, j = 2; i < asciiList.size(); i++) {
				String code = asciiList.get(i).substring(j, j + 4);
				char   chr  = (char) Integer.parseInt(code, 16);
				content = replace(content, asciiList.get(i), String.valueOf(chr));
			}
		}

		return content;
	}

	/**
	 * Test whether the given string matches the given substring
	 * at the given index.
	 *
	 * @param str       the original string (or StringBuilder)
	 * @param index     the index in the original string to start matching against
	 * @param substring the substring to match at the given index
	 */
	public static boolean substringMatch(String str, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;

			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}

		return true;
	}

	public static String charset(String str) {
		try {
			if (!DEFAULT_ENCODING.equals("UTF-8")) {
				return new String(str.getBytes(), DEFAULT_ENCODING);
			}

			return str;
		} catch (UnsupportedEncodingException ignored) {
		}

		return str;
	}

	public static void println(String str) {
		System.out.println(charset(str));
	}

	public static String hex2String(String hexString) {

		if (isNotEmpty(hexString)) {
			List<String> hexList = new ArrayList<String>();

			Matcher matcher = Pattern.compile("\\\\x[0-9a-z]{2}").matcher(hexString);
			while (matcher.find()) {
				hexList.add(matcher.group());
			}

			for (int i = 0, j = 2; i < hexList.size(); i++) {
				String code = hexList.get(i).substring(j, j + 2);
				char   chr  = (char) Integer.parseInt(code, 16);
				hexString = hexString.replace(hexList.get(i), String.valueOf(chr));
			}
		}

		return hexString;
	}

	/**
	 * Check whether the given {@code String} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code String} is not {@code null}, its length is greater than 0,
	 * and it contains at least one non-whitespace character.
	 *
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null}, its
	 * length is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		return (str != null && !str.isEmpty() && containsText(str));
	}

	private static boolean containsText(CharSequence str) {
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsTexts(CharSequence str, CharSequence... array) {
		for (CharSequence sequence : array) {
			if (contains(str, sequence)) return true;
		}

		return false;
	}

	/**
	 * Tokenize the given {@code String} into a {@code String} array via a
	 * {@link StringTokenizer}.
	 * <p>Trims tokens and omits empty tokens.
	 * <p>The given {@code delimiters} string can consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using .
	 *
	 * @param str        the {@code String} to tokenize (potentially {@code null} or empty)
	 * @param delimiters the delimiter characters, assembled as a {@code String}
	 *                   (each of the characters is individually considered as a delimiter)
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given {@code String} into a {@code String} array via a
	 * {@link StringTokenizer}.
	 * <p>The given {@code delimiters} string can consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using .
	 *
	 * @param str               the {@code String} to tokenize (potentially {@code null} or empty)
	 * @param delimiters        the delimiter characters, assembled as a {@code String}
	 *                          (each of the characters is individually considered as a delimiter)
	 * @param trimTokens        trim the tokens via {@link String#trim()}
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 *                          (only applies to tokens that are empty after trimming; StringTokenizer
	 *                          will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters,
	                                             boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return EMPTY_STRING_ARRAY;
		}

		StringTokenizer st     = new StringTokenizer(str, delimiters);
		List<String>    tokens = new ArrayList<String>();

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}

		return toStringArray(tokens);
	}

	/**
	 * Copy the given {@link Collection} into a {@code String} array.
	 * <p>The {@code Collection} must contain {@code String} elements only.
	 *
	 * @param collection the {@code Collection} to copy
	 *                   (potentially {@code null} or empty)
	 * @return the resulting {@code String} array
	 */
	public static String[] toStringArray(Collection<String> collection) {
		return (!ArrayUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
	}

	/**
	 * Copy the given {@link Enumeration} into a {@code String} array.
	 * <p>The {@code Enumeration} must contain {@code String} elements only.
	 *
	 * @param enumeration the {@code Enumeration} to copy
	 *                    (potentially {@code null} or empty)
	 * @return the resulting {@code String} array
	 */
	public static String[] toStringArray(Enumeration<String> enumeration) {
		return (enumeration != null ? toStringArray(list(enumeration)) : EMPTY_STRING_ARRAY);
	}

	public static boolean containsIgnoreCase(String str, String searchStr) {
		return org.apache.commons.lang3.StringUtils.containsIgnoreCase(str, searchStr);
	}

	public static boolean startWith(String str, String prefix) {
		return startsWith(str, prefix);
	}

	public static String getRandomString(int length) {
		return randomString(length, null);
	}

	public static String getRandomString(int length, String keyword) {
		return randomString(length, keyword);
	}

	/**
	 * 产生随机字符串
	 *
	 * @param length   生成的字符串的长度
	 * @param keyword  生成的字符范围
	 * @param excludes 不需要包含的字符
	 * @return 随机字符串
	 */
	public static String randomString(int length, String keyword, String... excludes) {
		if (length < 1) {
			return null;
		}

		if (keyword == null || "".equals(keyword)) {
			keyword = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}

		if (excludes.length > 0) {
			for (String word : excludes) {
				keyword = keyword.replace(word, "");
			}
		}

		Random random = new Random();
		char[] strs   = keyword.toCharArray();
		char[] chars  = new char[length];

		for (int i = 0; i < chars.length; i++) {
			chars[i] = strs[random.nextInt(strs.length)];
		}

		return new String(chars);
	}

	public static String join(final String[] array, final char separator) {
		return org.apache.commons.lang3.StringUtils.join(array, separator);
	}

	public static String join(final Iterable<?> iterable, final char separator) {
		return org.apache.commons.lang3.StringUtils.join(iterable, separator);
	}

	public static String[] split(final String str) {
		return split(str, null, -1);
	}

	public static String[] split(final String str, final String separatorChars) {
		return split(str, separatorChars, -1);
	}

	public static String[] split(final String str, final String separatorChars, final int max) {
		return org.apache.commons.lang3.StringUtils.split(str, separatorChars, max);
	}

	/**
	 * 检测字符串中是否包含大写字母
	 *
	 * @param str 字符串
	 * @return 检测结果
	 */
	public static boolean containsUpperCase(final String str) {
		if (isEmpty(str)) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			int ascii = str.charAt(i);

			if (ascii >= 'A' && ascii <= 'Z') {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取String#trim()的开始和结束的位置
	 *
	 * @param str 字符串
	 * @return offset/len
	 */
	public static int[] getTrimIndex(String str) {
		int start = 0;
		int end   = str.length();

		while ((start < end) && (str.charAt(start) <= ' ')) {
			start++;
		}

		while ((start < end) && (str.charAt(end - 1) <= ' ')) {
			end--;
		}

		return (start > 0 || end < str.length()) ? new int[]{start, end} : new int[]{0, str.length()};
	}

	/**
	 * 比较两个字符串是否相等，忽略大小写和空白符
	 *
	 * @param str    第一个字符串
	 * @param search 第二个字符串
	 * @return 两个字符串是否相等
	 */
	public static boolean equalIgnoreCaseAndTrim(String str, String search) {
		return eq(str, search, false);
	}

	/**
	 * 字符串trim、忽略大小写比较，计算字符串1是否以prefix开始
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 字符串1是否startWith prefix
	 */
	public static boolean startWithIgnoreCaseAndTrim(String str, String prefix) {
		return eq(str, prefix, true);
	}

	public static boolean eq(String str, String find) {
		return eq(str, find, false);
	}

	/**
	 * 字符串trim、忽略大小写比较
	 *
	 * @param str       字符串
	 * @param prefix    前缀
	 * @param startWith 是否使用startWith方式匹配
	 * @return 两个字符串是否相等
	 */
	public static boolean eq(String str, String prefix, boolean startWith) {
		// 比较null或this
		if (str == null || prefix == null) {
			return str == null && prefix == null;
		}

		int[] trim  = getTrimIndex(str);
		int[] trim2 = getTrimIndex(prefix);

		// 比较长度是否一样
		int lenDiff = (trim[1] - trim[0]) - (trim2[1] - trim2[0]);

		if (startWith) {
			if (lenDiff < 0) return false;
		} else {
			if (lenDiff != 0) return false;
		}

		// 比较内容是否一样
		for (int i = trim[0], j = trim2[0]; i < trim[1] && j < trim2[1]; i++, j++) {
			char chr  = str.charAt(i);
			char chr2 = prefix.charAt(j);

			// 忽略大小写
			if (chr != chr2 && toLowerCase(chr) != toLowerCase(chr2)) {
				return false;
			}
		}

		return true;
	}

	public static String genUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 统计字符串包含某个char字符的个数
	 *
	 * @param str 字符串
	 * @param chr char
	 * @return char出现次数
	 */
	public static int countOf(String str, char chr) {
		if (str == null) return -1;

		int count = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == chr) {
				count++;
			}
		}

		return count;
	}

	/**
	 * 替换字符串的第一个字符，如：ABc替换成aBc
	 *
	 * @param str 字符串
	 * @param chr 替换后的首字母
	 * @return 替换后的新字符串
	 */
	public static String replaceFirstChar(String str, char chr) {
		if (str == null) return null;

		return chr + str.substring(1);
	}

}