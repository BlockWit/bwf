package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.notifications.*;
import com.blockwit.bwf.service.rest.NotificationsRestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for notifications")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_NOTIFICATIONS)
public class AdminNotificationsRestController {

	@Autowired
	NotificationsRestService notificationsRestService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<NotificationDTO>> itemsNotifications(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return ResponseEntity.ok(notificationsRestService.findAllNotifications(page, pageSize));
	}

	@GetMapping(path = RestUrls.REST_URL_API_V_1_REL_ADMIN_NOTIFICATIONS_EXEC_STATES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<NotificationExecutorStateDTO>> itemsExecStates(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return ResponseEntity.ok(notificationsRestService.findAllExecStates(page, pageSize));
	}

	@GetMapping(path = RestUrls.REST_URL_API_V_1_REL_ADMIN_NOTIFICATIONS_EXECUTORS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<NotificationExecutorDTO>> itemsExecutors(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return ResponseEntity.ok(notificationsRestService.findAllExecutors(page, pageSize));
	}

	@GetMapping(path = RestUrls.REST_URL_API_V_1_REL_ADMIN_NOTIFICATIONS_NOTIFY_TYPES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<NotificationTypeDTO>> itemsNotifyTypes(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return ResponseEntity.ok(notificationsRestService.findAllNotifyTypes(page, pageSize));
	}

	@GetMapping(path = RestUrls.REST_URL_API_V_1_REL_ADMIN_NOTIFICATIONS_NOTIFY_ASSIGNS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<NotificationExecutorAssignDTO>> itemsAssigns(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return ResponseEntity.ok(notificationsRestService.findAllExecutorsAssigns(page, pageSize));
	}

}