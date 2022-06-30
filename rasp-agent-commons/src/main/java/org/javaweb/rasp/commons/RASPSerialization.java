package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.cache.RASPByteArrayInputStream;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

import java.rasp.proxy.loader.HookResult;

public interface RASPSerialization {

	HookResult<?> deserialization(RASPHttpRequestContext context, RASPByteArrayInputStream in);

}
