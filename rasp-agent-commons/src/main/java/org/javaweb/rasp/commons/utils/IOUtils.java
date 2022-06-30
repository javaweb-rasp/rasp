package org.javaweb.rasp.commons.utils;

import java.io.*;

public class IOUtils {

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ignored) {
		}
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		return copy(input, output, new byte[4096]);
	}

	public static int copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		if (input == null || output == null) throw new IOException();

		int n;
		int count = 0;

		for (; (n = input.read(buffer)) != -1; count += n) {
			output.write(buffer, 0, n);
		}

		return count;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);

		return output.toByteArray();
	}

	public static String toString(InputStream in) throws IOException {
		if (in == null) throw new IOException();
		return new String(toByteArray(in));
	}

	public static String toString(Reader reader) throws IOException {
		if (reader == null) throw new IOException();

		int          a;
		char[]       chars = new char[1024];
		StringWriter sw    = new StringWriter();

		while ((a = reader.read(chars)) != -1) {
			sw.write(chars, 0, a);
		}

		return sw.toString();
	}

}
