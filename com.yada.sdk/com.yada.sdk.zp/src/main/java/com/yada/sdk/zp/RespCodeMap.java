package com.yada.sdk.zp;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回码映射信息
 * 
 * @author JiangFengMing
 * 
 */
class RespCodeMap {

	private static final RespCodeMap respCodeMap = new RespCodeMap();

	private Map<String, String> map;

	private RespCodeMap() {
		map = new HashMap<String, String>();
		map.put("00", "交易成功");
		map.put("A2", "有缺陷的成功交易");
		map.put("01", "请与银行联系");
		map.put("03", "无效商户");
		map.put("05", "不批准交易");
		map.put("08", "请与银行联系");
		map.put("12", "无效交易");
		map.put("13", "无效金额");
		map.put("14", "无效卡号");
		map.put("21", "不做任何处理");
		map.put("25", "无此交易");
		map.put("26", "重复交易");
		map.put("28", "交易无法处理");
		map.put("30", "格式错误/");
		map.put("33", "过期卡");
		map.put("38", "PIN输入超次");
		map.put("39", "无此账户");
		map.put("40", "非法功能");
		map.put("41", "挂失卡");
		map.put("43", "被窃卡");
		map.put("51", "余额不足");
		map.put("54", "过期卡");
		map.put("55", "密码错误");
		map.put("56", "无此卡记录");
		map.put("57", "非法交易");
		map.put("61", "超出取款限额");
		map.put("62", "受限制卡");
		map.put("63", "违反安全保密规定");
		map.put("64", "无效原金额");
		map.put("65", "取款次数超过次数");
		map.put("67", "没收卡");
		map.put("75", "PIN输入超过次数/");
		map.put("77", "结算不平");
		map.put("78", "止付卡");
		map.put("79", "非法帐户");
		map.put("80", "交易拒绝");
		map.put("81", "卡已作废");
		map.put("82", "联网暂断,重做交易");
		map.put("83", "主机拒绝");
		map.put("84", "发卡下机");
		map.put("87", "PIN密钥同步错");
		map.put("88", "MAC密钥同步错");
		map.put("90", "系统暂停服务");
		map.put("91", "交易超时");
		map.put("92", "重做交易或电话授权");
		map.put("94", "重复交易/");
		map.put("96", "系统异常");
		map.put("97", "终端号错误");
		map.put("98", "暂与发卡行失去联络");
		map.put("99", "PIN格式错");
		map.put("N0", "不匹配的交易");
		map.put("N1", "Valid Unmatched Transaction");
		map.put("Q2", "有效期错");
		map.put("SK", "无效卡校验");
	}

	private String get(String respCode) {
		return map.get(respCode);
	}

	public static String getMessage(String respCode) {
		return respCodeMap.get(respCode);
	}
}
