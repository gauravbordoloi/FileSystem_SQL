package com.gaurav.sdk.db

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import androidx.core.database.getStringOrNull
import com.gaurav.sdk.ElementType
import com.gaurav.sdk.File

class FileReaderImpl(context: Context) : FileReader() {

    private val dbHelper = FileReaderDB(context)

    override fun scan(dirPath: String): List<String> {
        val paths = dirPath.split("/")
        if (paths.isEmpty()) return emptyList()

        dbHelper.readableDatabase.query(
            FileEntry.TABLE_NAME,
            null,
            "${FileEntry.COLUMN_NAME_DIRECTORY} = ? OR ${FileEntry.COLUMN_NAME_DIRECTORY} LIKE ?",
            arrayOf(dirPath, "$dirPath%"),
            null,
            null,
            "${FileEntry.COLUMN_NAME_DATE_CREATED} DESC"
        ).use {
            val itemIds = mutableListOf<File>()
            while (it.moveToNext()) {
                val directory =
                    it.getString(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_DIRECTORY))
                val value =
                    it.getStringOrNull(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_VALUE))
                val dateCreated =
                    it.getLong(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_DATE_CREATED))
                val dateModified =
                    it.getLong(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_DATE_MODIFIED))
                itemIds.add(
                    File(
                        directory, value, dateCreated, dateModified
                    )
                )
            }
            return itemIds.map { file ->
                file.directory
            }
        }
    }

    override fun create(elementPath: String, elementType: ElementType): Boolean {
        val lastSlashIndex = elementPath.indexOfLast {
            it == '/'
        }

        if (lastSlashIndex < 0 || lastSlashIndex == elementPath.length - 1) {
            return false
        }

        val selection = "${FileEntry.COLUMN_NAME_DIRECTORY} = ?"
        var args = elementPath.substring(0, lastSlashIndex + 1)
        if (args.length > 1) {
            args = args.substring(0, args.length - 1)
        }
        val selectionArgs = arrayOf(args)

        val parentId: Int
        dbHelper.readableDatabase.query(
            FileEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        ).use {
            if (!it.moveToNext()) {
                return false
            } else {
                parentId = it.getInt(it.getColumnIndexOrThrow(BaseColumns._ID))
            }
        }

        val values = ContentValues().apply {
            put(FileEntry.COLUMN_NAME_PARENT_ID, parentId)
            put(FileEntry.COLUMN_NAME_DIRECTORY, elementPath)
            put(FileEntry.COLUMN_NAME_TYPE, elementType.name)
            put(FileEntry.COLUMN_NAME_DATE_CREATED, System.currentTimeMillis())
        }
        return dbHelper.writableDatabase.insert(FileEntry.TABLE_NAME, null, values).toInt() != -1
    }

    override fun read(filePath: String): String? {
        val db = dbHelper.readableDatabase

        val selection =
            "${FileEntry.COLUMN_NAME_DIRECTORY} = ? AND ${FileEntry.COLUMN_NAME_TYPE} = ?"
        val selectionArgs = arrayOf(filePath, ElementType.FILE.name)

        db.query(FileEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null).use {
            while (it.moveToNext()) {
                val value =
                    it.getStringOrNull(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_VALUE))
                return value ?: ""
            }
        }
        return null
    }

    override fun write(filePath: String, stringContent: String): Boolean {
        val selection =
            "${FileEntry.COLUMN_NAME_DIRECTORY} = ? AND ${FileEntry.COLUMN_NAME_TYPE} = ?"
        val selectionArgs = arrayOf(filePath, ElementType.FILE.name)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FileEntry.COLUMN_NAME_VALUE, stringContent)
            put(FileEntry.COLUMN_NAME_DATE_MODIFIED, System.currentTimeMillis())
        }
        return db.update(FileEntry.TABLE_NAME, values, selection, selectionArgs) > 0
    }

    //"/IN/KA", "/US"
    override fun move(elmPath: String, dirPath: String): Boolean {
        if (dirPath.contains(elmPath)) {
            return false
        }

        val selection = "${FileEntry.COLUMN_NAME_DIRECTORY} = ?"
        val selectionArgs = arrayOf(dirPath)

        //Get parent id of destination folder, if not found return false
        val parentId: Int
        dbHelper.readableDatabase.query(
            FileEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        ).use {
            if (!it.moveToNext()) {
                return false
            } else {
                parentId = it.getInt(it.getColumnIndexOrThrow(BaseColumns._ID))
            }
        }

        val lastSlashIndex = elmPath.indexOfLast {
            it == '/'
        }

        val value = dbHelper.writableDatabase.update(
            FileEntry.TABLE_NAME,
            ContentValues().apply {
                put(FileEntry.COLUMN_NAME_PARENT_ID, parentId)
                put(
                    FileEntry.COLUMN_NAME_DIRECTORY,
                    dirPath + "/" + elmPath.substring(lastSlashIndex + 1)
                )
                put(FileEntry.COLUMN_NAME_DATE_MODIFIED, System.currentTimeMillis())
            },
            "${FileEntry.COLUMN_NAME_DIRECTORY} = ?",
            arrayOf(elmPath)
        )
        if (value <= 0) {
            return false
        }

        return try {
            dbHelper.writableDatabase.execSQL(
                "UPDATE ${FileEntry.TABLE_NAME} SET ${FileEntry.COLUMN_NAME_DIRECTORY} = REPLACE(${FileEntry.COLUMN_NAME_DIRECTORY}, '${elmPath}', '${dirPath}'), ${FileEntry.COLUMN_NAME_DATE_MODIFIED} = ${System.currentTimeMillis()}"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun rename(elmPath: String, newName: String): Boolean {
        if (newName.contains("/") || newName.contains("\\") || newName == "." || newName == "..") {
            return false
        }

        val lastSlashIndex = elmPath.indexOfLast {
            it == '/'
        }

        val newDir = elmPath.substring(0, lastSlashIndex) + "/" + newName

        return try {
            dbHelper.writableDatabase.execSQL(
                "UPDATE ${FileEntry.TABLE_NAME} SET ${FileEntry.COLUMN_NAME_DIRECTORY} = REPLACE(${FileEntry.COLUMN_NAME_DIRECTORY}, '${elmPath}', '${newDir}'), ${FileEntry.COLUMN_NAME_DATE_MODIFIED} = ${System.currentTimeMillis()}"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun delete(elmPath: String): Boolean {
        val selection =
            "${FileEntry.COLUMN_NAME_DIRECTORY} = ?"
        val selectionArgs = arrayOf(elmPath)
        val db = dbHelper.writableDatabase
        return db.delete(FileEntry.TABLE_NAME, selection, selectionArgs) > 0
    }

    override fun mtime(filePath: String): Long {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(FileEntry.COLUMN_NAME_DATE_MODIFIED)
        val selection = "${FileEntry.COLUMN_NAME_DIRECTORY} = ?"
        val selectionArgs = arrayOf(filePath)
        db.query(FileEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null).use {
            if (it.moveToNext()) {
                return it.getLong(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_DATE_MODIFIED))
            }
            return -1
        }
    }

    override fun ctime(filePath: String): Long {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(FileEntry.COLUMN_NAME_DATE_CREATED)
        val selection = "${FileEntry.COLUMN_NAME_DIRECTORY} = ?"
        val selectionArgs = arrayOf(filePath)
        db.query(FileEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null).use {
            if (it.moveToNext()) {
                return it.getLong(it.getColumnIndexOrThrow(FileEntry.COLUMN_NAME_DATE_CREATED))
            }
            return -1
        }
    }
}