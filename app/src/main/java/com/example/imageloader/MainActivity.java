package com.example.imageloader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.allfootball.news.imageloader.constant.ScaleType;
import com.allfootball.news.imageloader.glide.RoundCornersTransformation;
import com.allfootball.news.imageloader.progress.CircleProgressView;
import com.allfootball.news.imageloader.progress.OnProgressListener;
import com.allfootball.news.imageloader.ImageConfig;
import com.allfootball.news.imageloader.ImageLoader;
import com.allfootball.news.imageloader.ImageOption;
import com.allfootball.news.imageloader.util.ImageLoaderUtil;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView=findViewById(R.id.img);
        ImageView iv1=findViewById(R.id.img1);
        ImageView iv2=findViewById(R.id.img2);
        ImageView iv3=findViewById(R.id.img3);
        ImageView iv4=findViewById(R.id.img4);
        ImageView iv5=findViewById(R.id.img5);
        ImageView iv6=findViewById(R.id.img6);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TActivity.class);
                startActivity(intent);
            }
        });
        CircleProgressView circleProgressView=findViewById(R.id.progressView);

        String path= ImageLoaderUtil.getDownloadPath(this) +"/glide_download";
        ImageLoader.getInstance().init(new ImageConfig.Builder()
                .strategyType(ImageLoader.STRATEGY_TYPE.GLIDE)
                .downloadPath(path)
                .build());

        ImageLoader.getInstance().clearCache(this);

        String url1="https://img2.baidu.com/it/u=1297178068,356861250&fm=253&app=138&f=JPEG?w=800&h=1435";
//        url1="https://mp3.haoge500.com/9kuimg/zhuanji/20161114/1b33a084d1ad0af6.jpg?x-oss-process=image/resize,m_fill,w_200,h_200,limit_0/auto-orient,0";
//        Map<String,String> header=new HashMap<>();
//        header.put("referer","https://www.zz123.com/");
        ImageOption option=new ImageOption.Builder().url(url1)
                .radiusDp(10)
                .imageView(imageView)
                .addHeader("referer","https://www.zz123.com/")
                .cornerType(RoundCornersTransformation.CornerType.LEFT)
//                .imageView(imageView)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                        Log.e("asdf","percent:"+percentage);
                        circleProgressView.setProgress(percentage);
                        circleProgressView.setVisibility(View.VISIBLE);
                    }
                })
                .listener(new ImageLoader.ImageListener(){
                    @Override
                    public void onSuccess(Drawable drawable, boolean isGif) {
                        super.onSuccess(drawable, isGif);
                        Log.e("asdf","suc");
                        imageView.setImageDrawable(drawable);
                        circleProgressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {
                        super.onFail();
                        circleProgressView.setVisibility(View.GONE);
                        Log.e("asdf","onFail");
                    }
                })
                .build();
        ImageLoader.getInstance().loadImage(this,option);



        String url="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fu001.sy1114.com%2F20170618%2Fdf722f90fe846b8d9986dcd7daa6ccc4.gif&refer=http%3A%2F%2Fu001.sy1114.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623831963&t=82781bdeb5d76eb7e72c68b4728c92a4";
        //gif
        ImageLoader.getInstance().url(url)
                .imageView(iv1)
                .context(this)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .autoPlayGif(true)
                .roundAsCircle(true)
                .roundingBorderColor(0xffffffff)
                .roundingBorderWidth(6).loadImage(this);

        //圆角
        ImageLoader.getInstance().resId(R.drawable.yz)
                .imageView(iv2)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .radiusDp(10)

                .loadImage(this);
        //圆角
        ImageLoader.getInstance()
                .url(url1)
                .resId(R.drawable.nm1)
                .imageView(iv4)
                .scaleType(ScaleType.FitCenter.value)
                .cornerType(RoundCornersTransformation.CornerType.TOP)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .radiusDp(25).loadImage(this);

        //自定义效果：高斯模糊
        ImageLoader.getInstance().resId(R.drawable.yz)
                .imageView(iv3)
                .transformation(new MultiTransformation(new CenterCrop(), new BlurTransformation(25, 3)))
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .loadImage(this);
        //bitmap
        ImageLoader.getInstance()
                .url(url)
//                .resId(R.drawable.nm1)
                .asBitmap(true)
                .scaleType(ScaleType.FitCenter.value)
                .cornerType(RoundCornersTransformation.CornerType.TOP)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .radiusDp(25)
                .listener(new ImageLoader.ImageListener(){
                    @Override
                    public void onBitMapSuccess(Bitmap bitmap, boolean isGif) {
                        super.onBitMapSuccess(bitmap, isGif);
                        iv5.setImageBitmap(bitmap);
                    }
                })
                .loadImage(this);
        String down="https://img0.baidu.com/it/u=2945667683,2071297187&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1200";
        ImageLoader.getInstance().downloadImage(this,down,null,new ImageLoader.ImageListener(){
            @Override
            public void onDownloaded(String filePath, String reNameFilepath) {
                super.onDownloaded(filePath, reNameFilepath);

                File file=new File(ImageLoader.getInstance().getConfig().downloadPath);
                File[] list=file.listFiles();
                ImageLoader.getInstance()
                        .file(list[0])
                        .imageView(iv6)
                        .loadImage(MainActivity.this);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageLoader.getInstance().clearCache(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearCache(this);
    }
}