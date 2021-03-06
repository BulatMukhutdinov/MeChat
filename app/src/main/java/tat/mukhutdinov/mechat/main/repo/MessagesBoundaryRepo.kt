package tat.mukhutdinov.mechat.main.repo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import tat.mukhutdinov.mechat.main.model.FIELD_IMAGE
import tat.mukhutdinov.mechat.main.model.FIELD_LOCATION
import tat.mukhutdinov.mechat.main.model.FIELD_TEXT
import tat.mukhutdinov.mechat.main.model.FIELD_TIMESTAMP
import tat.mukhutdinov.mechat.main.model.GLOBAL_PATH
import tat.mukhutdinov.mechat.main.model.MESSAGE_PATH
import tat.mukhutdinov.mechat.main.model.Message
import timber.log.Timber

class MessagesBoundaryRepo(private val user: FirebaseUser) : MessagesRepo {

    private val dataSourceFactory = MessageDataSourceFactory(user)

    private val config = PagedList.Config.Builder()
        .setPageSize(50)
        .setInitialLoadSizeHint(50)
        .setPrefetchDistance(50)
        .setEnablePlaceholders(false)
        .build()

    private var isInitialDataLoad = true

    init {
        FirebaseFirestore.getInstance().collection(GLOBAL_PATH).document(user.uid).collection(MESSAGE_PATH)
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

    override fun sendLocation(latitude: Double, longitude: Double, scale: Int) {
        val message = HashMap<String, Any>()
        // in real apps *DON"T* store keys open
        message[FIELD_LOCATION] = "https://maps.googleapis.com/maps/api/staticmap?size=240x160&zoom=15&scale=$scale)}&markers=size:small|color:0x57BEED|$latitude,$longitude&key=AIzaSyBcjGN3lHSUEynSRqwi_UF_dGDbIUhzMrM"

        send(message)
    }

    override fun sendText(text: String) {
        val message = HashMap<String, Any>()
        message[FIELD_TEXT] = text

        send(message)
    }

    private fun send(message: HashMap<String, Any>) {
        message[FIELD_TIMESTAMP] = System.currentTimeMillis()

        FirebaseFirestore.getInstance().collection(GLOBAL_PATH).document(user.uid).collection(MESSAGE_PATH)
            .add(message)
            .addOnSuccessListener {

                Timber.d("New message: $message")
            }
            .addOnFailureListener { Timber.e(it) }
    }

    override fun sendImage(uri: Uri) {
        val message = HashMap<String, Any>()
        message[FIELD_TIMESTAMP] = System.currentTimeMillis()
        message[FIELD_IMAGE] = ""

        FirebaseFirestore.getInstance().collection(GLOBAL_PATH).document(user.uid).collection(MESSAGE_PATH)
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

        val path = storage.reference.child("images/${uri.lastPathSegment}")

        val uploadTask = path.putFile(uri, metadata)

        uploadTask
            .addOnCanceledListener {
                Timber.e("")
            }
            .addOnProgressListener { taskSnapshot ->
                // todo return to caller to show uploading progress
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            }
            .addOnSuccessListener {
                path.downloadUrl.addOnSuccessListener {
                    val updated = HashMap<String, Any>()
                    message[FIELD_IMAGE] = it.toString()

                    firestore.collection(GLOBAL_PATH).document(user.uid).collection(MESSAGE_PATH).document(documentRef.id)
                        .set(message)
                        .addOnSuccessListener { Timber.d("New message: $updated") }
                        .addOnFailureListener { Timber.e(it) }
                }
            }
            .addOnFailureListener {
                Timber.e(it)
            }
    }
}