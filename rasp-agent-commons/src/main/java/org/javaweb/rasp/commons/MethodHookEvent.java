package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.context.RASPHttpRequestContextManager;

import java.rasp.proxy.loader.HookEvent;

public class MethodHookEvent {

	private final HookEvent hookEvent;

	private RASPHttpRequestContext raspContext;

	public MethodHookEvent(HookEvent e) {
		this.hookEvent = e;
	}

	public Object getThisObject() {
		return hookEvent.getThisObject();
	}

	public String getThisClass() {
		return hookEvent.getThisClass();
	}

	public String getThisMethodName() {
		return hookEvent.getThisMethodName();
	}

	public String getThisMethodArgsDesc() {
		return hookEvent.getThisMethodArgsDesc();
	}

	public Object[] getThisArgs() {
		return hookEvent.getThisArgs();
	}

	/**
	 * 通过传入参数数组下标获取Hook方法的单个参数值
	 *
	 * @param index 索引
	 * @param <T>   参数类型
	 * @return 索引对应的类型
	 */
	public <T> T getThisArg(int index) {
		Object[] thisArgs = hookEvent.getThisArgs();

		if (thisArgs.length > index) {
			return (T) thisArgs[index];
		}

		return null;
	}

	/**
	 * 获取返回值,如果方法无返回值return null
	 *
	 * @return 返回值对象
	 */
	public <T> T getThisReturnValue() {
		return (T) hookEvent.getThisReturnValue();
	}

	public int getThisMethodEvent() {
		return hookEvent.getThisMethodEvent();
	}

	public int getHookHash() {
		return hookEvent.getHookHash();
	}

	public RASPHttpRequestContext getRASPContext() {
		if (raspContext != null) {
			return raspContext;
		}

		return this.raspContext = RASPHttpRequestContextManager.getContext();
	}

	/**
	 * 检测是否包含Http请求
	 *
	 * @return 返回当前线程中是否包含了Http请求(context 、 request 、 response都不为空)
	 */
	public boolean hasRequest() {
		return getRASPContext() != null;
	}

}
