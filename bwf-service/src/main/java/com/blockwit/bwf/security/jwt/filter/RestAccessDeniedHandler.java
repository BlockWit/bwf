package com.blockwit.bwf.security.jwt.filter;

import com.blockwit.bwf.security.jwt.model.ErrorMessage;
import com.blockwit.bwf.security.jwt.model.ResponseWrapper;
import com.blockwit.bwf.security.jwt.model.RestErrorList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

import static java.util.Collections.singletonMap;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The RestAccessDeniedHandler is called by the ExceptionTranslationFilter to handle all AccessDeniedExceptions.
 * These exceptions are thrown when the authentication is valid but access is not authorized.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		RestErrorList errorList = new RestErrorList(SC_FORBIDDEN, new ErrorMessage(accessDeniedException.getMessage()));
		ResponseWrapper responseWrapper = new ResponseWrapper(null, singletonMap("status", SC_FORBIDDEN), errorList);
		ObjectMapper objMapper = new ObjectMapper();

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setStatus(SC_FORBIDDEN);
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		wrapper.getWriter().println(objMapper.writeValueAsString(responseWrapper));
		wrapper.getWriter().flush();
	}

}
