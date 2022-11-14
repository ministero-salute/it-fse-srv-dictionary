/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_UTLS_IO_EMPTY;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_UTLS_IO_ERROR;

/**
 * The Class FileUtils.
 *
 *
 * Utility to manager file.
 */
@Slf4j
public final class FileUtility {
 
	/**
	 * Max size chunk.
	 */
	private static final int CHUNK_SIZE = 16384;

	/**
	 * Constructor.
	 */
	private FileUtility() {
	}

	/**
	 * Metodo per il recupero del contenuto di un file dalla folder interna "/src/main/resources".
	 *
	 * @param filename	nome del file
	 * @return			contenuto del file
	 */
	public static byte[] getFileFromInternalResources(final String filename) {
		byte[] b = null;
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			b = getByteFromInputStream(Objects.requireNonNull(is));
			is.close();
		} catch (Exception e) {
			log.error("FILE UTILS getFileFromInternalResources(): Errore in fase di recupero del contenuto di un file dalla folder '/src/main/resources'. ", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(""+e);
				}
			}
		}
		return b;
	}

	/**
	 * Recupero contenuto file da input stream.
	 *
	 * @param is
	 *            input stream
	 * @return contenuto file
	 */
	private static byte[] getByteFromInputStream(final InputStream is) {
		byte[] b;
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[CHUNK_SIZE];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			b = buffer.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return b;
	}

	public static byte[] throwIfEmpty(MultipartFile file) throws DataProcessingException {
		byte[] raw;
		// Retrieve byte array
		try {
			raw = file.getBytes();
		}catch (IOException ex) {
			throw new DataProcessingException(ERR_UTLS_IO_ERROR, ex);
		}
		// Check emptiness
		if(raw.length == 0) throw new DataProcessingException(ERR_UTLS_IO_EMPTY);
		return raw;
	}

}
