package com.wnc.qqnews.jpa.entity;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.wynews.jpa.entity.BaseEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class QqNews extends BaseEntity{
    @Id
    @Column(length = 95) // 主键最大长度767个字节
    private String id;

    private int comment_num;
    private String img;
    private int flag;
    private String category_chn;
    private int img_count;

    @Transient
    private String keywords;
    @ElementCollection
    private List<String> keywordList;//通过keywords转化

    private int source_fans;
    private String source;
    private String fm_url;
    private String title;

    @Transient
    private Map<String, List<String>> irs_imgs;

    @Transient
    private List<QqNewsImg> irsImgList; //通过irs_imgs转化

    private String vurl;
    private String bimg;
    private int duration;
    @Column(name = "news_update_time")
    private String update_time;
    private String category_id;
    private String publish_time;
    private double title_len;
    @Column(length = 4000)
    private String intro;
    @Column(name = "from_media")
    private String from;
    private int s_group;
    private String app_id;
    private String category2_id;
    private String pool_type;
    @Transient
    private QqNewsExt ext;

    @ElementCollection
    private List<String> multi_imgs;

    @Transient
    private Map<String, String> imgs;
    @Transient
    private List<QqNewsImg> imgList;//通过imgs转化

    private int article_type;
    private String play_url_small;
    private String surl;
    private String comment_id;
    private String mini_img;
    private String url;
    private String tags;
    private String category1_id;
    private String category2_chn;
    private String play_url_medium;
    private String play_url_high;
    private String source_id;
    private String category;
    private int strategy;
    private String category1_chn;

    @Transient
    private List<List<String>> tag_label;
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "QqNewsTagLabelRelation", joinColumns = {
            @JoinColumn( name = "news_id" )}, inverseJoinColumns = {
            @JoinColumn( name = "tag_id" )},uniqueConstraints = {@UniqueConstraint(columnNames={"news_id", "tag_id"})} )
    private List<QqNewsTagLabel> tagLabelList;//通过tag_label转化

    private int view_count;
    private String source_logo;
    private long ts;


    private int tonality_score;
    private int news_score;
    private int img_type;

    public QqNews cvtAll() {
        return cvtKeywords().cvtImgs().cvtIrsImgs().cvtTagLabels().cvtExt();
    }

    private QqNews cvtKeywords() {
        String[] split = keywords.split(";");
        if (StringUtils.isNotBlank(keywords) && split.length > 0) {
            keywordList = new ArrayList<String>(3);
            for (String k : split) {
                if (StringUtils.isNotBlank(k)) {
                    keywordList.add(k);
                }
            }
        }
        return this;
    }

    private QqNews cvtImgs() {
        if (MapUtils.isNotEmpty(imgs)) {
            imgList = new ArrayList<QqNewsImg>();
            for (Map.Entry<String, String> entry : imgs.entrySet()) {
                String key = entry.getKey();
                if (key.matches("\\d+X\\d+")) {
                    int width = BasicNumberUtil.getNumber(PatternUtil.getFirstPatternGroup(key, "\\d+"));
                    int height = BasicNumberUtil.getNumber(PatternUtil.getLastPatternGroup(key, "\\d+"));
                    imgList.add(new QqNewsImg(width, height, entry.getValue()).setNews(this));
                }
            }
        }
        return this;
    }

    private QqNews cvtIrsImgs() {
        if (MapUtils.isNotEmpty(irs_imgs)) {
            irsImgList = new ArrayList<QqNewsImg>();
            for (Map.Entry<String, List<String>> entry : irs_imgs.entrySet()) {
                String key = entry.getKey();
                if (key.matches("\\d+X\\d+")) {
                    int width = BasicNumberUtil.getNumber(PatternUtil.getFirstPatternGroup(key, "\\d+"));
                    int height = BasicNumberUtil.getNumber(PatternUtil.getLastPatternGroup(key, "\\d+"));
                    int pos = 1;
                    for (String src : entry.getValue()) {
                        irsImgList.add(new QqNewsImg(width, height, src, pos++).setNews(this));
                    }
                }
            }
        }
        return this;
    }

    private QqNews cvtTagLabels() {
        if (CollectionUtils.isNotEmpty(tag_label)) {
            tagLabelList = new ArrayList<QqNewsTagLabel>(3);
            for (List<String> list : tag_label) {
                if (CollectionUtils.isNotEmpty(list) && list.size() == 2) {
                    tagLabelList.add(new QqNewsTagLabel(list.get(1), list.get(0)));
                }
            }
        }
        return this;
    }

    private QqNews cvtExt() {
        if (this.ext != null) {
            this.tonality_score = ext.getTonality_score();
            this.news_score = ext.getNews_score();
            this.img_type = ext.getImg_type();
        }
        return this;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCategory_chn() {
        return category_chn;
    }

    public void setCategory_chn(String category_chn) {
        this.category_chn = category_chn;
    }

    public int getImg_count() {
        return img_count;
    }

    public void setImg_count(int img_count) {
        this.img_count = img_count;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    public int getSource_fans() {
        return source_fans;
    }

    public void setSource_fans(int source_fans) {
        this.source_fans = source_fans;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFm_url() {
        return fm_url;
    }

    public void setFm_url(String fm_url) {
        this.fm_url = fm_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, List<String>> getIrs_imgs() {
        return irs_imgs;
    }

    public void setIrs_imgs(Map<String, List<String>> irs_imgs) {
        this.irs_imgs = irs_imgs;
    }

    public List<QqNewsImg> getIrsImgList() {
        return irsImgList;
    }

    public void setIrsImgList(List<QqNewsImg> irsImgList) {
        this.irsImgList = irsImgList;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }

    public String getBimg() {
        return bimg;
    }

    public void setBimg(String bimg) {
        this.bimg = bimg;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public double getTitle_len() {
        return title_len;
    }

    public void setTitle_len(double title_len) {
        this.title_len = title_len;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getS_group() {
        return s_group;
    }

    public void setS_group(int s_group) {
        this.s_group = s_group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCategory2_id() {
        return category2_id;
    }

    public void setCategory2_id(String category2_id) {
        this.category2_id = category2_id;
    }

    public String getPool_type() {
        return pool_type;
    }

    public void setPool_type(String pool_type) {
        this.pool_type = pool_type;
    }

    public QqNewsExt getExt() {
        return ext;
    }

    public void setExt(QqNewsExt ext) {
        this.ext = ext;
    }

    public List<String> getMulti_imgs() {
        return multi_imgs;
    }

    public void setMulti_imgs(List<String> multi_imgs) {
        this.multi_imgs = multi_imgs;
    }

    public Map<String, String> getImgs() {
        return imgs;
    }

    public void setImgs(Map<String, String> imgs) {
        this.imgs = imgs;
    }

    public List<QqNewsImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<QqNewsImg> imgList) {
        this.imgList = imgList;
    }

    public int getArticle_type() {
        return article_type;
    }

    public void setArticle_type(int article_type) {
        this.article_type = article_type;
    }

    public String getPlay_url_small() {
        return play_url_small;
    }

    public void setPlay_url_small(String play_url_small) {
        this.play_url_small = play_url_small;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getMini_img() {
        return mini_img;
    }

    public void setMini_img(String mini_img) {
        this.mini_img = mini_img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory1_id() {
        return category1_id;
    }

    public void setCategory1_id(String category1_id) {
        this.category1_id = category1_id;
    }

    public String getCategory2_chn() {
        return category2_chn;
    }

    public void setCategory2_chn(String category2_chn) {
        this.category2_chn = category2_chn;
    }

    public String getPlay_url_medium() {
        return play_url_medium;
    }

    public void setPlay_url_medium(String play_url_medium) {
        this.play_url_medium = play_url_medium;
    }

    public String getPlay_url_high() {
        return play_url_high;
    }

    public void setPlay_url_high(String play_url_high) {
        this.play_url_high = play_url_high;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public String getCategory1_chn() {
        return category1_chn;
    }

    public void setCategory1_chn(String category1_chn) {
        this.category1_chn = category1_chn;
    }

    public List<List<String>> getTag_label() {
        return tag_label;
    }

    public void setTag_label(List<List<String>> tag_label) {
        this.tag_label = tag_label;
    }

    public List<QqNewsTagLabel> getTagLabelList() {
        return tagLabelList;
    }

    public void setTagLabelList(List<QqNewsTagLabel> tagLabelList) {
        this.tagLabelList = tagLabelList;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public String getSource_logo() {
        return source_logo;
    }

    public void setSource_logo(String source_logo) {
        this.source_logo = source_logo;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public int getTonality_score() {
        return tonality_score;
    }

    public void setTonality_score(int tonality_score) {
        this.tonality_score = tonality_score;
    }

    public int getNews_score() {
        return news_score;
    }

    public void setNews_score(int news_score) {
        this.news_score = news_score;
    }

    public int getImg_type() {
        return img_type;
    }

    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }
}
