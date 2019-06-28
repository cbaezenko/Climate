package com.singorenko.climate.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singorenko.climate.R


class MainCityFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_city, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainCityFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
