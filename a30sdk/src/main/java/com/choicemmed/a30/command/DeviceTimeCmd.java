package com.choicemmed.a30.command;


import android.util.Log;

import com.choicemmed.a30.BleConst;

public class DeviceTimeCmd extends ICommand {

	
	public DeviceTimeCmd() {
		super();
		cmd=BleConst.SEND_GET_TIME;
	}

	@Override
	public
	CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		CmdResponseResult result = new CmdResponseResult();
		if (resp.contains(BleConst.RECEIVE_TIME)) {
			String s = resp.substring(8, resp.length() - 2);
			result.state = 0;
			result.isBroad = true;
			result.data = "DateTime:" + analyize(s);
		}
		Log.i("1-19", "返回设备时间" + result.data);
		return result;
	}

	private String analyize(String s) {
		String str;
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
		str = strs[0] + "-" + strs[1] + "-" + strs[2] + ":" + strs[3] + ":"
				+ strs[4] + ":" + strs[5];
		return str;
	}
}
