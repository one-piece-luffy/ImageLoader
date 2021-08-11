package com.example.imageloader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.allfootball.news.imageloader.glide.RoundCornersTransformation;
import com.allfootball.news.imageloader.progress.CircleProgressView;
import com.allfootball.news.imageloader.progress.OnProgressListener;
import com.allfootball.news.imageloader.ImageConfig;
import com.allfootball.news.imageloader.ImageLoader;
import com.allfootball.news.imageloader.ImageOption;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView=findViewById(R.id.img);
        ImageView iv1=findViewById(R.id.img1);
        ImageView iv2=findViewById(R.id.img2);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TActivity.class);
                startActivity(intent);
            }
        });
        CircleProgressView circleProgressView=findViewById(R.id.progressView);

        ImageLoader.getInstance().init(new ImageConfig.Builder().strategyType(ImageLoader.STRATEGY_TYPE.GLIDE).build());

        ImageLoader.getInstance().clearCache(this);


        ImageOption option=new ImageOption.Builder().url("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhiphotos.baidu.com%2Ffeed%2Fpic%2Fitem%2Fa686c9177f3e67093183c94037c79f3df9dc558a.jpg&refer=http%3A%2F%2Fhiphotos.baidu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623831963&t=4f2d32725e0a946cc7303c098b52bc1d")
                .radius(100)
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



        ImageLoader.getInstance().url("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fu001.sy1114.com%2F20170618%2Fdf722f90fe846b8d9986dcd7daa6ccc4.gif&refer=http%3A%2F%2Fu001.sy1114.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623831963&t=82781bdeb5d76eb7e72c68b4728c92a4")
                .imageView(iv1)
                .context(this)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .autoPlayGif(true)
                .roundAsCircle(true)
                .roundingBorderColor(0xffffffff)
                .roundingBorderWidth(20).loadImage(this);

        ImageLoader.getInstance().resId(R.drawable.yz)
                .imageView(iv2)
                .placeholder(R.drawable.placeholder)
                .errorResourceId(R.drawable.error)
                .radiusDp(10).loadImage(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageLoader.getInstance().clearCache(this);
    }
}