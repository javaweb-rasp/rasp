package org.javaweb.rasp.commons.sync;

public abstract class RASPThreadSyncConfig {

	/**
	 * 是否是运行中
	 */
	private boolean running;

	/**
	 * 间隔时间
	 */
	private long syncInterval;

	public RASPThreadSyncConfig(long syncInterval, boolean running) {
		this.syncInterval = syncInterval;
		this.running = running;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getSyncInterval() {
		return this.syncInterval;
	}

	public void setSyncInterval(long syncInterval) {
		this.syncInterval = syncInterval;
	}

	public abstract void dataSynchronization();

}
