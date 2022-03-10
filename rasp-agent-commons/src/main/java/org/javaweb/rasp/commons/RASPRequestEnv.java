package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.config.RASPAppProperties;
import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RASPRequestEnv {

	private final String documentName;

	private final RASPPropertiesConfiguration<RASPAppProperties> appConfig;

	public static final Set<Class<?>> API_CLASS_LIST = new HashSet<Class<?>>();

	public static final Class<?>[] ARG_TYPES = new Class[]{Map.class, RASPRequestEnv.class};

	public static final Map<String, Method> API_METHOD_MAP = new HashMap<String, Method>();

	public RASPRequestEnv(RASPPropertiesConfiguration<RASPAppProperties> appConfig) {
		this.appConfig = appConfig;
		String configFileName = appConfig.getConfigFile().getName();
		this.documentName = configFileName.substring(0, configFileName.lastIndexOf("."));
	}

	public String getDocumentName() {
		return documentName;
	}

	public RASPPropertiesConfiguration<RASPAppProperties> getAppConfig() {
		return appConfig;
	}

}
