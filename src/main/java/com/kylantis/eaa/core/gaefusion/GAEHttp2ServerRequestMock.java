package com.kylantis.eaa.core.gaefusion;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.core.Exceptions;

import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpFrame;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerFileUpload;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.impl.Http2HeadersAdaptor;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Locale;
import io.vertx.ext.web.ParsedHeaderValues;
import io.vertx.ext.web.impl.HeaderParser;
import io.vertx.ext.web.impl.ParsableHeaderValue;
import io.vertx.ext.web.impl.ParsableHeaderValuesContainer;
import io.vertx.ext.web.impl.ParsableLanguageValue;
import io.vertx.ext.web.impl.ParsableMIMEValue;

public class GAEHttp2ServerRequestMock implements HttpServerRequest {

	private HttpServletRequest req;

	protected MultiMap headersMap = new Http2HeadersAdaptor(new DefaultHttp2Headers());
	protected MultiMap paramsMap = new Http2HeadersAdaptor(new DefaultHttp2Headers());
	protected MultiMap formAttributesMap = new Http2HeadersAdaptor(new DefaultHttp2Headers());

	protected Buffer body;

	private String path;
	private Boolean isSSL;
	private HttpMethod method;
	private String scheme;
	private SocketAddress localAddr;
	private SocketAddress remoteAddr;
	private Map<String, Cookie> cookies = new FluentHashMap<>();
	private Collection<FileUpload> fileUploads = new FluentArrayList<>();
	private List<Locale>locales = new FluentArrayList<>();
	private ParsableHeaderValuesContainer parsedHeaders;
	
	public GAEHttp2ServerRequestMock(HttpServletRequest request) {
		
		String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
		
		//Logger.info("------- " + "Adapting GAE servlet request @ " + path + " for fusion service" + "-------");
		
		this.req = request;
		
		// Add request headers

		Map<String, String> _headers = new FluentHashMap<>();
		Enumeration<String> _headerNames = request.getHeaderNames();
 
		while (_headerNames.hasMoreElements()) {
			String o = _headerNames.nextElement();
			_headers.put(o, req.getHeader(o));
		}
		this.headersMap.addAll(_headers);

		// Add request parameters

		request.getParameterMap().forEach((k, v) -> {
			for (String vv : v) {
				this.paramsMap.add(k, vv);
			}
		});

		// Add form attributes

		Map<String, String> _formAttributes = new FluentHashMap<>();
		Enumeration<String> _formAttributeNames = request.getAttributeNames();

		while (_formAttributeNames.hasMoreElements()) {
			String o = _formAttributeNames.nextElement();
			_formAttributes.put(o, req.getAttribute(o).toString());
		}
		this.formAttributesMap.addAll(_formAttributes);

		// Add extra information

		this.path = path;
		this.isSSL = request.isSecure();
		this.method = HttpMethod.valueOf(request.getMethod());
		this.scheme = request.getScheme();

		this.localAddr = SocketAddress.inetSocketAddress(request.getLocalPort(), request.getLocalName());
		this.remoteAddr = SocketAddress.inetSocketAddress(request.getRemotePort(), request.getRemoteHost());

		// Add cookies
 
		if(request.getCookies() != null) {
			for (javax.servlet.http.Cookie c : request.getCookies()) {
				cookies.put(c.getName(), HelperUtils.toVertxCookie(c));
			}
		}
		
		// Add request body
		try {
			this.body = Buffer.buffer(IOUtils.toByteArray(request.getInputStream()));
		
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		
		//Add multi-part data, if available
		
		if (request.getContentType() != null && request.getContentType().equals("multipart/form-data")) {

			try {
				Collection<Part> parts = request.getParts();

				for (Part part : parts) {
					fileUploads.add(new FileUploadImpl(part.getInputStream()));
				}
			} catch (IOException | ServletException e) {
				Exceptions.throwRuntime(e);
			}
			
		}
		
		
		// Add locales
		
		Enumeration<java.util.Locale> locales = request.getLocales();
		while (locales.hasMoreElements()) {
			java.util.Locale locale = locales.nextElement();
			this.locales.add(new LocaleImpl(locale.getLanguage(), locale.getCountry(), locale.getVariant()));
		}
		
		
		// Parse headers
	    String accept = request.getHeader("Accept");
	    String acceptCharset = request.getHeader ("Accept-Charset");
	    String acceptEncoding = request.getHeader("Accept-Encoding");
	    String acceptLanguage = request.getHeader("Accept-Language");
	    String contentType = ensureNotNull(request.getHeader("Content-Type"));

	    parsedHeaders = new ParsableHeaderValuesContainer(
	        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(accept, ParsableMIMEValue::new)),
	        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptCharset, ParsableHeaderValue::new)),
	        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptEncoding, ParsableHeaderValue::new)),
	        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptLanguage, ParsableLanguageValue::new)),
	        new ParsableMIMEValue(contentType)
	    );
	}

	protected Map<String, Cookie> cookies() {
		return cookies;
	}
	
	protected Buffer body() {
		return body;
	}
	
	protected Set<FileUpload> fileUploads() {
		return (Set<FileUpload>) fileUploads;
	}
	
	protected List<Locale> locales() {
		return locales;
	}
	
	protected ParsedHeaderValues parsedHeaders() {
		return parsedHeaders;
	}
	
	@Override
	public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest handler(Handler<Buffer> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest pause() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest resume() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest endHandler(Handler<Void> endHandler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpVersion version() {
		return HttpVersion.HTTP_2;
	}

	@Override
	public HttpMethod method() {
		return method;
	}

	@Override
	public String rawMethod() {
		return method.name();
	}

	@Override
	public boolean isSSL() {
		return isSSL;
	}

	@Override
	public @Nullable String scheme() {
		return scheme;
	}

	@Override
	public String uri() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable String path() {
		return path;
	}

	@Override
	public @Nullable String query() {
		return req.getQueryString();
	}

	@Override
	public @Nullable String host() {
		return remoteAddress().host();
	}

	@Override
	public HttpServerResponse response() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MultiMap headers() {
		return this.headersMap;
	}

	@Override
	public @Nullable String getHeader(String headerName) {
		return headers().get(headerName);
	}

	@Override
	public String getHeader(CharSequence headerName) {
		return headers().get(headerName);
	}

	@Override
	public MultiMap params() {
		return paramsMap;
	}

	@Override
	public @Nullable String getParam(String paramName) {
		return params().get(paramName);
	}

	@Override
	public SocketAddress remoteAddress() {
		return remoteAddr;
	}

	@Override
	public SocketAddress localAddress() {
		return localAddr;
	}

	@Override
	public SSLSession sslSession() {
		throw new UnsupportedOperationException();
	}

	@Override
	public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String absoluteURI() {
		throw new UnsupportedOperationException();
	}

	@Override
	public NetSocket netSocket() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest setExpectMultipart(boolean expect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isExpectMultipart() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest uploadHandler(@Nullable Handler<HttpServerFileUpload> uploadHandler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MultiMap formAttributes() {
		return formAttributesMap;
	}

	@Override
	public @Nullable String getFormAttribute(String attributeName) {
		return formAttributes().get(attributeName);
	}

	@Override
	public ServerWebSocket upgrade() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEnded() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerRequest customFrameHandler(Handler<HttpFrame> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpConnection connection() {
		throw new UnsupportedOperationException();
	}
	

	  private String ensureNotNull(String string){
	    return string == null ? "" : string;
	  }
	
}
