package enal1586.ju.viken_passage;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class BluetoothFunctions {

    public static String getLocalBluetoothName() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if device does not support Bluetooth
        if(mBluetoothAdapter==null){
            Log.d(TAG,"device does not support bluetooth");
            return null;
        }

        return mBluetoothAdapter.getName();
    }

    public static String getBluetoothMacAddress() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if device does not support Bluetooth
        if(mBluetoothAdapter==null){
            Log.d(TAG,"device does not support bluetooth");
            return null;
        }

        return mBluetoothAdapter.getAddress();
    }
}
