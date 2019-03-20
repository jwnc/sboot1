package com.wnc.sboot1.readlog.controller;

import com.wnc.sboot1.SpringContextUtils;
import com.wnc.sboot1.itbook.entity.BookLogCondition;
import com.wnc.sboot1.itbook.entity.BookLogVO;
import com.wnc.sboot1.itbook.helper.BookLogSearch;
import com.wnc.sboot1.itbook.helper.PageDataBean;
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
	public String g(String word) {
		// word = word.replaceAll("[^a-z]", "");
		return readLogService.findWord(word.toLowerCase()).toString();
	}

	@ResponseBody
	@RequestMapping(value = "/page", produces = "application/json;charset=utf-8")
	public PageDataBean pageData(int page, int size) {
		BookLogCondition condition = new BookLogCondition();
		condition.setType("1");
		BookLogSearch bookLogSearch = (BookLogSearch) SpringContextUtils.getContext().getBean("bookLogSearch");
		bookLogSearch.setCondition(condition);
		return new PageDataBean<BookLogVO>(bookLogSearch.search(page, size), page, size, bookLogSearch.getTotalRows());
	}
}
