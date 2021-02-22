package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.model.rest.ListResponse;
import com.blockwit.bwf.model.rest.RoleRestDTO;
import com.blockwit.bwf.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class Roles {

	RoleRepository roleRepository;

	public Roles(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
	public ListResponse<RoleRestDTO> list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
																				@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
		Page<Role> page = roleRepository.findAll(pageRequest);
		List<RoleRestDTO> list = page.getContent().stream()
			.map(t -> new RoleRestDTO(t.getId(), t.getName()))
			.collect(Collectors.toList());
		return new ListResponse<>(list);
	}

}
