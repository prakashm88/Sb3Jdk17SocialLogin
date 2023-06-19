package com.itechgenie.apps.jdk11.sb3.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itechgenie.apps.jdk11.sb3.dtos.FakeBlogDTO;
import com.itechgenie.apps.jdk11.sb3.dtos.FakeUserDTO;
import com.itechgenie.apps.jdk11.sb3.services.impl.FakeDataServiceImpl;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api")
public class FakeDataController {

	@Autowired
	private FakeDataServiceImpl dataService;

	@RequestMapping("/users/all")
	public ResponseEntity<List<FakeUserDTO>> users() {

		HttpStatus status = HttpStatus.OK;

		List<FakeUserDTO> userList = dataService.getUsers();

		return ResponseEntity.status(status).body(userList);
	}

	@RequestMapping("/users/find/{userId}")
	public ResponseEntity<FakeUserDTO> user(@PathVariable String userId) {
		HttpStatus status = HttpStatus.OK;

		FakeUserDTO userDto = dataService.getUserById(userId);
		return ResponseEntity.status(status).body(userDto);
	}

	@RequestMapping("/blogs/all")
	public ResponseEntity<List<FakeBlogDTO>> getBlogs(@PathVariable String userId) {
		HttpStatus status = HttpStatus.OK;

		List<FakeBlogDTO> blogs = dataService.getAllBlogs();
		return ResponseEntity.status(status).body(blogs);
	}

	@RequestMapping("/blogs/{blogId}")
	public ResponseEntity<Mono<FakeBlogDTO>> getBlogsById(@PathVariable String blogId) {
		HttpStatus status = HttpStatus.OK;

		Mono<FakeBlogDTO> blog = dataService.getAllBlog(blogId);
		return ResponseEntity.status(status).body(blog);
	}

	@RequestMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		return Collections.singletonMap("name", principal.getAttribute("name"));
	}

}
