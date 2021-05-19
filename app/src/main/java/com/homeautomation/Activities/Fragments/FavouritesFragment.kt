package com.homeautomation.Activities.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.homeautomation.R
import com.homeautomation.viewModels.DeviceViewModel

class FavouritesFragment : Fragment() {

  private lateinit var DeviceViewModel: DeviceViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    DeviceViewModel =
            ViewModelProvider(this).get(com.homeautomation.viewModels.DeviceViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_favourites, container, false)

    return root
  }
}