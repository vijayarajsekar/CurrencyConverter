package demo.currencyconverter.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by vijayaraj_s on 11/14/2017.
 */

public class ToastUtils {
    public static void shotToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
