package com.project.university.specializedproject.bktechstore

fun String.getFirstWord() : String{
    val firstWsIdx = this.indexOfFirst { c -> c == ' ' }
    return this.substring(0, firstWsIdx)
}