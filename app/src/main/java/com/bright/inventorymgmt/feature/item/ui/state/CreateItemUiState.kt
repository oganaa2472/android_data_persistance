package com.bright.inventorymgmt.feature.item.ui.state

data class CreateItemUiState(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean? = null
) {
    companion object{
        val Empty = CreateItemUiState()
    }
}
