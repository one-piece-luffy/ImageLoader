package com.allfootball.news.imageloader.glide

import android.content.Context
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.allfootball.news.imageloader.glide.OkHttpAppGlideModule
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.allfootball.news.imageloader.progress.ProgressManager
import com.bumptech.glide.load.model.GlideUrl
import android.os.Environment
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import java.io.InputStream

/**
 * glide 使用文档
 * https://muyangmin.github.io/glide-docs-cn/doc/configuration.html
 */
@GlideModule
class OkHttpAppGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
//        return super.isManifestParsingEnabled();
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setMemoryCache(LruResourceCache(1024 * 1024 * 20))
        // /data/user/0/com.allfootball.news/cache/image_manager_disk_cache/9ac362afb1982630262bf50025a9246ae3fb77c4426cbd41ed2108cfe8d37562.0
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 100));
        // 完全自定义路径
        builder.setDiskCache(
            DiskLruCacheFactory(
                getDownloadPath(context),
                "glide_cache", 1024 * 1024 * 100
            )
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val factory = OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient())
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    companion object {
        fun getDownloadPath(context: Context): String {
            if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) return context.filesDir.absolutePath
            var file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if (file != null) return file.absolutePath
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            return if (file != null) file.absolutePath else context.filesDir.absolutePath
        }
    }
}