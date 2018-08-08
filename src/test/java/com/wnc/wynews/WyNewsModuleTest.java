package com.wnc.wynews;

import com.wnc.wynews.service.MyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WyNewsModuleTest {
	@Autowired
	private MyService myService;

	@Test
	public void sendTemplateMail() {
		myService.s();
	}
}
