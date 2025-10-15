package com.bright.inventorymgmt.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bright.inventorymgmt.core.database.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: Item)
    @Update
    suspend fun updateItem(item: Item)
    @Delete
    suspend fun deleteItem(item: Item)
    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>
    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

}