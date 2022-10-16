package com.gaurav.sdk

data class File(
    val directory: String,
    val value: String?,
    val dateCreated: Long,
    val dateModified: Long
)