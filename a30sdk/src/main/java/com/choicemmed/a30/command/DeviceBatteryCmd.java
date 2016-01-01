package com.choicemmed.a30.command;

import android.util.Log;

import com.choicemmed.a30.BleConst;

public class DeviceBatteryCmd extends ICommand {

	public DeviceBatteryCmd() {
		super();
		cmd = BleConst.SEND_GET_BATTERYPOWER;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		CmdResponseResult result = new CmdResponseResult();

		if (resp.contains(BleConst.RECEIVE_BATTERYPOWER + "01")) {
			result.data = "电量低";
		} else if (resp.contains(BleConst.RECEIVE_BATTERYPOWER + "02")) {
			result.data = "电量中";
		} else if (resp.contains(BleConst.RECEIVE_BATTERYPOWER + "03")) {
			result.data = "电量高";
		}
		result.state = 0;
		result.isBroad = true;
		result.data += "    "+resp;
		result.action=BleConst.SF_ACTION_DEVICE_BATTERY;
		Log.i("1-19", "返回设备电量" + result.data);
		return result;
	}

}
