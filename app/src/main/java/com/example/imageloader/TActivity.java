package com.example.imageloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allfootball.news.imageloader.ImageLoader;
import com.allfootball.news.imageloader.ImageOption;
import com.allfootball.news.imageloader.glide.RoundCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class TActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tactivity);
        RecyclerView rv=findViewById(R.id.iv);
        rv.setLayoutManager(new GridLayoutManager(this,3));
//        rv.setLayoutManager(new LinearLayoutManager(this));
        List<String> list=new ArrayList<>();
        list.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhiphotos.baidu.com%2Ffeed%2Fpic%2Fitem%2Fa686c9177f3e67093183c94037c79f3df9dc558a.jpg&refer=http%3A%2F%2Fhiphotos.baidu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623831963&t=4f2d32725e0a946cc7303c098b52bc1d");
        list.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fu001.sy1114.com%2F20170618%2Fdf722f90fe846b8d9986dcd7daa6ccc4.gif&refer=http%3A%2F%2Fu001.sy1114.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623831963&t=82781bdeb5d76eb7e72c68b4728c92a4");
        list.add("https://img0.baidu.com/it/u=2330851010,38487412&fm=26&fmt=auto&gp=0.jpg");
        list.add("https://img1.baidu.com/it/u=1858423681,2635904233&fm=26&fmt=auto&gp=0.jpg");
        list.add("https://img1.baidu.com/it/u=3036377285,704057693&fm=26&fmt=auto&gp=0.jpg");
        list.add("https://img2.baidu.com/it/u=1163617028,1546411149&fm=15&fmt=auto&gp=0.jpg");
        list.add("https://img2.baidu.com/it/u=1937854995,541116557&fm=26&fmt=auto&gp=0.jpg");
        list.add("https://img1.baidu.com/it/u=124607369,3626505470&fm=26&fmt=auto&gp=0.jpg");
        MyAdapter adapter=new MyAdapter(this,list);
        rv.setAdapter(adapter);
    }

    class MyAdapter  extends RecyclerView.Adapter{

        public List<String> list;
        Context context;
        public MyAdapter(Context context, List<String> list){
            this.context=context;
            this.list=list;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list,null));
            return holder;
        }

        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
            MyViewHolder holder1= (MyViewHolder) holder;
            ImageOption option2=new ImageOption.Builder().url(list.get(position))
                    .imageView(holder1.iv)
                    .placeholder(R.drawable.placeholder)
                    .errorResourceId(R.drawable.error)
                    .radiusDp(10)
                    .build();
            ImageLoader.getInstance().loadImage(context,option2);


            RequestOptions options = new RequestOptions();
                    RoundCornersTransformation transform = new RoundCornersTransformation(context, 30, 0);
                    options.transform(new CenterCrop(),transform);


//                Glide.with(context)
//                    .load(list.get(position))
//                    .dontAnimate()
//                    .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.error)
//                        .apply(options)
//                    .into(holder1.iv);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }
    class  MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv;
        public MyViewHolder( View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
        }
    }
}