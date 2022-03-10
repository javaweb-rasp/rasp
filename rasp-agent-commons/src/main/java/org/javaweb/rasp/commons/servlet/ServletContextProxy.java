package org.javaweb.rasp.commons.servlet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

public interface ServletContextProxy {

	ServletContextProxy getContext(String uriPath);

	String getRealPath(String path);

	String getServerInfo();

	Object getAttribute(String name);

	Enumeration<String> getAttributeNames();

	void setAttribute(String name, Object object);

	void removeAttribute(String name);

	URL getResource(String path) throws MalformedURLException;

}
