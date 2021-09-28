package com.blockwit.bwf.model.rest.menu;

import com.blockwit.bwf.model.menu.MenuItem;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class MenuDTOMapper implements Serializable {

	public static MenuDTO map(List<MenuItem> menuItems) {
		return new MenuDTO(menuItems
			.stream()
			.map(MenuItemDTOMapper::map)
			.collect(Collectors.toList()));
	}

}
