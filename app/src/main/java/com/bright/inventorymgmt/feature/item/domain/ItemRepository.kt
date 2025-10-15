package com.bright.inventorymgmt.feature.item.domain

import com.bright.inventorymgmt.core.database.entity.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun insertItem(item: Item): Result<Unit>
    suspend fun updateItem(item: Item)
    suspend fun deleteItem(item: Item)
    fun getAllItems(): Flow<List<Item>>
    fun getItem(id: Int): Flow<Item>
}