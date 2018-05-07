
package com.wnc.sboot1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/word/iconshow")
	public String iconshow(Long id) {
		return "/word/iconshow";
	}

	@RequestMapping("/word/iconshow2")
	public String iconshow2(Long id) {
		return "/word/iconshow2";
	}

	@RequestMapping("/proxy")
	public String pp(Long id) {
		return "proxy";
	}
}