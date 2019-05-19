package tat.mukhutdinov.mechat.model

data class TextMessage(override val id: String, override val timestamp: Long, val text: String) : Message(id, timestamp)