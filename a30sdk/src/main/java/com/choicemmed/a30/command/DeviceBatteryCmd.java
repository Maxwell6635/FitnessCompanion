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
			result.data = "Level:Low";
		} else if (resp.contains(BleConst.RECEIVE_BATTERYPOWER + "02")) {
			result.data = "Level:Medium";
		} else if (resp.contains(BleConst.RECEIVE_BATTERYPOWER + "03")) {
			result.data = "Level:Full";
		}
		result.state = 0;
		result.isBroad = true;
		Log.i("1-19", "返回设备电量" + result.data);
		return result;
	}

}
