package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetTimeCmd extends ICommand {

	public SetTimeCmd(String data) {
		super();
		// 2014-06-09
		cmd = BleConst.SEND_SET_TIME + pack2Cmd(data);
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		return analyse(resp);
	}

	private String pack2Cmd(String str) {
		String[] split = str.split("-");
		String year = String.format("%02X", Integer.parseInt(split[0]) - 2000);
		String month = String.format("%02X", Integer.parseInt(split[1]));
		String day = String.format("%02X", Integer.parseInt(split[2]));
		String week = String.format("%02X", Integer.parseInt(split[3]));
		String hour = String.format("%02X", Integer.parseInt(split[4]));
		String minute = String.format("%02X", Integer.parseInt(split[5]));
		String second = String.format("%02X", Integer.parseInt(split[6]));

		return year + month + day + week + hour + minute + second;
	}

}
