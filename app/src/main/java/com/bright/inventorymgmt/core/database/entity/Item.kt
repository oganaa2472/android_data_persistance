package com.bright.inventorymgmt.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,//do this correction
    val name: String,
    val price: Double,
    val quantity: Int
)
