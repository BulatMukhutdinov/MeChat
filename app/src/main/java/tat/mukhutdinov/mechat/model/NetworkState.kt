package tat.mukhutdinov.mechat.model

sealed class NetworkState(val status: Status) {

    object Loaded : NetworkState(Status.SUCCESS)

    object Loading : NetworkState(Status.RUNNING)

    class Error(val message: String?) : NetworkState(Status.FAILED)
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}