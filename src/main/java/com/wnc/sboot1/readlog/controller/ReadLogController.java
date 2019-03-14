package com.wnc.sboot1.readlog.controller;

import com.wnc.sboot1.readlog.service.ReadLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readlog")
public class ReadLogController {
	@Autowired
	private ReadLogService readLogService;

	@ResponseBody
	@RequestMapping(value = "/readlog", produces = "application/json;charset=utf-8")
	public String g(String word){
//	    word = word.replaceAll("[^a-z]", "");
		return readLogService.findWord(word.toLowerCase()).toString();
	}
}
