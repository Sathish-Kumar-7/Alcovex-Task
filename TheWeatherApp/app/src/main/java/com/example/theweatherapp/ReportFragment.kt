package com.example.theweatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.theweatherapp.databinding.FragmentReportBinding
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class ReportFragment : Fragment(),LocationListener {
    private lateinit var binding:FragmentReportBinding
    private var longitude:Double = 0.0
    private var latitude:Double = 0.0
    private var API = "5f7a9ffa4a1df5a116c589298d1c03c0"
    private lateinit var locationManager: LocationManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(layoutInflater)
        changeCity()
        WeatherTask().execute()
        return binding.root
    }
    private fun changeCity(){
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location = if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }else
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            onLocationChanged(location!!)
            binding.cityTextView.text = getCurrentLocation(location)

    }
    inner class WeatherTask() : AsyncTask<String,Void,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            binding.progressBar.visibility= View.VISIBLE
            binding.linearLayout.visibility = View.GONE
        }
        override fun doInBackground(vararg p0: String?): String? {
            Thread.sleep(1000)
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=${binding.cityTextView.text}&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }catch (e:java.lang.Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val jsObj = JSONObject(result)
                val main = jsObj.getJSONObject("main")
                val temperature = main.getString("temp")+"°C"
                binding.temperatureTextView.text = temperature
                binding.progressBar.visibility= View.GONE
                binding.linearLayout.visibility = View.VISIBLE
            }catch (e:Exception){
                binding.progressBar.visibility= View.GONE
                binding.linearLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        longitude = p0.longitude
        latitude = p0.latitude
    }
    private fun getCurrentLocation(location:Location):String?{
        try {
            var geocoder = Geocoder(requireContext())
            var address: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            return address[0].locality
        }catch (e: IOException){
            Toast.makeText(requireContext(),"No Permission",Toast.LENGTH_SHORT).show()
        }
        return null
    }
}