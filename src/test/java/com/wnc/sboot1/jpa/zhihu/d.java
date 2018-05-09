package com.wnc.sboot1.jpa.zhihu;

import java.util.List;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class d {
	public static void main(String[] args) {
		initLastSeekTime();
	}

	private static void initLastSeekTime() {
		long LAST_SEEK_TIME = 0;
		try {
			List<String> readFrom = FileOp.readFrom("c:/zhihu-task.log");
			if (readFrom.size() > 0) {
				String string = readFrom.get(readFrom.size() - 1);
				String firstPattern = PatternUtil.getFirstPattern(string, "\\d+");
				System.out.println(firstPattern);
				LAST_SEEK_TIME = BasicNumberUtil.getLongNumber(firstPattern);
				System.out.println("Last Seek Time:" + LAST_SEEK_TIME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
