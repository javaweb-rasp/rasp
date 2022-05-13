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
package org.javaweb.rasp.commons.logback;

import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;

import java.util.Date;

import static ch.qos.logback.core.CoreConstants.SEE_FNP_NOT_SET;

/**
 * When rolling over, <code>FixedWindowRollingPolicy</code> renames files
 * according to a fixed window algorithm.
 * <p>
 * For more information about this policy, please refer to the online manual at
 * http://logback.qos.ch/manual/appenders.html#FixedWindowRollingPolicy
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class RASPFixedWindowRollingPolicy extends RASPRollingPolicyBase {

	private FileNamePattern fileNamePattern;

	private final RenameUtil util = new RenameUtil();

	public void start() {
		util.setContext(this.context);

		if (fileNamePatternStr != null) {
			fileNamePattern = new FileNamePattern(fileNamePatternStr, this.context);
			determineCompressionMode();
		} else {
			throw new IllegalStateException("FileNamePattern配置错误" + SEE_FNP_NOT_SET);
		}

		if (isParentPrudent()) {
			addError("Prudent mode is not supported with " + getClass().getName() + ".");
			throw new IllegalStateException("Prudent mode is not supported.");
		}

		if (getParentsRawFileProperty() == null) {
			addError("The File name property must be set before using this rolling policy.");
			throw new IllegalStateException("The \"File\" option must be set.");
		}

		super.start();
	}

	public void rollover() throws RolloverFailure {
		util.rename(getActiveFileName(), fileNamePattern.convert(new Date()));
	}

	/**
	 * Return the value of the parent's RawFile property.
	 */
	public String getActiveFileName() {
		return getParentsRawFileProperty();
	}

}
