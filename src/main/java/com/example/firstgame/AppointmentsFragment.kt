package com.example.firstgame


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_barber_appointments.*
import kotlinx.android.synthetic.main.fragment_appointments.*
import java.util.*
import kotlin.collections.ArrayList
import android.provider.CalendarContract
import com.google.android.material.snackbar.Snackbar
import android.content.ClipData
import android.content.Context.CLIPBOARD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.R.attr.label
import android.app.AlertDialog
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import android.text.ClipboardManager
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#008000"))
private lateinit var acceptIcon: Drawable

/**
 * A simple [Fragment] subclass.
 *
 */
class AppointmentsFragment : androidx.fragment.app.Fragment() {



        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment


            return inflater.inflate(R.layout.activity_barber_appointments, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {

            super.onActivityCreated(savedInstanceState)
            acceptIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_baseline_done_24px)!!

            recyclerView.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)



            val users: MutableList<User> = ArrayList()


            users.add(User("Mayowa Oloke", "02/12/1998", "16;30", "45 Burleigh Street"))
            users.add(User("Mayowa Oloke", "02/12/1998", "16:30", "45 Burleigh Street"))

            val acceptedApp: MutableList<User> = ArrayList()

            val adapter = customAdapter(users, acceptedApp)




            val ref = FirebaseDatabase.getInstance().getReference("/Bookings")

            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {


                        val name = p0.child("name").getValue().toString()
                    val date = p0.child("date").getValue().toString()
                    val time = p0.child("time").getValue().toString()
                    val address = p0.child("address").getValue().toString()

                        users.add(User(name, date, time, address))




                    recyclerView.adapter = adapter
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })














            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                    (adapter).removeItem(viewHolder)






                    /*----------------------------------------------------------------------*/

                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Google Calendar")
                    builder.setMessage("Appointment copied to clipboard, save in Google Calendar?")
                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        var text = acceptedApp.toString().substringAfter("(").substringBefore(')')

                        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

                        val clip = android.content.ClipData.newPlainText("Copied Text", text)
                        clipboard.primaryClip = clip

                        val calendarUri = CalendarContract.CONTENT_URI
                            .buildUpon()
                            .appendPath("time")
                            .build()
                        startActivity(Intent(Intent.ACTION_VIEW, calendarUri))

                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->

                    }


                    builder.show()


                    val ref = FirebaseDatabase.getInstance().getReference("Accepted").child("client1")
                    ref.setValue("yes").addOnCompleteListener {
                        Toast.makeText(context, "Appointment accepted", Toast.LENGTH_SHORT).show()
                    }


                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView

                    val iconMargin = (itemView.height - acceptIcon.intrinsicHeight)/2

                    if (dX > 0) {
                        swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                        acceptIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin, itemView.left + iconMargin + acceptIcon.intrinsicWidth,
                            itemView.bottom - iconMargin)

                    } else {
                        swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                        acceptIcon.setBounds(itemView.right - iconMargin - acceptIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin,
                            itemView.bottom - iconMargin)

                    }
                    swipeBackground.draw(c)

                    c.save()

                    if (dX > 0){
                        c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)

                    } else {
                        c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    }


                    acceptIcon.draw(c)

                    c.restore()


                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }




            }
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)







        }

    private fun fetchAppointments(adapter : customAdapter) {



    }


}

class AppointmentsShowDB(val name: String, val date: String, val time: String, val address: String) {
    constructor() : this("", "", "", "")
}

