package com.example.firstgame

import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_barber_login.*
import java.util.*

class customAdapter (val userList: MutableList<User>, val acceptedApp: MutableList<User>) : RecyclerView.Adapter<customAdapter.ViewHolder>() {

    private var removedPosition: Int = 0



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.rows_layout, p0, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user: User = userList[p1]
        p0.textViewName.text = user.name
        p0.textViewTime.text = user.time
        p0.textViewDate.text = user.date
        p0.textViewAddress.text = user.address



    }

    fun removeItem (viewHolder: RecyclerView.ViewHolder){
        removedPosition = viewHolder.adapterPosition

        val removedItem = userList[viewHolder.adapterPosition]
        acceptedApp.add(removedItem)
        var inlit = userList[viewHolder.adapterPosition]

        userList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

        Snackbar.make(viewHolder.itemView, "Appointment accepted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            userList.add(removedPosition, inlit)
            notifyItemInserted(removedPosition)
            acceptedApp.remove(removedItem)

        }.show()


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.textviewName) as TextView
        val textViewTime = itemView.findViewById(R.id.textviewTime) as TextView
        val textViewDate = itemView.findViewById(R.id.textviewDate) as TextView
        val textViewAddress = itemView.findViewById(R.id.textviewAddress) as TextView

    }
}