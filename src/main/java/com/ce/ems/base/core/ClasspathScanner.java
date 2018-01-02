package com.ce.ems.base.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.ce.ems.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ClasspathScanner<T> {

	private Path baseDir = getAppBaseDir();

	private final String fileExtension;
	private final String nameSuffix;
	private final ClassIdentityType classIdentityType;
	private final Class<T> classType;

	/**
	 * This constructor should for XML and JSON artifacts
	 */
	public ClasspathScanner(String nameSuffix, String fileExtension, Class<T> type) {

		this.nameSuffix = nameSuffix;
		this.fileExtension = fileExtension;

		this.classIdentityType = null;
		this.classType = type;

	}

	/**
	 * This constructor should be used for classes
	 */
	public ClasspathScanner(String nameSuffix, Class<T> type, ClassIdentityType identityType) {

		this.nameSuffix = nameSuffix;
		this.fileExtension = "class";

		this.classIdentityType = identityType;
		this.classType = type;
	}

	/**
	 * This constructor should be used for classes
	 */
	public ClasspathScanner(Class<T> type, ClassIdentityType identityType) {
		this(null, type, identityType);
	}

	private static boolean isExtensionSiupported(String ext) {
		return /* ext.equals("json") || */ext.equals("xml");
	}

	public List<T> scanArtifacts() {

		if (!isExtensionSiupported(fileExtension)) {
			Exceptions.throwRuntime(new RuntimeException("The specified file extension is not supported"));
		}

		if (classType == null) {
			return new ArrayList<>();
		}

		List<T> classes = new ArrayList<T>();

		// Scan classpath

		try {

			Files.walkFileTree(getAppBaseDir(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					String fullPath = file.toString();

					if (!fullPath.endsWith(nameSuffix + "." + fileExtension)) {
						return FileVisitResult.CONTINUE;
					}

					try {

						ObjectMapper xmlMapper = new XmlMapper();
						T o = xmlMapper.readValue(Utils.getString(fullPath), classType);
						classes.add(o);

					} finally {
						// Do nothing
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						// directory iteration failed
						throw e;
					}
				}
			});

		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}

		return classes;

	}

	public List<Class<? extends T>> scanClasses() {

		if (classIdentityType == null || classType == null) {
			return new ArrayList<>();
		}

		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>();

		// Scan classpath

		try {

			Files.walkFileTree(getAppBaseDir(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					String fullyQualifiedName = getAppBaseDir().relativize(file).toString();

					if (nameSuffix != null && !fullyQualifiedName.endsWith(nameSuffix + "." + fileExtension)) {
						return FileVisitResult.CONTINUE;
					}

					String className = fullyQualifiedName.replaceAll(Pattern.quote(File.separator), ".")
							.replaceAll(".class\\z", "");

					try {
						Class model = Class.forName(className);

						switch (classIdentityType) {
						case ANNOTATION:
							if (model.isAnnotationPresent(classType)) {
								classes.add((Class<? extends T>) model);
							}
							break;
						case SUPER_CLASS:
							if (classType.isAssignableFrom(model) && !model.getName().equals(classType.getName())) {
								classes.add((Class<? extends T>) model);
							}
							break;
						case DIRECT_SUPER_CLASS:
							
							if (model.getSuperclass() != null && model.getSuperclass().equals(classType)
									&& !model.getName().equals(classType.getName())) {
								classes.add((Class<? extends T>) model);
							}
							break;
						}

					} catch (ClassNotFoundException e) {
						Logger.error(e.getMessage());
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						// directory iteration failed
						throw e;
					}
				}
			});

		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}

		return classes;

	}

	public static Path getAppBaseDir() {
		File base = null;
		try {
			base = new File(ClasspathScanner.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			Exceptions.throwRuntime(e);
		}
		if (base.isFile()) {
			// Probably a Jar file
			Exceptions.throwRuntime(15);
		} else if (base.isDirectory()) {
			return Paths.get(base.getPath());
		}
		return null;
	}

	public Path getBaseDir() {
		return baseDir;
	}

	public ClasspathScanner<T> setBaseDir(Path baseDir) {
		if (baseDir != null) {
			this.baseDir = baseDir;
		}
		return this;
	}

	static {

	}

}
