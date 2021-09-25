package com.blockwit.bwf.model.rest.notifications;

import com.blockwit.bwf.model.notifications.NotificationType;

public class NotificationTypeDTOMapper {

	public static NotificationTypeDTO map(NotificationType model) {
		return new NotificationTypeDTO(
			model.getId());
	}

}