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

package com.blockwit.bwf.repository.notifications;

import com.blockwit.bwf.model.notifications.NotificationExecutorDescr;
import com.blockwit.bwf.model.notifications.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationExecutorsRepository extends JpaRepository<NotificationExecutorDescr, Long> {

  Optional<NotificationExecutorDescr> findById(Long id);

  List<NotificationExecutorDescr> findByNameIn(Set<String> defaultNames);

  Optional<NotificationExecutorDescr> findByName(String name);

}

