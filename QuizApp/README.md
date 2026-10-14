# KuisPintar - Aplikasi Kuis & Kuesioner Berbasis Android

Aplikasi kuis dan kuesioner interaktif berbasis Android yang dibangun dengan **Kotlin** dan **Material Design 3**. Aplikasi ini dirancang lengkap dengan fitur manajemen kuis, pembuatan soal secara fleksibel, sistem countdown timer otomatis, serta kategorisasi kuis yang terstruktur.

---

## 🌟 Fitur Utama

1. **Kategori di Setiap Judul Kuis**:
   - Setiap kuis memiliki kategori spesifik (contoh: *Teknologi*, *Pendidikan*, *Sains*, *Umum*, *Kesehatan*, dll.).
   - Filter cepat berdasarkan kategori menggunakan Chip bar di halaman utama.
   - Badge kategori berdesain modern dengan warna khusus sesuai jenis topik.

2. **Mengerjakan Kuis & Memberikan Jawaban**:
   - Tampilan kuis yang intuitif dengan indikator nomor soal (*misal: Pertanyaan 1 dari 5*).
   - Pilihan ganda interaktif (A, B, C, D) dengan efek visual saat dipilih.
   - Status jawaban ("Belum dijawab" atau "✓ Sudah dijawab").
   - Navigasi mudah: tombol *Sebelumnya*, *Berikutnya*, dan *Selesai & Kirim*.

3. **Countdown Timer Interaktif**:
   - Digital countdown timer (*MM:SS*) dengan bar progres waktu berjalan secara real-time.
   - Indikator bahaya (urgent warning): timer berubah warna menjadi merah berkedip saat sisa waktu $\le$ 10 detik.
   - **Otomatis Submit**: jika waktu habis (00:00), kuis otomatis selesai dan langsung diarahkan ke halaman hasil.

4. **Membuat Kuis & Menambah Pertanyaan Baru**:
   - Form pembuatan kuis lengkap: Judul kuis, Kategori (dropdown / input), Durasi countdown (detik), dan Deskripsi.
   - Penambahan pertanyaan dinamis (bisa menambah sebanyak mungkin pertanyaan).
   - Pengaturan 4 opsi pilihan (A, B, C, D) serta pemilihan kunci jawaban dengan RadioButton.
   - Kuis yang dibuat langsung tersimpan permanen di database lokal (SQLite).

5. **Hasil Skor & Ulasan Jawaban (Review)**:
   - Ringkasan nilai akhir (persentase skor, jumlah benar, jumlah salah, waktu yang dihabiskan).
   - Tinjauan detail setiap soal: menampilkan pertanyaan, jawaban pengguna, status Benar/Salah, dan kunci jawaban yang benar.
   - Tombol *Ulangi Kuis* dan *Kembali ke Beranda*.

---

## 📁 Struktur Proyek

```
QuizApp/
├── app/
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/quizapp/
│   │   │   ├── model/
│   │   │   │   ├── Quiz.kt                  # Model data Kuis
│   │   │   │   ├── Question.kt              # Model data Pertanyaan & Pilihan
│   │   │   │   └── QuizResult.kt            # Model data Hasil & Ringkasan Jawaban
│   │   │   ├── data/
│   │   │   │   └── QuizDbHelper.kt          # SQLite Database & Sample Data
│   │   │   ├── adapter/
│   │   │   │   ├── QuizAdapter.kt           # Adapter daftar kuis di MainActivity
│   │   │   │   └── QuestionReviewAdapter.kt # Adapter tinjauan jawaban di ResultActivity
│   │   │   └── ui/
│   │   │       ├── MainActivity.kt          # Layar Utama (Daftar & Filter Kategori)
│   │   │       ├── CreateQuizActivity.kt    # Layar Pembuatan Kuis & Soal
│   │   │       ├── PlayQuizActivity.kt      # Layar Pengerjaan Kuis + Countdown
│   │   │       └── ResultActivity.kt        # Layar Skor & Pembahasan Soal
│   │   └── res/
│   │       ├── layout/                      # Seluruh layout XML Material 3
│   │       ├── values/                      # colors.xml, strings.xml, themes.xml
│   │       └── drawable/                    # Vector icons & card backgrounds
├── web_preview/
│   └── index.html                           # Simulator web interaktif (langsung bisa dicoba)
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Cara Menjalankan Proyek

### Opsi 1: Build File APK Menggunakan Android Studio (1 Klik)
1. Buka software **Android Studio**.
2. Pilih menu **File > Open**, lalu arahkan ke folder:
   `C:\Users\Taufik Putra\.gemini\antigravity\scratch\QuizApp`
3. Tunggu hingga proses **Gradle Sync** selesai.
4. Klik menu **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Setelah selesai, Android Studio akan menampilkan pop-up notifikasi bertuliskan *"APK(s) generated successfully"*. Klik link **locate** untuk langsung mengambil file:
   `app/build/outputs/apk/debug/app-debug.apk`

---

### Opsi 2: Build APK Otomatis di Cloud (GitHub Actions)
Jika komputer Anda belum terpasang Android Studio atau Android SDK:
1. Proyek ini sudah dilengkapi file workflow CI/CD: [build-apk.yml](file:///C:/Users/Taufik%20Putra/.gemini/antigravity/scratch/QuizApp/.github/workflows/build-apk.yml).
2. Buat repositori baru di akun GitHub Anda dan unggah seluruh folder `QuizApp`.
3. Buka tab **Actions** di repositori GitHub Anda.
4. GitHub Cloud Runner (yang telah dilengkapi Android SDK & JDK 17) akan mengompilasi kode secara otomatis dalam 2 menit.
5. Unduh file **KuisPintar-Android-APK** pada bagian **Artifacts** di halaman Actions tersebut.

---

### Opsi 3: Pasang Langsung ke HP Android Tanpa APK (PWA)
1. Buka folder [web_preview/](file:///C:/Users/Taufik%20Putra/.gemini/antigravity/scratch/QuizApp/web_preview/) melalui browser HP Android (Chrome).
2. Tap tombol menu browser (titik 3 di kanan atas), lalu pilih **"Tambahkan ke Layar Utama" (Install App)**.
3. Aplikasi akan otomatis terpasang dengan ikon dan berjalan offline layaknya aplikasi native Android.
