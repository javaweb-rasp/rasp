package java.rasp.proxy.loader;

import static java.rasp.proxy.loader.HookResultType.*;

/**
 * Creator: yz
 * Date: 2019-06-20
 */
public class HookResult<T> {

	public static final HookResult<String> DEFAULT_STRING_RESULT = new HookResult<String>(RETURN);

	/**
	 * Hook结果处理方式
	 */
	private HookResultType hookResultType;

	/**
	 * Hook抛出的异常,如果HookMethodType的值为THROW那么exception的值必须设置
	 */
	private RASPHookException exception;

	/**
	 * Hook返回值,如果Hook的方法有返回值且HookMethodType的值为REPLACE,那么returnValue值必须被修改
	 */
	private T returnValue;

	/**
	 * 是否强制阻断
	 */
	private boolean forceReplace = false;

	public HookResult(HookResultType handlerType) {
		this.hookResultType = handlerType;
	}

	public HookResult(HookResultType handlerType, T returnValue) {
		this.hookResultType = handlerType;
		this.returnValue = returnValue;
	}

	public HookResult(HookResultType handlerType, T returnValue, boolean forceReplace) {
		this.hookResultType = handlerType;
		this.returnValue = returnValue;
		this.forceReplace = forceReplace;
	}

	public HookResult(HookResultType handlerType, RASPHookException exception) {
		this.hookResultType = handlerType;
		this.exception = exception;
	}

	/**
	 * 创建Hook处理结果对象
	 *
	 * @param handlerType  Hook处理类型
	 * @param exception    异常
	 * @param returnValue  返回值
	 * @param forceReplace 是否强制替换
	 */
	public HookResult(HookResultType handlerType, RASPHookException exception, T returnValue, boolean forceReplace) {
		this.hookResultType = handlerType;
		this.exception = exception;
		this.returnValue = returnValue;
		this.forceReplace = forceReplace;
	}

	public HookResultType getRASPHookResultType() {
		return hookResultType;
	}

	public void setRASPHookResultType(HookResultType hookResultType) {
		this.hookResultType = hookResultType;
	}

	public RASPHookException getException() {
		return exception;
	}

	public void setException(RASPHookException exception) {
		this.exception = exception;
	}

	public T getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(T returnValue) {
		this.returnValue = returnValue;
	}

	public boolean isForceReplace() {
		return forceReplace;
	}

	public void setForceReplace(boolean forceReplace) {
		this.forceReplace = forceReplace;
	}

	public HookResult<String> stringResult() {
		if (hookResultType == RETURN) {
			return DEFAULT_STRING_RESULT;
		} else if (hookResultType == THROW) {
			return new HookResult<String>(THROW, exception);
		}

		String value = null;

		if (this.returnValue instanceof String[]) {
			String[] values = (String[]) this.returnValue;

			if (values.length > 0) {
				value = values[0];
			}
		}

		// 替换返回值
		return new HookResult<String>(REPLACE_OR_BLOCK, value, forceReplace);
	}

	@Override
	public String toString() {
		return "HookResult{" +
				"hookResultType=" + hookResultType +
				", exception=" + exception +
				", returnValue=" + returnValue +
				", forceReplace=" + forceReplace +
				'}';
	}

}
