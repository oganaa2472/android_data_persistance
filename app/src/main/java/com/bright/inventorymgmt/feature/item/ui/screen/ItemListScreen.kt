package com.bright.inventorymgmt.feature.item.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bright.inventorymgmt.core.database.InventoryDatabase
import com.bright.inventorymgmt.core.database.entity.Item
import com.bright.inventorymgmt.feature.item.data.ItemRepositoryImpl
import com.bright.inventorymgmt.feature.item.ui.state.CreateItemUiState
import com.bright.inventorymgmt.feature.item.ui.viewmodel.CreateItemViewModel
import com.bright.inventorymgmt.feature.item.ui.viewmodel.ItemListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(modifier: Modifier = Modifier) {
    //we need app context
    val context = LocalContext.current
    val applicationContext = context.applicationContext
    //Get the db instance
    val database: InventoryDatabase = remember(applicationContext) {
        InventoryDatabase.getDatabase(applicationContext)
    }
    //get the dao instance
    val itemDao = remember { database.itemDao() }
    val itemListViewModel: ItemListViewModel = viewModel {
        ItemListViewModel(
            ItemRepositoryImpl(itemDao)
        )
    }
    //get the state from itemlistviewModel
    val itemListUiState by itemListViewModel.itemListUiState.collectAsStateWithLifecycle()
    val createItemViewModel: CreateItemViewModel = viewModel {
        CreateItemViewModel(
            ItemRepositoryImpl(itemDao)
        )
    }
    val createItemUiState by createItemViewModel.createItemUiState.collectAsStateWithLifecycle()

    val addDialog = rememberSaveable { mutableStateOf(false) }
    val editDialog = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //Open a dialog to add the item
                    addDialog.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            items(itemListUiState.items) {
                ListItem(
                    headlineContent = {
                        Text(text = it.name)
                    },
                    supportingContent = {
                        Text(
                            text = "$${it.price} x ${it.quantity} = $${it.price * it.quantity}"
                        )
                    },
                    trailingContent = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit",
                                modifier = Modifier
                                    .clickable {
                                        //Write the code to edit it
                                        //take the current item
//                                        val currentItem: Item = it
                                        //populate createItemUiStae with current item(include id)
                                        createItemViewModel.setCurrentUiState(it)
                                        editDialog.value = true
                                    }
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete",
                                modifier = Modifier
                                    .clickable {
                                        //Write the code to delete it

                                    }
                            )
                        }
                    }
                )
            }
        }
    }
    if (addDialog.value) {
        AddItemDialog(
            onDismissRequest = {addDialog.value = false},
            createItemUiState = createItemUiState,
            createItemViewModel = createItemViewModel
        )
    }
    if (editDialog.value) {
        EditItemDialog(
            onDismissRequest = {addDialog.value = false},
            createItemUiState = createItemUiState,
            createItemViewModel = createItemViewModel
        )
    }
    LaunchedEffect(
        createItemUiState.isSuccess
    ) {
        if (createItemUiState.isSuccess == true) {
            createItemViewModel.resetCreateUiState()
        }
    }
}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditItemDialog(
    onDismissRequest: () -> Unit,
    createItemUiState: CreateItemUiState,
    createItemViewModel: CreateItemViewModel
) {
    BasicAlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismissRequest()//onDismissRequest.invoke()
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = createItemUiState.name,
                    onValueChange = {
                        createItemViewModel.updateItemName(it)
                    },
                    label = {
                        Text(text = "Name")
                    }
                )
                OutlinedTextField(
                    value = createItemUiState.price,
                    onValueChange = {
                        createItemViewModel.updateItemPrice(it)
                    },
                    label = {
                        Text(text = "Price")
                    }
                )
                OutlinedTextField(
                    value = createItemUiState.quantity,
                    onValueChange = {
                        createItemViewModel.updateItemQuantity(it)
                    },
                    label = {
                        Text(text = "Quantity")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        createItemViewModel.insertItem()
                        onDismissRequest()
                    },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onDismissRequest: () -> Unit,
    createItemUiState: CreateItemUiState,
    createItemViewModel: CreateItemViewModel
) {
    BasicAlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismissRequest()
        }
    ) {
        Surface (
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = createItemUiState.name,
                    onValueChange = {
                        createItemViewModel.updateItemName(it)
                    },
                    label = {
                        Text(text = "Name")
                    }
                )
                OutlinedTextField(
                    value = createItemUiState.price,
                    onValueChange = {
                        createItemViewModel.updateItemPrice(it)
                    },
                    label = {
                        Text(text = "Price")
                    }
                )
                OutlinedTextField(
                    value = createItemUiState.quantity,
                    onValueChange = {
                        createItemViewModel.updateItemQuantity(it)
                    },
                    label = {
                        Text(text = "Quantity")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        createItemViewModel.insertItem()
                        onDismissRequest()
                    },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}