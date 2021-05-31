package com.krithik.notes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="UserId")
    var id : Int,

    @ColumnInfo(name = "UserName")
    var name : String,

    @ColumnInfo(name = "UserEmail")
    var email : String
)
