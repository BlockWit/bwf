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

package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.model.rest.menu.MenuDTO;
import com.blockwit.bwf.model.rest.menu.MenuDTOMapper;
import com.blockwit.bwf.service.MenuItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MenuItemsRestService {

	@Autowired
	private MenuItemsService menuItemsService;

	public MenuDTO mainMenu() {
		return MenuDTOMapper.map(menuItemsService.mainMenu());
	}

}
