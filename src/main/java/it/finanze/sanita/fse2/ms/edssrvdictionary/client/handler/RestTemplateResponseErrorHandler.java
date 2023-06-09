/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.client.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR || 
				httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		String result = IOUtils.toString(httpResponse.getBody(), StandardCharsets.UTF_8);
//		ErrorResponseDTO error = new Gson().fromJson(result, ErrorResponseDTO.class);
//		Integer status = httpResponse.getStatusCode().value();
//		if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
//			ErrorDTO e = new ErrorDTO("type", "title", "detail", "instance");
//			throw new NotFoundException(e);
//		} else {
//			throw new BusinessException("");
//		}
		System.out.println("Sono qui");
	}

}
