package tech.bgdigital.online.payment.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/testAPI")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public	.";
	}
	
	@GetMapping("/user")
	public String userAccess() {
		return "User.";
	}

}
