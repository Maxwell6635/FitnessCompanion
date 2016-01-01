package com.choicemmed.a30;

public class BleConst {
	/** 连接设备成功 **/
	public final static String SF_ACTION_DEVICE_CONNECTED_SUCCESS = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_CONNECTED_SUCCESS";
	/** 链接设备失败 **/
	public final static String SF_ACTION_DEVICE_CONNECTED_FAILED = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_CONNECTEDFAILED";
	/** 设备与手机链接状态断开 **/
	public final static String SF_ACTION_DEVICE_CONNECTED_STATE_DISCONNECTED = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_CONNECTED_STATE_DISCONNECTED";
	/** 设备与手机链接状态打开 **/
	public final static String SF_ACTION_DEVICE_CONNECTED_STATE_CONNECTED = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_CONNECTED_STATE_CONNECTED";
	/** 设备返回数据 **/
	public final static String SF_ACTION_DEVICE_RETURNDATA = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_RETURNDATA";
	/** 实时数据 **/
	public final static String SF_ACTION_DEVICE_RETURNDATA_STEP = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_RETURNDATA_STEP";
	/** gattService返回的uuid **/
	public final static String SF_ACTION_DEVICE_RETURNDATA_SERVICEID = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_RETURNDATA_SERVICEID";
	/** 返回配对密码 **/
	public final static String SF_ACTION_DEVICE_RETURNDATA_PWD = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_RETURNDATA_PWD";
	/** 查询设备id 所得 **/
	public final static String SF_ACTION_DEVICE_RETURNDATA_DEVICEID = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_RETURNDATA_DEVICEID";
	/** 打开蓝牙 **/
	public final static String SF_ACTION_OPEN_BLUETOOTH = "com.choicemmed.bluetooth.SF_ACTION_OPEN_BLUETOOTH";
	/** 发送密码 **/
	public final static String SF_ACTION_SEND_PWD = "com.choicemmed.bluetooth.SF_ACTION_SEND_PWD";
	
	/** 发送电池电量 **/
	public final static String SF_ACTION_DEVICE_BATTERY = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_BATTERY";
	
	/** 发送历史数据 **/
	public final static String SF_ACTION_DEVICE_HISDATA = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_HISDATA";
	
	/** 发送deviceID **/
	public final static String SF_ACTION_DEVICE_ID = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_ID";
	
	/** 发送rtc时间 **/
	public final static String SF_ACTION_DEVICE_RTCTIME = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_RTCTIME";
	
	/** 发送版本号 **/
	public final static String SF_ACTION_DEVICE_VERSION = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_VERSION";
	
	/** 密码审核 **/
	public final static String SF_ACTION_DEVICE_PWDAUDIT = "com.choicemmed.bluetooth.SF_ACTION_DEVICE_PWDAUDIT";
	
	/** 设置成功或失败 **/
	public final static String SF_ACTION_SETSUCCESSORFAILED = "com.choicemmed.bluetooth.SF_ACTION_SETSUCCESSORFAILED";

	
	
	
	
	/** 扫描蓝牙设备 **/
	public final static String SR_ACTION_SCANDEVICE = "com.choicemmed.bluetooth.SR_ACTION_SCANDEVICE";
	/** 停止扫描 **/
	public final static String SR_ACTION_STOPSCANDEVICE = "com.choicemmed.bluetooth.SR_ACTION_STOPSCANDEVICE";
	/** 与设备链接状态断开 **/
	public final static String SR_ACTION_STATE_DISCONNECTED = "com.choicemmed.bluetooth.SR_ACTION_STATE_DISCONNECTED";
	/** service接收并链接时保存的密码 并发送给下位机 **/
	public final static String SR_ACTION_SEND_PWD = "com.choicemmed.bluetooth.SR_ACTION_SEND_PWD";
	/** 删除步数 **/
	public final static String SR_ACTION_DEL_STEP = "com.choicemmed.bluetooth.SR_ACTION_DEL_STEP";
	/** 设置设备时间 **/
	public final static String SR_ACTION_SET_TIME = "com.choicemmed.bluetooth.SR_ACTION_SET_TIME";
	/** 设置设备基本信息 **/
	public final static String SR_ACTION_SET_BASEINFO = "com.choicemmed.bluetooth.SR_ACTION_SET_BASEINFO";
	/** 设置设备时区 **/
	public final static String SR_ACTION_SET_TIMEZONE = "com.choicemmed.bluetooth.SR_ACTION_SET_TIMEZONE";
	/** 设置设备距离单位 **/
	public final static String SR_ACTION_SET_DISTANCE_UNIT = "com.choicemmed.bluetooth.SR_ACTION_SET_DISTANCE_UNIT";
	/** 设置设备温度单位 **/
	public final static String SR_ACTION_SET_TEMPERATURE_UNIT = "com.choicemmed.bluetooth.SR_ACTION_SET_TEMPERATURE_UNIT";
	/** 设置设备时间制式 **/
	public final static String SR_ACTION_SET_TIMEFORMAT = "com.choicemmed.bluetooth.SR_ACTION_SET_TIMEFORMAT";
	/** 设置锻炼目标 **/
	public final static String SR_ACTION_SET_EXERCISE_TARGET = "com.choicemmed.bluetooth.SR_ACTION_SET_EXERCISE_TARGET";
	/** 设置设备问候语 **/
	public final static String SR_ACTION_SET_GREET = "com.choicemmed.bluetooth.SR_ACTION_SET_GREET";
	/** 设置个人信息 **/
	public final static String SR_ACTION_SET_PERINFO = "com.choicemmed.bluetooth.SR_ACTION_SET_PERINFO";
	/** 获取设备ID **/
	public final static String SR_ACTION_DEVICEID = "com.choicemmed.bluetooth.SR_ACTION_DEVICEID";
	/** 获取设备电池电量 **/
	public final static String SR_ACTION_BATTERYPOWER = "com.choicemmed.bluetooth.SR_ACTION_BATTERYPOWER";
	/** 获取设备时间 **/
	public final static String SR_ACTION_TIME = "com.choicemmed.bluetooth.SR_ACTION_TIME";
	/** 获取设备版本号 **/
	public final static String SR_ACTION_VERSION = "com.choicemmed.bluetooth.SR_ACTION_VERSION";
	/** 获取设备历史数据 **/
	public final static String SR_ACTION_HISTORYDATA = "com.choicemmed.bluetooth.SR_ACTION_HISTORYDATA";
	/** 清楚历史数据 **/
	public final static String SR_ACTION_DEL_HISTORYDATA = "com.choicemmed.bluetooth.SR_ACTION_DEL__HISTORYDATA";
	/** 发送获取设备ID **/
	public final static String SEND_GET_DEVICEID = "AA5502C0C2";
	/** 接受设备ID 前缀 **/
	public final static String RECEIVE_DEVICEID = "55AA08A0";

