package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.Account;
import com.blockwit.bwf.model.AppContext;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.OptionService;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class AppControllerAdvice {

	private final AccountService accountService;
	private final OptionService optionService;

	public AppControllerAdvice(AccountService accountService, OptionService optionService) {
		this.accountService = accountService;
		this.optionService = optionService;
	}

	@ModelAttribute("appCtx")
	public AppContext getAppContext(Authentication authentication) {
		AppContext appContext = new AppContext();
		Map<String, String> defaultOptions = optionService.getAllDefaultValues();
		appContext.setAppName(defaultOptions.get(OptionService.OPTION_APP_NAME));
		appContext.setAppVersion(defaultOptions.get(OptionService.OPTION_APP_VERSION));
		return appContext;
	}

	@ModelAttribute("authAccount")
	public Account getAuthAccount(Authentication authentication) {
		if (authentication == null)
			return null;
		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			String username = ((User) principal).getUsername();
			return accountService.findByEmailOrLogin(username).orElse(null);
		}
		return null;
	}

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;

		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();
		e.printStackTrace();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.addObject("status", "500");
		mav.addObject("error", "Internal server error");
		mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		mav.setViewName("error");
		return mav;
	}

/*
    @ExceptionHandler(value = DefaultAdminRoleNotExistsServiceException.class)
    public ModelAndView exception(DefaultAdminRoleNotExistsServiceException e) {
        return new ModelAndView("error/customError", Map.of("description", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DefaultUserRoleNotExistsServiceException.class)
    public ModelAndView exception(DefaultUserRoleNotExistsServiceException e) {
        return new ModelAndView("error/customError", Map.of("description", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
*/
}
