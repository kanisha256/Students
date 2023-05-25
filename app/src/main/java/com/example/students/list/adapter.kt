package com.example.students.list

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.students.R
import java.io.IOException
import java.util.*

class adapter(private val context: Context,var datalist: ArrayList<model?>?) :
    RecyclerView.Adapter<adapter.myviewholder>() {

    // Define a listener interface
    interface OnItemClickListener {
        fun onItemClick(item: model?, coord1:String, coord2:String)
    }

    // Declare a listener variable
    private var listener: OnItemClickListener? = null

    // Set the listener
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.singlerow, parent, false)
        return myviewholder(view)
    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {
        holder.t1.setText(datalist!![position]!!.name)
        holder.t2.setText(datalist!![position]!!.nap)
        holder.t3.setText("Стоимость: " + datalist!![position]!!.forma)
        holder.t5.setText(getAddressFromLocation(context, datalist!![position]!!.coord1!!.toDouble(),datalist!![position]!!.coord2!!.toDouble()))
    }

    override fun getItemCount(): Int {
        return datalist!!.size
    }

    fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var result = ""
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val thoroughfare = address.thoroughfare ?: ""
                val subThoroughfare = address.subThoroughfare ?: ""
                val locality = address.locality ?: ""
                result = "$locality, $thoroughfare $subThoroughfare"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }


    inner class myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var t1: TextView
        var t2: TextView
        var t3: TextView
        var t5: TextView

        init {
            t1 = itemView.findViewById<TextView>(R.id.t1)
            t2 = itemView.findViewById<TextView>(R.id.t2)
            t3 = itemView.findViewById<TextView>(R.id.t3)
            t5 = itemView.findViewById<TextView>(R.id.t5)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = datalist?.get(position)
                    listener?.onItemClick(datalist?.get(position), item?.coord1.toString(),item?.coord2.toString())
                }
            }
        }
    }
}