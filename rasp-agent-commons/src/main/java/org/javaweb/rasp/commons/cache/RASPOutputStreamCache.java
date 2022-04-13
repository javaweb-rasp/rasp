package org.javaweb.rasp.commons.cache;

import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

import java.io.IOException;
import java.io.OutputStream;
import java.rasp.proxy.loader.HookResult;

import static java.rasp.proxy.loader.HookResultType.THROW;
import static org.javaweb.rasp.commons.cache.RASPSerialization.serialization;

public class RASPOutputStreamCache extends OutputStream {

	private int cachedBufferSize = 0;

	/**
	 * 最大缓存大小，默认10M
	 */
	private int maxCacheSize = -1;

	/**
	 * 是否已经反序列化了
	 */
	private boolean deserialized;

	/**
	 * RASP上下文
	 */
	RASPHttpRequestContext context;

	/**
	 * 非API请求默认最大值不能超过100M
	 */
	private static final int DEFAULT_MAX_SIZE = 100 * 1024 * 1024;

	private final RASPByteArrayOutputStream cachedStream = new RASPByteArrayOutputStream();

	public RASPOutputStreamCache(RASPHttpRequestContext context) {
		this.context = context;

		int contentLength = context.getContentLength();
		int maxCacheSize  = context.getMaxStreamCacheSize();

		// 非API请求必须限制缓存流字节数
		if (!context.isWebApiRequest()) {
			// 缓存字节数最大值那么必须大于0，小于100M
			if (contentLength == -1 || contentLength > DEFAULT_MAX_SIZE) {
				this.maxCacheSize = maxCacheSize;
				return;
			}

		}

		this.maxCacheSize = contentLength;
	}

	/**
	 * 获取缓存输入流
	 *
	 * @return 缓存输入流
	 */
	public RASPByteArrayInputStream getInputStream() {
		return cachedStream.toRASPByteArrayInputStream();
	}

	@Override
	public void write(int b) throws IOException {
		// 检测当前缓存的byte是否已经超过最大的缓存值
		if (maxCacheSize > 0 && cachedBufferSize > maxCacheSize) {
			return;
		}

		cachedStream.write(b);
		cachedBufferSize++;

		// 检测缓存流是否达到了最大值
		if (cachedBufferSize == maxCacheSize) {
			completed();
		}
	}

	@Override
	public void close() throws IOException {
		completed();
	}

	public void completed() throws IOException {
		// 检测是否已反序列化，非API请求不需要反序列化
		if (deserialized || !context.isWebApiRequest()) {
			return;
		}

		// 修改序列化状态为true
		deserialized = true;

		HookResult<?> result = serialization(context, getInputStream());

		// 检测到有攻击且非静默模式需要抛出异常阻断程序逻辑
		if (result.getRASPHookResultType() == THROW && !context.isSilent()) {
			throw new IOException(result.getException().getMessage());
		}
	}

}
