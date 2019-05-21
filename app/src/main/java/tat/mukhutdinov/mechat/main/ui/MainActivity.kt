package tat.mukhutdinov.mechat.main.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View.GONE
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.chatBox
import kotlinx.android.synthetic.main.activity_main.loading
import kotlinx.android.synthetic.main.activity_main.messages
import kotlinx.android.synthetic.main.chat_box.image
import kotlinx.android.synthetic.main.chat_box.location
import org.koin.android.viewmodel.ext.android.viewModel
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.main.ui.adapter.MessagesAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupRecycler()

        setupClicks()
    }

    private fun getScale(): Int {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        return if (metrics.scaledDensity >= 2.0) {
            2
        } else {
            1
        }
    }

    private fun setupClicks() {
        chatBox.onSendClicked = { viewModel.sendText(it) }

        image.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/jpeg"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        location.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                sendLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Toast.makeText(this, R.string.failed_to_get_location, LENGTH_LONG).show()
                } else {
                    viewModel.sendLocation(location.latitude, location.longitude, getScale())
                }
            }
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        messages.layoutManager = linearLayoutManager

        val adapter = MessagesAdapter()
        messages.adapter = adapter

        viewModel.messages.observe(this, Observer {
            loading.visibility = GONE
            adapter.submitList(it)
        })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                linearLayoutManager.scrollToPosition(adapter.itemCount - 1)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.size == 1
                && permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendLocation()
            } else {
                Toast.makeText(this, R.string.location_required, LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri: Uri?
            if (resultData != null) {
                uri = resultData.data
                viewModel.sendImage(uri)
            }
        }
    }

    companion object {
        private const val READ_REQUEST_CODE = 47
        private const val LOCATION_REQUEST_CODE = 22
    }
}