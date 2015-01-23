/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client;

/**
 * Http request contract.
 * Each method (except #execute) should return
 * original object to allow chaining.
 *
 * TODO Add method to add cache headers
 */
public interface HttpRequest {

	/**
	 * Return http request method.
	 *
	 * @return Method.
	 */
	HttpMethod getMethod();

	/**
	 * Add getHeader.
	 *
	 * @param name Header name.
	 * @param value Header value.
	 * @return Http request that can be used for chaining.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name or value is blank.
	 */
	HttpRequest addHeader(String name, String value);

	/**
	 * Add query parameters.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest addQueryParam(String name, String value);

	/**
	 * Add collection of query parameters.
	 *
	 * @param parameter Parameter.
	 * @param parameters Optional next parameters.
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest addQueryParams(HttpParameter parameter, HttpParameter... parameters);

	/**
	 * Add form parameters.
	 * This method should be used for POST or PUT request only, otherwise
	 * it will throw {@link UnsupportedOperationException} exception.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Http request that can be used for chaining.
	 * @throws UnsupportedOperationException if request is not POST or PUT request.
	 */
	HttpRequest addFormParam(String name, String value);

	/**
	 * Add collection of parameters.
	 * This method should be used for POST or PUT request only, otherwise
	 * it will throw {@link UnsupportedOperationException} exception.
	 *
	 * @param parameter Parameter.
	 * @param parameters Optional next parameters.
	 * @return Http request that can be used for chaining.
	 * @throws UnsupportedOperationException if request is not POST or PUT request.
	 */
	HttpRequest addFormParams(HttpParameter parameter, HttpParameter... parameters);

	/**
	 * Set request body.
	 * This method should be used for POST or PUT request only, otherwise
	 * it will throw {@link UnsupportedOperationException} exception.
	 *
	 * @param body Body request.
	 * @return Http request that can be used for chaining.
	 * @throws UnsupportedOperationException if request is not POST or PUT request.
	 */
	HttpRequest setBody(String body);

	/**
	 * Add getHeader specific to standard js library.
	 *
	 * Most library (such as jQuery) add automatically getHeader
	 * named "X-Requested-With" with value "XMLHttpRequest", this
	 * method add this getHeader and can be used to simulate ajax
	 * call.
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest asXmlHttpRequest();

	/**
	 * Add getHeader to specify that content type
	 * is "application/x-www-form-urlencoded".
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest asFormUrlEncoded();

	/**
	 * Add getHeader to specify that content type
	 * is "multipart/form-data".
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest asMultipartFormData();

	/**
	 * Add getHeader to specify that content type
	 * is "application/json".
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest asJson();

	/**
	 * Add getHeader to specify that content type
	 * is "application/xml".
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest asXml();

	/**
	 * Add getHeader to specify that accept type
	 * is "application/json".
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest acceptJson();

	/**
	 * Add getHeader to specify that accept type
	 * is "application/xml".
	 *
	 * @return Http request that can be used for chaining.
	 */
	HttpRequest acceptXml();

	/**
	 * Execute request and return http response.
	 * Execution is synchronous and will block until
	 * response is available.
	 *
	 * @return Http response.
	 */
	HttpResponse execute();

	/**
	 * Execute request and return http response.
	 * Execution is synchronous and will block until
	 * response is available.
	 *
	 * This method automatically add json getHeader (i.e
	 * methods #asJson and #acceptJson will be automatically
	 * called before execution).
	 *
	 * @return Http response.
	 */
	HttpResponse executeJson();

	/**
	 * Execute request and return http response.
	 * Execution is synchronous and will block until
	 * response is available.
	 *
	 * This method automatically add xml getHeader (i.e
	 * methods #asXml and #acceptXml will be automatically
	 * called before execution).
	 *
	 * @return Http response.
	 */
	HttpResponse executeXml();
}
