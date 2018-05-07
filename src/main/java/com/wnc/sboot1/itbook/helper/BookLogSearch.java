
package com.wnc.sboot1.itbook.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.itbook.entity.BookLogCondition;
import com.wnc.sboot1.itbook.entity.BookLogVO;

import db.DbExecMgr;

public class BookLogSearch {
	final static Logger logger = Logger.getLogger(BookLogSearch.class);
	BookLogCondition condition;

	public BookLogSearch(BookLogCondition condition) {
		this.condition = condition;
	}

	public List<BookLogVO> search(int page, int size) {
		String sql = getSql(page, size);
		logger.info("itbook search:" + sql);

		List<BookLogVO> list = new ArrayList<BookLogVO>();
		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);
		Map fieldMap;
		BookLogVO blog;
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			fieldMap = (Map) selectAllSqlMap.get(i);
			blog = new BookLogVO();
			blog.setId(cvtInt(fieldMap, "ID"));
			blog.setContent(cvtStr(fieldMap, "CONTENT"));
			blog.setDevice(cvtStr(fieldMap, "DEVICE"));
			blog.setDeviceCnName(cvtStr(fieldMap, "DEVICECNNAME"));
			blog.setTopic(cvtStr(fieldMap, "DICT_ID"));
			blog.setTime(cvtStr(fieldMap, "LOG_TIME"));
			blog.setType(cvtStr(fieldMap, "TYPE"));
			blog.setMean(cvtStr(fieldMap, "MEAN_CN"));
			blog.setWeight(cvtInt(fieldMap, "WEIGHT"));
			list.add(blog);
		}

		return list;
	}

	private int cvtInt(Map fieldMap, String string) {
		String s = cvtStr(fieldMap, string);
		if (s != null && s.matches("[\\d\\.]+")) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	private String cvtStr(Map fieldMap, String key) {
		return fieldMap.get(key) == null ? null : fieldMap.get(key).toString();
	}

	private String getSql(int page, int size) {
		return getSql() + String.format(" limit %d, %d", (page - 1) * size, size);
	}

	private String getSql() {
		String sql = "SELECT b.*,d.mean_cn,d.weight,dv.cn_name DEVICECNNAME FROM ITBOOK_LOG b left join device dv on b.device=dv.name left join dictionary d on b.dict_id=d.id WHERE b.deleted = 0 ";
		if (StringUtils.isNotBlank(condition.getDevice())) {
			sql += " AND b.DEVICE='" + condition.getDevice() + "'";
		}
		if (StringUtils.isNotBlank(condition.getWord())) {
			sql += " AND upper(b.CONTENT) LIKE '%"
					+ StringEscapeUtils.escapeSql(condition.getWord().trim().toUpperCase()) + "%'";
		}
		if (StringUtils.isNotBlank(condition.getDayStart())) {
			sql += " AND b.LOG_TIME>='" + cvtDay(condition.getDayStart(), 0) + "'";
		}
		if (StringUtils.isNotBlank(condition.getDayEnd())) {
			sql += " AND b.LOG_TIME<='" + cvtDay(condition.getDayEnd(), 1) + "'";
		}
		if (StringUtils.isNotBlank(condition.getType())
				&& StringUtils.containsAny(condition.getType(), '1', '2', '3')) {
			sql += " AND b.TYPE = " + condition.getType();
		}
		if (condition.getWeightStart() != null) {
			sql += " AND d.weight >= " + condition.getWeightStart();
		}
		if (condition.getWeightEnd() != null) {
			sql += " AND ifnull(d.weight,0) <= " + condition.getWeightEnd();// weight字段在数据库可能为空
		}
		if (condition.getStar() != null && condition.getStar() == 1) {
			sql += " AND b.content in (select word from word_questions wq,question q where wq.question_id = q.id and q.star = 1)";
		}
		sql += " order by log_time desc";

		return sql;

	}

	private String cvtDay(String day, int i) {
		if (day.matches("\\d{8}")) {
			return day.substring(0, 4) + "-" + day.substring(4, 6) + "-" + day.substring(6)
					+ (i == 0 ? " 00:00:00" : " 23:59:59");
		}
		if (day.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return day + (i == 0 ? " 00:00:00" : " 23:59:59");
		}
		return day;
	}

	public static void main(String[] args) {
		BookLogCondition condition = new BookLogCondition();
		condition.setDayStart("20171110");
		// condition.setDayEnd( "20171117" );
		condition.setDevice("");
		condition.setWord("g");
		List<BookLogVO> search = new BookLogSearch(condition).search(3, 2);
		System.out.println(JSONObject.toJSONString(search, SerializerFeature.PrettyFormat));
		System.out.println(search);

	}

	public int getTotalRows() {
		return BasicNumberUtil.getNumber(
				String.valueOf(DbExecMgr.getSelectSqlMap("SELECT COUNT(1) CNT FROM (" + getSql() + ") t").get("CNT")));
	}
}