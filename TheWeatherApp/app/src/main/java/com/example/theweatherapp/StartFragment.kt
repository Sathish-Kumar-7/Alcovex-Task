package com.example.theweatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.theweatherapp.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private lateinit var binding:FragmentStartBinding
    private val LOCATION_PERMISSION_REQUEST_CODE=101
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(layoutInflater)
        binding.fetchDataBtn.setOnClickListener{
            if(hasLocationPermissionGranted()) requestPermission()
            else jumpToReportFragment()
        }
        requestPermission()
        return binding.root
    }
    private fun jumpToReportFragment(){
        findNavController().navigate(StartFragmentDirections.actionStartFragmentToReportFragment())
    }
    private fun requestPermission() {
        var permission = mutableListOf<String>()
        if (hasLocationPermissionGranted()) {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (permission.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permission.toTypedArray(), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    private fun hasLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(requireContext(),"Permission Granted", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Permission Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}