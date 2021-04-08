package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010

    //var personas:ArrayList<Persona>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //findViewById<EditText>(R.id.editTextUsuario).editableText?.text("casai_usuario1@gmail.com")
        //findViewById<EditText>(R.id.editTextContrasena).text("usuariocasai")


        /*personas = ArrayList()
        personas?.add(Persona("roko1", R.drawable.basketball))
        personas?.add(Persona("roko2", R.drawable.slipknot))
        personas?.add(Persona("roko3", R.drawable.lamb))
        personas?.add(Persona("roko4", R.drawable.basketball))
        personas?.add(Persona("roko5", R.drawable.slipknot))
        personas?.add(Persona("roko6", R.drawable.lamb))
        personas?.add(Persona("roko7", R.drawable.basketball))*/

    }

    fun loginEmail(v: View){
        val usuario = findViewById<EditText>(R.id.editTextUsuario).text.toString()
        val contrasena = findViewById<EditText>(R.id.editTextContrasena).text.toString()
        Log.e("Login","Usuario: $usuario  |  Contraseña: $contrasena")

        if (usuario.isNotEmpty() || contrasena.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contrasena).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("Login","Success!")
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                    Log.e("Login:","Permission: " + CheckPermission().toString())
                    Log.e("Login:","Location Enable: " + isLocationEnabled().toString())
                    RequestPermission()
                    getLastLocation()
                }
                else{
                    //TODO MOSTRAR TOAST DE ERROR EN EL LOGIN
                    Log.e("Login","Error!")
                    Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        val x = location.latitude
                        val y = location.longitude
                        val city = getCityName(location.latitude,location.longitude)
                        val cadena = "You Current Location is \nLat: "+ x + "\nLon: " + y +"\n" + city
                        //textViewGeo.text = cadena

                        Log.d("1Debug:" ,"Your Lat:"+ x)
                        Log.d("1Debug:" ,"Your Lon:"+ y)
                        Log.d("1Debug:" ,"Your City:"+ city)
                        val intent = Intent(this, MyNewsFeed::class.java)
                        intent.putExtra("latitud", x.toString())
                        intent.putExtra("longitud", y.toString())
                        startActivity(intent)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation

            val x = lastLocation.latitude.toString()
            val y = lastLocation.longitude.toString()
            val city = getCityName(lastLocation.latitude,lastLocation.longitude)
            val cadena = "You Last Location is \nLat: "+ x + "\nLon: " + y +"\n" + city

            //Log.d("Debug:" ,"Your last Lat:"+ x)
            //Log.d("Debug:" ,"Your last Lon:"+ y)
            //Log.d("Debug:" ,"Your last City:"+ city)

            //Log.d("Debug:","your last last location: " + lastLocation.longitude.toString())
            //textViewGeo.text = cadena
        }
    }

    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        //Log.d("Debug:","Your City: " + cityName + "\nYour Country " + countryName)
        return cityName
    }
}