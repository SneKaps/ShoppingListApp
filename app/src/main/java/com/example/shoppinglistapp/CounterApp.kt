package com.example.shoppinglistapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel(){
    private var _count: MutableState<Int> = mutableStateOf(0)

    var count: ImmutableState<Int> = immutableStateOf(_count)
}