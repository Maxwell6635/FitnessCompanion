package com.choicemmed.a30.command;

import android.util.Log;

import com.choicemmed.a30.BleConst;

public class DeviceIDCmd extends ICommand {

	public DeviceIDCmd() {
		super();
		cmd = BleConst.SEND_GET_DEVICEID;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {

		CmdResponseResult result = new CmdResponseResult();
		if (resp.contains(BleConst.RECEIVE_DEVICEID)) {

			result.state = 0;
			result.isBroad = true;
			result.data = "Device ID:" + analys(resp.substring(8, resp.length() - 2));
			// TODO 解析
		}
		result.state = 0;
		result.isBroad = true;
		Log.i("1-19", "返回设备ID" + result.data);
		result.data = result.data + "";
		return result;
	}

	// rsa
	private String analys(String s) {
		int m = s.length() / 2;
		if (m * 2 < s.length()) {
			m++;
		}

		String[] strs = new String[m];
		int j = 0;
		for (int i = 0; i < s.length(); i++) {
			if (i % 2 == 0) {
				strs[j] = "" + s.charAt(i);
			} else {
				strs[j] = strs[j] + s.charAt(i);
				j++;
			}
		}

		for (int i = 0; i < m; i++) {
			strs[i] = Integer.valueOf(strs[i], 16) + "";
		}

		return String.format("%03d", Integer.parseInt(strs[0]) )
				+  String.format("%03d", Integer.parseInt(strs[1]) )
		        +  String.format( "%010d", ((Integer.parseInt(strs[strs.length - 1]) << 24)
				+ (Integer.parseInt(strs[strs.length - 2]) << 16)
				+ (Integer.parseInt(strs[strs.length - 3]) << 8)
				+ (Integer.parseInt(strs[strs.length - 4]))));
	}
}
