package tat.mukhutdinov.mechat.repo

interface MessagesRepo {
//    fun getPaged(): Listing<Message>

    fun sendText(text: String)

    companion object {
        const val COLLECTION_PATH = "message"
        const val ORDER_BY_FIELD = "timestamp"
    }
}