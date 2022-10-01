package com.skywalker.test.view.adpaters

import android.telephony.mbms.DownloadRequest
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.skywalker.test.R
import com.skywalker.test.services.model.Photo
import com.skywalker.test.view.fragment.Task_Two
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PhotoAdpater(
    private val appCompatActivity: AppCompatActivity,
    private val listOfPhotos: ArrayList<Photo>
) : RecyclerView.Adapter<PhotoAdpater.PhotoViewHolder>(),OnDownloadListener {

    var canDownload:Boolean=false;

    constructor( appCompatActivity: AppCompatActivity,
                listOfPhotos: ArrayList<Photo>,canDownload:Boolean) : this(appCompatActivity,listOfPhotos){
                    this.canDownload=canDownload;
                }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoAdpater.PhotoViewHolder {
        val view =
            LayoutInflater.from(appCompatActivity).inflate(R.layout.photo_layout, parent, false);
        return PhotoViewHolder(view);
    }

    override fun onBindViewHolder(holder: PhotoAdpater.PhotoViewHolder, position: Int) {
        val photo: Photo = listOfPhotos.get(position);
        holder.photo_tv.text = photo.title;
        val url = GlideUrl(
            photo.url, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )
        Glide.with(appCompatActivity).load(url).into(holder.photo_iv);

        if (photo.isDownloading){
            holder.downlaod_img.visibility=View.GONE
            holder.progress_download.visibility=View.VISIBLE
        }

        if (photo.onDownlaodCompleted){
            holder.download_rl.visibility=View.GONE
        }

    }

    override fun getItemCount(): Int = listOfPhotos.size;

    inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo_iv: ImageView = view.findViewById(R.id.photo_iv);
        val photo_tv: TextView = view.findViewById(R.id.photo_tv);
        val download_rl:RelativeLayout=view.findViewById(R.id.download_rl);
        val progress_download:ProgressBar=view.findViewById(R.id.progess_downlaod);
        val downlaod_img:ImageView=view.findViewById(R.id.img_download)


        init {
            if (canDownload){
                download_rl.visibility=View.VISIBLE
            }

            downlaod_img.setOnClickListener(View.OnClickListener {
                val photo:Photo=listOfPhotos.get(layoutPosition);
                photo.isDownloading= !photo.isDownloading
                notifyItemChanged(layoutPosition)

                startDownload(photo.url!!,layoutPosition);
            })

        }




    }

    fun setListPhotos(listOfPhotos: ArrayList<Photo>) {
        this.listOfPhotos.addAll(listOfPhotos);
        notifyDataSetChanged();
    }


    fun startDownload(DOWNLOAD_URL:String,position: Int) {
     val downloadId =
            PRDownloader.download(
                DOWNLOAD_URL, Task_Two.getRootDirPath(appCompatActivity), URLUtil.guessFileName(
                    DOWNLOAD_URL, null, null
                )
            )
                .build()
                .setOnStartOrResumeListener {

                }
                .setOnPauseListener {

                }
                .setOnCancelListener {

                }
                .setOnProgressListener {
                 if (it.currentBytes==it.totalBytes){
                     val photo=listOfPhotos.get(position);
                     photo.onDownlaodCompleted=true
                     notifyItemChanged(position)
                 }
                }.start(

                    this);


    }

    override fun onDownloadComplete() {
        Log.e("Download","Complered")
    }

    override fun onError(error: Error?) {
    }


}

