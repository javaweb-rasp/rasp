package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.context.RASPHttpRequestContextManager;
import org.javaweb.rasp.commons.loader.hooks.HookEvent;

import static org.javaweb.rasp.commons.context.RASPHttpRequestContextManager.hasRequestContext;

public class MethodHookEvent extends HookEvent {

	private RASPHttpRequestContext raspContext;

	public MethodHookEvent(HookEvent e) {
		super(
				e.getThisObject(), e.getThisClass(), e.getThisMethodName(), e.getThisMethodArgsDesc(),
				e.getThisArgs(), e.getThisReturnValue(), e.getThisMethodEvent(), e.getHookHash()
		);
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
		return hasRequestContext(getRASPContext());
	}

}
