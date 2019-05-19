package tat.mukhutdinov.mechat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.FirebaseFirestore
import tat.mukhutdinov.mechat.model.TextMessage
import tat.mukhutdinov.mechat.repo.MessagesBoundaryRepo
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(10)
        .setPrefetchDistance(10)
        .setEnablePlaceholders(false)
        .build()

    private val dataSourceFactory = MessageDataSourceFactory()

    val messages: LiveData<PagedList<TextMessage>> =
        LivePagedListBuilder<TextMessage, TextMessage>(dataSourceFactory, config).build()

    val repo = MessagesBoundaryRepo()

    init {
        FirebaseFirestore.getInstance().collection("message")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    dataSourceFactory.liveDataSource.value?.invalidate()
                }
            }
    }

    fun onTextSend(text: String) {
        repo.sendText(text)
    }
}