package com.alone.coder.framework.common.util.string;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 *
 * @author 芋道源码
 */
public class StrUtils {

	public static final char DELIMITER = '/';

	public static final Character PATH_SEPARATOR_CHAR = '/';

	public static final String DELIMITER_STR = "/";

	public static final String HTTP = "http";

	public static final String HTTP_PROTOCOL = "http://";

	public static final String HTTPS_PROTOCOL = "https://";

	public static String maxLength(CharSequence str, int maxLength) {
		return StrUtil.maxLength(str, maxLength - 3); // -3 的原因，是该方法会补充 ... 恰好
	}

	/**
	 * 给定字符串是否以任何一个字符串开始 给定字符串和数组为空都返回 false
	 * @param str 给定字符串
	 * @param prefixes 需要检测的开始字符串
	 * @since 3.0.6
	 */
	public static boolean startWithAny(String str, Collection<String> prefixes) {
		if (StrUtil.isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
			return false;
		}

		for (CharSequence suffix : prefixes) {
			if (StrUtil.startWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	public static List<Long> splitToLong(String value, CharSequence separator) {
		long[] longs = StrUtil.splitToLong(value, separator);
		return Arrays.stream(longs).boxed().collect(Collectors.toList());
	}

	public static Set<Long> splitToLongSet(String value) {
		return splitToLongSet(value, StrPool.COMMA);
	}

	public static Set<Long> splitToLongSet(String value, CharSequence separator) {
		long[] longs = StrUtil.splitToLong(value, separator);
		return Arrays.stream(longs).boxed().collect(Collectors.toSet());
	}

	public static List<Integer> splitToInteger(String value, CharSequence separator) {
		int[] integers = StrUtil.splitToInt(value, separator);
		return Arrays.stream(integers).boxed().collect(Collectors.toList());
	}

	/**
	 * 移除字符串中，包含指定字符串的行
	 * @param content 字符串
	 * @param sequence 包含的字符串
	 * @return 移除后的字符串
	 */
	public static String removeLineContains(String content, String sequence) {
		if (StrUtil.isEmpty(content) || StrUtil.isEmpty(sequence)) {
			return content;
		}
		return Arrays.stream(content.split("\n"))
			.filter(line -> !line.contains(sequence))
			.collect(Collectors.joining("\n"));
	}

	/**
	 * 拼接 URL，并去除重复的分隔符 '/'，但不会影响 http:// 和 https:// 这种头部.
	 * @param strs 拼接的字符数组
	 * @return 拼接结果
	 */
	public static String concat(String... strs) {
		StringBuilder sb = new StringBuilder(DELIMITER_STR);
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			if (StrUtil.isEmpty(str)) {
				continue;
			}
			sb.append(str);
			if (i != strs.length - 1) {
				sb.append(DELIMITER);
			}
		}
		return removeDuplicateSlashes(sb.toString());
	}

	/**
	 * 去除路径中所有重复的 '/'
	 * @param path 路径
	 * @return 如 path = '/folder1//file1/', 返回 '/folder1/file1/' 如 path =
	 * '/folder1////file1///', 返回 '/folder1/file1/'
	 */
	public static String removeDuplicateSlashes(String path) {
		if (StrUtil.isEmpty(path)) {
			return path;
		}
		StringBuilder sb = new StringBuilder();
		// 是否包含 http 或 https 协议信息
		boolean containProtocol = StrUtil.containsAnyIgnoreCase(path, HTTP_PROTOCOL, HTTPS_PROTOCOL);

		if (containProtocol) {
			path = trimStartSlashes(path);
		}
		// 是否包含 http 协议信息
		boolean startWithHttpProtocol = StrUtil.startWithIgnoreCase(path, HTTP_PROTOCOL);
		// 是否包含 https 协议信息
		boolean startWithHttpsProtocol = StrUtil.startWithIgnoreCase(path, HTTPS_PROTOCOL);

		if (startWithHttpProtocol) {
			sb.append(HTTP_PROTOCOL);
		}
		else if (startWithHttpsProtocol) {
			sb.append(HTTPS_PROTOCOL);
		}
		for (int i = sb.length(); i < path.length() - 1; i++) {
			char current = path.charAt(i);
			char next = path.charAt(i + 1);
			if (!(current == DELIMITER && next == DELIMITER)) {
				sb.append(current);
			}
		}
		sb.append(path.charAt(path.length() - 1));
		return sb.toString();
	}

	/**
	 * 移除 URL 中的前后的所有 '/'
	 * @param path 路径
	 * @return 如 path = '/folder1/file1/', 返回 'folder1/file1' 如 path =
	 * '///folder1/file1//', 返回 'folder1/file1'
	 */
	public static String trimSlashes(String path) {
		path = trimStartSlashes(path);
		path = trimEndSlashes(path);
		return path;
	}

	/**
	 * 移除 URL 中的第一个 '/'
	 * @param path 路径
	 * @return 如 path = '/folder1/file1', 返回 'folder1/file1' 如 path = '/folder1/file1', 返回
	 * 'folder1/file1'
	 */
	public static String trimStartSlashes(String path) {
		if (StrUtil.isEmpty(path)) {
			return path;
		}

		while (path.startsWith(DELIMITER_STR)) {
			path = path.substring(1);
		}

		return path;
	}

	/**
	 * 移除 URL 中的最后一个 '/'
	 * @param path 路径
	 * @return 如 path = '/folder1/file1/', 返回 '/folder1/file1' 如 path =
	 * '/folder1/file1///', 返回 '/folder1/file1'
	 */
	public static String trimEndSlashes(String path) {
		if (StrUtil.isEmpty(path)) {
			return path;
		}

		while (path.endsWith(DELIMITER_STR)) {
			path = path.substring(0, path.length() - 1);
		}

		return path;
	}

	/**
	 * 拼接 URL，并去除重复的分隔符 '/'，但不会影响 http:// 和 https:// 这种头部.
	 * @param encodeAllIgnoreSlashes 是否 encode 编码 (忽略 /)
	 * @param strs 拼接的字符数组
	 * @return 拼接结果
	 */
	public static String concat(boolean encodeAllIgnoreSlashes, String... strs) {
		StringBuilder sb = new StringBuilder(DELIMITER_STR);
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			if (StrUtil.isEmpty(str)) {
				continue;
			}
			sb.append(str);
			if (i != strs.length - 1) {
				sb.append(DELIMITER);
			}
		}
		if (encodeAllIgnoreSlashes) {
			return encodeAllIgnoreSlashes(removeDuplicateSlashes(sb.toString()));
		}
		else {
			return removeDuplicateSlashes(sb.toString());
		}
	}

	/**
	 * 编码全部字符
	 * @param str 被编码内容
	 * @return 编码后的字符
	 */
	public static String encodeAllIgnoreSlashes(String str) {
		if (StrUtil.isEmpty(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder();

		int prevIndex = -1;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == PATH_SEPARATOR_CHAR) {
				if (prevIndex < i) {
					String substring = str.substring(prevIndex + 1, i);
					sb.append(URLEncodeUtil.encodeAll(substring));
					prevIndex = i;
				}
				sb.append(c);
			}

			if (i == str.length() - 1 && prevIndex < i) {
				String substring = str.substring(prevIndex + 1, i + 1);
				sb.append(URLEncodeUtil.encodeAll(substring));
			}
		}

		return sb.toString();
	}

	/**
	 * 拼接 URL，并去除重复的分隔符 '/'，并去除开头的 '/', 但不会影响 http:// 和 https:// 这种头部.
	 * @param strs 拼接的字符数组
	 * @return 拼接结果
	 */
	public static String concatTrimStartSlashes(String... strs) {
		return trimStartSlashes(concat(strs));
	}

}
