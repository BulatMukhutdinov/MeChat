package tat.mukhutdinov.mechat.main.model

data class Message(val id: String, val timestamp: Long, val text: String?, val image: String?, val location: String?)

const val GLOBAL_PATH = "users"
const val MESSAGE_PATH = "message"
const val FIELD_ID = "id"
const val FIELD_TIMESTAMP = "timestamp"
const val FIELD_TEXT = "text"
const val FIELD_IMAGE = "image"
const val FIELD_LOCATION = "location"