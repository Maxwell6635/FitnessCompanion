package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetDistanceUnitCmd extends ICommand {

	public SetDistanceUnitCmd(String data) {
		super();
		cmd = BleConst.SEND_SET_DISUNIT + String.format("%02X", Integer.parseInt(data));
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		return analyse(resp);
	}

}
