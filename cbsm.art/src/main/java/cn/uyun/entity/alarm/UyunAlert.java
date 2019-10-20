package cn.uyun.entity.alarm;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 吴晗 on 2017/9/26.
 */
@Getter
@Setter
public class UyunAlert {
	private String type;

	private UyunAlertField fields;
}
