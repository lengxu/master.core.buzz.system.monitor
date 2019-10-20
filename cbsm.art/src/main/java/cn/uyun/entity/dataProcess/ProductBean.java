package cn.uyun.entity.dataProcess;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lihonghua on 2018/5/25.
 */
@Setter
@Getter
public class ProductBean {
    private String cts;
    private String dpc;
    private String sod;
    private String time;
    //private String date;
    private int idRateState;
    private int idActualNum;
    private int idTdNum;
    private String detail;
    private String status;  // 0 时间点未到 1 数据到库缺少 2 数据到库正常

    private String forcastTime;     //预报时次

    public ProductBean(String cts, String dpc, String sod, String time, int idActualNum, int idTdNum,String status, String detail){
        this.detail = detail;
        this.cts = cts;
        this.dpc = dpc;
        this.sod = sod;
        this.time = time;
        this.idActualNum = idActualNum;
        this.idTdNum = idTdNum;
        this.status = status;
    }
}
