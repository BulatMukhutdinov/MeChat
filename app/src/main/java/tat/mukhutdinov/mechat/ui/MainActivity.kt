package tat.mukhutdinov.mechat.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.chatBox
import kotlinx.android.synthetic.main.activity_main.messages
import kotlinx.android.synthetic.main.chat_box.image
import org.koin.android.viewmodel.ext.android.viewModel
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.ui.adapter.MessagesAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecycler()

        setupClicks()
    }

    private fun setupClicks() {
        chatBox.onSendClicked = { viewModel.sendText(it) }

        image.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/jpeg"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        messages.layoutManager = linearLayoutManager

        val adapter = MessagesAdapter()
        messages.adapter = adapter

        viewModel.messages.observe(this, Observer {
            adapter.submitList(it)
        })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                linearLayoutManager.scrollToPosition(adapter.itemCount - 1)
            }
        })
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
    }
}