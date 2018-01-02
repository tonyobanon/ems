package com.ce.ems.base.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.ce.ems.utils.Utils;

public class AppUtils {

	private static ClassLoader classloader;
	private static Properties config;

	public static Path getPath(String resource) {
		try {
			return Paths.get(classloader.getResource(resource).toURI());
		} catch (URISyntaxException e) {
			Exceptions.throwRuntime(e);
			return null;
		}
	}

	public static InputStream getInputStream(String resource) {
		return classloader.getResourceAsStream(resource);
	}

	public static OutputStream getOutputStream(String resource) {
		try {
			return Files.newOutputStream(getPath(resource));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
			return null;
		}
	}

	public static String getConfig(String key) {
		return config.getProperty(key);
	}

	public static Integer getConfigAsInt(String key) {
		return Integer.parseInt(config.getProperty(key));
	}

	public static Boolean getConfigAsBool(String key) {
		return Boolean.parseBoolean(config.getProperty(key));
	}


	static {
		classloader = Thread.currentThread().getContextClassLoader();
		config = Utils.getProperties("config.properties");
	}

}
