/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 * <p>
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 * <p>
 * or (per the licensee's choosing)
 * <p>
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package org.javaweb.rasp.commons.logger;

import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * A <code>RollingPolicy</code> is responsible for performing the rolling over
 * of the active log file. The <code>RollingPolicy</code> is also responsible
 * for providing the <em>active log file</em>, that is the live file where
 * logging output will be directed.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public interface RASPRollingPolicy extends LifeCycle {

	/**
	 * Rolls over log files according to implementation policy.
	 *
	 * <p>This method is invoked by {@link RASPRollingFileAppender}, usually at the
	 * behest of its {@link TriggeringPolicy}.
	 *
	 * @throws RolloverFailure Thrown if the rollover operation fails for any reason.
	 */
	void rollover() throws RolloverFailure;

	/**
	 * Get the name of the active log file.
	 *
	 * <p>With implementations such as {@link TimeBasedRollingPolicy}, this
	 * method returns a new file name, where the actual output will be sent.
	 *
	 * <p>On other implementations, this method might return the FileAppender's
	 * file property.
	 */
	String getActiveFileName();

	/**
	 * The compression mode for this policy.
	 *
	 * @return
	 */
	CompressionMode getCompressionMode();

	/**
	 * This method allows RollingPolicy implementations to be aware of their
	 * containing appender.
	 *
	 * @param appender
	 */
	void setParent(RASPFileAppender<?> appender);

}
