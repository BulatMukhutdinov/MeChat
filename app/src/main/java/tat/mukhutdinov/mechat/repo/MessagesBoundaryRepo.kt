package tat.mukhutdinov.mechat.repo

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tat.mukhutdinov.mechat.model.Listing
import tat.mukhutdinov.mechat.model.Message
import tat.mukhutdinov.mechat.model.NetworkState
import java.util.concurrent.TimeUnit

class MessagesBoundaryRepo(private val pageSize: Long) : MessagesRepo {

    private val compositeDisposable = CompositeDisposable()

//    @MainThread
//    override fun getPaged(): Listing<Message> {
//        val boundaryCallback = MessagesBoundaryCallback(
//            compositeDisposable = compositeDisposable,
//            handleResponse = { posts -> save(posts) },
//            pageSize = pageSize
//        )
//
//        val refreshTrigger = MutableLiveData<Unit>()
//        val refreshState = Transformations.switchMap(refreshTrigger) {
//            refresh()
//        }
//
//        val livePagedList = postDao.getAll()
//            .toLiveData(
//                pageSize = pageSize,
//                boundaryCallback = boundaryCallback
//            )
//
//        return Listing(
//            pagedList = livePagedList,
//            networkState = boundaryCallback.networkState,
//            retry = { boundaryCallback.helper.retryAllFailed() },
//            refresh = { refreshTrigger.value = null },
//            refreshState = refreshState
//        )
//    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     * <p>
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
//    @MainThread
//    private fun refresh(): LiveData<NetworkState> {
//        val networkState = MutableLiveData<NetworkState>()
//        networkState.value = NetworkState.Loading
//
//        compositeDisposable.add(
//            Single
//                .fromCallable {
//                    val options = HashMap<String, Any>()
//                    options["limit"] = pageSize
//                    options["type"] = "text"
//                    options["filter"] = "text"
//
//                    jumblr.userDashboard(options)
//                }
//                .subscribeOn(Schedulers.io())
//                .timeout(TIMEOUT_SEC, TimeUnit.SECONDS)
//                .subscribe(
//                    { posts ->
//                        db.runInTransaction {
//                            postDao.clear()
//                            save(posts)
//                        }
//                        networkState.postValue(NetworkState.Loaded)
//                    },
//                    { networkState.postValue(NetworkState.Error(it.message)) })
//        )
//
//        return networkState
//    }
//
//    private fun save(posts: List<PostDto>) {
//        posts.forEach { postDto ->
//            val postEntity = PostConverter.fromNetwork(postDto)
//            postEntity.avatar = jumblr.blogAvatar(postDto.blogName)
//            postDao.insert(postEntity)
//        }
//    }
}