package org.javaweb.rasp.commons.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.*;
import static org.javaweb.rasp.commons.config.RASPConfiguration.RASP_DIRECTORY;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public class FileUtils {

	/**
	 * 判断当前系统是否是windows系统
	 */
	public static boolean IS_WINDOWS = File.separatorChar == '\\';

	/**
	 * 获取文件后缀
	 *
	 * @param fileName 文件名
	 * @return 文件后缀
	 */
	public static String getFileSuffix(String fileName) {
		int lastIndexOf = fileName.lastIndexOf(".");
		return lastIndexOf > 0 ? fileName.substring(lastIndexOf + 1) : "";
	}

	/**
	 * 获取内存使用信息
	 *
	 * @return 剩余、总计、已用、最大、可用内存
	 */
	public static long[] getMemoryInfo() {
		Runtime runtime         = Runtime.getRuntime();
		long    freeMemory      = runtime.freeMemory();
		long    totalMemory     = runtime.totalMemory();
		long    usedMemory      = totalMemory - freeMemory;
		long    maxMemory       = runtime.maxMemory();
		long    availableMemory = maxMemory - totalMemory + freeMemory;

		return new long[]{freeMemory, totalMemory, usedMemory, maxMemory, availableMemory};
	}

	/**
	 * 获取磁盘使用信息
	 *
	 * @return 空闲/总计数
	 */
	public static long[] getDiskInfo() {
		return new long[]{RASP_DIRECTORY.getFreeSpace(), RASP_DIRECTORY.getTotalSpace()};
	}

	/* A normal Unix pathname contains no duplicate slashes and does not end
       with a slash.  It may be the empty string. */
	/* Normalize the given pathname, whose length is len, starting at the given
	   offset; everything before this offset is already normal. */
	private static String unixNormalize(String pathname, int len, int off) {
		if (len == 0) return pathname;
		int n = len;
		while ((n > 0) && (pathname.charAt(n - 1) == '/')) n--;
		if (n == 0) return "/";

		StringBuilder sb = new StringBuilder(pathname.length());
		if (off > 0) sb.append(pathname, 0, off);
		char prevChar = 0;

		for (int i = off; i < n; i++) {
			char c = pathname.charAt(i);
			if ((prevChar == '/') && (c == '/')) continue;
			sb.append(c);
			prevChar = c;
		}

		return sb.toString();
	}

	/* Check that the given pathname is normal.  If not, invoke the real
	   normalizer on the part of the pathname that requires normalization.
	   This way we iterate through the whole pathname string only once. */
	public static String unixNormalize(String pathname) {
		int  n        = pathname.length();
		char prevChar = 0;

		for (int i = 0; i < n; i++) {
			char c = pathname.charAt(i);

			if ((prevChar == '/') && (c == '/'))
				return unixNormalize(pathname, n, i - 1);
			prevChar = c;
		}

		if (prevChar == '/') return unixNormalize(pathname, n, n - 1);

		return pathname;
	}

	/* A normal Win32 pathname contains no duplicate slashes, except possibly
       for a UNC prefix, and does not end with a slash.  It may be the empty
       string.  Normalized Win32 pathnames have the convenient property that
       the length of the prefix almost uniquely identifies the type of the path
       and whether it is absolute or relative:

           0  relative to both drive and directory
           1  drive-relative (begins with '\\')
           2  absolute UNC (if first char is '\\'),
                else directory-relative (has form "z:foo")
           3  absolute local pathname (begins with "z:\\")
     */

	private static int winNormalizePrefix(String path, int len, StringBuffer sb) {
		int src = 0;
		while ((src < len) && isSlash(path.charAt(src))) src++;
		char c;

		if ((len - src >= 2) && isLetter(c = path.charAt(src)) && path.charAt(src + 1) == ':') {
            /* Remove leading slashes if followed by drive specifier.
               This hack is necessary to support file URLs containing drive
               specifiers (e.g., "file://c:/path").  As a side effect,
               "/c:/path" can be used as an alternative to "c:/path". */
			sb.append(c);
			sb.append(':');
			src += 2;
		} else {
			src = 0;
			if ((len >= 2) && isSlash(path.charAt(0)) && isSlash(path.charAt(1))) {
                /* UNC pathname: Retain first slash; leave src pointed at
                   second slash so that further slashes will be collapsed
                   into the second slash.  The result will be a pathname
                   beginning with "\\\\" followed (most likely) by a host
                   name. */
				src = 1;
				sb.append(separator);
			}
		}

		return src;
	}

	/* Normalize the given pathname, whose length is len, starting at the given
	   offset; everything before this offset is already normal. */
	private static String winNormalize(String path, int len, int off) {
		if (len == 0) return path;
		if (off < 3) off = 0;   /* Avoid fencepost cases with UNC pathnames */
		int          src;
		char         slash = separatorChar;
		StringBuffer sb    = new StringBuffer(len);

		if (off == 0) {
			/* Complete normalization, including prefix */
			src = winNormalizePrefix(path, len, sb);
		} else {
			/* Partial normalization */
			src = off;
			sb.append(path, 0, off);
		}

        /* Remove redundant slashes from the remainder of the path, forcing all
           slashes into the preferred slash */
		while (src < len) {
			char c = path.charAt(src++);

			if (isSlash(c)) {
				while ((src < len) && isSlash(path.charAt(src))) src++;

				if (src == len) {
					/* Check for trailing separator */
					int sn = sb.length();

					if ((sn == 2) && (sb.charAt(1) == ':')) {
						/* "z:\\" */
						sb.append(slash);
						break;
					}

					if (sn == 0) {
						/* "\\" */
						sb.append(slash);
						break;
					}
					if ((sn == 1) && (isSlash(sb.charAt(0)))) {
                        /* "\\\\" is not collapsed to "\\" because "\\\\" marks
                           the beginning of a UNC pathname.  Even though it is
                           not, by itself, a valid UNC pathname, we leave it as
                           is in order to be consistent with the win32 APIs,
                           which treat this case as an invalid UNC pathname
                           rather than as an alias for the root directory of
                           the current drive. */
						sb.append(slash);
						break;
					}

                    /* Path does not denote a root directory, so do not append
                       trailing slash */
					break;
				} else {
					sb.append(slash);
				}
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/* Check that the given pathname is normal.  If not, invoke the real
	   normalizer on the part of the pathname that requires normalization.
	   This way we iterate through the whole pathname string only once. */
	public static String winNormalize(String path) {
		int  n     = path.length();
		char slash = separatorChar;
		char prev  = 0;

		for (int i = 0; i < n; i++) {
			char c = path.charAt(i);

			if (c == pathSeparatorChar)
				return winNormalize(path, n, (prev == slash) ? i - 1 : i);
			if ((c == slash) && (prev == slash) && (i > 1))
				return winNormalize(path, n, i - 1);
			if ((c == ':') && (i > 1))
				return winNormalize(path, n, 0);

			prev = c;
		}

		if (prev == slash) return winNormalize(path, n, n - 1);
		return path;
	}

	/**
	 * 输出格式化后的路径，移除多余的"/"但不会移除"../"
	 *
	 * @param path 文件路径
	 * @return 标准文件路径
	 */
	public static String normalize(String path) {
		if (path == null) {
			return null;
		}

		// Windows和Unix使用不一样的路径处理方式
		return IS_WINDOWS ? winNormalize(path) : unixNormalize(path);
	}

	public static boolean isSlash(char c) {
		return (c == '\\') || (c == '/');
	}

	public static boolean isLetter(char c) {
		return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
	}

	public static List<File> split(File targetFile, long fileSize) throws IOException {
		List<File> fileList = new ArrayList<File>();

		if (targetFile.length() > fileSize) {
			String           str;
			int              index  = 0;
			int              len    = 0;
			BufferedReader   br     = null;
			FileOutputStream fos    = null;
			File             parent = targetFile.getParentFile();

			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile)), 8192);

				while ((str = br.readLine()) != null) {
					// 切换新的临时文件
					if (fos != null && len > fileSize) {
						fos.flush();
						fos.close();

						len = 0;
						fos = null;
					}

					if (fos == null) {
						File   tmpFile;
						String fileName = targetFile.getName();
						int    lastDot  = fileName.lastIndexOf(".");

						if (lastDot > 0) {
							String name   = fileName.substring(0, lastDot);
							String suffix = fileName.substring(lastDot);
							tmpFile = new File(parent, name + "." + (++index) + suffix);
						} else {
							tmpFile = new File(parent, fileName + "." + (++index));
						}

						fos = new FileOutputStream(tmpFile);

						fileList.add(tmpFile);
					}

					len += str.length();
					fos.write((str + "\r\n").getBytes());
				}

				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} finally {
				if (br != null) IOUtils.closeQuietly(br);
			}
		}

		return fileList;
	}

	public static List<File> listFiles(File directory, String[] extensions, boolean recursive) {
		List<File> fileList = new ArrayList<File>();

		listDir(directory, extensions, fileList, recursive);

		return fileList;
	}

	private static void listDir(File dir, final String[] extensions, List<File> fileList, boolean recursive) {
		if (dir == null || extensions == null || fileList == null) return;

		if (dir.isFile()) {
			throw new RuntimeException(dir.getAbsolutePath() + " is not a file!");
		}

		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File name) {
				for (String extension : extensions) {
					if (name.getName().endsWith("." + extension)) return true;
				}

				return false;
			}
		});

		if (files == null) return;

		for (File file : files) {
			if (file.isDirectory() && recursive) {
				listDir(dir, extensions, fileList, recursive);
			} else {
				fileList.add(file);
			}
		}
	}

	public static List<String> readLines(File file, String encoding) throws IOException {
		String         line;
		List<String>   lines;
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			lines = new ArrayList<String>();

			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			if (br != null) br.close();
		}

		return lines;
	}

	public static void writeLines(File file, List<String> lines, String encoding, boolean append) throws IOException {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file, append);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, encoding));

			for (String line : lines) {
				bw.write((line + "\r\n"));
			}

			bw.flush();
			bw.close();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static void copyFile(File file, File dstFile) throws IOException {
		File parentFile = dstFile.getParentFile();

		// 解决复制的目标目录不存在时异常问题
		if (!parentFile.exists()) {
			if (!parentFile.mkdirs()) throw new IOException(AGENT_NAME + "无法创建目录：" + parentFile);
		}

		byte[] bytes = readFileBytes(file);
		writeFileBytes(dstFile, bytes);
	}

	public static void writeStringToFile(File file, String content) throws IOException {
		writeStringToFile(file, content, "UTF-8");
	}

	public static void writeStringToFile(File file, String content, String encoding) throws IOException {
		writeFileBytes(file, content.getBytes(encoding));
	}

	public static void writeFileBytes(File file, byte[] bytes) throws IOException {
		writeFileBytes(file, bytes, false);
	}

	public static void writeFileBytes(File file, byte[] bytes, boolean append) throws IOException {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file, append);
			fos.write(bytes);
			fos.flush();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static String readFileToString(File file, String encoding) throws IOException {
		return new String(readFileBytes(file), encoding);
	}

	public static byte[] readFileBytes(File file) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream       fis  = null;

		try {
			fis = new FileInputStream(file);

			int    a;
			byte[] bytes = new byte[2048];

			while ((a = fis.read(bytes)) != -1) {
				baos.write(bytes, 0, a);
			}

			return baos.toByteArray();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

}
