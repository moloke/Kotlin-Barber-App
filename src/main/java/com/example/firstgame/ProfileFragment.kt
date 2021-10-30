package com.example.firstgame


import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_profile.*
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.img_select_dialog.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : androidx.fragment.app.Fragment() {


    companion object {
         const val ARG_NAME = "name"


        fun newInstance(name: String): ProfileFragment {
            val fragment = ProfileFragment()

            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
            }

            fragment.arguments = bundle

            return fragment
        }
    }
    private val IMAGE_PICK_CODE = 1000;
    private val PERMISSION_CODEE = 1001;
    private val IMAGE_CAPURE_CODE  = 1001
    private val PERMISSION_CODE  = 1000
    private var imagePick = ""
    private var profileImg = ""
    private var img = ""
    var count = 0


    var image_uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_profile, container, false)

    }






    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val name = arguments?.getString(ARG_NAME)
        welcomeTxt.text = getString(R.string.welcome_message, name)

        val mypreference = MyPreference(activity!!)
        val readNumber = mypreference.getNumber()
        val readPostcode = mypreference.getPostcode()
        val readDes = mypreference.getDes()


        var barberNum = readNumber
        numbertxt.text = getString(R.string.barber_number, readNumber)

        var barberPost = readPostcode
        postcodetxt.text = getString(R.string.barber_postcode, readPostcode)
        var barberDes = readDes
        descriptiontxt.text = getString(R.string.barber_des, readDes)




        val ref = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child(name!!).child("profile image")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                img = p0.child("profileImageUrl").getValue().toString()
                if(img != "") {
                    Picasso.get().load(img).into(profilePicShow)
                    profilePicShow.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
        val reff = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child(name!!).child("haircut image1")
        reff.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val img = p0.child("profileImageUrl").getValue().toString()
                Picasso.get().load(img).into(haircut1)
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
        val refff = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child(name!!).child("haircut image2")
        refff.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val img = p0.child("profileImageUrl").getValue().toString()
                Picasso.get().load(img).into(haircut2)
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
        val reffff = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child(name!!).child("haircut image3")
        reffff.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val img = p0.child("profileImageUrl").getValue().toString()
                Picasso.get().load(img).into(haircut3)
            }
            override fun onCancelled(p0: DatabaseError) {}
        })


















        profilePicShow.setOnLongClickListener {
            imagePick = "profile"
            //Inflate the dialog with custom view

            val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.img_select_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity!!)
                .setView(mDialogView)
                .setTitle("Profile Picture")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            mDialogView.galleryBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODEE)
                    }
                    else {
                        //permission already granted
                        count = 1
                        pickImageFromGallery()
                    }
                }
                else {
                    //system OS < Marshmallow
                    count = 1
                    pickImageFromGallery()
                }


            }
            mDialogView.cameraBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permission, PERMISSION_CODE)

                    }
                    else {
                        count = 2
                        openCamera()

                    }
                }
                else {
                    count = 2
                    openCamera()

                }


            }
            true


        }


        /*------------------------------------------Hair Btn 1-----------------------------------------------*/

        haircut1.setOnLongClickListener {
            imagePick = "hairone"
            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.img_select_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity!!)
                .setView(mDialogView)
                .setTitle("Haircut 1")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            mDialogView.galleryBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODEE)
                    }
                    else {
                        //permission already granted
                        count = 1
                        pickImageFromGallery()
                    }
                }
                else {
                    //system OS < Marshmallow
                    count = 1
                    pickImageFromGallery()
                }


            }
            mDialogView.cameraBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permission, PERMISSION_CODE)

                    }
                    else {
                        count = 2
                        openCamera()

                    }
                }
                else {
                    count = 2
                    openCamera()

                }


            }
            true


        }






        /*------------------------------------------Hair Btn 2-----------------------------------------------*/

        haircut2.setOnLongClickListener {
            imagePick = "hairtwo"
            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.img_select_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity!!)
                .setView(mDialogView)
                .setTitle("Haircut 2")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            mDialogView.galleryBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODEE)
                    }
                    else {
                        //permission already granted
                        count = 1
                        pickImageFromGallery()
                    }
                }
                else {
                    //system OS < Marshmallow
                    count = 1
                    pickImageFromGallery()
                }


            }
            mDialogView.cameraBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permission, PERMISSION_CODE)

                    }
                    else {
                        count = 2
                        openCamera()

                    }
                }
                else {
                    count = 2
                    openCamera()

                }


            }
            true


        }



        /*------------------------------------------Hair Btn 3-----------------------------------------------*/

        haircut3.setOnLongClickListener {
            imagePick = "hairthree"
            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.img_select_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity!!)
                .setView(mDialogView)
                .setTitle("Haircut 3")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            mDialogView.galleryBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODEE)
                    }
                    else {
                        //permission already granted
                        count = 1
                        pickImageFromGallery()
                    }
                }
                else {
                    //system OS < Marshmallow
                    count = 1
                    pickImageFromGallery()
                }


            }
            mDialogView.cameraBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permission, PERMISSION_CODE)

                    }
                    else {
                        count = 2
                        openCamera()

                    }
                }
                else {
                    count = 2
                    openCamera()

                }


            }
            true


        }





        settings_button.setOnClickListener {
            editProfile()
        }

        save_button.setOnClickListener {
            if (editNumber.text.isEmpty()) {
                editNumber.error = "Contact number"
            } else if (postcodeedit.text.isEmpty()) {
                postcodeedit.error = "Postcode"
            } else if (descriptionedit.text.isEmpty()) {
                descriptionedit.error = "Description"
            }else {

                saveProfile(barberNum, barberPost, barberDes)
                val toast = Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP, 0, 220)
                toast.show()
                editNumber.hideKeyboard()

            }
        }



    }

    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }



    //handle permission result















    private fun openCamera() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = context!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPURE_CODE)



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (imagePick == "profile") {

            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE ) {
                profilePicShow.setImageURI(data?.data)
                val name = arguments?.getString(ARG_NAME)
                val filename = name
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(data!!.data)

                profilePicShow.setBackgroundColor(Color.TRANSPARENT)

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            } else if (resultCode == Activity.RESULT_OK) {
                profilePicShow.setImageURI(image_uri)
                val name = arguments?.getString(ARG_NAME)
                val filename = name
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(image_uri!!)
                profilePicShow.setBackgroundColor(Color.TRANSPARENT)
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            }

        } else if (imagePick == "hairone") {

            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE ) {
                haircut1.setImageURI(data?.data)
                val name = arguments?.getString(ARG_NAME)
                val filename = name + "1"
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(data!!.data)

                haircut1.setBackgroundColor(Color.TRANSPARENT)

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            } else if (resultCode == Activity.RESULT_OK) {
                haircut1.setImageURI(image_uri)
                val name = arguments?.getString(ARG_NAME)
                val filename = name + "1"
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(image_uri!!)
                haircut1.setBackgroundColor(Color.TRANSPARENT)
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            }

        } else if (imagePick == "hairtwo") {

            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE ) {
                haircut2.setImageURI(data?.data)
                val name = arguments?.getString(ARG_NAME)
                val filename = name + "2"
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(data!!.data)

                haircut2.setBackgroundColor(Color.TRANSPARENT)

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            } else if (resultCode == Activity.RESULT_OK) {
                haircut2.setImageURI(image_uri)
                val name = arguments?.getString(ARG_NAME)
                val filename = name + "2"
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(image_uri!!)
                haircut2.setBackgroundColor(Color.TRANSPARENT)
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            }

        } else if (imagePick == "hairthree") {

            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE ) {
                haircut3.setImageURI(data?.data)
                val name = arguments?.getString(ARG_NAME)
                val filename = name + "3"
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(data!!.data)

                haircut3.setBackgroundColor(Color.TRANSPARENT)

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            } else if (resultCode == Activity.RESULT_OK) {
                haircut3.setImageURI(image_uri)
                val name = arguments?.getString(ARG_NAME)
                val filename = name + "3"
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(image_uri!!)
                haircut3.setBackgroundColor(Color.TRANSPARENT)
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString(), filename!!)
                }
            }

        }





    }

    private fun saveUserToDatabase(profileImageUrl: String, filename: String) {
        val name = arguments?.getString(ARG_NAME)
        if (imagePick == "profile") {
                val ref = FirebaseDatabase.getInstance().getReference("Barber Profiles").child(name!!)
                val profileImageId = ref.push().key


                val barber = Barber(profileImageId!!, filename, profileImageUrl)
                ref.child("profile image").setValue(barber).addOnCompleteListener{
                Toast.makeText(activity, "Image saved", Toast.LENGTH_SHORT).show()
                    profileImg = profileImageUrl
            }
            saveToBarbers()


            } else if (imagePick == "hairone") {
            val ref = FirebaseDatabase.getInstance().getReference("Barber Profiles").child(name!!)
            val profileImageId = ref.push().key

            val barber = Barber(profileImageId!!, filename, profileImageUrl)
            ref.child("haircut image1").setValue(barber).addOnCompleteListener {
                Toast.makeText(activity, "Image saved", Toast.LENGTH_SHORT).show()
            }
        } else if (imagePick == "hairtwo") {
            val ref = FirebaseDatabase.getInstance().getReference("Barber Profiles").child(name!!)
            val profileImageId = ref.push().key

            val barber = Barber(profileImageId!!, filename, profileImageUrl)
            ref.child("haircut image2").setValue(barber).addOnCompleteListener {
                Toast.makeText(activity, "Image saved", Toast.LENGTH_SHORT).show()
            }
            } else if (imagePick == "hairthree") {
            val ref = FirebaseDatabase.getInstance().getReference("Barber Profiles").child(name!!)
            val profileImageId = ref.push().key

            val barber = Barber(profileImageId!!, filename, profileImageUrl)
            ref.child("haircut image3").setValue(barber).addOnCompleteListener {
                Toast.makeText(activity, "Image saved", Toast.LENGTH_SHORT).show()
            }
            }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (count == 1){
                        pickImageFromGallery()
                    } else if (count == 2) {
                        openCamera()
                    }


                }
                else {
                    Toast.makeText(activity!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun editProfile(){
        save_button.visibility= View.VISIBLE
        settings_button.visibility= View.INVISIBLE
        numbertxt.visibility=View.INVISIBLE
        editNumber.visibility= View.VISIBLE
        postcodetxt.visibility= View.INVISIBLE
        postcodeedit.visibility= View.VISIBLE
        descriptiontxt.visibility= View.INVISIBLE
        descriptionedit.visibility=View.VISIBLE
    }

    fun saveProfile(barberNum : String, barberPost : String, barberDes : String){
        save_button.visibility= View.INVISIBLE
        settings_button.visibility= View.VISIBLE
        numbertxt.visibility=View.VISIBLE
        editNumber.visibility= View.INVISIBLE
        postcodetxt.visibility= View.VISIBLE
        postcodeedit.visibility= View.INVISIBLE
        descriptiontxt.visibility= View.VISIBLE
        descriptionedit.visibility=View.INVISIBLE

        val name = arguments?.getString(ARG_NAME)
        val ref = FirebaseDatabase.getInstance().getReference("Barber Profiles").child(name!!)
        val profileId = ref.push().key

        var barberNum = editNumber.text.toString()
        var barberPost = postcodeedit.text.toString()
        var barberDes = descriptionedit.text.toString()

        saveToBarbers()


        val mypreference = MyPreference(activity!!)

        val readNumber = mypreference.getNumber()
        mypreference.setNumber(barberNum)
        numbertxt.text = getString(R.string.barber_number, readNumber)

        val readPostcode = mypreference.getPostcode()
        mypreference.setPostcode(barberPost)
        numbertxt.text = getString(R.string.barber_postcode, readPostcode)

        val readDes = mypreference.getDes()
        mypreference.setDes(barberDes)
        numbertxt.text = getString(R.string.barber_des, readDes)




        numbertxt.text = getString(R.string.barber_number, barberNum)
        postcodetxt.text = getString(R.string.barber_postcode, barberPost)
        descriptiontxt.text = getString(R.string.barber_des, barberDes)



        val profile = profileDB(profileId, name.toString(), barberNum, barberPost, barberDes)
        ref.child("profile").setValue(profile).addOnCompleteListener{
            Toast.makeText(activity, "Profile Saved", Toast.LENGTH_SHORT).show()

        }



    }

    private fun saveToBarbers() {
        val name = arguments?.getString(ARG_NAME)
        var barberNum = editNumber.text.toString()
        var barberPost = postcodeedit.text.toString()
        val barbershow = barberShowDB(name!!, barberNum, barberPost, img)

        val ref = FirebaseDatabase.getInstance().getReference("Barbers").child(name!!)
        ref.setValue(barbershow).addOnCompleteListener{
            Toast.makeText(activity, "Profile Saved", Toast.LENGTH_SHORT).show()
        }

    }


}
class Barber(val Id: String, val name: String, val profileImageUrl: String)






