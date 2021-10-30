package com.example.firstgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.barber_row.view.*
import kotlinx.android.synthetic.main.client_search.*

class clientSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_search)
        supportActionBar?.title =  "Client"

        val clientPostcode= intent.getStringExtra("Postcode")



        fetchBarbers()
    }
    companion object {
        val BARBER_KEY = "BARBER_KEY"
        val CLIENT_POSTCODE = "CLIENT_POSTCODE"
    }

    private fun fetchBarbers() {
        val ref = FirebaseDatabase.getInstance().getReference("/Barbers")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val barberShow = it.getValue(barberShowDB::class.java)
                    adapter.add(BarberItem(barberShow!!))
                }

                adapter.setOnItemClickListener{ item, view ->
                    val clientPostcode= intent.getStringExtra("Postcode")
                    val barbershow = item as BarberItem
                    val intent = Intent(view.context, barberInfo::class.java)
                    intent.putExtra(BARBER_KEY, barbershow.barberShow.name)
                    intent.putExtra(CLIENT_POSTCODE, clientPostcode)
                    startActivity(intent)

                }


                searchRecyclerView.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class BarberItem(val barberShow: barberShowDB): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.barberNameTxt.text = barberShow.name
        viewHolder.itemView.barberPostcodeTxt.text = barberShow.postcode
        viewHolder.itemView.barberNumberTxt.text = barberShow.number
        Picasso.get().load(barberShow.image).into(viewHolder.itemView.barberImage)
    }
    override fun getLayout(): Int {
        return R.layout.barber_row
    }
}