package bases;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication  extends Application {

    private static Context sContext;
    private static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        sHandler=new Handler();
    }

    public static Context getContext() {
        return sContext;
    }

    public static Handler getHandler() {
        return sHandler;
    }
}
