package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.cache.RASPCachedParameter;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

import java.rasp.proxy.loader.HookResult;

/**
 * Creator: yz
 * Date: 2019-08-01
 */
public interface RASPParameterFilter {

	HookResult<String[]> filter(RASPCachedParameter parameter, RASPHttpRequestContext context);

}
