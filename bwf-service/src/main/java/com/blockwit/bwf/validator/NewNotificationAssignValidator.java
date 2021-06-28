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

package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.NewNotificationAssign;
import com.blockwit.bwf.repository.notifications.NotificationExecutorsRepository;
import com.blockwit.bwf.repository.notifications.NotificationTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewNotificationAssignValidator extends CommonValidator {

  @Autowired
  NotificationTypesRepository notificationTypesRepository;

  @Autowired
  NotificationExecutorsRepository notificationExecutorsRepository;

  public NewNotificationAssignValidator(javax.validation.Validator javaxValidator) {
    super(javaxValidator, NewNotificationAssign.class);
  }

  @Override
  public void performValidate(Object o, Errors errors) {
    NewNotificationAssign newNotificationAssign = (NewNotificationAssign) o;

    if (notificationTypesRepository.findByName(newNotificationAssign.getNotificationTypeName()).isEmpty()) {
      errors.rejectValue("notificationTypeName",
          "Can't find notification type with name " + newNotificationAssign.getNotificationTypeName(),
          "Can't find notification type with name " + newNotificationAssign.getNotificationTypeName());
    }

    if (notificationExecutorsRepository.findByName(newNotificationAssign.getNotificationExecutorName()).isEmpty()) {
      errors.rejectValue("notificationTypeName",
          "Can't find notification executor with name " + newNotificationAssign.getNotificationExecutorName(),
          "Can't find notification executor with name " + newNotificationAssign.getNotificationExecutorName());
    }

  }

}
