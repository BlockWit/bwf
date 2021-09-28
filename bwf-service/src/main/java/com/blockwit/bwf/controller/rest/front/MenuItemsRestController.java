package com.blockwit.bwf.controller.rest.front;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.rest.menu.MenuDTO;
import com.blockwit.bwf.service.rest.MenuItemsRestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Front REST API for menu items")
@RequestMapping(RestUrls.REST_URL_API_V_1_FRONT_MENU)
public class MenuItemsRestController {

	@Autowired
	MenuItemsRestService menuItemsRestService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MenuDTO> item() {
		return ResponseEntity.ok(menuItemsRestService.mainMenu());
	}

}