package java.rasp.proxy.loader;

/**
 * Creator: yz
 * Date: 2019-07-31
 */
public class RASPHookException extends Exception {

	public RASPHookException(RASPModuleType modulesType) {
		super("RASP检测到恶意攻击类型:" + modulesType.getModuleDesc() + "，您的请求可能包含了恶意攻击行为,请勿尝试非法攻击!");
	}

}
