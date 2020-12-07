package com.example.tugaspraktikum9

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel
    private val newWordActivityRequestCode = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inisiasi adapter dan recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter(this)
        //menggunakan adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        wordViewModel.allWords.observe(this, Observer { words ->
            words?.let {
                adapter.setWords(it)
                adapter.setOnClickListener {
                    val current = words[it]
                    Toast.makeText(this, "Jawaban anda "+ current.word , Toast.LENGTH_SHORT).show()
                }
            }

        })

        //pengaturan icon tambah
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity,NewWordActivity::class.java)
            startActivityForResult(intent,newWordActivityRequestCode)

        }

        //pengaturan tombol hapus
        val del = findViewById<Button>(R.id.btnhapus)
        del.setOnClickListener{
            wordViewModel.deleteALL()
            Toast.makeText(applicationContext,
                R.string.Hapus, Toast.LENGTH_LONG).show()

        }

    }

    //menampilkan kata
    override fun onActivityResult(requestCode: Int,     resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let   {
                val word = Word(it)
                wordViewModel.insert(word)
            }
        }
        else {
            Toast.makeText(applicationContext,
                R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}