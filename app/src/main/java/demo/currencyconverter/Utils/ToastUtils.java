package demo.currencyconverter.Utils;

import android.widget.Toast;

import demo.currencyconverter.Singleton;

/**
 * Created by vijayaraj_s on 11/14/2017.
 */

public class ToastUtils {
    public static void shotToast(String msg) {
        Toast.makeText(Singleton.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
