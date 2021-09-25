package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.notifications.*;
import com.blockwit.bwf.service.notifications.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class NotificationsRestService {

	NotificationService notificationService;

	public NotificationsRestService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public PageDTO<NotificationDTO> findAllNotifications(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> notificationService.findAllNotifications(t),
			page,
			pageSize,
			t -> NotificationDTOMapper.map(t));
	}

	public PageDTO<NotificationExecutorStateDTO> findAllExecStates(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> notificationService.findAllExecStates(t),
			page,
			pageSize,
			t -> NotificationExecutorStateDTOMapper.map(t));
	}

	public PageDTO<NotificationExecutorDTO> findAllExecutors(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> notificationService.findAllNotificationExecutors(t),
			page,
			pageSize,
			t -> NotificationExecutorDTOMapper.map(t));
	}

	public PageDTO<NotificationTypeDTO> findAllNotifyTypes(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> notificationService.findAllNotificationTypes(t),
			page,
			pageSize,
			t -> NotificationTypeDTOMapper.map(t));
	}

	public PageDTO<NotificationExecutorAssignDTO> findAllExecutorsAssigns(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> notificationService.findAllNotificationExecAssigns(t),
			page,
			pageSize,
			t -> NotificationExecutorAssignDTOMapper.map(t));
	}

}
