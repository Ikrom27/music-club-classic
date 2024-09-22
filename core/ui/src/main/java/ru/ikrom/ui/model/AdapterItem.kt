package ru.ikrom.ui.model

abstract class AdapterItem {
    open val onClick: (() -> Unit)? = null
    open val onLongClick: (() -> Unit)? = null
}