package com.blockwit.bwf.model.rest.notifications;

import com.blockwit.bwf.model.notifications.NotificationExecutorState;

public class NotificationExecutorStateDTOMapper {

	public static NotificationExecutorStateDTO map(NotificationExecutorState model) {
		return new NotificationExecutorStateDTO(
			model.getId());
	}

}