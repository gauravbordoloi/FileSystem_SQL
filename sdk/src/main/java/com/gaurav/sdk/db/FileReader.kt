package com.gaurav.sdk.db

import com.gaurav.sdk.ElementType

abstract class FileReader {

    abstract fun scan(dirPath: String): List<String>

    abstract fun create(elementPath: String, elementType: ElementType): Boolean

    abstract fun read(filePath: String): String?

    abstract fun write(filePath: String, stringContent: String): Boolean

    abstract fun move(elmPath: String, dirPath: String): Boolean

    abstract fun rename(elmPath: String, newName: String): Boolean

    abstract fun delete(elmPath: String): Boolean

    abstract fun mtime(filePath: String): Long

    abstract fun ctime(filePath: String): Long

}