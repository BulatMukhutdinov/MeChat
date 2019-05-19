package tat.mukhutdinov.mechat.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MessageEntity::class
    ],
    version = 1,
    exportSchema = true)
abstract class MeChatDb : RoomDatabase() {

    abstract fun messageDao(): MessageDao
}