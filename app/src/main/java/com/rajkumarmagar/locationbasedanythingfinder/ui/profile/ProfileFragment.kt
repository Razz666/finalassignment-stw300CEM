package com.rajkumarmagar.locationbasedanythingfinder.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.adapter.MyPostAdapter
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.repository.PostRepository
import com.rajkumarmagar.locationbasedanythingfinder.repository.UserRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var imgProfile: CircleImageView
    private lateinit var displayUsername: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        imgProfile = root.findViewById(R.id.imgProfile)
        displayUsername = root.findViewById(R.id.displayUsername)

        val profileRecyclerView: RecyclerView = root.findViewById(R.id.profileRecyclerView)
        val update: Button = root.findViewById(R.id.update)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.getMe()

                val repository1 = PostRepository()
                val response1 = repository1.getMyPost(requireContext())

                withContext(Main) {
                    val adapter = MyPostAdapter(response1, requireContext())
                    profileRecyclerView.layoutManager = LinearLayoutManager(context)
                    profileRecyclerView.adapter = adapter
                }

                if (response.success == true) {
                    withContext(Main) {
                        displayUsername.text = response.me!!.username
                        val imagePath = MyServiceBuilder.BASE_URL + response.me.profileImage
                        Glide.with(requireContext())
                                .load(imagePath)
                                .into(imgProfile)

                    }
                }
            } catch (ex: IOException) {
                withContext(Main) {
                    Toast.makeText(
                            requireContext(),
                            ex.toString(),
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        imgProfile.setOnClickListener {
            loadPopUpMenu()
        }

        update.setOnClickListener {
            uploadImage()
        }
        return root
    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(requireContext(), imgProfile)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    openCamera()
                R.id.menuGallery ->
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    private fun uploadImage() {
        if (imageUrl != null) {
            val profileImage = File(imageUrl!!)

            val reqFile =
                    RequestBody.create(MediaType.parse("image/jpeg"), profileImage)

            val body =
                    MultipartBody.Part.createFormData("profileImage", profileImage.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.updateUser(body)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Main) {
                        Toast.makeText(
                                requireContext(),
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = requireActivity().contentResolver
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }

    private fun bitmapToFile(
            bitmap: Bitmap,
            fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}