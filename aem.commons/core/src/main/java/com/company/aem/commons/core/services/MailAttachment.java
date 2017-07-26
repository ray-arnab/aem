package com.company.aem.commons.core.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class encapsulates email-attachment specific details
 */

public class MailAttachment {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MailAttachment.class);
	private InputStream inputStream;
	private String contentType;
	private String fileName;
	private byte[] bytes;
	private List<ByteArrayInputStream> duplicateStreams = new ArrayList<ByteArrayInputStream>();

	public void setInputStream(InputStream stream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IOUtils.copy(stream, baos);
			bytes = baos.toByteArray();

		} catch (IOException e) {
			LOGGER.error("Exception creating BAOS : " + e);
		}

	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ByteArrayInputStream getByteArrayInputStream() {

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		duplicateStreams.add(bais);
		return bais;

	}

	public void close() {

		try {

			for (ByteArrayInputStream item : duplicateStreams) {
				if (item != null) {
					item.close();
					item = null;
				}
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}

		} catch (IOException e) {

			LOGGER.error("Exception closing stream : " + e);
		}

	}
}
