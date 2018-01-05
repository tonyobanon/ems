package com.kylantis.eaa.core.gaefusion;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.core.Exceptions;
import com.kylantis.eaa.core.fusion.CookieImpl;

import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.Http2HeadersAdaptor;
import io.vertx.ext.web.Cookie;

public class GAEHttp2ServerResponseMock implements HttpServerResponse {

	private long bytesWritten = 0;
	private boolean isEnded;
	
	private Integer statusCode = 200;
	private MultiMap headersMap = new Http2HeadersAdaptor(new DefaultHttp2Headers());
	private Buffer body = null;
	
	private Map<String, Cookie> cookies = new FluentHashMap<>();

	
	public void transform(HttpServletResponse response) {
	
		//Note: In certain scenarios, some fusion services only call end() in the bodyHandler,
		//which is why we do not enforce isEnded == true

		//Add status code and message
		response.setStatus(statusCode);
		
		
		// Add cookies
		
		cookies.values().forEach(cookie -> {
			response.addCookie(HelperUtils.toServletCookie((CookieImpl) cookie));
		});
		
		// Add headers
		headersMap.forEach(entry -> {
			response.addHeader(entry.getKey(), entry.getValue());
		});
		
		
		//Write body
		if (body != null) {
			
			byte[] bytes = body.getBytes();
			
			response.setBufferSize(bytes.length);
			try {
				IOUtils.write(bytes, response.getOutputStream());
			} catch (IOException e) {
				Exceptions.throwRuntime(e);
			}
		}
		
	}
	
	protected void addCookie(Cookie cookie) {
		if (ended()) {
			throwResponseEndedException();
		}
		this.cookies.put(cookie.getName(), cookie);
	}
	
	@Override
	public HttpServerResponse exceptionHandler(Handler<Throwable> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public HttpServerResponse setStatusCode(int statusCode) {
		if (ended()) {
			throwResponseEndedException();
		}
		this.statusCode = statusCode;
		return this;
	}

	@Override
	public String getStatusMessage() {
		return null;
	}

	@Override
	public HttpServerResponse setStatusMessage(String statusMessage) {
		return this;
	}

	@Override
	public HttpServerResponse setChunked(boolean chunked) {
		return this;
	}

	@Override
	public boolean isChunked() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MultiMap headers() {
		return headersMap;
	}

	@Override
	public HttpServerResponse putHeader(String name, String value) {
		if (ended()) {
			throwResponseEndedException();
		}
		headers().set(name, value);
		return this;
	}

	@Override
	public HttpServerResponse putHeader(CharSequence name, CharSequence value) {
		if (ended()) {
			throwResponseEndedException();
		}
		headers().set(name, value);
		return this;
	}

	@Override
	public HttpServerResponse putHeader(String name, Iterable<String> values) {
		if (ended()) {
			throwResponseEndedException();
		}
		headers().set(name, values);
		return this;
	}

	@Override
	public HttpServerResponse putHeader(CharSequence name, Iterable<CharSequence> values) {
		if (ended()) {
			throwResponseEndedException();
		}
		headers().set(name, values);
		return this;
	}

	@Override
	public MultiMap trailers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse putTrailer(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse putTrailer(CharSequence name, CharSequence value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse putTrailer(String name, Iterable<String> values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse putTrailer(CharSequence name, Iterable<CharSequence> value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse closeHandler(Handler<Void> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse endHandler(@Nullable Handler<Void> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse writeContinue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse write(Buffer chunk) {
		if (ended()) {
			throwResponseEndedException();
		}
		body = chunk;
		bytesWritten = chunk.length();
		return this;
	}

	@Override
	public HttpServerResponse write(String chunk, String enc) {
		return write(Buffer.buffer(chunk, enc));
	}

	@Override
	public HttpServerResponse write(String chunk) {
		return write(Buffer.buffer(chunk));
	}

	@Override
	public void end(String chunk) {
		 write(Buffer.buffer(chunk));
		 end();
	}

	@Override
	public void end(String chunk, String enc) {
		write(Buffer.buffer(chunk, enc));
		end();
	}

	@Override
	public void end(Buffer chunk) {
		write(chunk);
		end();
	}

	@Override
	public void end() {
		isEnded = true;
	}

	@Override
	public HttpServerResponse writeCustomFrame(int type, int flags, Buffer payload) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean writeQueueFull() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse setWriteQueueMaxSize(int maxSize) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse drainHandler(Handler<Void> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse sendFile(String filename, long offset, long length) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse sendFile(String filename, long offset, long length,
			Handler<AsyncResult<Void>> resultHandler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean ended() {
		return isEnded;
	}

	@Override
	public boolean closed() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean headWritten() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse headersEndHandler(@Nullable Handler<Void> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse bodyEndHandler(@Nullable Handler<Void> handler) {
		return this;
	}

	@Override
	public long bytesWritten() {
		return bytesWritten;
	}

	@Override
	public int streamId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset(long code) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse push(HttpMethod method, String host, String path,
			Handler<AsyncResult<HttpServerResponse>> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse push(HttpMethod method, String path, MultiMap headers,
			Handler<AsyncResult<HttpServerResponse>> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse push(HttpMethod method, String host, String path, MultiMap headers,
			Handler<AsyncResult<HttpServerResponse>> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServerResponse push(HttpMethod method, String path, Handler<AsyncResult<HttpServerResponse>> handler) {
		throw new UnsupportedOperationException();
	}
	
	private void throwResponseEndedException() {
		throw new IllegalStateException("The response has already ended, and cannot be written to");
	}
}
