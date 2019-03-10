package com.wnc.jijin;

/**
 * @Description 当日净值以及估值信息
 * @author nengcai.wang
 * @date 2018/12/5
 */
public class ValueData {
    private double gusuan;// 估值增长率
    private double accurate;// 准确增长率
    private String day;// 日期 2018-01-01

    public ValueData(double gusuan, double accurate, String day) {
        this.gusuan = gusuan;
        this.accurate = accurate;
        this.day = day;
    }

    public ValueData(String day, double accurate) {
        this.accurate = accurate;
        this.day = day;
    }

    public double getGusuan() {
        return gusuan;
    }

    public void setGusuan(double gusuan) {
        this.gusuan = gusuan;
    }

    public double getAccurate() {
        return accurate;
    }

    public void setAccurate(double accurate) {
        this.accurate = accurate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "ValueData{" +
                "gusuan=" + gusuan +
                ", accurate=" + accurate +
                ", day='" + day + '\'' +
                '}';
    }
}
