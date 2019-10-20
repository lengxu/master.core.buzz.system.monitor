package cn.uyun.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Language {

	private static Map<String, String> musicTransMap = null;
	
	public static Map<String,String> getMusicTransMap(){
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("SURF", "地面气象");
		map.put("UPAR", "高空气象");
		map.put("OCEN", "海洋气象");
		map.put("RADI", "气象辐射");
		map.put("AGME", "农业生态");
		map.put("NAFP", "数值分析");
		map.put("HPXY", "历史待用");
		map.put("RADA", "雷达数据");
		map.put("SATE", "卫星数据");
		map.put("SCEX", "科学考察");
		map.put("SEVP", "气象服务");
		map.put("CAWN", "沙尘暴");
		map.put("STA_", "沙尘暴");
		map.put("DISA", "灾害数据");
		map.put("OTHE", "其它");
		map.put("", "其它");
		musicTransMap = map;
		return musicTransMap;
	}
	
	public static String languageTrans(String str,Map<String,String> map){
		if(str.length()>4){
			String newStr = str.substring(0, 4);
			return map.get(newStr)==null?"其它":map.get(newStr);
		}else{
			return map.get(str)==null?"其它":map.get(str);
		}
	}
	
}
