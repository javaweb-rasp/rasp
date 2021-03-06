package org.javaweb.rasp.commons.utils;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

import static javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier;
import static javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory;

public class SslUtils {

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager   tm            = new MITM();
		trustAllCerts[0] = tm;

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);

		setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	/**
	 * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
	 */
	public static void ignoreSsl() {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};

		try {
			trustAllHttpsCertificates();
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultHostnameVerifier(hv);
	}

	private static class MITM implements TrustManager, X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

	}

}