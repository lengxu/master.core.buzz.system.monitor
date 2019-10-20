package cn.uyun.entity.dataProcess;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wuhan
 * @date 2019-05-08
 */
@Getter
@Setter
public class McpQueryBean {
    private String id_state = "ACTUAL";
    private String co_state = "ACTUAL";
    private String cts;
    private String dpc;
    private String sod;
    private String time;

    public McpQueryBean(String cts, String dpc, String sod, String time) {
        this.cts = cts;
        this.dpc = dpc;
        this.sod = sod;
        this.time = time;
    }

    public static void main(String[] args) {
        McpQueryBean mcpQueryBean = new McpQueryBean("", "", "", "");
        System.out.println("id_state："+mcpQueryBean.id_state);
    }
}
