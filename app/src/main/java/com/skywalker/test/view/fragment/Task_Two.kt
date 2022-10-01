package com.skywalker.test.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.skywalker.test.R
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val DOWNLOAD_URL = "https://speed.hetzner.de/100MB.bin";
private const val FILE_NAME = "binFile"
private const val SHA256 = "20492a4d0d84f8beb1767f6616229f85d44c2827b64bdbfb260ee12fa1109e0e";


class Task_Two : Fragment(), OnDownloadListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var downloadId: Int? = null;

    lateinit var btn_start: Button;
    lateinit var btn_pause: Button;
    lateinit var linearProgressIndicator: LinearProgressIndicator;
    lateinit var progess_indicator_tv: TextView;
    lateinit var sha_tv: TextView;
    private var viewTask_Two: View? = null;

    private lateinit var appCompatActivity: AppCompatActivity;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(DOWNLOAD_URL)

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity;
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (viewTask_Two == null) {
            viewTask_Two = inflater.inflate(R.layout.fragment_task__two, container, false);
        }
        initViews();
        return viewTask_Two;
    }

    private fun initViews() {
        viewTask_Two?.let {
            btn_pause = it.findViewById(R.id.btn_pause);
            btn_start = it.findViewById(R.id.btn_start);
            linearProgressIndicator = it.findViewById(R.id.linearProgressIndicator);
            progess_indicator_tv = it.findViewById(R.id.progess_indicator_tv);
            sha_tv = it.findViewById(R.id.sha_tv);
        }

        btn_pause.setOnClickListener(View.OnClickListener {

            downloadId?.let {
                if (btn_pause.text.toString().lowercase().equals("pause")) {
                    btn_pause.isEnabled = false
                    btn_pause.text = "Resume"
                    PRDownloader.pause(it)
                } else {
                    btn_pause.text = "Pause"
                    PRDownloader.resume(it)
                }
            }

        })

        btn_start.setOnClickListener(View.OnClickListener {

            if (btn_start.text.toString().lowercase().equals("start")) {
                btn_start.text = "Stop"
                btn_start.isClickable = false;
                startDownload()
            } else {
                //Stop
                btn_start.text = "Start"
                downloadId?.let {
                    PRDownloader.cancel(it)
                }

            }

        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enabling database for resume support even after the application is killed:
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(activity, config)
    }


    fun startDownload() {
        downloadId =
            PRDownloader.download(
                DOWNLOAD_URL, getRootDirPath(appCompatActivity), URLUtil.guessFileName(
                    DOWNLOAD_URL, null, null
                )
            )
                .build()
                .setOnStartOrResumeListener {
                    Log.e(
                        "DownloadDetails",
                        "downloadID ${downloadId.toString()} downloadPath ${
                            getRootDirPath(appCompatActivity)
                        } downloadName ${
                            URLUtil.guessFileName(
                                DOWNLOAD_URL, null, null
                            )
                        }"
                    );
                    btn_start.isClickable = true
                }
                .setOnPauseListener {
                    btn_pause.isEnabled = true

                }
                .setOnCancelListener {
                    progess_indicator_tv.text = ""
                    linearProgressIndicator.setProgress(0)
                }
                .setOnProgressListener {
                    val progressPer: Long = it.currentBytes * 100 / it.totalBytes
                    // setting the progress to progressbar
                    // setting the progress to progressbar
                    linearProgressIndicator.setProgress(progressPer.toInt())
                    progess_indicator_tv.text =
                        getProgressDisplayLine(it.currentBytes, it.totalBytes)
                }.start(this);

    }

    override fun onDownloadComplete() {
        btn_start.isEnabled = false;
        btn_pause.isEnabled = false;
        verifyFile();
    }

    private fun verifyFile() {
        val file: File? = File(
            "${getRootDirPath(appCompatActivity)}/${
                URLUtil.guessFileName(
                    DOWNLOAD_URL, null, null
                )
            }"
        )
        file?.let {
            val messageDigest = MessageDigest.getInstance("SHA-256");
            val sha256 = getFileChecksum(messageDigest, it)
            Log.e("DownloadDetails", "SHA_256 ${sha256}")
            sha_tv.text = "SHA-256 -> ${sha256}"
        }

    }


    override fun onError(error: Error?) {
        TODO("Not yet implemented")
    }

    companion object {

        @Throws(IOException::class)
        private fun getFileChecksum(digest: MessageDigest, file: File): String? {
            //Get file input stream for reading the file content
            val fis = FileInputStream(file)

            //Create byte array to read data in chunks
            val byteArray = ByteArray(1024)
            var bytesCount = 0

            //Read file data and update in message digest
            while (fis.read(byteArray).also { bytesCount = it } != -1) {
                digest.update(byteArray, 0, bytesCount)
            }

            //close the stream; We don't need it now.
            fis.close()

            //Get the hash's bytes
            val bytes = digest.digest()

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(Integer.toString((bytes[i] and 0xff.toByte()) + 0x100, 16).substring(1))
            }

            //return complete hash
            return sb.toString()
        }


        fun getRootDirPath(context: Context): String? {
            return if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                val file: File =
                    ContextCompat.getExternalFilesDirs(context.getApplicationContext(), null)[0]
                file.getAbsolutePath()
            } else {
                context.getApplicationContext().getFilesDir().getAbsolutePath()
            }
        }

        fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String? {
            return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
        }

        private fun getBytesToMBString(bytes: Long): String {
            return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
        }
    }


}


