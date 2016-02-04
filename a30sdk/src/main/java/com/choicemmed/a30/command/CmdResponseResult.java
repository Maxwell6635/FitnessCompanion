package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;


public class CmdResponseResult {

	public int state;

	public Object data;

	public boolean isBroad;

	public String action = BleConst.SF_ACTION_DEVICE_RETURNDATA;
}
