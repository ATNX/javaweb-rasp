package org.javaweb.rasp.commons.cache;

import java.io.*;

import static org.javaweb.rasp.commons.cache.RASPSerialization.serialization;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public class RASPOutputStreamCache extends OutputStream {

	private int cachedBufferSize = 0;

	/**
	 * 最大缓存大小，默认10M
	 */
	private int maxCacheSize = -1;

	/**
	 * 是否已经序列化了
	 */
	private boolean serialized;

	private final ByteArrayOutputStream bout = new ByteArrayOutputStream();

	public RASPOutputStreamCache(int maxCacheSize) {
		// 必须大于0，小于100M
		if (maxCacheSize > 0 && maxCacheSize < 100) {
			this.maxCacheSize = maxCacheSize * 1024 * 1000 * 1000;
		}
	}

	/**
	 * 获取缓存输入流
	 *
	 * @return 缓存输入流
	 * @throws IOException IO异常
	 */
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(bout.toByteArray());
	}

	@Override
	public void write(int b) throws IOException {
		// 检测当前缓存的byte是否已经超过最大的缓存值
		if (maxCacheSize > 0 && cachedBufferSize > maxCacheSize) {
			return;
		}

		bout.write(b);
		cachedBufferSize++;
	}

	@Override
	public void close() {
		completed();
	}

	public void completed() {
		if (serialized) {
			return;
		}

		// 修改序列化状态为true
		serialized = true;

		try {
			serialization(getInputStream());
		} catch (Exception e) {
			AGENT_LOGGER.error(AGENT_NAME + "处理JSON序列化请求异常:" + e, e);
		}
	}

}
