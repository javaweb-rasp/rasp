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

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.util.ContextUtil;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.FileUtil;
import org.javaweb.rasp.commons.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static ch.qos.logback.core.CoreConstants.*;

/**
 * FileAppender appends log events to a file.
 * <p>
 * For more information about this appender, please refer to the online manual
 * at http://logback.qos.ch/manual/appenders.html#FileAppender
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class RASPFileAppender<E> extends RASPOutputStreamAppender<E> {

	public static final long DEFAULT_BUFFER_SIZE = 8192;

	protected static String COLLISION_WITH_EARLIER_APPENDER_URL = CODES_URL + "#earlier_fa_collision";

	/**
	 * Append to or truncate the file? The default value for this variable is
	 * <code>true</code>, meaning that by default a <code>FileAppender</code> will
	 * append to an existing file and not truncate it.
	 */
	protected boolean append = true;

	/**
	 * The name of the active log file.
	 */
	protected String fileName = null;

	private boolean prudent = false;

	private FileSize bufferSize = new FileSize(DEFAULT_BUFFER_SIZE);

	private long fileSize;

	private final long minFileSize = 1024 * 1000;

	/**
	 * The <b>File</b> property takes a string value which should be the name of
	 * the file to append to.
	 */
	public void setFile(String file) {
		if (file == null) {
			fileName = file;
		} else {
			// Trim spaces from both ends. The users probably does not want
			// trailing spaces in file names.
			fileName = file.trim();
		}
	}

	/**
	 * Returns the value of the <b>Append</b> property.
	 */
	public boolean isAppend() {
		return append;
	}

	/**
	 * This method is used by derived classes to obtain the raw file property.
	 * Regular users should not be calling this method.
	 *
	 * @return the value of the file property
	 */
	final public String rawFileProperty() {
		return fileName;
	}

	/**
	 * Returns the value of the <b>File</b> property.
	 *
	 * <p>
	 * This method may be overridden by derived classes.
	 */
	public String getFile() {
		return fileName;
	}

	/**
	 * If the value of <b>File</b> is not <code>null</code>, then
	 * {@link #openFile} is called with the values of <b>File</b> and
	 * <b>Append</b> properties.
	 */
	public void start() {
		int errors = 0;

		if (getFile() != null) {
			addInfo("File property is set to [" + fileName + "]");

			if (prudent) {
				if (!isAppend()) {
					setAppend(true);
					addWarn("Setting \"Append\" property to true on account of \"Prudent\" mode");
				}
			}

			if (checkForFileCollisionInPreviousFileAppenders()) {
				addError("Collisions detected with FileAppender/RollingAppender instances defined earlier. Aborting.");
				addError(MORE_INFO_PREFIX + COLLISION_WITH_EARLIER_APPENDER_URL);
				errors++;
			} else {
				// file should be opened only if collision free
				try {
					openFile(getFile());
				} catch (IOException e) {
					errors++;
					addError("openFile(" + fileName + "," + append + ") call failed.", e);
				}
			}
		} else {
			errors++;
			addError("\"File\" property not set for appender named [" + name + "].");
		}

		if (errors == 0) {
			super.start();
		}
	}

	@Override
	public void stop() {
		super.stop();

		Map<String, String> map = ContextUtil.getFilenameCollisionMap(context);

		if (map == null || getName() == null)
			return;

		map.remove(getName());
	}

	protected boolean checkForFileCollisionInPreviousFileAppenders() {
		boolean collisionsDetected = false;

		if (fileName == null) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) context.getObject(FA_FILENAME_COLLISION_MAP);

		if (map == null) {
			return collisionsDetected;
		}

		for (Entry<String, String> entry : map.entrySet()) {
			if (fileName.equals(entry.getValue())) {
				addErrorForCollision("File", entry.getValue(), entry.getKey());
				collisionsDetected = true;
			}
		}

		if (name != null) {
			map.put(getName(), fileName);
		}

		return collisionsDetected;
	}

	protected void addErrorForCollision(String optionName, String optionValue, String appenderName) {
		addError("'" + optionName + "' option has the same value \"" +
				optionValue + "\" as that given for appender [" + appenderName + "] defined earlier.");
	}

	/**
	 * <p>
	 * Sets and <i>opens</i> the file where the log output will go. The specified
	 * file must be writable.
	 *
	 * <p>
	 * If there was already an opened file, then the previous file is closed
	 * first.
	 *
	 * <p>
	 * <b>Do not use this method directly. To configure a FileAppender or one of
	 * its subclasses, set its properties one by one and then call start().</b>
	 *
	 * @param fileName The path to the log file.
	 */
	public void openFile(String fileName) throws IOException {
		lock.lock();

		try {
			File    file   = new File(fileName);
			boolean result = FileUtil.createMissingParentDirectories(file);

			if (!result) {
				addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
			}

			ResilientFileOutputStream stream = new ResilientFileOutputStream(file, append, bufferSize.getSize());
			stream.setContext(context);
			setOutputStream(stream);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @return true if in prudent mode
	 * @see #setPrudent(boolean)
	 */
	public boolean isPrudent() {
		return prudent;
	}

	/**
	 * When prudent is set to true, file appenders from multiple JVMs can safely
	 * write to the same file.
	 *
	 * @param prudent
	 */
	public void setPrudent(boolean prudent) {
		this.prudent = prudent;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public void setBufferSize(FileSize bufferSize) {
		addInfo("Setting bufferSize to [" + bufferSize.toString() + "]");
		this.bufferSize = bufferSize;
	}

	private void safeWrite(E event) throws IOException {
		ResilientFileOutputStream resilientFOS = getOutputStream();
		FileChannel               fileChannel  = resilientFOS.getChannel();

		if (fileChannel == null) {
			return;
		}

		// Clear any current interrupt (see LOGBACK-875)
		boolean interrupted = Thread.interrupted();

		FileLock fileLock = null;

		try {
			fileLock = fileChannel.lock();
			long position = fileChannel.position();
			long size     = fileChannel.size();

			if (size != position) {
				fileChannel.position(size);
			}

			super.writeOut(event);
		} catch (IOException e) {
			// Mainly to catch FileLockInterruptionExceptions (see LOGBACK-875)
			resilientFOS.postIOFailure(e);
		} finally {
			if (fileLock != null && fileLock.isValid()) {
				fileLock.release();
			}

			// Re-interrupt if we started in an interrupted state (see LOGBACK-875)
			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	protected void writeOut(E event) throws IOException {
		if (prudent) {
			safeWrite(event);
		} else {
			super.writeOut(event);
		}
	}

	public synchronized void rollover() {
		File file       = new File(getFile());
		File parentFile = file.getParentFile();

		if (file.exists() && file.length() > 0) {
			try {
				stop();

				// 临时日志文件
				File targetFile = new File(getFile() + "." + System.nanoTime() + ".txt");

				if (!parentFile.exists()) {
					if (!parentFile.mkdirs()) {
						throw new RolloverFailure("Directory [" + parentFile + "] create failed.");
					}
				}

				if (file.renameTo(targetFile)) {
					try {
						if (targetFile.length() > fileSize) {
							List<File> fileList = FileUtils.split(targetFile, fileSize);

							// 如果切割了多个文件，直接删除切割之前的文件
							if (fileList.size() > 0) {
								targetFile.delete();
							}
						}
					} catch (IOException e) {
						throw new RolloverFailure("File [" + targetFile + "] split failed.");
					}
				} else {
					throw new RolloverFailure("File [" + file + "] rename failed.");
				}
			} finally {
				start();
			}
		}
	}

	public void setFileSize(long fileSize) {
		if (fileSize > 0) {
			this.fileSize = fileSize;
		} else {
			this.fileSize = minFileSize;
		}
	}

}
