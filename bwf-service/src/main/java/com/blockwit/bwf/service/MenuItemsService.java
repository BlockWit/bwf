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

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.menu.MenuHelper;
import com.blockwit.bwf.model.menu.MenuItem;
import com.blockwit.bwf.repository.MenuItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MenuItemsService {

	@Autowired
	private MenuItemsRepository menuItemsRepository;

	@Autowired
	private OptionService optionService;

	public List<MenuItem> findMenuById(Long menuId) {
		return menuItemsRepository.findAllByMenuId(menuId);
	}

	public List<MenuItem> findMenuByIdAssembled(Long menuId) {
		return MenuHelper.assemblyMenu(menuItemsRepository.findAllByMenuId(menuId));
	}

	public List<MenuItem> mainMenu() {
		Optional<Option> optionOpt = optionService.findByName(OptionService.OPTION_MAIN_MENU_ID);
		if (optionOpt.isEmpty())
			return Collections.emptyList();
		return findMenuByIdAssembled(optionOpt.get().getPerformedValueCast());
	}

}
