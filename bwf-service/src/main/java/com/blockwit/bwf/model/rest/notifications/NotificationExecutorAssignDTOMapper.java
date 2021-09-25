package com.blockwit.bwf.model.rest.notifications;

import com.blockwit.bwf.model.notifications.ExecutorToNotificationTypeAssign;

public class NotificationExecutorAssignDTOMapper {

	public static NotificationExecutorAssignDTO map(ExecutorToNotificationTypeAssign model) {
		return new NotificationExecutorAssignDTO(
			model.getId());
	}

}