package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.rest.OptionDTO;
import com.blockwit.bwf.model.rest.common.PageableDTO;
import com.blockwit.bwf.model.rest.mappers.OptionDTOMapper;
import com.blockwit.bwf.service.OptionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

@Slf4j
@RestController
@Api("REST API for OPTIONS")
@RequestMapping(RestUrls.REST_URL_API_V_1_OPTIONS)
public class OptionsRestController {

	@Autowired
	OptionService optionService;

//	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<List<OptionDTO>> options() {
//		return ResponseEntity.ok(optionService.findPageable().stream()
//			.map(model -> OptionDTOMapper.map(model))
//			.collect(Collectors.toList()));
//	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageableDTO<OptionDTO>> options(@RequestParam(name = "page", defaultValue = "1") int page,
														  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize) {
		return PageableHelper.pageable(optionService, page, pageSize, OptionDTOMapper::map);
	}
//
//	@GetMapping(params = {"page", "size"})
//	public List<Foo> options(@RequestParam("page") int page,
//							 @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
//							 HttpServletResponse response) {
//		Page<Foo> resultPage = service.findPaginated(page, size);
//		if (page > resultPage.getTotalPages()) {
//			throw new MyResourceNotFoundException();
//		}
//		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Foo>(
//			Foo.class, uriBuilder, response, page, resultPage.getTotalPages(), size));
//
//		return resultPage.getContent();
//	}

}