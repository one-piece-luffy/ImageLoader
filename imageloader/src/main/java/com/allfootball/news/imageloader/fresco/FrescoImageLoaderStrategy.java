//
//package com.allfootball.news.util.image;
//
//import com.allfootball.news.BaseApplication;
//import com.allfootball.news.util.AppUtils;
//import com.allfootball.news.util.FrescoInterceptor;
//import com.allfootball.news.util.Trace;
//import com.facebook.binaryresource.BinaryResource;
//import com.facebook.binaryresource.FileBinaryResource;
//import com.facebook.cache.common.CacheKey;
//import com.facebook.cache.disk.DiskCacheConfig;
//import com.facebook.common.executors.CallerThreadExecutor;
//import com.facebook.common.internal.Supplier;
//import com.facebook.common.memory.MemoryTrimType;
//import com.facebook.common.memory.MemoryTrimmable;
//import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
//import com.facebook.common.references.CloseableReference;
//import com.facebook.common.soloader.SoLoaderShim;
//import com.facebook.common.util.ByteConstants;
//import com.facebook.common.util.UriUtil;
//import com.facebook.datasource.DataSource;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
//import com.facebook.drawee.controller.BaseControllerListener;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
//import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
//import com.facebook.imagepipeline.cache.MemoryCacheParams;
//import com.facebook.imagepipeline.common.RotationOptions;
//import com.facebook.imagepipeline.core.ImagePipeline;
//import com.facebook.imagepipeline.core.ImagePipelineConfig;
//import com.facebook.imagepipeline.core.ImagePipelineFactory;
//import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
//import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
//import com.facebook.imagepipeline.image.CloseableImage;
//import com.facebook.imagepipeline.image.ImageInfo;
//import com.facebook.imagepipeline.image.ImmutableQualityInfo;
//import com.facebook.imagepipeline.image.QualityInfo;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//import com.getkeepsafe.relinker.ReLinker;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Animatable;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.widget.ImageView;
//
//import java.io.File;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//
///**
// * Created by lihaiyi on 2018/6/14.
// */
//
//public class FrescoImageLoaderStrategy implements BaseImageLoaderStrategy {
//
//    public static final String TAG = "FrescoImageLoaderStrategy";
//
//    @Override
//    public void init() {
//        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
//            @Override
//            public int getNextScanNumberToDecode(int scanNumber) {
//                return scanNumber + 2;
//            }
//
//            public QualityInfo getQualityInfo(int scanNumber) {
//                boolean isGoodEnough = (scanNumber >= 5);
//                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
//            }
//        };
//
//        // 内存配置
//        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(20 * 1024 * 1024, // 内存缓存中总图片的最大大小,以字节为单位。
//                Integer.MAX_VALUE, // 内存缓存中图片的最大数量。
//                20 * 1024 * 1024, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
//                Integer.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
//                Integer.MAX_VALUE); // 内存缓存中单个图片的最大大小。
//
//        // 修改内存图片缓存数量，空间策略（这个方式有点恶心）
//        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
//            @Override
//            public MemoryCacheParams get() {
//                return bitmapCacheParams;
//            }
//        };
//        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
//            @Override
//            public void trim(MemoryTrimType trimType) {
//                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
//                if (MemoryTrimType.OnCloseToDalvikHeapLimit
//                        .getSuggestedTrimRatio() == suggestedTrimRatio
//                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground
//                        .getSuggestedTrimRatio() == suggestedTrimRatio
//                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground
//                        .getSuggestedTrimRatio() == suggestedTrimRatio
//                        || MemoryTrimType.OnAppBackgrounded
//                        .getSuggestedTrimRatio() == suggestedTrimRatio) {
//                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
//                }
//            }
//        });
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(new FrescoInterceptor()).connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS);
//        // if (AppUtils.isDnsOn(BaseApplication.getInstance())) {
////        clientBuilder.addInterceptor(HttpDnsUtils.getDnsInterceptor());
//        // }
//        // if (BaseApplication.DEBUG)
//        // clientBuilder.addNetworkInterceptor(new StethoInterceptor());
//
////        final ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
////                .newBuilder(context, clientBuilder.scheme()).setProgressiveJpegConfig(pjpegConfig)
//        final ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
//                .newBuilder(BaseApplication.getInstance(), clientBuilder.build()).setProgressiveJpegConfig(pjpegConfig)
//                // .setNetworkFetcher(new HttpUrlConnectionNetworkFetcher())
//                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
//                .setMainDiskCacheConfig(
//                        DiskCacheConfig.newBuilder(BaseApplication.getInstance()).setBaseDirectoryName("image_cache")
//                                .setBaseDirectoryPath(new File(AppUtils.getPicturePath(BaseApplication.getInstance())))
//                                .setMaxCacheSize(100 * ByteConstants.MB)
//                                .setMaxCacheSizeOnLowDiskSpace(50 * ByteConstants.MB)
//                                .setMaxCacheSizeOnVeryLowDiskSpace(20 * ByteConstants.MB).build())
//                .build();
//        // 尝试解决动态库加载失败问题
//        // bug：java.lang.NoClassDefFoundError: com/facebook/imagepipeline/memory/NativeMemoryChunk
//        SoLoaderShim.setHandler(new SoLoaderShim.Handler() {
//            @Override
//            public void loadLibrary(String libraryName) {
//                Trace.e(TAG, "library:" + libraryName);
//                ReLinker.loadLibrary(BaseApplication.getInstance(), libraryName);
//            }
//        });
//
//        Fresco.initialize(BaseApplication.getInstance(), config);
//    }
//
//    @Override
//    public void loadImage(Context context, String url, int placeholder, int errorResourceId,
//            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
//        // TODO: 2018/6/18
//
//    }
//
//    @Override
//    public void loadImage(Context context, Uri uri, int placeholder, int errorResourceId,
//            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
//        // TODO: 2018/6/18
//    }
//
//    @Override
//    public void loadImage(Context context, File file, int placeholder, int errorResourceId,
//            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
//        // TODO: 2018/6/18
//        imageView.setImageURI(Uri.parse("file://"+file.getAbsolutePath()));
//    }
//
//    @Override
//    public void loadImage(Context context, int resourceId, int placeholder, int errorResourceId,
//            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
//        imageView.setImageURI(AppUtils.parse(resourceId));
//    }
//
//    @Override
//    public void loadImage(final Context context, String url, final ImageLoader.ImageListener listener) {
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//
//        Uri uri = Uri.parse(url);
//        if (!UriUtil.isNetworkUri(uri)) {
//            uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(url).build();
//        }
//
//
//        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setProgressiveRenderingEnabled(true).build();
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
//                .fetchDecodedImage(imageRequest, this);
//        dataSource.subscribe(new BaseBitmapDataSubscriber() {
//            @Override
//            public void onNewResultImpl(@Nullable Bitmap bitmap) {
//                // Message msg = mHandler.obtainMessage();
//                if (listener != null) {
//                    listener.onSuccess( new BitmapDrawable(context.getResources(), bitmap));
//                }
//
//            }
//
//            @Override
//            public void onFailureImpl(DataSource dataSource) {
//
//            }
//
//            @Override
//            public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onCancellation(dataSource);
//            }
//
//            @Override
//            public void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onFailure(dataSource);
//                if (listener != null) {
//                    listener.onFail();
//                }
//            }
//
//            @Override
//            public void onNewResult(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onNewResult(dataSource);
//
//            }
//
//            @Override
//            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onNewResultImpl(dataSource);
//
//            }
//        }, CallerThreadExecutor.getInstance());
//    }
//
//    @Override
//    public void loadImage(Context context, String url, ImageView imageView, boolean autoPlayGif, final ImageLoader.ImageListener listener) {
//        if(imageView instanceof SimpleDraweeView){
//            SimpleDraweeView simpleDraweeView=(SimpleDraweeView)imageView;
//            ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(AppUtils.parse(url));
//            imageRequestBuilder.setRotationOptions(RotationOptions.autoRotate());
//            // 不支持图片渐进式加载，理由：https://github.com/facebook/fresco/issues/1204
//            imageRequestBuilder.setProgressiveRenderingEnabled(false);
//
//
//
//            ImageRequest imageRequest = imageRequestBuilder.build();
//
//            PipelineDraweeControllerBuilder draweeControllerBuilder = Fresco.newDraweeControllerBuilder();
//            draweeControllerBuilder.setOldController(simpleDraweeView.getController());
//            draweeControllerBuilder.setImageRequest(imageRequest);
//
//            draweeControllerBuilder.setControllerListener( new BaseControllerListener<ImageInfo>(){
//                @Override
//                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//                    super.onFinalImageSet(id, imageInfo, animatable);
//                    if(listener!=null){
//                        listener.onSuccess(null);
//                    }
//                }
//
//                @Override
//                public void onFailure(String id, Throwable throwable) {
//                    super.onFailure(id, throwable);
//                    if(listener!=null){
//                        listener.onFail();
//                    }
//                }
//            });
//
//            draweeControllerBuilder.setTapToRetryEnabled(true); // 开启重试功能
//            draweeControllerBuilder.setAutoPlayAnimations(autoPlayGif); // 自动播放gif动画
//            DraweeController draweeController = draweeControllerBuilder.build();
//            simpleDraweeView.setController(draweeController);
//
//
////        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(
////                context.getResources()).setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
////                .setProgressBarImage(AppUtils.getImageLoadProgress(context.getApplicationContext()))
//////                .setRetryImage(context.getResources().getDrawable(R.drawable.load_failed_retry))
////                .setFailureImage(errorResourceId).build();
////
////        simpleDraweeView.setHierarchy(hierarchy);
//        }
//
//    }
//
//    @Override
//    public void downloadImage(final Context context, final String url, final ImageLoader.ImageListener listener) {
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//
//        Uri uri = Uri.parse(url);
//        if (!UriUtil.isNetworkUri(uri)) {
//            uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(url).build();
//        }
//
//
//        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setProgressiveRenderingEnabled(true).build();
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
//                .fetchDecodedImage(imageRequest, this);
//        dataSource.subscribe(new BaseBitmapDataSubscriber() {
//            @Override
//            public void onNewResultImpl(@Nullable Bitmap bitmap) {
//                // Message msg = mHandler.obtainMessage();
//                if (listener != null) {
//                    listener.onSuccess(new BitmapDrawable(context.getResources(), bitmap));
//                    File file = getFrescoCachedImageOnDisk(AppUtils.parse(url));
//                    if (file != null) {
//                        listener.onDownloaded(file.getAbsolutePath());
//                        Trace.e("FrescoImageLoaderStrategy","file path:"+file.getAbsolutePath());
//                    } else {
//                        listener.onFail();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailureImpl(DataSource dataSource) {
//
//            }
//
//            @Override
//            public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onCancellation(dataSource);
//            }
//
//            @Override
//            public void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onFailure(dataSource);
//                if (listener != null) {
//                    listener.onFail();
//                }
//            }
//
//            @Override
//            public void onNewResult(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onNewResult(dataSource);
//
//            }
//
//            @Override
//            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                super.onNewResultImpl(dataSource);
//
//            }
//        }, CallerThreadExecutor.getInstance());
//
//
//    }
//
//    @Override
//    public void clearCache() {
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.clearMemoryCaches();
//        imagePipeline.clearDiskCaches();
//    }
//
//    @Override
//    public void clearMemoryCache() {
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.clearMemoryCaches();
//    }
//
//
//    @Override
//    public File getCachedOnDisk(String url, ImageLoader.ImageListener listener) {
//        Uri loadUri=AppUtils.parse(url);
//        File localFile = null;
//        try {
//            if (loadUri != null) {
//                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                        .getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
//                if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
//                    BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache()
//                            .getResource(cacheKey);
//                    localFile = ((FileBinaryResource) resource).getFile();
//                } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache()
//                        .hasKey(cacheKey)) {
//                    BinaryResource resource = ImagePipelineFactory.getInstance()
//                            .getSmallImageFileCache().getResource(cacheKey);
//                    localFile = ((FileBinaryResource) resource).getFile();
//                }
//
//            }
//            if (listener != null) {
//                if(localFile!=null){
//
//                    listener.onDownloaded(localFile.getAbsolutePath());
//                }else {
//                    listener.onFail();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return localFile;
//    }
//
//    public static File getFrescoCachedImageOnDisk(Uri loadUri) {
//        File localFile = null;
//        try {
//            if (loadUri != null) {
//                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                        .getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
//                if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
//                    BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache()
//                            .getResource(cacheKey);
//                    localFile = ((FileBinaryResource) resource).getFile();
//                } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache()
//                        .hasKey(cacheKey)) {
//                    BinaryResource resource = ImagePipelineFactory.getInstance()
//                            .getSmallImageFileCache().getResource(cacheKey);
//                    localFile = ((FileBinaryResource) resource).getFile();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return localFile;
//    }
//
//}
