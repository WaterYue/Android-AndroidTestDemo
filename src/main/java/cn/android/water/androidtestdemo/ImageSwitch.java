package cn.android.water.androidtestdemo;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import java.io.File;

import cn.android.water.androidtestdemo.util.OnRecyclerItemClickListener;

public class ImageSwitch extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int Wimg = 300;
    private int Wimg2 = 600;
    private int Himg2 = 500;
    public String[] myDataset;
    ImageSwitcher imageSwitcher;
    //int currentPosition=0;
    ImageView currentImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_switch);

        mRecyclerView = (RecyclerView) findViewById(R.id.Recycler);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        imageSwitcher = (ImageSwitcher)this.findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imgV = new ImageView(ImageSwitch.this);
                //imgV.setImageBitmap(currentImage);
                imgV.setAdjustViewBounds(true);
                imgV.setLayoutParams(new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imgV.setBackgroundColor(Color.BLACK);
                return imgV;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        // specify an adapter (see also next example)
        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if(c!=null)
        {
            //if(currentImage == null) currentImage = ImageSwitchAdapter.decodeThumbBitmapForFile( myDataset[0],Wimg2,Himg2,true);
            final String[] myDataset = new String[c.getCount()];
            int ci=0;
            while(c.moveToNext())
            {
                myDataset[ci] = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                ci ++;
            }
            //first time load the first image
            Uri uri = Uri.fromFile(new File(myDataset[0]));
            imageSwitcher.setImageURI(uri);

            mAdapter = new ImageSwitchAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(this, new OnRecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(myDataset!=null && myDataset.length > 0) {
                        Uri uri = Uri.fromFile(new File(myDataset[position]));
                        imageSwitcher.setImageURI(uri);
                        if(currentImage!=null)currentImage.setImageAlpha(255);//set old ImageView Alpha to 100
                        currentImage =(ImageView)((RelativeLayout) view).getChildAt(0);
                        currentImage.setImageAlpha(100);
                    }
                }
            }));
        }
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_switch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
