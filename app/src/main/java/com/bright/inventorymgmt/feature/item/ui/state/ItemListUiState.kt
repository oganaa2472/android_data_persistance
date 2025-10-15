package com.bright.inventorymgmt.feature.item.ui.state

import com.bright.inventorymgmt.core.database.entity.Item

data class ItemListUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
