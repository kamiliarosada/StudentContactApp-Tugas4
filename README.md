NAMA: KAMILIA ROSADA
NIM: F1D02310063
Kelas: B

## Deskripsi Singkat Aplikasi

StudentContactApp adalah aplikasi Android sederhana yang digunakan untuk mengelola data mahasiswa seperti buku kontak digital. Aplikasi ini dibuat untuk menggabungkan beberapa materi yang telah dipelajari selama praktikum.

Fitur utama yang ada di aplikasi ini yaitu:

* **Autentikasi**: Sistem login dengan fitur *Remember Me* agar pengguna tidak perlu login ulang setiap membuka aplikasi.
* **Manajemen Data (CRUD)**: Pengguna bisa menambah, melihat, mengubah, dan menghapus data mahasiswa seperti nama, NIM, dan prodi.
* **Pencarian**: Data mahasiswa dapat dicari berdasarkan nama atau NIM secara langsung.
* **Catatan Personal**: Setiap mahasiswa memiliki catatan masing-masing yang bisa disimpan dan dibuka kembali.
* **Pengaturan**: Terdapat fitur Dark Mode dan pengaturan notifikasi untuk kenyamanan pengguna.

## Screenshot Aplikasi

### Hands on 1
login <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 47" src="https://github.com/user-attachments/assets/bf52bdaf-382a-48bc-ad2f-442a629bc420" />
beranda <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 47 (1)" src="https://github.com/user-attachments/assets/fd952205-a908-420d-ab49-1651a940ac8e" />
setting profil <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 48 (1)" src="https://github.com/user-attachments/assets/e0a5e98e-4210-4bd0-9c8a-acf366efcd73" />
### Hands on 2
notes <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 48 (2)" src="https://github.com/user-attachments/assets/41434821-e4fa-4df0-8234-c34841858963" />
notes tersimpan <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 49 (1)" src="https://github.com/user-attachments/assets/178fa560-4b3b-4d25-93c8-7eb1954a3c8d" />
### Tugas Akhir
tampilan beranda dengan pengguna crud <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 49 (2)" src="https://github.com/user-attachments/assets/db243236-5dbb-436c-bf3c-7fa7e603b94d" />
hapus mahasiswa <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 50 (3)" src="https://github.com/user-attachments/assets/706cca68-e338-4570-b29a-b3c3cafb83cf" />
tambah mahasiswa <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 50 (1)" src="https://github.com/user-attachments/assets/721862d3-bb8f-4c82-8a5e-e99f14daed86" />
pencarian <img width="576" height="1280" alt="WhatsApp Image 2026-05-05 at 13 53 51" src="https://github.com/user-attachments/assets/9f6a68a2-9628-4cc4-9300-e8bcbcfc7580" />



## Metode Penyimpanan dan Alasan Penggunaan

Dalam aplikasi ini saya menggunakan tiga jenis penyimpanan, karena masing-masing punya fungsi yang berbeda.

### 1. Room Database (SQLite)

Digunakan untuk menyimpan data utama mahasiswa seperti nama, NIM, dan prodi.

Saya memilih Room karena:

* Lebih mudah digunakan dibanding SQLite biasa
* Cocok untuk data yang terstruktur
* Data bisa langsung ter-update di tampilan ketika ada perubahan

---

### 2. Internal File Storage (.txt)

Digunakan untuk menyimpan catatan mahasiswa dalam bentuk file teks, misalnya `note_NIM.txt`.

Alasannya:

* Lebih sederhana untuk menyimpan teks panjang
* Tidak perlu dimasukkan ke database
* Membuat database tetap ringan

---

### 3. SharedPreferences

Digunakan untuk menyimpan data sederhana seperti:

* Status login
* Username
* Pengaturan aplikasi (dark mode dan notifikasi)

Alasannya:

* Praktis dan ringan
* Cocok untuk data kecil berbentuk key-value

---

## Kendala dan Cara Mengatasinya

Selama mengerjakan aplikasi ini, ada beberapa kendala yang saya temui:

### 1. Aplikasi sering error saat dijalankan

Penyebabnya karena ada komponen di layout XML yang belum lengkap atributnya, seperti `layout_width` atau `layout_height`.

Solusinya dengan mengecek ulang file XML dan memastikan semua komponen sudah memiliki atribut yang lengkap.

---

### 2. Error pada Room Database

Saya sempat mengalami error terkait *unexpected jvm signature*, yang ternyata disebabkan oleh ketidakcocokan versi Room dengan Kotlin yang digunakan.

Solusinya:

* Mengupdate versi Room ke yang lebih baru
* Mengubah tipe return pada beberapa fungsi DAO agar sesuai

---

### 3. Masalah pada plugin KSP

Saat menggunakan Room, terjadi konflik antara plugin Kotlin dan KSP sehingga file database tidak terbentuk.

Solusinya dengan menambahkan konfigurasi:

"android.disallowKotlinSourceSets=false"

di file `gradle.properties` agar KSP bisa berjalan dengan baik.

---

## Kesimpulan

Dari pengerjaan aplikasi ini, saya lebih memahami bagaimana cara menggabungkan beberapa metode penyimpanan dalam satu aplikasi. Saya juga belajar bagaimana memilih jenis penyimpanan yang sesuai dengan kebutuhan data.

Walaupun masih sederhana, aplikasi ini sudah bisa berjalan dengan baik dan cukup membantu saya dalam memahami konsep database dan penyimpanan data di Android.
