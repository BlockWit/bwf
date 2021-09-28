package com.blockwit.bwf.model.menu;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MenuHelper {

	public static List<MenuItem> assemblyMenu(List<MenuItem> menuItems) {
		return
			menuItems
				.stream()
				.filter(t -> t.getParentId() == null)
				.map(t -> assemblyMenuItem(t,
					menuItems
						.stream()
						.filter(t1 -> t1.getParentId() != null)
						.collect(Collectors.toList())
				))
				.sorted(Comparator.comparingLong(MenuItem::getItemOrder))
				.collect(Collectors.toList());
	}

	private static MenuItem assemblyMenuItem(MenuItem menuItem, List<MenuItem> menuItems) {
		return menuItem
			.toBuilder()
			.children(
				menuItems
					.stream()
					.map(t -> assemblyMenuItem(t, menuItems)
						.toBuilder()
						.parent(menuItem)
						.build()
					).sorted(Comparator.comparingLong(MenuItem::getItemOrder))
					.collect(Collectors.toList())
			)
			.build();
	}

}