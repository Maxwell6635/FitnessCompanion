package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetGreetCmd extends ICommand {

	public SetGreetCmd(String str) {
		super();
		cmd = BleConst.SEND_SET_GREET + pack2Cmd(str);
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		return analyse(resp);
	}

	private String pack2Cmd(String str) {
		char[] charArray = str.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < charArray.length; i++) {
			builder.append(String.format("%02X", (int) charArray[i]));
		}

		return builder.toString() + "00";
	}
}
