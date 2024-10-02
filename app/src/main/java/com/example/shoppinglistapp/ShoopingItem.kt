package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class shoppingItem(
    var name:String, var qty:Int, var isEditing:Boolean=false, var id:Int
){

}


@Composable
fun ShoppingList() {
    var sItem by remember { mutableStateOf(listOf<shoppingItem>()) }
    var showDialog by remember {mutableStateOf(false)}
    var itemName by remember{ mutableStateOf("")}
    var quantity by remember{ mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize(), //make column occupy full screen size
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {showDialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .padding(5.dp)
        ){
            items(sItem) {
                item ->
                if(item.isEditing){
                    ShoppingListEditor(item = item, onEditComplete = {
                        editedName, editedQty ->
                        sItem = sItem.map {it.copy(isEditing = false) }
                        val editedName = sItem.find { it.id == item.id }
                        editedName?.let {
                            it.name = editedName.toString()
                            it.qty = editedQty
                        }
                    })
                }
                else{
                    ShoppingListItem(item = item,
                        onEditClick = {
                        //finding which item is getting edited and making is Editing true
                        sItem = sItem.map { it.copy(isEditing = it.id == item.id) }
                    },
                           onDeleteClick = {
                        sItem = sItem-item
                    })
                }
            }
        }
        if(showDialog){
            AlertDialog(onDismissRequest = {showDialog = false}, 
                confirmButton = {},
                title = {Text(text ="Add Shopping List")},
                text= {
                    Column() {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            label = {Text(text = "Add Item")}
                        )
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            label = {Text(text = "Quantity")}
                        )
                        Row( modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Button(onClick = {
                                if(itemName.isNotBlank()){
                                    val newItem = shoppingItem(
                                        name = itemName,
                                        qty = quantity.toInt(),
                                        id = sItem.size+1
                                    )
                                    sItem = sItem+newItem
                                    showDialog = false
                                    itemName = ""
                                    quantity = ""
                                }
                            }, ) {
                                Text(text = "Add")
                            }
                            Button(onClick = {
                                showDialog = false
                            } ) {
                                Text(text = "Cancel")
                            }
                        }
                    }
                })
        }
    }
}


@Composable
fun ShoppingListItem(
    item: shoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 2.dp, Color.Blue),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.qty}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = { onEditClick }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = {onDeleteClick}) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }

    }
}


@Composable
fun ShoppingListEditor(item:shoppingItem, onEditComplete:(String,Int) -> Unit){
    var editedName by remember{mutableStateOf(item.name)}
    var editedQty by remember{ mutableStateOf(item.qty.toString())}
    var isEditing by remember{mutableStateOf(item.isEditing)}

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column(){
            BasicTextField(value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(value = editedQty,
                onValueChange = {editedQty =it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQty.toIntOrNull() ?: 1)
            }
        ){
            Text(text = "Save")
        }
    }
}