	/** 获取设备软件版本 **/
	public final static String SEND_GET_VERSION = "AA5502C1C3";
	/** 接受设备软件版本前缀 **/
	public final static String RECEIVE_VERSION = "55AA0DA1";

	/** 前缀 设置 返回 F0表示成功 FE表示失败 FF表示硬件故障 **/
	public final static String RECEIVE_STATE = "55AA02";

	/** 设置设备时间 **/
	public final static String SEND_SET_TIME = "AA5509C2";

	/** 设置设备基本信息 前缀 **/
	public final static String SEND_SET_BASEINFO = "AA550AC3";

	/** 设置设备时区 前缀 **/
	public final static String SEND_SET_TIMEZONE = "AA5504C4";

	/** 设置设备时间制式 12/24 **/
	public final static String SEND_SET_TIME_FORMAT = "AA5503C5";

	/** 获取电池电量 **/
	public final static String SEND_GET_BATTERYPOWER = "AA5502CED0";
	/** 设备返回电量信息 前缀 01 电量低 02 电量中 03 电量高 **/
	public final static String RECEIVE_BATTERYPOWER = "55AA03B9";

	/** 获取设备rtc时间 **/
	public final static String SEND_GET_TIME = "AA5502D2D4";
	/** 设备返回 时间 前缀 **/
	public final static String RECEIVE_TIME = "55AA08B2";

	/** 清除设备计步数据 **/
	public final static String SEND_DEL_STEP = "AA5502E0E2";

	/**
	 * A30 前缀 设置问候语 必须是asiic码 最多支持8个字符 以0x00结束
	 * 可以使用!#%&'()+,-./0123456789:;=?ABCDEFGHIJKLMNOPQRSTUVWXYZ 不支持小写
	 **/
	public final static String SEND_SET_GREET = "AA550BC6";

	// /** 设置设备功能模式显示顺序 **/

	/** 前缀 设置用户锻炼运动目标步数 最大步数为100000步 **/
	public final static String SEND_SET_PERGOAL = "AA5506C8";

	/** 获取用户运动数据(历史数据) 0x00表示分钟数据 **/
	public final static String SEND_GET_HISTORYDATA = "AA5503C900CC";
	/** 获取历史数据返回 有数据 前缀 **/

	/** 获取历史数据返回 结束 **/
	public final static String RECEIVE_HISTORYDATA_OVER = "55AA02F0F2";

	/**
	 * 设备升级指令 当需要对设备进行升级时 发送如下指令就可唤醒设备升级功能 设备只支持usb hid 方式升级， 升级协议符合ST提供的USB
	 * HID类Bloadloader标准
	 **/
	public final static String SEND_DEVICE_UPGRADE = "AA5502CACC";// 设备收到此命令后直接进入升级模式
																	// 不回复任何信息

	/** 前缀 设置设备距离单位 可是公制km 或者是英制mil 00公制 01 英制 **/
	public final static String SEND_SET_DISUNIT = "AA5503CB";

	/** 前缀 设置温度单位 可以是 00摄氏度 01 华氏摄氏度 **/
	public final static String SEND_SET_TEMPUNIT = "AA5503CC";

	/** 发送请求配对 **/
	public final static String SEND_PWD2MATCH = "AA5502B0B2";
	/** 接收配对密码前缀 **/
	public final static String RECEIVE_PWD_PREFIX = "55AA06B3";
	/** 发送密码配对 **/
	public final static String SEND_PWD4MATHCH_PREFIX = "AA5504B1";
	/** 接收密码后审核 后跟00表示正确 01 表示密码审核失败 **/
	public final static String RECEIVE_PWD_REVIEW = "55AA03B1";
	/** 实时上数 实时步数 小段模式 **/
	public final static String Receive_DATA_REALTIME = "55AA05";

	public static final String Characteristic_UUID_CD01 = "0000cd01-0000-1000-8000-00805f9b34fb";
	public static final String Characteristic_UUID_CD02 = "0000cd02-0000-1000-8000-00805f9b34fb";
	public static final String Characteristic_UUID_CD03 = "0000cd03-0000-1000-8000-00805f9b34fb";
	public static final String Characteristic_UUID_CD04 = "0000cd04-0000-1000-8000-00805f9b34fb";
	public static final String Characteristic_UUID_CD20 = "0000cd20-0000-1000-8000-00805f9b34fb";

}
