package com.wnc.jijin;

/**
 * @Description 实时估值信息
 * @author nengcai.wang
 * @date 2018/12/5
 */
public class CurrentValueData {
    private double gusuan;// 估值增长率
    private String datetime;//20180101 11:00:00

    public CurrentValueData(double gusuan, String datetime) {
        this.gusuan = gusuan;
        this.datetime = datetime;
    }

    public double getGusuan() {
        return gusuan;
    }

    public void setGusuan(double gusuan) {
        this.gusuan = gusuan;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "CurrentValueData{" +
                "gusuan=" + gusuan +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
