package com.example.firstgame


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_availability.*
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.FirebaseDatabase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 *
 */
class AvailabilityFragment : androidx.fragment.app.Fragment() {

    companion object {
        const val ARG_NAME = "name"


        fun newInstance(name: String): AvailabilityFragment {
            val fragment = AvailabilityFragment()

            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
            }

            fragment.arguments = bundle

            return fragment
        }
    }





    val avDates: MutableList<String> = ArrayList()
    val unavDates: MutableList<String> = ArrayList()
    val dateChosen = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_availability, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val name = arguments?.getString(ARG_NAME)


        availabilitytxt.visibility= View.INVISIBLE
        avSwitch.visibility= View.INVISIBLE

        availabilityBtn.setOnClickListener {


            saveAvailability(name!!, avDates.toString())
        }



        avCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->

            availabilitytxt.visibility= View.VISIBLE
            avSwitch.visibility= View.VISIBLE


            val dateChosen = "" + dayOfMonth + "/" + (month + 1) + "/" + year
            datetxt.setText(dateChosen)



            val toFind = datetxt.text
            var found = false

            for (n in avDates) {
                if (n == toFind) {
                    found = true
                    break
                }
            }

            if (found) {
                avSwitch.isChecked = true
            } else {
                avSwitch.isChecked = false
            }




        }


        avSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                availabilitytxt.text = "Available"
                availabilitytxt.setTextColor("#26a85a".toColor())
                if (avDates.contains(datetxt.text.toString())) {

                } else {
                    avDates.add(datetxt.text.toString())
                }



            } else {
                // The switch is disabled
                availabilitytxt.text = "Unavailable"
                availabilitytxt.setTextColor("#b20000".toColor())
                avDates.remove(datetxt.text.toString())

            }
        }

    }

    private fun saveAvailability(name: String, avDates: String) {

        val ref = FirebaseDatabase.getInstance().getReference("Barber Profiles").child(name!!)
        val availabilityId = ref.push().key
        val availability = availabilityDB(availabilityId, name, avDates)
        ref.child("availability").setValue(availability).addOnCompleteListener{
            Toast.makeText(activity, "Availability Saved", Toast.LENGTH_SHORT).show()
        }


    }


    fun String.toColor(): Int = Color.parseColor(this)

}
