package cn.android.water.androidtestdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundServices extends Service {

    private int count = 0;
    private boolean threadDisable=false;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    public BackgroundServices() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!threadDisable) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException inx) {
                        Log.e("Demo Error:", inx.getMessage());
                    }

                    count++;
                    Log.d("Demo debug:", "Now Count is:" + count);

                    //send broadcast message to activity
                    Intent intent = new Intent();
                    intent.putExtra("count", count);
                    intent.setAction("cn.android.water.androidtestdemo.BackgroundServices");
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    public class LocalBinder extends Binder {
        BackgroundServices getService() {
            // Return this instance of LocalService so clients can call public methods
            return BackgroundServices.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
        threadDisable = true;
        Log.d("Demo debug:","Service destroy");
    }
}
