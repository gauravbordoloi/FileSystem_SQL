package com.gaurav.sdk.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class FileReaderDB(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FileReader.db"

//        CREATE TABLE `pos`.`new_table` (
//        `_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
//        `parent_id` INT NULL REFERENCES pos(_id) ON DELETE CASCADE,
//        `directory` TEXT NOT NULL,
//        `value` TEXT NULL,
//        `type` VARCHAR(10) NOT NULL,
//        `date_created` BIGINT NOT NULL,
//        `date_modified` BIGINT NOT NULL DEFAULT -1);

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FileEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INT PRIMARY KEY," +
                    "${FileEntry.COLUMN_NAME_PARENT_ID} INT REFERENCES pos(_id) ON DELETE CASCADE," +
                    "${FileEntry.COLUMN_NAME_DIRECTORY} TEXT NOT NULL," +
                    "${FileEntry.COLUMN_NAME_VALUE} TEXT," +
                    "${FileEntry.COLUMN_NAME_TYPE} VARCHAR(10) NOT NULL," +
                    "${FileEntry.COLUMN_NAME_DATE_CREATED} BIGINT NOT NULL," +
                    "${FileEntry.COLUMN_NAME_DATE_MODIFIED} BIGINT NOT NULL DEFAULT -1)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FileEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        //Add root directory
        db.execSQL(
            "INSERT INTO ${FileEntry.TABLE_NAME} VALUES " +
                    "(1, null, '/', null, 'FOLDER', ${System.currentTimeMillis()}, -1)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}

object FileEntry : BaseColumns {
    const val TABLE_NAME = "file"
    const val COLUMN_NAME_PARENT_ID = "parent_id"
    const val COLUMN_NAME_DIRECTORY = "directory"
    const val COLUMN_NAME_VALUE = "value"
    const val COLUMN_NAME_TYPE = "type"
    const val COLUMN_NAME_DATE_CREATED = "date_created"
    const val COLUMN_NAME_DATE_MODIFIED = "date_modified"
}