 ImageLoader.getInstance().url("https://img1.baidu.com/it/u=1268638900,302550486&fm=26&fmt=auto&gp=0.jpg")
 
                .imageView(iv1)
                
                .context(this)
                
                .placeholder(R.drawable.placeholder)
                
                .errorResourceId(R.drawable.error)
                
                .autoPlayGif(true)
                
                .roundAsCircle(true)
                
                .roundingBorderColor(0xffffffff)
                
                .roundingBorderWidth(20).loadImage(this);
