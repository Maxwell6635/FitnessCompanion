package com.choicemmed.a30.command;

import android.util.Log;

import com.choicemmed.a30.BleConst;

public class DeviceRequsePwdCmd extends ICommand {
	public DeviceRequsePwdCmd() {
		super();
		cmd = BleConst.SEND_PWD2MATCH;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {

		CmdResponseResult result = new CmdResponseResult();
		if (resp.contains(BleConst.RECEIVE_PWD_PREFIX)) {

			result.state = 0;
			result.isBroad = true;
			result.data = analys(resp.subSequence(8, resp.length() - 2) + "");

			result.action = BleConst.SR_ACTION_SEND_PWD;
			// result.data = analyize(s);
		}
		Log.i("1-19", "接收到密码了。。" + result.data);
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

		long code1 = (Integer.parseInt(strs[1]) << 8)
				+ Integer.parseInt(strs[0]);
		long code2 = (Integer.parseInt(strs[strs.length - 1]) << 8)
				+ Integer.parseInt(strs[strs.length - 2]);

		long rsaData = Get_d();// 获取密钥

		long decode1 = DecrData(code1);
		long decode2 = DecrData(code2);

		long pairCode = (decode2 << 8) + decode1; // pairCode 就是密码
		SetPwdCmd pwdCmd = new SetPwdCmd(BleConst.SEND_PWD4MATHCH_PREFIX
				+ pairCode + "");
		//
		// CmdQuene.getInstance().addCmdTOP(pwdCmd);
		// CmdQuene.getInstance().executeNextCmd(service, gatt);
		String str = pairCode + "";
		if (str.length() < 4) {
			str = "0" + pairCode;
		}

		return str;
	}

	static long Prime_stat_1 = 43; // 密钥生成素数1
	static long Prime_stat_2 = 47; // 密钥生成素数2
	static long Rsa_Key = 31; // 公钥

	// 获取最大公约数,加密解密核心程序
	static long candp(long mc, long d, long t) {
		long r = 1;
		d = d + 1;
		while (d != 1) // 模运算后取余数
		{
			r = r * mc;
			r = r % t;
			d--;
		}
		return r;
	}

	static long Get_d() {
		long d = 0;
		long Euler;
		Euler = (Prime_stat_1 - 1) * (Prime_stat_2 - 1);
		while (!(Rsa_Key * (++d) % Euler == 1))
			;
		return d;

	}

	static long DecrData(long Data) {
		return candp(Data, Rsa_Key, Prime_stat_1 * Prime_stat_2);
	}

}
