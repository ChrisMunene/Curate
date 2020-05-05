package com.chriskinyua.shoppinglist.touch

interface ListItemTouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}