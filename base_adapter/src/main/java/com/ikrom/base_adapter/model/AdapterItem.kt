package com.ikrom.base_adapter.model

abstract class AdapterItem {
    open val onClick: (() -> Unit)? = null
    open val onLongClick: (() -> Unit)? = null
}