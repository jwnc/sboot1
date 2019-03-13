package com.wnc.sboot1.readlog;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readlog")
public class ReadLogController {
	
	@ResponseBody
	@RequestMapping("/readlog")
	public String g(String word){
		return "Hello:" + word;
	}
}
