package org.javaweb.rasp.commons.config;

public abstract class RASPProperties {

	private RASPConfigMap<String, Object> configMap;

	public void reloadConfig(RASPConfigMap<String, Object> configMap) {
		if (configMap == null) {
			throw new NullPointerException();
		}

		this.configMap = configMap;
	}

	public RASPConfigMap<String, Object> getConfigMap() {
		return configMap;
	}

}