package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetTimeZoneCmd extends ICommand {

	public SetTimeZoneCmd(String data) {
		super();
		cmd = BleConst.SEND_SET_TIMEZONE + pack2Cmd(data);
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		return analyse(resp);
	}

	private String pack2Cmd(String str) {
		String[] split = str.split("&");
		String hour = String.format("%02X", Integer.parseInt(split[0]));
		String minute = String.format("%02X", Integer.parseInt(split[1]));

		return hour + minute;
	}
}
