//package tat.mukhutdinov.mechat.repo
//
//import androidx.annotation.MainThread
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.PagedList
//import androidx.paging.PagingRequestHelper
//import com.google.firebase.firestore.FirebaseFirestore
//import io.reactivex.disposables.CompositeDisposable
//import tat.mukhutdinov.mechat.db.MessageEntity
//import tat.mukhutdinov.mechat.model.NetworkState
//import tat.mukhutdinov.mechat.model.TextMessage
//import java.util.concurrent.Executors
//
//class MessagesBoundaryCallback(
//    private val compositeDisposable: CompositeDisposable,
//    private val handleResponse: (List<MessageEntity>) -> Unit,
//    private val pageSize: Long
//) : PagedList.BoundaryCallback<MessageEntity>() {
//
//    val helper = PagingRequestHelper(Executors.newSingleThreadExecutor())
//    val networkState = helper.createStatusLiveData()
//
//    private var lastLoadedItem: MessageEntity? = null
//
//    /**
//     * Database returned 0 items. We should query the backend for more items.
//     */
//    @MainThread
//    override fun onZeroItemsLoaded() {
//        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { pagingHelperCallback ->
//            FirebaseFirestore.getInstance().collection("message") // todo move collection name to const
//                .orderBy("timestamp")
//                .limit(pageSize)
//                .get()
//                .addOnSuccessListener {
//                    val messages = it.toObjects(TextMessage::class.java)
//
//                    lastLoadedItem = messages.lastOrNull()
//
//                    handleResponse(messages)
//                    pagingHelperCallback.recordSuccess()
//                }
//                .addOnFailureListener { pagingHelperCallback.recordFailure(it) }
//        }
//    }
//
//    /**
//     * User reached to the end of the list.
//     */
//    @MainThread
//    override fun onItemAtEndLoaded(itemAtEnd: MessageEntity) {
//        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { pagingHelperCallback ->
//            FirebaseFirestore.getInstance().collection("message") // todo move collection name to const
//                .orderBy("timestamp")
//                .startAfter(lastLoadedItem)
//                .limit(pageSize)
//                .get()
//                .addOnSuccessListener {
//                    val messages = it.toObjects(TextMessage::class.java)
//
//                    lastLoadedItem = messages.lastOrNull()
//
//                    handleResponse(messages)
//                    pagingHelperCallback.recordSuccess()
//                }
//                .addOnFailureListener { pagingHelperCallback.recordFailure(it) }
//        }
//    }
//}
//
//fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkState> {
//    val liveData = MutableLiveData<NetworkState>()
//    addListener { report ->
//        when {
//            report.hasRunning() -> liveData.postValue(NetworkState.Loading)
//            report.hasError() -> liveData.postValue(NetworkState.Error(getErrorMessage(report)))
//            else -> liveData.postValue(NetworkState.Loaded)
//        }
//    }
//    return liveData
//}
//
//private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String? {
//    return PagingRequestHelper.RequestType.values().mapNotNull {
//        report.getErrorFor(it)?.message
//    }.firstOrNull()
//}