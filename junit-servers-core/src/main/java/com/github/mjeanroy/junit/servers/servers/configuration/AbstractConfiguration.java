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

package com.github.mjeanroy.junit.servers.servers.configuration;

import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;
import com.github.mjeanroy.junit.servers.servers.Hook;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

/**
 * Generic configuration that should be extended for
 * each custom embedded server.
 */
public abstract class AbstractConfiguration {

	/**
	 * Server Path.
	 * This path is "/" by default, but it can be customized (and path
	 * suffix will have to be used to query application url).
	 */
	private final String path;

	/**
	 * Webapp Path.
	 *
	 * By default, the path follow maven convention and is
	 * set to "src/main/webapp".
	 *
	 * Path should be customized if project do not follow maven
	 * convention or webapp path must be set to maven sub-module.
	 */
	private final String webapp;

	/**
	 * Server port, default is to use a random port.
	 *
	 * Use this configuration port to define a specific port, otherwise
	 * use a random port to be sure application start on an
	 * available port.
	 */
	private final int port;

	/**
	 * Additional classpath.
	 *
	 * The path will be added to application classpath
	 * before server is started.
	 *
	 * This additional classpath entry is useful to start
	 * application configured with java configuration instead
	 * of "classic" web.xml file.
	 */
	private final String classpath;

   /**
	 * Additional parent (classloader) classpath.
	 *
	 * The path will be added to application parent classlodaer classpath
	 * before server is started.
	 *
	 */
	private final Collection<URL> parentClasspath;
	
	/**
	 * Map of environment properties to set before server start.
	 *
	 * These properties could also be set / unset using
	 * a custom hooks but since it is a common tasks, this is
	 * supported under the hood.
	 *
	 * Note that custom properties will be set before embedded server
	 * is started and removed after server is stopped.
	 */
	private final Map<String, String> envProperties;

	/**
	 * Execution hooks.
	 *
	 * These hooks will be executed during server start and stop
	 * phases.
	 *
	 * Be careful that these hooks should be thread safe.
	 */
	private final List<Hook> hooks;

	/**
	 * The path to the custom descriptor file (a.k.a web.xml).
	 */
	private final String overrideDescriptor;

	/**
	 * Initialize configuration.
	 *
	 * @param builder Configuration builder.
	 */
	protected AbstractConfiguration(AbstractConfigurationBuilder<?, ?> builder) {
		this.classpath = builder.getClasspath();
		this.path = builder.getPath();
		this.webapp = builder.getWebapp();
		this.port = builder.getPort();
		this.envProperties = builder.getEnvProperties();
		this.hooks = builder.getHooks();
		this.parentClasspath = builder.getParentClasspath();
		this.overrideDescriptor = builder.getOverrideDescriptor();
	}

	/**
	 * Get {@link #path}.
	 *
	 * @return {@link #path}
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get {@link #webapp}.
	 *
	 * @return {@link #webapp}
	 */
	public String getWebapp() {
		return webapp;
	}

	/**
	 * Get {@link #classpath}.
	 *
	 * @return {@link #classpath}
	 */
	public String getClasspath() {
		return classpath;
	}

	/**
	 * Get {@link #parentClasspath}, as a non-modifiable collection.
	 *
	 * @return {@link #parentClasspath}
	 */
	public Collection<URL> getParentClasspath() {
		return unmodifiableCollection(parentClasspath);
	}

	/**
	 * Get {@link #port}.
	 *
	 * @return {@link #port}
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get {@link #overrideDescriptor}.
	 *
	 * @return {@link #overrideDescriptor}
	 */
	public String getOverrideDescriptor() {
		return overrideDescriptor ;
	}

	/**
	 * Get {@link #envProperties} as a non-modifiable map.
	 *
	 * @return {@link #envProperties}
	 */
	public Map<String, String> getEnvProperties() {
		return unmodifiableMap(envProperties);
	}

	/**
	 * Get {@link #hooks} as a non-modifiable list.
	 *
	 * @return {@link #hooks}
	 */
	public List<Hook> getHooks() {
		return unmodifiableList(hooks);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof AbstractConfiguration) {
			AbstractConfiguration c = (AbstractConfiguration) o;
			return c.canEqual(this) &&
					Objects.equals(port, c.port) &&
					Objects.equals(path, c.path) &&
					Objects.equals(webapp, c.webapp) &&
					Objects.equals(classpath, c.classpath) &&
					Objects.equals(envProperties, c.envProperties) &&
					Objects.equals(hooks, c.hooks) &&
					Objects.equals(overrideDescriptor, c.overrideDescriptor) &&
					Objects.equals(parentClasspath, c.parentClasspath);
		}

		return false;
	}

	/**
	 * Ensure that an object can be equal to the current instance.
	 *
	 * @param o The tested object.
	 * @return {@code true} if {@code o} can be equal to {@code this}, {@code false} otherwise.
	 */
	protected boolean canEqual(Object o) {
		return o instanceof AbstractConfiguration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(port, path, webapp, classpath, envProperties, hooks, overrideDescriptor, parentClasspath);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("port", port)
			.append("path", path)
			.append("webapp", webapp)
			.append("classpath", classpath)
			.append("overrideDescriptor", overrideDescriptor)
			.append("parentClasspath", parentClasspath)
			.build();
	}
}
