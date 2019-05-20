package tat.mukhutdinov.mechat.repo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import tat.mukhutdinov.mechat.model.COLLECTION_PATH
import tat.mukhutdinov.mechat.model.FIELD_IMAGE
import tat.mukhutdinov.mechat.model.FIELD_TEXT
import tat.mukhutdinov.mechat.model.FIELD_TIMESTAMP
import tat.mukhutdinov.mechat.model.Message
import timber.log.Timber
import java.util.UUID

class MessagesBoundaryRepo : MessagesRepo {

    private val dataSourceFactory = MessageDataSourceFactory()

    private val config = PagedList.Config.Builder()
        .setPageSize(50)
        .setInitialLoadSizeHint(50)
        .setPrefetchDistance(50)
        .setEnablePlaceholders(false)
        .build()

    private var isInitialDataLoad = true

    init {
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Timber.e(exception)
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    if (isInitialDataLoad) {
                        isInitialDataLoad = false
                    } else {
                        dataSourceFactory.liveDataSource.value?.invalidate()
                    }
                }
            }
    }

    override fun getMessages(): LiveData<PagedList<Message>> =
        LivePagedListBuilder<Message, Message>(dataSourceFactory, config).build()

    override fun sendText(text: String) {
        val message = HashMap<String, Any>()
        message[FIELD_TEXT] = text

        send(message)
    }

    override fun sendImage(uri: Uri) {
        val message = HashMap<String, Any>()
        message[FIELD_TIMESTAMP] = System.currentTimeMillis()
        message[FIELD_IMAGE] = ""

        FirebaseFirestore.getInstance().collection(COLLECTION_PATH)
            .add(message)
            .addOnSuccessListener { documentRef -> uploadImage(uri, documentRef, message) }
            .addOnFailureListener { Timber.e(it) }
    }

    private fun uploadImage(uri: Uri, documentRef: DocumentReference, message: HashMap<String, Any>) {
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

        val path = storage.reference.child("images/${UUID.randomUUID()}")

        val uploadTask = path.putFile(uri, metadata)

        uploadTask
            .addOnProgressListener { taskSnapshot ->
                // todo return to caller to show uploading progress
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            }
            .addOnSuccessListener {
                path.downloadUrl.addOnSuccessListener {
                    val updated = HashMap<String, Any>()
                    message[FIELD_IMAGE] = it.toString()

                    firestore.collection(COLLECTION_PATH).document(documentRef.id)
                        .set(message)
                        .addOnSuccessListener { Timber.d("New message: $updated") }
                        .addOnFailureListener { Timber.e(it) }
                }
            }
            .addOnFailureListener { Timber.e(it) }
    }

    private fun send(message: HashMap<String, Any>) {
        message[FIELD_TIMESTAMP] = System.currentTimeMillis()

        FirebaseFirestore.getInstance().collection(COLLECTION_PATH)
            .add(message)
            .addOnSuccessListener {

                Timber.d("New message: $message")
            }
            .addOnFailureListener { Timber.e(it) }
    }
}