package org.javaweb.rasp.commons.cache;

import java.io.IOException;
import java.io.InputStream;

public class RASPByteArrayInputStream extends InputStream {

	protected byte[] buf;

	protected int pos;

	protected int mark = 0;

	protected int count;

	protected boolean closed;

	public RASPByteArrayInputStream(byte[] buf) {
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	public RASPByteArrayInputStream(byte[] buf, int offset, int length) {
		this.buf = buf;
		this.pos = offset;
		this.count = Math.min(offset + length, buf.length);
		this.mark = offset;
	}

	public byte[] getBuf() {
		return buf;
	}

	public String getBufString() {
		return new String(buf, pos, count);
	}

	public int getPos() {
		return pos;
	}

	public int getMark() {
		return mark;
	}

	public int getCount() {
		return count;
	}

	public boolean isClosed() {
		return closed;
	}

	public synchronized int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	public synchronized int read(byte[] b, int off, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}

		if (pos >= count) {
			return -1;
		}

		int avail = count - pos;

		if (len > avail) {
			len = avail;
		}

		if (len <= 0) {
			return 0;
		}

		System.arraycopy(buf, pos, b, off, len);
		pos += len;

		return len;
	}

	public synchronized long skip(long n) {
		long k = count - pos;

		if (n < k) {
			k = n < 0 ? 0 : n;
		}

		pos += k;
		return k;
	}

	public synchronized int available() {
		return count - pos;
	}

	public boolean markSupported() {
		return true;
	}

	public void mark(int readAheadLimit) {
		mark = readAheadLimit;
	}

	public synchronized void reset() {
		pos = mark;
	}

	public void close() throws IOException {
		buf = null;

		this.closed = true;
	}

}
