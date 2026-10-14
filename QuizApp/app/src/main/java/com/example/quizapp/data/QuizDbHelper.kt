package com.example.quizapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quizapp.model.Question
import com.example.quizapp.model.Quiz

class QuizDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz_app.db"
        private const val DATABASE_VERSION = 1

        // Table Quizzes
        const val TABLE_QUIZZES = "quizzes"
        const val COLUMN_QUIZ_ID = "id"
        const val COLUMN_QUIZ_TITLE = "title"
        const val COLUMN_QUIZ_CATEGORY = "category"
        const val COLUMN_QUIZ_DESCRIPTION = "description"
        const val COLUMN_QUIZ_DURATION = "duration_seconds"
        const val COLUMN_QUIZ_CREATED_AT = "created_at"

        // Table Questions
        const val TABLE_QUESTIONS = "questions"
        const val COLUMN_QUESTION_ID = "id"
        const val COLUMN_QUESTION_QUIZ_ID = "quiz_id"
        const val COLUMN_QUESTION_TEXT = "question_text"
        const val COLUMN_OPTION_A = "option_a"
        const val COLUMN_OPTION_B = "option_b"
        const val COLUMN_OPTION_C = "option_c"
        const val COLUMN_OPTION_D = "option_d"
        const val COLUMN_CORRECT_ANSWER = "correct_answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createQuizzesTable = """
            CREATE TABLE $TABLE_QUIZZES (
                $COLUMN_QUIZ_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUIZ_TITLE TEXT NOT NULL,
                $COLUMN_QUIZ_CATEGORY TEXT NOT NULL,
                $COLUMN_QUIZ_DESCRIPTION TEXT,
                $COLUMN_QUIZ_DURATION INTEGER DEFAULT 60,
                $COLUMN_QUIZ_CREATED_AT INTEGER
            )
        """.trimIndent()

        val createQuestionsTable = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_QUIZ_ID INTEGER NOT NULL,
                $COLUMN_QUESTION_TEXT TEXT NOT NULL,
                $COLUMN_OPTION_A TEXT NOT NULL,
                $COLUMN_OPTION_B TEXT NOT NULL,
                $COLUMN_OPTION_C TEXT NOT NULL,
                $COLUMN_OPTION_D TEXT NOT NULL,
                $COLUMN_CORRECT_ANSWER INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_QUESTION_QUIZ_ID) REFERENCES $TABLE_QUIZZES($COLUMN_QUIZ_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createQuizzesTable)
        db.execSQL(createQuestionsTable)

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUIZZES")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        // Kuis 1: Teknologi - Pemrograman Android & Kotlin (Durasi 60 detik)
        val quiz1Id = insertQuizRaw(
            db,
            "Dasar Pemrograman Android & Kotlin",
            "Teknologi",
            "Uji kemampuan dasar komponen Android, Activity lifecycle, dan sintaks Kotlin modern.",
            60
        )
        insertQuestionRaw(
            db, quiz1Id,
            "Apa fungsi keyword 'val' dalam bahasa pemrograman Kotlin?",
            "Mendeklarasikan variabel immutable (tidak bisa diubah)",
            "Mendeklarasikan variabel mutable (bisa diubah)",
            "Mendeklarasikan fungsi statis",
            "Mendeklarasikan class turunan",
            0
        )
        insertQuestionRaw(
            db, quiz1Id,
            "Siklus hidup (lifecycle) Activity yang pertama kali dipanggil saat activity dibuat adalah...",
            "onStart()",
            "onResume()",
            "onCreate()",
            "onPause()",
            2
        )
        insertQuestionRaw(
            db, quiz1Id,
            "Komponen apa yang digunakan untuk menampilkan daftar data scrollable dalam jumlah banyak secara efisien?",
            "TextView",
            "RecyclerView",
            "ImageView",
            "Button",
            1
        )
        insertQuestionRaw(
            db, quiz1Id,
            "Tool resmi yang dikembangkan Google untuk membuat aplikasi Android adalah...",
            "Visual Studio Code",
            "Eclipse",
            "Android Studio",
            "Xcode",
            2
        )

        // Kuis 2: Pengetahuan Umum - Nusantara & Dunia (Durasi 45 detik)
        val quiz2Id = insertQuizRaw(
            db,
            "Pengetahuan Umum Nusantara",
            "Umum",
            "Seberapa kenal kamu dengan sejarah, geografi, dan kebudayaan Indonesia?",
            45
        )
        insertQuestionRaw(
            db, quiz2Id,
            "Ibu Kota Nusantara (IKN) berlokasi di provinsi mana?",
            "Kalimantan Timur",
            "Kalimantan Barat",
            "Sulawesi Selatan",
            "Jawa Barat",
            0
        )
        insertQuestionRaw(
            db, quiz2Id,
            "Candi Buddha terbesar di dunia yang berlokasi di Magelang adalah...",
            "Candi Prambanan",
            "Candi Borobudur",
            "Candi Mendut",
            "Candi Sewu",
            1
        )
        insertQuestionRaw(
            db, quiz2Id,
            "Lagu kebangsaan Indonesia Raya pertama kali dikumandangkan pada tahun...",
            "1945",
            "1928 (Sumpah Pemuda)",
            "1908 (Kebangkitan Nasional)",
            "1930",
            1
        )

        // Kuis 3: Sains - Alam & Tata Surya (Durasi 60 detik)
        val quiz3Id = insertQuizRaw(
            db,
            "Eksplorasi Sains & Tata Surya",
            "Sains",
            "Pertanyaan seputar astronomi, biologi dasar, dan fenomena alam semesta.",
            60
        )
        insertQuestionRaw(
            db, quiz3Id,
            "Planet terbesar dalam tata surya kita adalah...",
            "Mars",
            "Saturnus",
            "Jupiter",
            "Neptunus",
            2
        )
        insertQuestionRaw(
            db, quiz3Id,
            "Bagian sel tumbuhan yang bertanggung jawab dalam proses fotosintesis adalah...",
            "Kloroplas",
            "Mitokondria",
            "Ribosom",
            "Inti Sel (Nukleus)",
            0
        )
        insertQuestionRaw(
            db, quiz3Id,
            "Gas yang paling melimpah di atmosfer bumi adalah...",
            "Oksigen",
            "Karbon Dioksida",
            "Nitrogen",
            "Hidrogen",
            2
        )

        // Kuis 4: Pendidikan - Kuesioner Minat & Gaya Belajar (Durasi 90 detik)
        val quiz4Id = insertQuizRaw(
            db,
            "Kuesioner Gaya Belajar Siswa",
            "Pendidikan",
            "Kenali kecenderungan gaya belajar dominan Anda: visual, auditori, atau kinestetik.",
            90
        )
        insertQuestionRaw(
            db, quiz4Id,
            "Ketika mempelajari topik baru, cara apa yang paling membantu Anda memahami?",
            "Melihat diagram, infografis, dan video animasi",
            "Mendengarkan penjelasan guru atau podcast",
            "Mempraktikkan langsung dan membuat contoh sendiri",
            "Membaca buku teks berulang kali dalam keheningan",
            0
        )
        insertQuestionRaw(
            db, quiz4Id,
            "Ketika menghadapi tugas kelompok yang sulit, Anda lebih memilih:",
            "Menuliskan rencana kerja secara sistematis di papan",
            "Mengadakan diskusi lisan bersama tim",
            "Membagi tugas dan langsung mengerjakan bagian masing-masing",
            "Mencari contoh proyek terdahulu di internet",
            1
        )
    }

    private fun insertQuizRaw(db: SQLiteDatabase, title: String, category: String, desc: String, duration: Int): Long {
        val cv = ContentValues().apply {
            put(COLUMN_QUIZ_TITLE, title)
            put(COLUMN_QUIZ_CATEGORY, category)
            put(COLUMN_QUIZ_DESCRIPTION, desc)
            put(COLUMN_QUIZ_DURATION, duration)
            put(COLUMN_QUIZ_CREATED_AT, System.currentTimeMillis())
        }
        return db.insert(TABLE_QUIZZES, null, cv)
    }

    private fun insertQuestionRaw(
        db: SQLiteDatabase,
        quizId: Long,
        text: String,
        optA: String,
        optB: String,
        optC: String,
        optD: String,
        correct: Int
    ) {
        val cv = ContentValues().apply {
            put(COLUMN_QUESTION_QUIZ_ID, quizId)
            put(COLUMN_QUESTION_TEXT, text)
            put(COLUMN_OPTION_A, optA)
            put(COLUMN_OPTION_B, optB)
            put(COLUMN_OPTION_C, optC)
            put(COLUMN_OPTION_D, optD)
            put(COLUMN_CORRECT_ANSWER, correct)
        }
        db.insert(TABLE_QUESTIONS, null, cv)
    }

    fun insertQuizWithQuestions(quiz: Quiz, questions: List<Question>): Long {
        val db = writableDatabase
        db.beginTransaction()
        var quizId: Long = -1
        try {
            val cv = ContentValues().apply {
                put(COLUMN_QUIZ_TITLE, quiz.title)
                put(COLUMN_QUIZ_CATEGORY, quiz.category)
                put(COLUMN_QUIZ_DESCRIPTION, quiz.description)
                put(COLUMN_QUIZ_DURATION, quiz.durationSeconds)
                put(COLUMN_QUIZ_CREATED_AT, System.currentTimeMillis())
            }
            quizId = db.insert(TABLE_QUIZZES, null, cv)

            for (q in questions) {
                val qCv = ContentValues().apply {
                    put(COLUMN_QUESTION_QUIZ_ID, quizId)
                    put(COLUMN_QUESTION_TEXT, q.questionText)
                    put(COLUMN_OPTION_A, q.optionA)
                    put(COLUMN_OPTION_B, q.optionB)
                    put(COLUMN_OPTION_C, q.optionC)
                    put(COLUMN_OPTION_D, q.optionD)
                    put(COLUMN_CORRECT_ANSWER, q.correctAnswer)
                }
                db.insert(TABLE_QUESTIONS, null, qCv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        return quizId
    }

    fun getAllQuizzes(categoryFilter: String? = null, searchQuery: String? = null): List<Quiz> {
        val db = readableDatabase
        val quizList = mutableListOf<Quiz>()

        var selection: String? = null
        val selectionArgs = mutableListOf<String>()

        if (!categoryFilter.isNullOrBlank() && !categoryFilter.equals("Semua", ignoreCase = true)) {
            selection = "$COLUMN_QUIZ_CATEGORY = ?"
            selectionArgs.add(categoryFilter)
        }

        if (!searchQuery.isNullOrBlank()) {
            val searchCondition = "($COLUMN_QUIZ_TITLE LIKE ? OR $COLUMN_QUIZ_DESCRIPTION LIKE ?)"
            selection = if (selection == null) searchCondition else "$selection AND $searchCondition"
            selectionArgs.add("%$searchQuery%")
            selectionArgs.add("%$searchQuery%")
        }

        val cursor = db.query(
            TABLE_QUIZZES,
            null,
            selection,
            if (selectionArgs.isNotEmpty()) selectionArgs.toTypedArray() else null,
            null,
            null,
            "$COLUMN_QUIZ_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_QUIZ_ID))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_QUIZ_TITLE))
                val category = it.getString(it.getColumnIndexOrThrow(COLUMN_QUIZ_CATEGORY))
                val desc = it.getString(it.getColumnIndexOrThrow(COLUMN_QUIZ_DESCRIPTION))
                val duration = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUIZ_DURATION))
                val createdAt = it.getLong(it.getColumnIndexOrThrow(COLUMN_QUIZ_CREATED_AT))

                val questionCount = getQuestionCountForQuiz(db, id)
                quizList.add(Quiz(id, title, category, desc, duration, createdAt, questionCount))
            }
        }
        return quizList
    }

    private fun getQuestionCountForQuiz(db: SQLiteDatabase, quizId: Long): Int {
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_QUESTIONS WHERE $COLUMN_QUESTION_QUIZ_ID = ?",
            arrayOf(quizId.toString())
        )
        var count = 0
        cursor.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        return count
    }

    fun getQuestionsForQuiz(quizId: Long): List<Question> {
        val db = readableDatabase
        val list = mutableListOf<Question>()
        val cursor = db.query(
            TABLE_QUESTIONS,
            null,
            "$COLUMN_QUESTION_QUIZ_ID = ?",
            arrayOf(quizId.toString()),
            null,
            null,
            "$COLUMN_QUESTION_ID ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_QUESTION_ID))
                val text = it.getString(it.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT))
                val optA = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION_A))
                val optB = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION_B))
                val optC = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION_C))
                val optD = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION_D))
                val correct = it.getInt(it.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER))

                list.add(Question(id, quizId, text, optA, optB, optC, optD, correct))
            }
        }
        return list
    }

    fun deleteQuiz(quizId: Long): Boolean {
        val db = writableDatabase
        db.delete(TABLE_QUESTIONS, "$COLUMN_QUESTION_QUIZ_ID = ?", arrayOf(quizId.toString()))
        val rows = db.delete(TABLE_QUIZZES, "$COLUMN_QUIZ_ID = ?", arrayOf(quizId.toString()))
        return rows > 0
    }
}
