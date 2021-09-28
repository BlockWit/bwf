package com.blockwit.bwf.model.rest.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class MenuDTO implements Serializable {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<MenuItemDTO> children;

}
