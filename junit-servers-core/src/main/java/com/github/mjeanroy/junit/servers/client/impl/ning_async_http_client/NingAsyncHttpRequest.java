/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import static java.lang.System.nanoTime;

/**
 * Implementation for {@link HttpRequest} that use (ning) async-http-client
 * under the hood.
 *
 * @see <a href="https://github.com/ning/async-http-client">https://github.com/ning/async-http-client</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#NING_ASYNC_HTTP_CLIENT
 */
class NingAsyncHttpRequest extends AbstractHttpRequest implements HttpRequest {

	/**
	 * Original http client, will be used to execute http request.
	 */
	private final AsyncHttpClient client;

	/**
	 * Create http request.
	 *
	 * @param client Client used to execute request using async-http-client.
	 * @param httpMethod Http method.
	 * @param url Request URL.
	 */
	NingAsyncHttpRequest(AsyncHttpClient client, HttpMethod httpMethod, String url) {
		super(url, httpMethod);
		this.client = client;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		RequestBuilder builder = new RequestBuilder()
			.setUrl(getUrl())
			.setMethod(getMethod().getVerb());

		handleQueryParameters(builder);
		handleBody(builder);
		handleHeaders(builder);
		handleCookies(builder);

		Request request = builder.build();
		long start = nanoTime();
		Response response = client.executeRequest(request).get();
		return new NingAsyncHttpResponse(response, nanoTime() - start);
	}

	/**
	 * Add query parameter to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addQueryParam(String, String)
	 */
	private void handleQueryParameters(RequestBuilder builder) {
		for (HttpParameter p : queryParams.values()) {
			builder.addQueryParam(p.getName(), p.getValue());
		}
	}

	/**
	 * Add request body:
	 *
	 * <ul>
	 *   <li>Set request body if {@link #body} is defined.</li>
	 *   <li>Add form parmeters ({@link #formParams}) otherwise if it is not empty.</li>
	 * </ul>
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addFormParam(String, String)
	 * @see RequestBuilder#setBody(String)
	 */
	private void handleBody(RequestBuilder builder) {
		if (!hasBody()) {
			return;
		}

		if (body != null) {
			handleRequestBody(builder);
		} else {
			handleFormParameters(builder);
		}
	}

	/**
	 * Add form parameters to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addFormParam(String, String)
	 */
	private void handleFormParameters(RequestBuilder builder) {
		for (HttpParameter p : formParams.values()) {
			builder.addFormParam(p.getName(), p.getValue());
		}
	}

	/**
	 * Set request body value.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#setBody(String)
	 */
	private void handleRequestBody(RequestBuilder builder) {
		builder.setBody(body);
	}

	/**
	 * Add cookies to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addCookie(com.ning.http.client.cookie.Cookie)
	 */
	private void handleCookies(RequestBuilder builder) {
		for (Cookie cookie : cookies) {
			handleCookie(builder, cookie);
		}
	}

	/**
	 * Add request headers.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addHeader(String, String)
	 */
	private void handleHeaders(RequestBuilder builder) {
		for (HttpHeader header : headers.values()) {
			builder.addHeader(header.getName(), header.serializeValues());
		}
	}

	/**
	 * Add single cookie to the final HTTP request.
	 *
	 * @param builder The pending request.
	 * @param cookie The cookie to add.
	 * @see RequestBuilder#addCookie(com.ning.http.client.cookie.Cookie)
	 */
	private void handleCookie(RequestBuilder builder, Cookie cookie) {
		String name = cookie.getName();
		String value = cookie.getValue();
		boolean wrap = true;
		String domain = cookie.getDomain();
		String path = cookie.getPath();
		boolean secured = cookie.isSecure();
		boolean httpOnly = cookie.isHttpOnly();

		final Long maxAge = cookie.getMaxAge();
		final Long expires = cookie.getExpires();

		final long maxAgeValue;
		if (maxAge != null) {
			// Max-Age must be tested first.
			maxAgeValue = maxAge;
		} else if (expires != null) {
			// Then, fallback to expires
			maxAgeValue = expires - System.currentTimeMillis();
		} else {
			// Final default value.
			maxAgeValue = 0;
		}

		builder.addCookie(com.ning.http.client.cookie.Cookie.newValidCookie(name, value, wrap, domain, path, maxAgeValue, secured, httpOnly));
	}
}
