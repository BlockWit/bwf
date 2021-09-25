package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.notifications.Notification;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.notifications.NotificationDTO;
import com.blockwit.bwf.model.rest.notifications.NotificationDTOMapper;
import com.blockwit.bwf.service.notifications.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class NotificationsRestService {

	NotificationService notificationService;

	public NotificationsRestService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public PageDTO<NotificationDTO> findAllNotifications(int page, int pageSize) {
		return PageableHelper.<Notification, NotificationDTO>pageable(
			(Function<Pageable, Page<Notification>>) t -> notificationService.findAllNotifications(t),
			page,
			pageSize,
			t -> NotificationDTOMapper.map(t));
	}

}
