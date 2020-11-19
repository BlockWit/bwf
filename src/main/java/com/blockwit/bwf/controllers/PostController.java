package com.blockwit.bwf.controllers;

import com.blockwit.bwf.controllers.model.NewPostForm;
import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.repository.PostRepository;
import com.blockwit.bwf.models.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getPrincipal());
        return new ModelAndView("front/post-new");
    }

}
