package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

/**
 * 分析回令结果
 * 
 * @author lazy_xia
 * 
 */
public class CmdResponseResult {

	/** 0 成功 1 失败 或者需要继续执行**/
	public int state;

	public Object data;

	public boolean isBroad;

	public String action = BleConst.SF_ACTION_DEVICE_RETURNDATA;
}
