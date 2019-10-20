package cn.uyun.entity.dataProcess;

import java.util.List;
import java.util.Map;

/**
 * Created by lihonghua on 2018/5/28.
 * SELECT time,coTdNum,coActualNum,coLocNum,coRateState,cts,dpc,sod FROM demo where cts = '%s'  and  ltime >= '%s' and ltime <= '%s'
 */
public class ProductResultBean {
    private String cts;
    private String dpc;
    private String sod;
    private String music_code;
    private String name;
    private String productType;
    private String time;
    private int fileNumber;
    private Map<String,List<FileBean>> fileBean;

    public ProductResultBean(){}

    public ProductResultBean(String cts, String dpc, String sod, String music_code, String name, String productType, String time, int fileNumber, Map<String, List<FileBean>> fileBean) {
        this.cts = cts;
        this.dpc = dpc;
        this.sod = sod;
        this.music_code = music_code;
        this.name = name;
        this.productType = productType;
        this.time = time;
        this.fileNumber = fileNumber;
        this.fileBean = fileBean;
    }

    public String getCts() {
        return cts;
    }

    public void setCts(String cts) {
        this.cts = cts;
    }

    public String getMusic_code() {
        return music_code;
    }

    public void setMusic_code(String music_code) {
        this.music_code = music_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Map<String, List<FileBean>> getFileBean() {
        return fileBean;
    }

    public void setFileBean(Map<String, List<FileBean>> fileBean) {
        this.fileBean = fileBean;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDpc() {
        return dpc;
    }

    public void setDpc(String dpc) {
        this.dpc = dpc;
    }

    public String getSod() {
        return sod;
    }

    public void setSod(String sod) {
        this.sod = sod;
    }
}
