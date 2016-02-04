package com.choicemmed.a30.command;


import com.choicemmed.a30.BleConst;

public class SetPwdCmd extends ICommand {

	
	public SetPwdCmd(String pwd) {
		super();
		cmd=BleConst.SEND_PWD4MATHCH_PREFIX+pwd;
	}

	@Override
	public CmdResponseResult analyseResponse(String resp) {
		// TODO Auto-generated method stub
		CmdResponseResult result = new CmdResponseResult();
		if (resp.contains(BleConst.RECEIVE_PWD_REVIEW)) {
			result.data = 0;
			result.isBroad = true;
			if (resp.contains(BleConst.RECEIVE_PWD_REVIEW + "00")) {
				result.data = "密码审核成功。。";
			} else if (resp.contains(BleConst.RECEIVE_PWD_REVIEW + "01")) {
				result.data = "密码审核失败。。";
			}
		}
		result.action=BleConst.SF_ACTION_DEVICE_RETURNDATA;
		return result;
	}

}
