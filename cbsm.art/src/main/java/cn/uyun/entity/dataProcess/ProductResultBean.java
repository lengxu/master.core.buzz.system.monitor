package cn.uyun.entity.dataProcess;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by lihonghua on 2018/5/28.
 * SELECT time,coTdNum,coActualNum,coLocNum,coRateState,cts,dpc,sod FROM demo where cts = '%s'  and  ltime >= '%s' and ltime <= '%s'
 */
@Setter
@Getter
public class ProductResultBean {
    private String cts;
    private String dpc;
    private String sod;
    private String music_code;
    private String name;
    private int productType; //0小时，1每天8点， 2每天10点， 3每天早晚8点， 4每天早晚10点， 5每10分钟
    private String time;
    private int fileNumber;
    private Map<String,List<FileBean>> fileBean;

    public ProductResultBean(){}

    public ProductResultBean(String cts, String dpc, String sod, String music_code, String name, int productType, String time, int fileNumber, Map<String, List<FileBean>> fileBean) {
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
}
