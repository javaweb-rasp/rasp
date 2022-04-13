package java.rasp.proxy.loader;

import static java.lang.String.format;

/**
 * Creator: yz
 * Date: 2019-07-31
 */
public class RASPHookException extends Exception {

	private static final String EXCEPTION_DESC = "[%s]检测到恶意攻击类型:[%s]，" +
			"您的请求可能包含了恶意攻击行为,请勿尝试非法攻击!";

	private static final String DEFAULT_NAME = "RASP";

	public RASPHookException(RASPModuleType modulesType) {
		super(format(EXCEPTION_DESC, DEFAULT_NAME, modulesType.getModuleDesc()));
	}

	public RASPHookException(String type) {
		super(format(EXCEPTION_DESC, DEFAULT_NAME, type));
	}

	public RASPHookException(String agentName, RASPModuleType modulesType) {
		super(format(EXCEPTION_DESC, agentName, modulesType.getModuleDesc()));
	}

	public RASPHookException(String agentName, String type) {
		super(format(EXCEPTION_DESC, agentName, type));
	}

}
