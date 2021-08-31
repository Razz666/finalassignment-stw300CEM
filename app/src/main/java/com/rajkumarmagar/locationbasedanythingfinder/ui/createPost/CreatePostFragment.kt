package com.rajkumarmagar.locationbasedanythingfinder.ui.createPost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.channel.NotificationChannel
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post
import com.rajkumarmagar.locationbasedanythingfinder.repository.PostRepository
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
import java.text.SimpleDateFormat
import java.util.*

class CreatePostFragment : Fragment() {
    private lateinit var createPostViewModel: CreatePostViewModel
    private lateinit var postImage: ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        createPostViewModel =
                ViewModelProvider(this).get(CreatePostViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_post, container, false)
        val etCaption: EditText = root.findViewById(R.id.etCaption)
        val etContact: EditText = root.findViewById(R.id.etContact)
        postImage= root.findViewById(R.id.postImage)
        val btnCreate: Button = root.findViewById(R.id.btnCreate)

        postImage.setOnClickListener {
            loadPopUpMenu()
        }

        btnCreate.setOnClickListener {
            val caption = etCaption.text.toString()
            val contact = etContact.text.toString()

            val post = Post(postCaption = caption, contact = contact)
            createPost(post)
        }
        return root
    }

    private fun createPost(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = PostRepository()
                val response = repository.createPost(post)

                if (response.success == true) {
                    withContext(Main) {
                        uploadImage(response._id!!)
                    }
                } else {
                    withContext(Main) {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    private fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(requireContext())

        val notificationChannel = NotificationChannel(requireContext())
        notificationChannel.createNotificationChannel()

        val notification = NotificationCompat.Builder(requireContext(), notificationChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentTitle("You have successfully added a post")
                .setColor(Color.BLACK)
                .build()

        notificationManager.notify(1, notification)
    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(requireContext(), postImage)
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

    private fun uploadImage(_id: String) {
        if (imageUrl != null) {
            val postPhoto = File(imageUrl!!)

            val reqFile =
                    RequestBody.create(MediaType.parse("image/jpeg"), postPhoto)

            val body =
                    MultipartBody.Part.createFormData("postPhoto", postPhoto.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val postRepository = PostRepository()
                    val response = postRepository.uploadPostPhoto(body, _id)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT)
                                    .show()
                            showNotification()
                            requireActivity().fragmentManager.popBackStack();
                        }
                    }
                } catch (ex: java.lang.Exception) {
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
                postImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                postImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
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