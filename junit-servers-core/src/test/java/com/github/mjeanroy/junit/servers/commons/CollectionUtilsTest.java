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

package com.github.mjeanroy.junit.servers.commons;

import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.filter;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.isEmpty;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.join;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectionUtilsTest {

	@Test
	public void it_should_return_true_if_collection_is_null_or_empty() {
		assertThat(isEmpty(null)).isTrue();
		assertThat(isEmpty(emptyList())).isTrue();
	}

	@Test
	public void it_should_return_false_if_collection_is_not_null_and_not_empty() {
		assertThat(isEmpty(singletonList("foo"))).isFalse();
	}

	@Test
	public void it_should_join_elements() {
		String separator = ";";
		assertThat(join(null, separator)).isEqualTo(null);
		assertThat(join(emptyList(), separator)).isEqualTo("");
		assertThat(join(singleton("foo"), separator)).isEqualTo("foo");
		assertThat(join(asList("foo", "bar"), separator)).isEqualTo("foo;bar");
		assertThat(join(asList("foo", "bar"), null)).isEqualTo("foobar");
	}

	@Test
	public void it_should_filter_list() {
		List<Integer> numbers = asList(1, 2, 3, 4, 5, 6);

		List<Integer> results = filter(numbers, new Predicate<Integer>() {
			@Override
			public boolean apply(Integer object) {
				return object % 2 == 0;
			}
		});

		assertThat(results)
				.isNotNull()
				.isNotEmpty()
				.hasSize(3)
				.are(new Condition<Integer>() {
					@Override
					public boolean matches(Integer value) {
						return value % 2 == 0;
					}
				});
	}
}
