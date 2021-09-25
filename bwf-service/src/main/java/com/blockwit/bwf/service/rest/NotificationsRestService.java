package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.notifications.NotificationDTO;
import com.blockwit.bwf.model.rest.notifications.NotificationDTOMapper;
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

}
