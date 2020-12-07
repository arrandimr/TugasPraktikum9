package com.example.tugaspraktikum9

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao){

    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
    //menyimpan data dengan perintah insert dari wordDao
    suspend fun insert(word: Word){
        wordDao.insert(word)
    }
    //menghapus semua data dengan perintah deleteALL dari wordDao
    suspend fun deleteALL(){
        wordDao.deleteALL()
    }
}