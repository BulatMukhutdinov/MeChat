package tat.mukhutdinov.mechat.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tat.mukhutdinov.mechat.db.MessageEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    var id: String,
    @ColumnInfo(name = COLUMN_TIMESTAMP)
    var timestamp: Long,
    @ColumnInfo(name = COLUMN_TEXT)
    var text: String? = null
) {
    companion object {
        const val TABLE_NAME = "post"
        const val COLUMN_ID = "id"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_TEXT = "text"
    }
}