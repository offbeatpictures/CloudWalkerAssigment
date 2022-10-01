package com.skywalker.test.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skywalker.test.R
import com.skywalker.test.services.model.Photo
import com.skywalker.test.view.adpaters.PhotoAdpater
import com.skywalker.test.viewmodel.HomeActivityViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Task_One : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var appCompatActivity: AppCompatActivity;

    private var task_one_view: View? = null;
    private lateinit var rv_view: RecyclerView;
    private lateinit var homeActivityViewModel: HomeActivityViewModel;
    private lateinit var floating_btn: FloatingActionButton;
    private lateinit var photoAdpater: PhotoAdpater;
    private lateinit var progress_bar: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (task_one_view == null) {
            task_one_view = inflater.inflate(R.layout.fragment_task__one, container, false);
        }
        initViews()
        initPhotoAdpater()
        return task_one_view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeActiviyViewModel()
    }


    private fun initPhotoAdpater() {
        photoAdpater = PhotoAdpater(appCompatActivity, ArrayList());
        rv_view.apply {
            layoutManager = GridLayoutManager(appCompatActivity, 2);
            adapter = photoAdpater;
        }

    }

    private fun initHomeActiviyViewModel() {
        homeActivityViewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java];
        homeActivityViewModel.getPhotoApi(0,200)
        homeActivityViewModel.responseModel.observe(appCompatActivity, Observer {
            progress_bar.visibility = View.GONE
            floating_btn.isClickable = true;
            when (it.resultCode) {

                200 -> {
                    //Success
                    if (photoAdpater.itemCount > 0) {
                        rv_view.smoothScrollToPosition(photoAdpater.itemCount - 1)
                    }
                    photoAdpater.setListPhotos(it.response as ArrayList<Photo>)
                }

                404 -> {
                    //some error
                    Toast.makeText(appCompatActivity, "Something Went Wrong!", Toast.LENGTH_SHORT)
                        .show();
                }

                500 -> {
                    //some error
                    Toast.makeText(appCompatActivity, "Something Went Wrong!", Toast.LENGTH_SHORT)
                        .show();
                }


            }

        })
    }

    private fun initViews() {
        task_one_view?.let {
            rv_view = it.findViewById(R.id.rv_view)
            floating_btn = it.findViewById(R.id.floating_btn)
            progress_bar = it.findViewById(R.id.progress_bar);

        }

        floating_btn.setOnClickListener(View.OnClickListener {
            progress_bar.visibility = View.VISIBLE
            homeActivityViewModel.loadMoreItems()
            floating_btn.isClickable = false;
        })
    }


}