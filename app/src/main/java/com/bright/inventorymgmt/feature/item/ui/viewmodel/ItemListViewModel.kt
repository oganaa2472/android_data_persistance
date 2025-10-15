package com.bright.inventorymgmt.feature.item.ui.viewmodel

import android.R.attr.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bright.inventorymgmt.core.database.entity.Item
import com.bright.inventorymgmt.feature.item.domain.ItemRepository
import com.bright.inventorymgmt.feature.item.ui.state.ItemListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ItemListViewModel(
    private val itemRepository: ItemRepository
): ViewModel() {

    val itemListUiState: StateFlow<ItemListUiState> =
        itemRepository.getAllItems()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
        .map { items -> ItemListUiState(items = items) }
        .onStart { emit(ItemListUiState(isLoading = true))  }
        .catch { exception-> emit(ItemListUiState(errorMessage = exception.message))  }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ItemListUiState()
        )

}