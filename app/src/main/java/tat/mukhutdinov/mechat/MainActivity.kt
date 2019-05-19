package tat.mukhutdinov.mechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import kotlinx.android.synthetic.main.activity_main.*
import tat.mukhutdinov.mechat.adapter.MessagesAdapter
import tat.mukhutdinov.mechat.model.Message

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MessagesAdapter {}
        messages.adapter = adapter

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.messages.observe(this, Observer {
                adapter.submitList(it as PagedList<Message>)
        })
    }
}
