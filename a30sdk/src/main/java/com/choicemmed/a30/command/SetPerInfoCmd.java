package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public class SetPerInfoCmd extends ICommand {

	public SetPerInfoCmd(String data) {
		super();
		cmd = BleConst.SEND_SET_BASEINFO + pack2Cmd(data);
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		return analyse(resp);
	}

	private String pack2Cmd(String str) {
		String[] split = str.split("&");
		String gender = String.format("%02X", Integer.parseInt(split[0]));

		String hex = AnayizeUtil.int2HexString(Integer.parseInt(split[1]));
		String year = hex.substring(0, 4);

		String month = String.format("%02X", Integer.parseInt(split[2]));
		String day = String.format("%02X", Integer.parseInt(split[3]));
		String height = String.format("%02X", Integer.parseInt(split[4]));

		String strWeight = String.format("%04d",
				Integer.parseInt(split[5]) * 10);
		String weight = AnayizeUtil.int2HexString(Integer.parseInt(strWeight))
				.substring(0, 4);

		return gender + year + month + day + height + weight;
	}

}
