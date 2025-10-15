package com.bright.inventorymgmt.feature.item.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bright.inventorymgmt.core.database.entity.Item
import com.bright.inventorymgmt.feature.item.domain.ItemRepository
import com.bright.inventorymgmt.feature.item.ui.state.CreateItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateItemViewModel(
    private val itemRepository: ItemRepository
): ViewModel() {
    private val _createItemUiState = MutableStateFlow<CreateItemUiState>(CreateItemUiState.Empty)
    val createItemUiState = _createItemUiState.asStateFlow()

    fun updateItemName(name: String) {
        _createItemUiState.update {
            it.copy(name = name,)
        }
    }

    fun updateItemPrice(price: String) {
        _createItemUiState.update {
            it.copy(price = price,)
        }
    }

    fun updateItemQuantity(quantity: String) {
        _createItemUiState.update {
            it.copy(quantity = quantity,)
        }
    }

    fun setCurrentUiState(item: Item) {
        _createItemUiState.update {
            it.copy(
                id = item.id,
                name = item.name,
                price = item.price.toString(),
                quantity = item.quantity.toString()
            )
        }
    }

    fun insertItem() {
        viewModelScope.launch {
            _createItemUiState.update {
                it.copy(isLoading = true,)
            }
            val result = withContext(Dispatchers.IO) {
                itemRepository.insertItem(
                    Item(
                        name = _createItemUiState.value.name,
                        price = _createItemUiState.value.price.toDouble(),
                        quantity = _createItemUiState.value.quantity.toInt()
                    )
                )
            }
            result.onSuccess {
                _createItemUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }.onFailure {error->
                _createItemUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }
    }

    fun updateItem() {
        viewModelScope.launch {
            _createItemUiState.update {
                it.copy(isLoading = true)
            }
            try {
                withContext(Dispatchers.IO) {
                    itemRepository.updateItem(
                        Item(
                            id = _createItemUiState.value.id!!,
                            name = _createItemUiState.value.name,
                            price = _createItemUiState.value.price.toDouble(),
                            quantity = _createItemUiState.value.quantity.toInt()
                        )
                    )
                }
                _createItemUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _createItemUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun resetCreateUiState() {
        _createItemUiState.update {
            CreateItemUiState.Empty
        }
    }
}