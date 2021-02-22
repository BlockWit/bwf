package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.rest.ListResponse;
import com.blockwit.bwf.model.rest.PermissionRestDTO;
import com.blockwit.bwf.repository.PermissionRepository;
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
public class Permissions {

	PermissionRepository permissionRepository;

	public Permissions(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	@GetMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ListResponse<PermissionRestDTO> list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
																							@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
		Page<Permission> page = permissionRepository.findAll(pageRequest);
		List<PermissionRestDTO> list = page.getContent().stream()
			.map(t -> new PermissionRestDTO(t.getId(), t.getName()))
			.collect(Collectors.toList());
		return new ListResponse<>(list);
	}

}
