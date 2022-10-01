package com.skywalker.test.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.test.R
import com.skywalker.test.services.model.Photo
import com.skywalker.test.view.adpaters.PhotoAdpater
import com.skywalker.test.viewmodel.HomeActivityViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Task_Three.newInstance] factory method to
 * create an instance of this fragment.
 */
class Task_Three : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var rv_view:RecyclerView;
     var thrid_view:View?=null;
     lateinit var appCompatActivity:AppCompatActivity;
     lateinit var photoAdpater:PhotoAdpater;
    lateinit var homeActivityViewModel:HomeActivityViewModel;



    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity=context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (thrid_view==null) {
            thrid_view= inflater.inflate(R.layout.fragment_task__three, container, false)
        }
        initView()
        initPhotoAdpater()
        return thrid_view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeActiviyViewModel()
    }

    private fun initHomeActiviyViewModel() {
        homeActivityViewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java];
        homeActivityViewModel.getPhotoApi(0,10)
        homeActivityViewModel.responseModel.observe(appCompatActivity, Observer {

            when (it.resultCode) {

                200 -> {
                    //Success

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


    private fun initPhotoAdpater() {
        photoAdpater = PhotoAdpater(appCompatActivity, ArrayList(),true);
        rv_view.apply {
            layoutManager = GridLayoutManager(appCompatActivity, 2);
            adapter = photoAdpater;
        }

    }

    private fun initView() {
        thrid_view?.let {
            rv_view=it.findViewById(R.id.rv_view);

        }

    }


}