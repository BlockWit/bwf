/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.model.tasks;

public class SystemSchedTasks {

  public static final String TASK_ID_COMMON_INFO_UPDATER = "TASK_ID_COMMON_INFO_UPDATER";

  public static final String TASK_ID_NOTIFICATIONS_PERFORMER = "TASK_ID_NOTIFICATIONS_PERFORMER";

  public static final String TASK_ID_NOTIFICATIONS_EXECUTOR = "TASK_ID_NOTIFICATIONS_EXECUTOR";

  public static final String TASK_DESCR_COMMON_INFO_UPDATER = "Update info about chains, invoker accounts and platform state";

  public static final String TASK_DESCR_NOTIFICATIONS_PERFORMER = "Read all notifications which waiting for prepare." +
      " Generate for each notification state corresponding to executor.";

  public static final String TASK_DESCR_NOTIFICATIONS_EXECUTOR = "Read all notifications executor states which have status - waiting for sending." +
      " Read all notifications for states. Try to send notifications and then update notifications states and notification statuses.";

}
