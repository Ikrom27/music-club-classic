package com.ikrom.base_adapter.model

abstract class AdapterItem {
    open val onClick: () -> Unit = {}
    open val onLongClick: () -> Unit = {}
}