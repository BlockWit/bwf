package com.blockwit.bwf.model.rest.menu;

import com.blockwit.bwf.model.menu.MenuItem;

import java.io.Serializable;
import java.util.Collections;
import java.util.stream.Collectors;

public class MenuItemDTOMapper implements Serializable {

	public static MenuItemDTO map(MenuItem menuItem) {
		return new MenuItemDTO(menuItem.getLink(),
			menuItem.getName(),
			menuItem.getChildren() == null ? Collections.emptyList() : menuItem
				.getChildren()
				.stream()
				.map(MenuItemDTOMapper::map)
				.collect(Collectors.toList()),
			menuItem.getParent() == null ? null : MenuItemDTOMapper.map(menuItem.getParent())
		);
	}

}
