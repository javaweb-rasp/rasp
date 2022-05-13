package org.javaweb.rasp.commons.log;

import org.slf4j.Logger;

public class RASPLogData {

	private final RASPLog raspLog;

	private final Logger logger;

	/**
	 * 是否加密JSON
	 */
	private final boolean encrypt;


	public RASPLogData(RASPLog raspLog, Logger logger, boolean encrypt) {
		this.raspLog = raspLog;
		this.logger = logger;
		this.encrypt = encrypt;
	}

	public RASPLog getRaspLog() {
		return raspLog;
	}

	public Logger getLogger() {
		return logger;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

}
