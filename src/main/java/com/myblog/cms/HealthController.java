package com.myblog.cms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
	public String handle() {
		return "Server is up and running!";
	}
}
