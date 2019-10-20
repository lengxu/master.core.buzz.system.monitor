package cn.uyun.entity;

import cn.uyun.entity.dataProcess.FileBean;
import cn.uyun.entity.dataProcess.ProductResultBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInfo {
    protected static ArrayList<ProductResultBean> productResultBeans = new ArrayList<ProductResultBean>();

    private static HashMap<String, HashMap<String, String[]>> viewTimeList = new HashMap();

    //定义好接入的资料相关信息，包含接入资料应收的文件名称
    static {
        //小时采集入库数据（小时降水快速）
        FileBean fileBean = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FAST_CHN_0P05_HOR-PRE-%s.GRB2", 12, 1, "P_CMPA_FAST_CHN_0P05_HOR-PRE-%s.GRB2");
        FileBean fileBean1 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FAST_CHN_0P05_3HOR-PRE-%s.GRB2", 13, 1, "P_CMPA_FAST_CHN_0P05_3HOR-PRE-%s.GRB2");
        Map<String, List<FileBean>> fileBeanMap = new HashMap<String, List<FileBean>>();
        List<FileBean> hourList = new ArrayList<FileBean>();
        hourList.add(fileBean);
        hourList.add(fileBean1);
        fileBeanMap.put("hour", hourList);
        ProductResultBean productResultBeanHour = new ProductResultBean("A.0042.0003.R001", "A.0042.0003.P001", "A.0042.0003.S001", "SURF_CMPA_FAST_5KM", "小时降水（快速）", "小时", "1", 2, fileBeanMap);
        productResultBeans.add(productResultBeanHour);

        //日采集入库数据（日降水快速）
        FileBean fileBean2 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FAST_CHN_0P05_DAY-PRE-%s08.GRB2", 12, 8, "P_CMPA_FAST_CHN_0P05_DAY-PRE-%s08.GRB2");
        FileBean fileBean3 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FAST_CHN_0P05_DAY-PRE-%s20.GRB2", 12, 20, "P_CMPA_FAST_CHN_0P05_DAY-PRE-%s20.GRB2");
        Map<String, List<FileBean>> fileBeanMap1 = new HashMap<String, List<FileBean>>();
        List<FileBean> AMList = new ArrayList<FileBean>();
        AMList.add(fileBean2);
        List<FileBean> PMList = new ArrayList<FileBean>();
        PMList.add(fileBean3);
        fileBeanMap1.put("AM", AMList);
        fileBeanMap1.put("PM", PMList);
        ProductResultBean productResultBeanDay = new ProductResultBean("A.0042.0004.R001", "A.0042.0004.P001", "A.0042.0004.S001", "SURF_CMPA_FAST_5KM_DAY", "日降水（快速）", "日", "08", 2, fileBeanMap1);
        productResultBeans.add(productResultBeanDay);

        //小时采集入库数据（小时降水实时）
        fileBean = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FRT_CHN_0P05_HOR-PRE-%s.GRB2", 60, 1, "P_CMPA_FRT_CHN_0P05_HOR-PRE-%s.GRB2");
        fileBean1 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FRT_CHN_0P05_3HOR-PRE-%s.GRB2", 60, 1, "P_CMPA_FRT_CHN_0P05_3HOR-PRE-%s.GRB2");
        fileBeanMap = new HashMap<String, List<FileBean>>();
        hourList = new ArrayList<FileBean>();
        hourList.add(fileBean);
        hourList.add(fileBean1);
        fileBeanMap.put("hour", hourList);
        productResultBeanHour = new ProductResultBean("A.0042.0005.R001", "A.0042.0005.P001", "A.0042.0005.S001", "SURF_CMPA_FRT_5KM", "小时降水（实时）", "小时", "1", 2, fileBeanMap);
        productResultBeans.add(productResultBeanHour);

        //日采集入库数据（日降水实时）
        fileBean2 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FRT_CHN_0P05_DAY-PRE-%s08.GRB2", 60, 8, "P_CMPA_FRT_CHN_0P05_DAY-PRE-%s08.GRB2");
        fileBean3 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FRT_CHN_0P05_DAY-PRE-%s20.GRB2", 60, 20, "P_CMPA_FRT_CHN_0P05_DAY-PRE-%s20.GRB2");
        fileBeanMap1 = new HashMap<String, List<FileBean>>();
        AMList = new ArrayList<FileBean>();
        AMList.add(fileBean2);
        PMList = new ArrayList<FileBean>();
        PMList.add(fileBean3);
        fileBeanMap1.put("AM", AMList);
        fileBeanMap1.put("PM", PMList);
        fileBeanMap1.put("AM", AMList);
        fileBeanMap1.put("PM", PMList);
        productResultBeanDay = new ProductResultBean("A.0042.0006.R001", "A.0042.0006.P001", "A.0042.0006.S001", "SURF_CMPA_FRT_5KM_DAY", "日降水（实时）", "日", "08", 2, fileBeanMap1);
        productResultBeans.add(productResultBeanDay);


        //10分钟采集入库数据（10分钟降水）
        FileBean fileBean4 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FAST_CHN_0P05_10MIN-PRE-%s.GRB2", 10, 10, "P_CMPA_FAST_CHN_0P05_10MIN-PRE-%s.GRB2");
        FileBean fileBean5 = new FileBean("Z_SURF_C_BABJ_yyyymmddhhmmss_P_CMPA_FAST_CHN_0P05_A10MIN-PRE-%s.GRB2", 10, 1, "P_CMPA_FAST_CHN_0P05_A10MIN-PRE-%s.GRB2");
        Map<String, List<FileBean>> fileBeanMap2 = new HashMap<String, List<FileBean>>();
        List<FileBean> minuteList = new ArrayList<FileBean>();
        minuteList.add(fileBean4);
        minuteList.add(fileBean5);
        fileBeanMap2.put("minute", minuteList);
        ProductResultBean productResultBeanMintus = new ProductResultBean("A.0042.0007.R001", "A.0042.0007.P001", "A.0042.0007.S001", "SURF_CMPA_FRT_5KM_10MIN", "10分钟降水", "分钟", "1", 2, fileBeanMap2);
        productResultBeans.add(productResultBeanMintus);


        //小时采集入库数据（小时地面要素）
        fileBean = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_HOR-TEM-%s.GRB2", 12, 1, "P_CLDAS_RT_CHN_0P05_HOR-TEM-%s.GRB2");
        fileBean1 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_HOR-RHU-%s.GRB2", 12, 1, "P_CLDAS_RT_CHN_0P05_HOR-RHU-%s.GRB2");
        FileBean fileBean6 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_HOR-WIN-%s.GRB2", 12, 1, "P_CLDAS_RT_CHN_0P05_HOR-WIN-%s.GRB2");
        FileBean fileBean7 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_HOR-VIS-%s.GRB2", 12, 1, "P_CLDAS_RT_CHN_0P05_HOR-VIS-%s.GRB2");
        FileBean fileBean8 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_HOR-TCDC-%s.GRB2", 12, 1, "P_CLDAS_RT_CHN_0P05_HOR-TCDC-%s.GRB2");
        fileBeanMap = new HashMap<String, List<FileBean>>();
        hourList = new ArrayList<FileBean>();
        hourList.add(fileBean);
        hourList.add(fileBean1);
        hourList.add(fileBean6);
        hourList.add(fileBean7);
        hourList.add(fileBean8);
        fileBeanMap.put("hour", hourList);
        productResultBeanHour = new ProductResultBean("F.0035.0003.R001", "F.0035.0003.P001", "F.0035.0003.S001", "NAFP_CLDAS2.0_RT_GRB", "小时地面要素", "小时", "1", 5, fileBeanMap);
        productResultBeans.add(productResultBeanHour);

        //日采集入库数据（日地面要素）
        fileBean2 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MXT-%s08.GRB2", 12, 8, "P_CLDAS_RT_CHN_0P05_DAY-MXT-%s08.GRB2");
        FileBean fileBean9 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MNT-%s08.GRB2", 12, 8, "P_CLDAS_RT_CHN_0P05_DAY-MNT-%s08.GRB2");
        FileBean fileBean10 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MXWIN-%s08.GRB2", 12, 8, "P_CLDAS_RT_CHN_0P05_DAY-MXWIN-%s08.GRB2");
        FileBean fileBean11 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MXRHU-%s08.GRB2", 12, 8, "P_CLDAS_RT_CHN_0P05_DAY-MXRHU-%s08.GRB2");
        FileBean fileBean12 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MNRHU-%s08.GRB2", 12, 8, "P_CLDAS_RT_CHN_0P05_DAY-MNRHU-%s08.GRB2");
        FileBean fileBean13 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MXT-%s20.GRB2", 12, 20, "P_CLDAS_RT_CHN_0P05_DAY-MXT-%s20.GRB2");
        FileBean fileBean14 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MNT-%s20.GRB2", 12, 20, "P_CLDAS_RT_CHN_0P05_DAY-MNT-%s20.GRB2");
        FileBean fileBean15 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MXWIN-%s20.GRB2", 12, 20, "P_CLDAS_RT_CHN_0P05_DAY-MXWIN-%s20.GRB2");
        FileBean fileBean16 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MXRHU-%s20.GRB2", 12, 20, "P_CLDAS_RT_CHN_0P05_DAY-MXRHU-%s20.GRB2");
        FileBean fileBean17 = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_CLDAS_RT_CHN_0P05_DAY-MNRHU-%s20.GRB2", 12, 20, "P_CLDAS_RT_CHN_0P05_DAY-MNRHU-%s20.GRB2");
        fileBeanMap1 = new HashMap<String, List<FileBean>>();
        AMList = new ArrayList<FileBean>();
        AMList.add(fileBean2);
        AMList.add(fileBean9);
        AMList.add(fileBean10);
        AMList.add(fileBean11);
        AMList.add(fileBean12);
        PMList = new ArrayList<FileBean>();
        PMList.add(fileBean13);
        PMList.add(fileBean14);
        PMList.add(fileBean15);
        PMList.add(fileBean16);
        PMList.add(fileBean17);
        fileBeanMap1.put("AM", AMList);
        fileBeanMap1.put("PM", PMList);
        productResultBeanDay = new ProductResultBean("F.0035.0004.R001", "F.0035.0004.P001", "F.0035.0004.S001", "NAFP_CLDAS2.0_RT_5KM_DAY_GRB", "日地面要素", "日", "08", 10, fileBeanMap1);
        productResultBeans.add(productResultBeanDay);


        //小时三维要素
        fileBean = new FileBean("Z_NAFP_C_BABJ_yyyymmddhhmmss_P_3DCloudA_RT_CHN_0P05_HOR-CCP3-%s.GRB2", 12, 1, "P_3DCloudA_RT_CHN_0P05_HOR-CCP3-%s.GRB2");
        fileBeanMap = new HashMap<String, List<FileBean>>();
        hourList = new ArrayList<FileBean>();
        hourList.add(fileBean);
        fileBeanMap.put("hour", hourList);
        productResultBeanHour = new ProductResultBean("F.0043.0002.R001", "F.0043.0002.P001", "F.0043.0002.S001", "NAFP_3DCloudA_GRIB2", "小时三维要素", "小时", "1", 1, fileBeanMap);
        productResultBeans.add(productResultBeanHour);

        //全球海表温度
        fileBean2 = new FileBean("Z_OCEN_C_BABJ_yyyymmddhhmmss_P_CODAS_GLB_0P25_DAY-SST-%s10.grb2", 12, 10, "P_CODAS_GLB_0P25_DAY-SST-%s.grb2");
        fileBean9 = new FileBean("Z_OCEN_C_BABJ_yyyymmddhhmmss_P_CODAS_GLB_0P25_DAY-SST-%s10.nc", 12, 10, "P_CODAS_GLB_0P25_DAY-SST-%s.nc");
        fileBean10 = new FileBean("Z_OCEN_C_BABJ_yyyymmddhhmmss_P_CODAS_GLB_0P25_DAY-SST-%s10.png", 12, 10, "P_CODAS_GLB_0P25_DAY-SST-%s.png");
        fileBeanMap1 = new HashMap<String, List<FileBean>>();
        AMList = new ArrayList<FileBean>();
        AMList.add(fileBean2);
        AMList.add(fileBean9);
        AMList.add(fileBean10);
        fileBeanMap1.put("AM", AMList);
        productResultBeanDay = new ProductResultBean("C.0011.0001.R001", "C.0011.0001.P001", "C.0011.0001.S001", "OCEN_PRODUCT_PART1_FILE", "全球海表温度", "日", "10", 3, fileBeanMap1);
        productResultBeans.add(productResultBeanDay);

        //全球海冰
        fileBean2 = new FileBean("Z_OCEN_C_BABJ_yyyymmddhhmmss_P_CODAS_GLB_0P25_DAY-SIC-%s08.nc", 12, 8, "P_CODAS_GLB_0P25_DAY-SIC08-%s.nc");
        fileBeanMap1 = new HashMap<String, List<FileBean>>();
        AMList = new ArrayList<FileBean>();
        AMList.add(fileBean2);
        fileBeanMap1.put("AM", AMList);
        productResultBeanDay = new ProductResultBean("C.0011.0002.R001", "C.0011.0002.P001", "C.0011.0002.S001", "OCEN_PRODUCT_PART1_FILE", "海冰密集度", "日", "8", 1, fileBeanMap1);
        productResultBeans.add(productResultBeanDay);
    }

    /*
    * int i=0 小时
    * int i=1 10分钟
    * int i=2 每天08时
    * int i=3 每天10时
    * int i=4 每天早晚8点
    * */
    public HashMap<String, String[]> getViewTimeList(int i){
        if(i == 0){
            String[] hourList = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        }else if(i == 1){

        }else if(i == 2){

        }else if(i == 3){

        }else if(i == 4){

        }
    }
}
