package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.rest.ListResponse;
import com.blockwit.bwf.model.rest.PropertyRestDTO;
import com.blockwit.bwf.repository.OptionRepository;
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
public class Properties {

	OptionRepository optionRepository;

	public Properties(OptionRepository optionRepository) {
		this.optionRepository = optionRepository;
	}

	@GetMapping(value = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
	public ListResponse<PropertyRestDTO> list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
																								@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
		Page<Option> page = optionRepository.findAll(pageRequest);
		List<PropertyRestDTO> list = page.getContent().stream()
			.map(t -> new PropertyRestDTO(t.getId(), t.getName(), t.getDescr(), t.getType(), t.getValue()))
			.collect(Collectors.toList());
		return new ListResponse<>(list);
	}

}
