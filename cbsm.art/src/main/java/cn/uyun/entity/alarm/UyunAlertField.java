package cn.uyun.entity.alarm;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 吴晗 on 2018/6/25.
 */
@Getter
@Setter
public class UyunAlertField {
	/**
	 * 告警对象
	 */
	@JSONField(name="SYSTEM")
	private String System;

	/**
	 * 事件业务分类（监视点类别属性编号）
	 */
	@JSONField(name="GROUP_ID")
	private String groupId;

	/**
	 * 内容类型
	 */
	@JSONField(name="MSG_TYPE")
	private String msgType;

	/**
	 * 采集方式
	 */
	@JSONField(name="COL_TYPE")
	private String colType;

	/**
	 * 拓展信息：数据来源
	 */
	@JSONField(name="DATA_FROM")
	private String dataForm;

	/**
	 * 事件类型编码
	 */
	@JSONField(name="EVENT_TYPE")
	private String eventType;

	/**
	 * 告警级别，触发告警设置为03，恢复告警设置为00
	 */
	@JSONField(name="EVENT_LEVEL")
	private String evnetLevel;

	/**
	 * 告警名称
	 */
	@JSONField(name="EVENT_TITLE")
	private String eventTitle;

	/**
	 * 告警描述(故障内容)
	 */
	@JSONField(name="KEvent")
	private String kEvent;


	/**
	 * 故障结果
	 */
	@JSONField(name="KResult")
	private String kResult;

	/**
	 * 关键信息
	 */
	@JSONField(name="KIndex")
	private String kIndex;

	/**
	 * 事件发生可能原因
	 */
	@JSONField(name="EVENT_TRAG")
	private String eventTrag;

	/**
	 * 事件建议
	 */
	@JSONField(name="EVENT_SUGGEST")
	private String eventSuggest;
}
