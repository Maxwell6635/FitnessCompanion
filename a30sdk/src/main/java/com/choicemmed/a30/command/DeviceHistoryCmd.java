package com.choicemmed.a30.command;

import android.util.Log;

import com.choicemmed.a30.BleConst;

import java.util.Arrays;
import java.util.HashMap;

public class DeviceHistoryCmd extends ICommand {

	CmdResponseResult result = new CmdResponseResult();

	String str = "";

	public DeviceHistoryCmd() {
		super();
		cmd = BleConst.SEND_GET_HISTORYDATA;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		if (resp.contains(BleConst.RECEIVE_HISTORYDATA_OVER)) {
			result.state = 0;
			result.isBroad = true;
			result.data = analyize(str);
			Log.i("1-19", result.data + "");
		}
		else {
			str += resp;
			String s = resp.substring(10, resp.length() - 2);
			result.state = 1;
			result.isBroad = false;
		}
		Log.i("1-19", "返回设备历史数据" + result.data);
		return result;
	}

	private String analyize(String ss) {
		HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
		HashMap<String, Object> sleepMap = new HashMap<String, Object>();
		HashMap<String, Object> stepMap = new HashMap<String, Object>();

		String[] data = ss.split("55AA39C3");
		// System.out.println(Arrays.toString(data));

		String result = "";
		for (int k = 1; k < data.length; k++) {
			int count = Integer.parseInt(data[k].substring(0, 2), 16);// 有效数据个数
			// System.out.println("count=" + count);

			int m = data[k].length() / 4;
			String[] strs = new String[m];
			int j = 0;
			int a = 0;
			String date = "";// 日期
			int min = 0;// 分钟数
			String spStr = data[k].substring(2, data[k].length());

			for (int i = 1; i < spStr.length(); i++) {

				if (i % 4 == 0) {
					strs[j] = spStr.substring(a, i);
					if (strs[j] != null) {
						String binaryString = Integer.toBinaryString(Integer
								.parseInt(strs[j], 16));
						String s = String.format("%016d", Long.parseLong(binaryString));

						if (s.startsWith("11")) {
							int year = 2018 - Integer.parseInt(s.substring(2, 7), 2);
							int month = Integer.parseInt(s.substring(8, 12), 2)-1;
							int day = Integer.parseInt(s.substring(13, s.length()), 2)+7;
							strs[j] = year + "年" + month + "月" + day + "日:";
							date = strs[j];
						} else if (s.startsWith("10")) {
							int minutes = Integer.parseInt(s.substring(2, s.length()), 2);
							strs[j] = minutes + "分:";
							min = minutes;
						} else if (511 == Integer.parseInt(s.substring(s.length() - 9, s.length()), 2)) {
							int s_data = Integer.parseInt(s.substring(1, 7), 2);
							strs[j] = "睡眠数据：" + s_data;
							sleepMap.put(date + min, strs[j]);
							min++;
						} else {
							int s_data = Integer.parseInt(s.substring(s.length() - 9, s.length()), 2);
							strs[j] = "步数：" + s_data;
							min++;
							stepMap.put(date + min, strs[j]);
						}

					}

					j++;
					a = i;
				}

			}
			result += Arrays.toString(strs).substring(1, Arrays.toString(strs).length() - 5);
		}
		map.put("step", stepMap);
		map.put("sleep", sleepMap);
//		System.out.println(map);
		System.out.println(result);
		return result;

	}
}
