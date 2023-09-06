package com.urb.poemscollection.database

import android.R.attr
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.urb.poemscollection.model.FavModel


class DbHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    lateinit var myDbInstance: SQLiteDatabase

    companion object{

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "AddNotes"
        private val TABLE_FAV = "AddNotesTable"
        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_IMAGE = "image_uri"
        private val KEY_VIDEO="video_uri"

    }
    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_FAVORITE_TABLE = ("CREATE TABLE " + TABLE_FAV + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT," + KEY_IMAGE + " TEXT,"
                + KEY_VIDEO + " TEXT "
                + " )")

        db?.execSQL(CREATE_FAVORITE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV)
        onCreate(db)
    }

    fun addFav(title : String, image_uri : String, video_uri : String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(KEY_TITLE, title)
        values.put(KEY_IMAGE, image_uri)
        values.put(KEY_VIDEO, video_uri)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_FAV, null, values)

        // at last we are
        // closing our database
        db.close()
    }
    
    fun openDB() {
        myDbInstance = this.writableDatabase
    }

    fun closeDB() {
        myDbInstance.close()
    }
    fun viewFav(): List<FavModel> {
        openDB()
        val selectQuery = "SELECT  * FROM $TABLE_FAV"

        val favList: ArrayList<FavModel> = ArrayList<FavModel>()

        val cursor = myDbInstance.rawQuery(selectQuery, null)

        var id: Int
        var title: String
        var image_uri: String
        var video_uri: String

        if (cursor.moveToFirst()) {
            do {

                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE))
                image_uri = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE))
                video_uri = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIDEO))

                val fav = FavModel(
                    id = id,
                    name = title,
                    thumbnail = image_uri,
                    videoUri = video_uri,
                    )



                favList.add(fav)
            } while (cursor.moveToNext())
        }

        closeDB()
        return favList
    }

    fun removeFav(recordId: Int?) {
        val db = this.writableDatabase
        //db.delete()
        val Query = String.format("DELETE FROM $TABLE_FAV WHERE $KEY_ID='%s'", recordId)
        db.execSQL(Query)
    }
    fun isNameExists(name: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FAV WHERE $KEY_TITLE = ?"
        val cursor = db.rawQuery(query, arrayOf(name))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}