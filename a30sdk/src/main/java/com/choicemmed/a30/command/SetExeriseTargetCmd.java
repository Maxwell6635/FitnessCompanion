package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetExeriseTargetCmd extends ICommand {

	public SetExeriseTargetCmd(String data) {
		super();
		cmd = BleConst.SEND_SET_PERGOAL + pack2Cmd(data);
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		return analyse(resp);
	}

	private String pack2Cmd(String str) {

		return AnayizeUtil.int2HexString(Integer.parseInt(str));
	}

}
