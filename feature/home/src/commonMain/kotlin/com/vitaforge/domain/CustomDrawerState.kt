package com.vitaforge.domain

enum class CustomDrawerState {
    Opened,
    Closed,
}

fun CustomDrawerState.isOpened() = this == CustomDrawerState.Opened

fun CustomDrawerState.opposite() = if (this == CustomDrawerState.Opened) CustomDrawerState.Closed else CustomDrawerState.Opened
