package com.blockwit.bwf.controller;

import com.blockwit.bwf.dto.PostDTO;
import com.blockwit.bwf.model.PostStatus;
import com.blockwit.bwf.model.User;
import com.blockwit.bwf.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
@Controller
public class PostController implements WebMvcConfigurer {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/posts")
	public ModelAndView viewPosts(@RequestParam(name = "page", defaultValue = "1") int pageNumber, @RequestParam(name = "size", defaultValue = "1") int pageSize, Model model) {
		Page<PostDTO> page = postService.findAll(pageNumber, pageSize, null);
		model.addAttribute("page", page);
		return new ModelAndView("pages/posts");
	}

	@GetMapping("/posts/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView initCreatePost(Model model) {
		PostDTO postDTO = new PostDTO();
		model.addAttribute(postDTO);
		return new ModelAndView("pages/post-create");
	}

	@PostMapping("/posts/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView processCreatePost(@Valid PostDTO postDTO, BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) return new ModelAndView("pages/post-create", HttpStatus.BAD_REQUEST);
		User user = (User) authentication.getPrincipal();
		postDTO.setOwner(user.getAccount());
		postDTO.setStatus(PostStatus.SANDBOX);
		PostDTO createdPost = postService.save(postDTO);
		return new ModelAndView("redirect:/posts/" + createdPost.getId());
	}

	@GetMapping("/posts/{id}")
	public ModelAndView viewPost(@PathVariable("id") Long id, Model model) {
		PostDTO post = postService.findById(id).orElseThrow(NoSuchElementException::new);
		model.addAttribute("post", post);
		return new ModelAndView("pages/post");
	}

	@GetMapping("/posts/{id}/update")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView initUpdatePost(@PathVariable("id") Long id, Model model) {
		PostDTO post = postService.findById(id).orElseThrow(NoSuchElementException::new);
		model.addAttribute(post);
		return new ModelAndView("pages/post-update");
	}

	@PostMapping("/posts/{id}/update")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView processUpdatePost(@Valid PostDTO postDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) return new ModelAndView("pages/post-update", HttpStatus.BAD_REQUEST);
		PostDTO createdPost = postService.save(postDTO);
		return new ModelAndView("redirect:/posts/" + createdPost.getId());
	}

}
