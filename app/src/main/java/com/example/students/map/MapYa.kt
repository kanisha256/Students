package com.example.students.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.students.R
import com.example.students.list.Univers
import com.example.students.prof.HomeActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider


class MapYa : ComponentActivity() {
    private val MAPKIT_API_KEY = "ba1e4ee2-a331-4269-8e6b-085954773601"
    private val TARGET_LOCATION: Point = Point(55.7887, 49.1221)

    private var mapView: MapView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        // Создание MapView

        setContentView(R.layout.map)
        super.onCreate(savedInstanceState)
        mapView = findViewById<View>(R.id.mapview) as MapView

        drawMyLocationMark()

        var button = findViewById(R.id.button2) as Button
        button.setOnClickListener {
            startActivity(Intent(this@MapYa, Univers::class.java))
            finish()
        }
        // Перемещение камеры в центр Казани
        mapView!!.map.move(
            CameraPosition(TARGET_LOCATION, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )
    }

    private fun drawMyLocationMark() {
        val view = View(this).apply {
            background = getDrawable(R.drawable.placeholder)
        }
        val coord1 = intent.getStringExtra("coord1")
        val coord2 = intent.getStringExtra("coord2")
        mapView!!.map.mapObjects.addPlacemark(
            Point(coord1!!.toDouble(),coord2!!.toDouble()),
            ViewProvider(view)
        )
    }

    override fun onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView!!.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView!!.onStart()
    }
}


