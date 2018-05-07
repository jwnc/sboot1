package com.wnc.sboot1.itbook.helper;

import java.util.Map;

import org.apache.log4j.Logger;

import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.itbook.MyAppParams;

import db.DbExecMgr;

public class BookLogRetrieving {
	static final Logger logger = Logger.getLogger(BookLogRetrieving.class);

	public static Map getLastTime(String device) {
		return DbExecMgr.getSelectSqlMap(
				"SELECT *  FROM UPLOAD_LOG WHERE DEVICE='" + device + "' order by time desc,file_name desc limit 1");
	}

	public static boolean retrieved(String device, String modifyTime) {
		return DbExecMgr
				.isExistData("SELECT 1 FROM UPLOAD_LOG WHERE DEVICE='" + device + "' AND TIME='" + modifyTime + "'");
	}

	private static String getLogContent(String device, String modifyTime) {
		return device + ':' + modifyTime;
	}

	public static void log(String device, String logFileName, String modifyTime) {
		try {
			BasicFileUtil.writeFileString(MyAppParams.getInstance().getitwordSaveFolder() + "savelog.txt",
					getLogContent(device, modifyTime) + "\r\n", null, true);
			DbExecMgr.execOnlyOneUpdate("INSERT INTO UPLOAD_LOG(DEVICE,FILE_NAME,TIME) VALUES('" + device + "','"
					+ logFileName + "','" + modifyTime + "')");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

}