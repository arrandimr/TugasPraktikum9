package com.example.tugaspraktikum9

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//memanggil database
@Database (entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase(){
    abstract fun wordDao(): WordDao

    //titik akses ke database sqllite
    private class  WordDataBaseCallBack (
        private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        //menambahkan contoh data
        suspend fun populateDatabase(wordDao: WordDao){
            wordDao.deleteALL()

            // Add sample words.
            var word = Word("NIK")
            wordDao.insert(word)
            //word = Word("World!")
            //wordDao.insert(word)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,WordRoomDatabase::class.java,
                    "word_database")
                    .addCallback(WordDataBaseCallBack(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}