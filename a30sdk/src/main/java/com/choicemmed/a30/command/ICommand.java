package com.choicemmed.a30.command;

import com.choicemmed.a30.BleConst;

public abstract class ICommand {

	protected static String TAG;

	public ICommand() {
		TAG = getClass().getSimpleName();
	}

	/** 命令 如果有参数 +.. **/
	public String cmd;

	/**
	 * 分析回令
	 **/
	public abstract CmdResponseResult analyseResponse(String resp);

	CmdResponseResult analyse(String resp) {

		CmdResponseResult result = new CmdResponseResult();
		if (resp.contains(BleConst.RECEIVE_STATE)) {
			result.state = 0;
			if (resp.contains("F0")) {
				result.data = "设置成功";
				 
			} else if (resp.contains("FE")) {
				result.data = "设置失败";
			} else if (resp.contains("FF")) {
				result.data = "硬件故障";
				result.state = 1;
			}
			result.isBroad = true;
			result.action=BleConst.SF_ACTION_SETSUCCESSORFAILED;
			
		}
		return result;

	}


	 
}
