package com.blockwit.bwf.model.rest.notifications;

import com.blockwit.bwf.model.notifications.NotificationExecutorDescr;

public class NotificationExecutorDTOMapper {

	public static NotificationExecutorDTO map(NotificationExecutorDescr model) {
		return new NotificationExecutorDTO(
			model.getId());
	}

}