package cn.android.water.androidtestdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar pbar ;
    Button btnStart;
    BackgroundServices mService;
    boolean mBound = false;
    boolean draw=true;
    SurfaceView sv;
    FrameLayout frame1 ;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame1 = (FrameLayout)this.findViewById(R.id.frame1);
        sv = (SurfaceView)this.findViewById(R.id.surfaceView);
        pbar = (ProgressBar)this.findViewById(R.id.progressBar);
        pbar.setMax(100);

        btnStart = (Button) this.findViewById(R.id.btnStart);

        //register broadcast receiver
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.android.water.androidtestdemo.BackgroundServices");
        MainActivity.this.registerReceiver(receiver, filter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {
                    //start background service
                    Intent intent = new Intent(MainActivity.this, BackgroundServices.class);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                    btnStart.setText("Stop Service");
                } else {
                    unbindService(mConnection);
                    mBound = false;
                    btnStart.setText("Start Service");
                }
            }
        });
        //Image Switch View
        btn1 = (Button)this.findViewById(R.id.btn1);
        btn1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ImageSwitch.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
            //return true;
          break;
            case R.id.action_pen:
                if(!draw) {
                    draw = true;
                    frame1.removeAllViews();
                }else {
                    draw = false;
                    WindmillView myview = new WindmillView(MainActivity.this);
                    frame1.addView(myview);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        //stop service
        super.onDestroy();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BackgroundServices.LocalBinder binder = (BackgroundServices.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    /*
     * Broadcast Receiver
     */
     class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //get parameters from service
            Bundle bundle = intent.getExtras();
            int count = bundle.getInt("count");
            if(count >= 100)
            {
                if (mBound) {
                    unbindService(mConnection);
                    mBound = false;
                    btnStart.setText("Start Service");
                }
            }else {
                //show on screen
                pbar.setProgress(count);
            }
        }
    }
}
