package vn.edu.tdc.doan_d2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import vn.edu.tdc.doan_d2.model.responsive.RecipeResponsiveRetrofit;

public class DateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_DATE_CHANGED)){
            RecipeResponsiveRetrofit.resetRetrofitRunCount(context);
        }
    }
}
