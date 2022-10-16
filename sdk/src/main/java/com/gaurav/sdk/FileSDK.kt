package com.gaurav.sdk

import android.content.Context
import com.gaurav.sdk.db.FileReader
import com.gaurav.sdk.db.FileReaderImpl

class FileSDK {

    lateinit var fileReader: FileReader
        private set

    fun init(context: Context) {
        fileReader = FileReaderImpl(context)
    }

}