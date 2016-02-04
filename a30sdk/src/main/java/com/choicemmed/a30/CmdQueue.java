package com.choicemmed.a30;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.choicemmed.a30.command.AnayizeUtil;
import com.choicemmed.a30.command.CmdResponseResult;
import com.choicemmed.a30.command.DeviceRequsePwdCmd;
import com.choicemmed.a30.command.ICommand;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CmdQueue {

    private static Context context;
    private static final String TAG = "CmdQueue";
    private static List<ICommand> cmdList = new LinkedList<ICommand>();
    private static CmdQueue queue;

    private CmdQueue() {
    }

    public void initializationCmdList() {
        cmdList.clear();
        cmdList.add(new DeviceRequsePwdCmd());
        Log.i(TAG, cmdList.size() + "  " + cmdList);
    }

    public synchronized static CmdQueue getInstance(Context ctx) {
        context = ctx;
        if (queue == null) {
            queue = new CmdQueue();
        }
        return queue;
    }

    public ICommand getCurrentCmd() {
        if (cmdList.size() > 0)
            return cmdList.get(0);
        else
            return null;

    }

    public void removeCurrentCmd() {
        if (cmdList.size() > 0) {
            cmdList.remove(0);
            // TODO 取消延迟重发
        }
    }

    public void addCmd(ICommand cmd) {
        cmdList.add(cmd);
    }

    public void addCmdTOP(ICommand cmd) {
        cmdList.add(0, cmd);
    }

    public void executeNextCmd(BluetoothGattService service, BluetoothGatt gatt) {
        Log.i(TAG, "executeNextCmd ");
        if (cmdList.isEmpty()) {
            return;
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleConst.Characteristic_UUID_CD20));
        String cmd = cmdList.get(0).cmd;
        Log.i("analysResp", "Send Command" + cmd);
        byte[] value = AnayizeUtil.getBytesIncludeVerifySum(cmd);
        Log.i(TAG, "send order.." + Arrays.toString(value));
        characteristic.setValue(value);
        gatt.writeCharacteristic(characteristic);

    }

    /**
     * 分析设备响应数据
     *
     * @param characteristic
     * @param service
     * @param gatt
     * @param dataString
     * @return
     */
    public void analysResp(BluetoothGattCharacteristic characteristic,
                           BluetoothGattService service, BluetoothGatt gatt, String dataString) {
        Log.i("1-19", "response" + dataString);
        if (characteristic.getUuid().equals(UUID.fromString(BleConst.Characteristic_UUID_CD04))) {
            if (dataString.startsWith(BleConst.Receive_DATA_REALTIME)) {
                Log.i("analysResp", "Real Time Step");

                byte[] bytes = AnayizeUtil.getHexBytes(dataString);
                int a = (bytes[3] & 0x000000ff)
                        | ((bytes[4] & 0x000000ff) << 8)
                        | ((bytes[5] & 0x000000ff) << 16)
                        | ((bytes[6] & 0x000000ff) << 24);
                updateBroadCast(BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP, a + "");
                executeNextCmd(service, gatt);
            }
        } else {
            ICommand cmd = cmdList.get(0);
            CmdResponseResult result = cmd.analyseResponse(dataString);
            if (result.state == 0) {
                //移除掉当前的命令
                String str = cmdList.remove(0).cmd;
                Log.i("analysResp", "Remove。。。" + str);
            }

            if (result.isBroad) {
                updateBroadCast(result.action, result.data + "");
                executeNextCmd(service, gatt);
            }

        }
    }

    private void updateBroadCast(String action, String data) {
        Intent intent = new Intent(action);
        if (data != null && data != "") {
            intent.putExtra("DATA", data);
        } else {
            intent.putExtra("DATA", "EMPTY");
        }
        if (context != null)
            context.sendBroadcast(intent);
    }

    public void clearList() {
        if (cmdList != null) {
            cmdList.clear();
        }
    }
}
