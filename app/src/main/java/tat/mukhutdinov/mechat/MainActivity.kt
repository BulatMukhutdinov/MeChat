package tat.mukhutdinov.mechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.chatBox
import kotlinx.android.synthetic.main.activity_main.messages
import tat.mukhutdinov.mechat.adapter.MessagesAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecycler()

        setupClicks()
    }

    private fun setupClicks() {
        chatBox.onSendClicked = { viewModel.onTextSend(it) }
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        messages.layoutManager = linearLayoutManager

        val adapter = MessagesAdapter {}
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
}
