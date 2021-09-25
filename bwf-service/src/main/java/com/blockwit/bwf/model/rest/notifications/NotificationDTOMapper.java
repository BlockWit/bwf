package com.blockwit.bwf.model.rest.notifications;

import com.blockwit.bwf.model.notifications.Notification;

public class NotificationDTOMapper {

	public static NotificationDTO map(Notification model) {
		return new NotificationDTO(
			model.getId());
	}

}
