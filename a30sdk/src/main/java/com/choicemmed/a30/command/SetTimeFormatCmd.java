package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetTimeFormatCmd extends ICommand {

	public SetTimeFormatCmd(String data) {
		super();
		cmd = BleConst.SEND_SET_TIME_FORMAT + pack2Cmd(data);

	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		return analyse(resp);
	}

	private String pack2Cmd(String str) {
		if ("12".equals(str)) {
			return "01";
		}

		else if ("24".equals(str)) {
			return "00";
		}
		return null;
	}
}
