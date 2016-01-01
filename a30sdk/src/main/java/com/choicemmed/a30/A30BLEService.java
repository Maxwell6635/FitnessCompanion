package com.choicemmed.a30;

import android.content.Context;
import android.content.Intent;

public class A30BLEService {

	static A30BLEService instance;
	private static Context context;
	private static final String DATA = "DATA";

	private A30BLEService() {
		super();
	}

	public static synchronized A30BLEService getInstance(Context context) {
		A30BLEService.context = context;
		if (instance == null) {
			instance = new A30BLEService();
		}
		return instance;
	}

	private void updateBroadcast(String action, String str) {
		Intent intent = new Intent(action);
		if (str != null) {
			intent.putExtra(DATA, str);

		}
		context.sendBroadcast(intent);
	}

	/**
	 * 获取设备ID
	 **/
	public void didGetDeviceID() {

		updateBroadcast(BleConst.SR_ACTION_DEVICEID, null);
	}

	/**
	 * 获取电池电量
	 */
	public void didGetDeviceBattery() {
		updateBroadcast(BleConst.SR_ACTION_BATTERYPOWER, null);
	}

	/**
	 * 获取设备版本
	 */
	public void didGetVerson() {
		updateBroadcast(BleConst.SR_ACTION_VERSION, null);
	}

	/**
	 * 获取设备历史数据
	 */
	public void didGetHistoryDate() {
		updateBroadcast(BleConst.SR_ACTION_HISTORYDATA, null);
	}

	/**
	 * 设置welcome 显示字符 仅支持答谢字母和部分标点符号 最多不超过8个字符
	 * 
	 * @param str
	 *            !#%&'()+,-./0123456789:;=?ABCDEFGHIJKLMNOPQRSTUVWXYZ 不支持小写
	 */
	public void didSetGreet(String str) {
		updateBroadcast(BleConst.SR_ACTION_SET_GREET, str);
	}

	/**
	 * 设置基本信息
	 * 
	 * @param str
	 */
	public void didSetBaseInfo(String str) {
		updateBroadcast(BleConst.SR_ACTION_SET_BASEINFO, str);
	}

	/**
	 * 设置时间格式
	 * @param d  12or 24
	 */
	public void didSetTimeFormat(int d) {
		updateBroadcast(BleConst.SR_ACTION_SET_TIMEFORMAT, d + "");
	}

	/**
	 * 设置时间 rtc
	 * 
	 * @param ymd
	 *            年-月-日
	 * @param week
	 *            周 0-6 （0：周日）
	 * @param hour
	 *            时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 */
	public void didSetTime(String ymd, int week, int hour, int minute, int second) {
		updateBroadcast(BleConst.SR_ACTION_SET_TIME, ymd + "-" + week + "-"
				+ hour + "-" + minute + "-" + second);
	}

	/**
	 * 设置时区
	 * 
	 * @param hour_Tzone
	 *            小时时区 -12~12
	 * @param minute_Tzone
	 *            分钟时区 0~59
	 */
	public void didSetTimeZone(int hour_Tzone, int minute_Tzone) {
		updateBroadcast(BleConst.SR_ACTION_SET_TIMEZONE, hour_Tzone + "&"
				+ minute_Tzone);
	}

	/**
	 * 设置锻炼目标
	 * 
	 * @param str
	 *            最大不超过100000
	 */
	public void didSetExerciseTarget(int target) {
		updateBroadcast(BleConst.SR_ACTION_SET_EXERCISE_TARGET, target + "");
	}

	/**
	 * 设置距离 单位
	 * 
	 * @param str
	 *            0---公制 1--英制
	 */
	public void didSetDistanceUnit(int unit) {
		updateBroadcast(BleConst.SR_ACTION_SET_DISTANCE_UNIT, unit + "");
	}

	/**
	 * 设置温度 单位
	 * 
	 * @param str
	 *            0 摄氏度 1 华氏度
	 */
	public void didSetTempertureUnit(int t) {
		updateBroadcast(BleConst.SR_ACTION_SET_TEMPERATURE_UNIT, t + "");
	}

	/**
	 * 清除 历史数据
	 */
	public void didDelHistoryData() {
		updateBroadcast(BleConst.SR_ACTION_DEL_HISTORYDATA, null);
	}

	/**
	 * 获取设备时间
	 */
	public void didGetTime() {
		// TODO Auto-generated method stub
		updateBroadcast(BleConst.SR_ACTION_TIME, null);
	}

	/**
	 * 找蓝牙设备
	 */
	public void didFindDeivce() {
		updateBroadcast(BleConst.SR_ACTION_SCANDEVICE, null);
	}

	/**
	 * 设置 用户信息
	 * 
	 * @param gender
	 *            性别 1：保密 2：男 3：女 1
	 * @param year_birthday
	 *            出生年份
	 * @param month_birthday
	 *            出生月
	 * @param day_birthday
	 *            出生日
	 * @param height
	 *            身高
	 * @param weight
	 *            体重
	 */
	public void didSetPerInfo(int gender, int year_birthday,
			int month_birthday, int day_birthday, int height, int weight) {
		// TODO Auto-generated method stub
		updateBroadcast(BleConst.SR_ACTION_SET_PERINFO, gender + "&"
				+ year_birthday + "&" + month_birthday + "&" + day_birthday
				+ "&" + height + "&" + weight);

	}

	/**
	 * 链接之前已经绑定的蓝牙设备
	 * 
	 * @param serviceId2Compare
	 *            配对serviceUUID
	 * @param pwd2Compare
	 *            配对密码
	 * 
	 */
	public void didLinkDevice(String serviceId2Compare, String pwd2Compare) {
		updateBroadcast(BleConst.SR_ACTION_SCANDEVICE, serviceId2Compare + "&"
				+ pwd2Compare);
	}
}
