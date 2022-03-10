/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.javaweb.rasp.commons.utils;

/**
 * Copy form:https://github.com/apache/tomcat/blob/main/java/org/apache/tomcat/util/buf/CharChunk.java
 */
public final class CharBuffer {

	private int start;

	private int end;

	private char[] buffer;

	public CharBuffer(char[] buffer) {
		setBuffer(buffer, 0, buffer.length);
	}

	public CharBuffer(char[] buffer, int offset, int len) {
		setBuffer(buffer, offset, len);
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int i) {
		end = i;
	}

	public int getOffset() {
		return start;
	}

	public void setBuffer(char[] buffer, int offset, int len) {
		this.buffer = buffer;
		this.start = offset;
		this.end = start + len;
	}

	public void setOffset(int off) {
		if (end < off) {
			end = off;
		}

		start = off;
	}

	/**
	 * @return the length of the data in the buffer
	 */
	public int getLength() {
		return end - start;
	}

	public char[] getBuffer() {
		return buffer;
	}

	public int indexOf(String src, int srcOff, int srcLen, int myOff) {
		char first = src.charAt(srcOff);

		// 查找第一个字符
		int srcEnd = srcOff + srcLen;

		mainLoop:
		for (int i = myOff + start; i <= (end - srcLen); i++) {
			if (buffer[i] != first) {
				continue;
			}

			// 找到第一个字符, 开始匹配
			int myPos = i + 1;

			for (int srcPos = srcOff + 1; srcPos < srcEnd; ) {
				if (buffer[myPos++] != src.charAt(srcPos++)) {
					continue mainLoop;
				}
			}

			return i - start;
		}

		return -1;
	}

	public int indexOf(char search, int starting) {
		int ret = indexOf(buffer, start + starting, end, search);
		return ret >= start ? ret - start : -1;
	}

	public static int indexOf(char[] buff, int start, int end, char search) {
		int offset = start;

		while (offset < end) {
			char b = buff[offset];

			if (b == search) {
				return offset;
			}

			offset++;
		}

		return -1;
	}

	public static int findBuffer(char[] buffer, int start, int end, char[] search) {
		int offset = start;

		while (offset < end) {
			for (char value : search) {
				if (buffer[offset] == value) {
					return offset;
				}
			}

			offset++;
		}

		return -1;
	}

}