package com.example.quizapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.R
import com.example.quizapp.adapter.QuizAdapter
import com.example.quizapp.data.QuizDbHelper
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.model.Quiz

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: QuizDbHelper
    private lateinit var quizAdapter: QuizAdapter

    private var selectedCategory: String = "Semua"
    private var currentSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = QuizDbHelper(this)

        setupRecyclerView()
        setupCategoryChips()
        setupSearch()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        loadQuizzes()
    }

    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter(
            context = this,
            quizzes = emptyList(),
            onQuizClick = { quiz ->
                if (quiz.questionCount == 0) {
                    Toast.makeText(this, "Kuis ini belum memiliki pertanyaan.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, PlayQuizActivity::class.java).apply {
                        putExtra(PlayQuizActivity.EXTRA_QUIZ_ID, quiz.id)
                    }
                    startActivity(intent)
                }
            },
            onQuizLongClick = { quiz ->
                showDeleteDialog(quiz)
            }
        )

        binding.rvQuizzes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = quizAdapter
        }
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                selectedCategory = "Semua"
            } else {
                when (checkedIds.first()) {
                    R.id.chipTech -> selectedCategory = "Teknologi"
                    R.id.chipEducation -> selectedCategory = "Pendidikan"
                    R.id.chipScience -> selectedCategory = "Sains"
                    R.id.chipGeneral -> selectedCategory = "Umum"
                    R.id.chipHealth -> selectedCategory = "Kesehatan"
                    else -> selectedCategory = "Semua"
                }
            }
            loadQuizzes()
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s?.toString()?.trim() ?: ""
                loadQuizzes()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFab() {
        binding.fabCreateQuiz.setOnClickListener {
            val intent = Intent(this, CreateQuizActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadQuizzes() {
        val filter = if (selectedCategory == "Semua") null else selectedCategory
        val list = dbHelper.getAllQuizzes(filter, currentSearchQuery)

        quizAdapter.updateData(list)

        if (list.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvQuizzes.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvQuizzes.visibility = View.VISIBLE
        }
    }

    private fun showDeleteDialog(quiz: Quiz) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Kuis")
            .setMessage("Apakah Anda yakin ingin menghapus kuis \"${quiz.title}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                val deleted = dbHelper.deleteQuiz(quiz.id)
                if (deleted) {
                    Toast.makeText(this, "Kuis berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadQuizzes()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
