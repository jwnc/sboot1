
package com.wnc.itbooktool.dao;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.wnc.basic.BasicFileUtil;
import com.wnc.itbooktool.word.DicWord;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import translate.bean.WordExchange;

@Repository
public class DictionaryDao{
	@PersistenceContext
	private EntityManager entityManager;

	Set<DicWord> weightWords = new HashSet<DicWord>();

	public void initDictToMem() {
		if (weightWords.size() > 0) {
			return;
		}
		try {
			String sql = "select id,topic_word,topic_id,word_third,word_done,word_er,word_est,word_ing,word_pl,word_past,mean_cn from  dictionary dict  LEFT JOIN word_exchange ex on "
					+ "ex.dict_id=dict.id where ex.word_org is not null or dict.weight >= 10 "
					+ "order by dict.topic_word asc";
//			Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);
//			System.out.println(selectAllSqlMap);
			Query createNativeQuery = entityManager.createNativeQuery( sql );
	        List resultList = createNativeQuery.getResultList();
			for (Object obj : resultList) {
				Object[] arr = (Object[])obj;
				// word_third word_done word_pl word_ing word_past word_er
				// word_est
				DicWord dicWord = new DicWord();
				dicWord.setId(getArrLong(arr, 0));
				dicWord.setBase_word(getArrStr(arr, 1));
				dicWord.setTopic_id(getArrInt(arr, 2));
				dicWord.setWord_third(getArrStr(arr, 3));
				dicWord.setWord_done(getArrStr(arr, 4));
				dicWord.setWord_er(getArrStr(arr, 5));
				dicWord.setWord_est(getArrStr(arr, 6));
				dicWord.setWord_ing(getArrStr(arr, 7));
				dicWord.setWord_pl(getArrStr(arr, 8));
				dicWord.setWord_past(getArrStr(arr,9));
				dicWord.setCn_mean(getArrStr(arr, 10));
//				dicWord.setBook_name(getArrStr(arr, 11));
				weightWords.add(dicWord);
			}
			// topics.remove("target");
			// topics.remove("guy");
			// topics.remove("blank");
			// topics.remove("span");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Integer getArrInt(Object[] rowMap, int idx) {
		Object obj = rowMap[idx];
		return obj == null ? null : Integer.parseInt(obj.toString());
	}
	
	public static Long getArrLong(Object[] rowMap, int idx) {
		Object obj = rowMap[idx];
		return obj == null ? null : Long.parseLong(obj.toString());
	}

	public static String getArrStr(Object[] rowMap, int idx) {
		Object obj = rowMap[idx];
		return obj == null ? null : obj.toString();
	}
	

	public synchronized DicWord findWord(String word) {
		DicWord dicWord = null;
		word = word.toLowerCase().toString();
		try {
			String sql = "select e.word_done,e.word_er,e.word_est,e.word_ing,e.word_pl,e.word_past,e.word_third,d.id,d.topic_id,d.topic_word,d.mean_cn,d.weight FROM  dictionary D LEFT JOIN word_exchange E  ON E.dict_id=D.id where LOWER(topic_word)='"
					+ word + "' or  word_third='" + word + "' or  word_done='" + word + "' or  word_er='" + word
					+ "' or  word_est='" + word + "' or  word_ing='" + word + "' or  word_pl='" + word
					+ "' or  word_past='" + word + "'";
			System.out.println(sql);
			Query createNativeQuery = entityManager.createNativeQuery( sql );
	        List resultList = createNativeQuery.getResultList();
			for (Object obj : resultList) {
				Object[] arr = (Object[])obj;
				dicWord = new DicWord();
				dicWord.setId(getArrLong(arr, 7));
				dicWord.setWeight(getArrInt(arr, 11));
				dicWord.setBase_word(getArrStr(arr, 9));
				dicWord.setTopic_id(getArrInt(arr, 8));
				dicWord.setWord_third(getArrStr(arr, 6));
				dicWord.setWord_done(getArrStr(arr, 0));
				dicWord.setWord_er(getArrStr(arr, 1));
				dicWord.setWord_est(getArrStr(arr, 2));
				dicWord.setWord_ing(getArrStr(arr, 3));
				dicWord.setWord_pl(getArrStr(arr, 4));
				dicWord.setWord_past(getArrStr(arr, 5));
				dicWord.setCn_mean(getArrStr(arr, 10));
				return dicWord;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dicWord;
	}

	public DicWord findWeightWord(String word) {
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

	private boolean hasFind(String word, String dwStr) {
		if (dwStr != null && dwStr.equalsIgnoreCase(word)) {
			return true;
		}
		return false;
	}

	public void insertNewWord(String currentWord, String mean) {
	}

	public boolean insertExchange(String word_org, WordExchange wordExchange) throws SQLException {
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