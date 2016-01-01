package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class DelStepCmd extends ICommand {

	public DelStepCmd() {
		super();
		cmd = BleConst.SEND_DEL_STEP;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		return analyse(resp);
	}

}
