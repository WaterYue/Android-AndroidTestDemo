package cn.android.water.androidtestdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by water.yue on 2015/7/10.
 */
public class WindmillView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;

    private boolean isRunning = true;
    /**
     * 屏幕的像素
     */
    private int screenWidth;
    private int screenHeiht;

    private Bitmap windPoint;

    /**
     * 风车图片
     */
    private Bitmap Windmill;

    /**
     * 背景图片
     */
    private Bitmap viewBg;

    public WindmillView(Context context) {
        super(context);
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888); // 顶层绘制SurfaceView设成透明
        getViewSize(context);
        LoadWindmillImage();

    }

    private void LoadWindmillImage() {
        viewBg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_na);
        Windmill = BitmapFactory.decodeResource(getResources(),
                R.drawable.point);
        windPoint = BitmapFactory.decodeResource(getResources(),
                R.drawable.cic);
        float percent = percentumW();
        Log.v("icers", screenWidth + "");

        int _witdh = (int) (250 / percent);//250是风车基点左侧像素
        Log.v("icers", _witdh + "");
        Windmill = Bitmap.createScaledBitmap(Windmill, _witdh * 2, _witdh * 2,
                true);
    }

    // 获取屏幕的分辨率
    private void getViewSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.screenHeiht = metrics.heightPixels;
        this.screenWidth = metrics.widthPixels;

        Log.d("Windmill", "Windmill:"+screenHeiht+"|"+screenWidth);
    }
    /**
     * 获取背景图和风车的比率 ，从而根据这个比例改变各个手机上面的风车图片大小
     *
     *
     * @return
     */
    private float percentumW() {
        float bg_width = viewBg.getWidth();
        return  bg_width/screenWidth ;
    }
    /**
     * 获取背景图和风车的比率 ，从而根据这个比例改变各个手机上面的风车图片大小
     *
     *
     * @return
     */
    private float percentumH() {
        float bg_height = viewBg.getHeight();
        return  bg_height/(screenHeiht);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        float rotate = 0;// 旋转角度变量
        while (isRunning) {
            Log.i("icer", "Running");
            Canvas canvas = null;
            synchronized (this) {
                try{
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);

                        // 对图片抗锯齿
                        paint.setFilterBitmap(true);
                        RectF rect = new RectF(0, 0, screenWidth, screenHeiht
                        );
                        canvas.drawBitmap(viewBg, null, rect, paint);
                        Matrix matrix = new Matrix();
                        matrix.postRotate((rotate += 2) % 360f,
                                Windmill.getWidth() / 2,
                                Windmill.getHeight() / 2);

                        int _dy = (int) (500 /percentumH()); //500是风车基点到背景定点的像素
                        matrix.postTranslate(0, (_dy - (Windmill.getHeight()/2)));
                        canvas.drawBitmap(Windmill, matrix, paint);

                        int _dx = (int) (250 / percentumW());//250是风车基点左侧像素
                        canvas.drawBitmap(windPoint,_dx-windPoint.getWidth()/2,_dy-windPoint.getHeight()/2,paint);
                        Thread.sleep(3);
                    }
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    public void setRunning(boolean state) {
        isRunning = state;

    }

}
