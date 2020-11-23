package com.blockwit.bwf.controller;

import com.blockwit.bwf.form.NewPostForm;
import com.blockwit.bwf.repository.PostRepository;
import com.blockwit.bwf.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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

	private final AccountService accountService;
	private final PostRepository postService;

	public PostController(AccountService accountService, PostRepository postService) {
		this.accountService = accountService;
		this.postService = postService;
	}

	@PostMapping("/posts/new")
	public ModelAndView createPost(@ModelAttribute("newPost") @Valid NewPostForm newPostForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ModelAndView("front/post-new", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
		}
		return new ModelAndView("");
	}

	@GetMapping("/posts/new")
	public ModelAndView createPost() {
		return new ModelAndView("front/post-new");
	}

}
