package com.wnc.sboot1.readlog.service;

import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.itbooktool.word.DicWord;
import com.wnc.itbooktool.word.NullDicWord;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class ReadLogServiceImpl implements ReadLogService {
    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public DicWord findWord(String word) {
        log.info("查找单词:" + word);
        DicWord findWord = null;
        try {
            findWord = dictionaryDao.findWeightWord(word);
            if (findWord == null) {
                findWord = dictionaryDao.findWord(word);
            }
            if (findWord == null) {
                log.warn("未找到该单词:" + word);
                return new NullDicWord();
            }
            log.info("找到单词:" + findWord);
        } catch (Exception e) {
            log.error("查找单词失败:" + word, e);
        }
        return findWord;
    }
}
