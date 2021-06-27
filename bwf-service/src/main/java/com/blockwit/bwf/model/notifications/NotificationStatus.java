/*
 * Copyright (c) 2017-present BlockWit, LLC. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  details.
 *
 */

package com.blockwit.bwf.model.notifications;

public enum NotificationStatus {

  SENT, // все успешно отправлено
  WAIT_FOR_PREPARE, // только что созданная нотификация, ожидает подготовки (создания задач по отправщикам нотификаций) ???
  WAIT_FOR_SENDING, // все подготовлено, ожидает отправки
  SEND_IN_PROGRESS, // в процессе отправки, смотреть статусы задач на отправку по отправителям
  FAIL // ошибка

}
