package com.choicemmed.a30.command;

import android.util.Log;

import com.choicemmed.a30.BleConst;

public class DeviceVersionCmd extends ICommand {

	public DeviceVersionCmd() {
		cmd = BleConst.SEND_GET_VERSION;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		CmdResponseResult result = new CmdResponseResult();
		if (resp.contains(BleConst.RECEIVE_VERSION)) {
			String s = resp.substring(8, resp.length() - 2);
			result.state = 0;
			result.isBroad = true;
			result.data = "Version:" + anaylize(s);
			result.action=BleConst.SF_ACTION_DEVICE_RETURNDATA;
		}
		Log.i("1-19", "返回设备版本" + result.data);
		return result;
	}

	private String anaylize(String s) {
		Log.i("1-19", "返回设备版本11" + s);
		int m = s.length() / 2;
		if (m * 2 < s.length()) {
			m++;
		}
		String[] strs = new String[m];
		char[] chars = new char[m];
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

		StringBuilder sbBuilder = new StringBuilder();
		for (int i = 0; i < m; i++) {
			chars[i] = (char) Integer.parseInt(strs[i]);
			sbBuilder.append(chars[i]);
		}
		return sbBuilder.toString();
	}
}
