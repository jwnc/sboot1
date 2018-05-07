package com.wnc.itbooktool.dao;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.wnc.basic.BasicFileUtil;
import com.wnc.itbooktool.word.DicWord;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import translate.bean.WordExchange;

public class DictionaryDao {
	static Set<DicWord> weightWords = new HashSet<DicWord>();
	static {
		initDictToMem();
	}

	public synchronized static DicWord findWordById(int dict_id) {
		DicWord dicWord = null;
		try {
			String sql = "select e.word_done,e.word_er,e.word_est,e.word_ing,e.word_pl,e.word_past,e.word_third,d.topic_id,d.topic_word,d.mean_cn,d.weight FROM  dictionary D LEFT JOIN word_exchange E  ON E.dict_id=D.id where D.id="
					+ dict_id;
			Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);
			if (selectAllSqlMap.size() > 0) {
				Map rowMap = (Map) selectAllSqlMap.get(1);
				dicWord = new DicWord();
				dicWord.setBase_word(getMapStr(rowMap, "topic_word"));
				dicWord.setId(getMapInt(rowMap, "id"));
				dicWord.setWeight(getMapInt(rowMap, "weight"));
				dicWord.setWord_third(getMapStr(rowMap, "word_third"));
				dicWord.setWord_done(getMapStr(rowMap, "word_done"));
				dicWord.setWord_er(getMapStr(rowMap, "word_er"));
				dicWord.setWord_est(getMapStr(rowMap, "word_est"));
				dicWord.setWord_ing(getMapStr(rowMap, "word_ing"));
				dicWord.setWord_pl(getMapStr(rowMap, "word_pl"));
				dicWord.setWord_past(getMapStr(rowMap, "word_past"));
				dicWord.setCn_mean(getMapStr(rowMap, "mean_cn"));
				return dicWord;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dicWord;
	}

	private static Integer getMapInt(Map rowMap, String key) {
		Object obj = rowMap.get(key.toUpperCase());
		return obj == null ? null : Integer.parseInt(obj.toString());
	}

	private static String getMapStr(Map rowMap, String key) {
		Object obj = rowMap.get(key.toUpperCase());
		return obj == null ? null : obj.toString();
	}

	public synchronized static DicWord findWord(String word) {
		DicWord dicWord = null;
		word = word.toLowerCase().toString();
		try {
			String sql = "select e.word_done,e.word_er,e.word_est,e.word_ing,e.word_pl,e.word_past,e.word_third,d.id,d.topic_id,d.topic_word,d.mean_cn,d.weight FROM  dictionary D LEFT JOIN word_exchange E  ON E.dict_id=D.id where LOWER(topic_word)='"
					+ word + "' or  word_third='" + word + "' or  word_done='" + word + "' or  word_er='" + word
					+ "' or  word_est='" + word + "' or  word_ing='" + word + "' or  word_pl='" + word
					+ "' or  word_past='" + word + "'";
			Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);
			if (selectAllSqlMap.size() > 0) {
				Map rowMap = (Map) selectAllSqlMap.get(1);
				dicWord = new DicWord();
				dicWord.setId(getMapInt(rowMap, "id"));
				dicWord.setWeight(getMapInt(rowMap, "weight"));
				dicWord.setBase_word(getMapStr(rowMap, "topic_word"));
				dicWord.setTopic_id(getMapInt(rowMap, "topic_id"));
				dicWord.setWord_third(getMapStr(rowMap, "word_third"));
				dicWord.setWord_done(getMapStr(rowMap, "word_done"));
				dicWord.setWord_er(getMapStr(rowMap, "word_er"));
				dicWord.setWord_est(getMapStr(rowMap, "word_est"));
				dicWord.setWord_ing(getMapStr(rowMap, "word_ing"));
				dicWord.setWord_pl(getMapStr(rowMap, "word_pl"));
				dicWord.setWord_past(getMapStr(rowMap, "word_past"));
				dicWord.setCn_mean(getMapStr(rowMap, "mean_cn"));
				return dicWord;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dicWord;
	}

	public synchronized static void initDictToMem() {
		if (weightWords.size() > 0) {
			return;
		}
		try {
			String sql = "select * from  dictionary dict  LEFT JOIN word_exchange ex on ex.dict_id=dict.id where ex.word_org is not null or dict.weight >= 10 order by dict.topic_word asc";
			Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);
			for (int i = 1; i <= selectAllSqlMap.size(); i++) {
				Map rowMap = (Map) selectAllSqlMap.get(i);
				// word_third word_done word_pl word_ing word_past word_er
				// word_est
				DicWord dicWord = new DicWord();
				dicWord.setId(getMapInt(rowMap, "id"));
				dicWord.setBase_word(getMapStr(rowMap, "topic_word"));
				dicWord.setTopic_id(getMapInt(rowMap, "topic_id"));
				dicWord.setWord_third(getMapStr(rowMap, "word_third"));
				dicWord.setWord_done(getMapStr(rowMap, "word_done"));
				dicWord.setWord_er(getMapStr(rowMap, "word_er"));
				dicWord.setWord_est(getMapStr(rowMap, "word_est"));
				dicWord.setWord_ing(getMapStr(rowMap, "word_ing"));
				dicWord.setWord_pl(getMapStr(rowMap, "word_pl"));
				dicWord.setWord_past(getMapStr(rowMap, "word_past"));
				dicWord.setCn_mean(getMapStr(rowMap, "mean_cn"));
				dicWord.setBook_name(getMapStr(rowMap, "name"));

				weightWords.add(dicWord);
			}
			// topics.remove("target");
			// topics.remove("guy");
			// topics.remove("blank");
			// topics.remove("span");
		} catch (Exception e) {
		}
	}

	public static DicWord findWeightWord(String word) {
		for (DicWord dw : weightWords) {
			if (hasFind(word, dw.getBase_word()) || hasFind(word, dw.getWord_er()) || hasFind(word, dw.getWord_est())
					|| hasFind(word, dw.getWord_done()) || hasFind(word, dw.getWord_ing())
					|| hasFind(word, dw.getWord_past()) || hasFind(word, dw.getWord_pl())
					|| hasFind(word, dw.getWord_third())) {
				return dw;
			}
		}
		return null;
	}

	private static boolean hasFind(String word, String dwStr) {
		if (dwStr != null && dwStr.equalsIgnoreCase(word)) {
			return true;
		}
		return false;
	}

	public static void insertNewWord(String currentWord, String mean) {
	}

	public static boolean insertExchange(String word_org, WordExchange wordExchange) throws SQLException {
		DbFieldSqlUtil util = new DbFieldSqlUtil("WORD_EXCHANGE", "");
		util.addInsertField(new DbField("WORD_ORG", word_org));
		util.addInsertField(new DbField("WORD_THIRD", wordExchange.getWord_third()));
		util.addInsertField(new DbField("WORD_DONE", wordExchange.getWord_done()));
		util.addInsertField(new DbField("WORD_PL", wordExchange.getWord_pl()));
		util.addInsertField(new DbField("WORD_ING", wordExchange.getWord_ing()));
		util.addInsertField(new DbField("WORD_PAST", wordExchange.getWord_past()));
		util.addInsertField(new DbField("WORD_EST", wordExchange.getWord_est()));
		util.addInsertField(new DbField("WORD_ER", wordExchange.getWord_er()));
		BasicFileUtil.writeFileString("exchange_sql.txt", util.getInsertSql() + "\r\n", null, true);
		System.out.println(util.getInsertSql());
		return DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
	}

}