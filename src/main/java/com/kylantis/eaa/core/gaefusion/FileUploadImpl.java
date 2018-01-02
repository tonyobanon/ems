package com.kylantis.eaa.core.gaefusion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.ce.ems.base.core.Exceptions;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.fusion.WebRoutes;

import io.vertx.ext.web.FileUpload;

public class FileUploadImpl implements FileUpload {

	private final long size;
	private final String fileName;
	private final String mimeType;
	
	public FileUploadImpl(InputStream in) {

		byte[] bytes = null;
		File tmpFile = null;

		try {

			bytes = IOUtils.toByteArray(in);
			tmpFile = new File("/tmp/file-uploads/" + Utils.newRandom());

			FileUtils.writeByteArrayToFile(tmpFile, bytes);
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}

		this.mimeType = WebRoutes.TIKA_INSTANCE.detect(bytes);
		this.size = bytes.length;
		this.fileName = tmpFile.getAbsolutePath();
	}

	@Override
	public String name() {
		return fileName;
	}

	@Override
	public String uploadedFileName() {
		return fileName;
	}

	@Override
	public String fileName() {
		return fileName;
	}

	@Override
	public long size() {
		return size;
	}

	@Override
	public String contentType() {
		return mimeType;
	}

	@Override
	public String contentTransferEncoding() {
		return "UTF-8";
	}

	@Override
	public String charSet() {
		return Charset.defaultCharset().name();
	}

}
