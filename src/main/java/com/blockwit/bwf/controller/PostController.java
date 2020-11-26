package com.blockwit.bwf.controller;

import com.blockwit.bwf.form.NewPostForm;
import com.blockwit.bwf.model.User;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Log4j2
@Controller
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/posts/new")
	public ModelAndView createPost(@ModelAttribute("newPost") @Valid NewPostForm newPostForm, Authentication authentication, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ModelAndView("pages/post-new", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
		}
		User user = (User)authentication.getPrincipal();
		postService.create(newPostForm.getBody(), newPostForm.getTitle(), user.getAccount().getId());
		return new ModelAndView("");
	}

	@GetMapping("/posts/new")
	public ModelAndView createPost( Authentication authentication) {
		return new ModelAndView("pages/post-new");
	}

}
