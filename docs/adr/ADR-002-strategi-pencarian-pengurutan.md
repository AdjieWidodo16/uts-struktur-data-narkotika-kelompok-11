# ADR-002: Strategi Pencarian dan Pengurutan

Status: Diterima
Tanggal: 2026-11-06

## Konteks
Struktur utama adalah Singly Linked List (ADR-001). Tidak ada akses indeks O(1),
dan penyimpanan inti tidak boleh memakai koleksi java.util. Aplikasi wajib punya
fitur Cari (nomor perkara/caseId, dengan jumlah langkah), Filter, dan Urutan
berdasarkan vonis tanpa merusak invariant.

## Keputusan

### Pencarian
- Linear search dari head sampai tail pada nomor perkara atau caseId.
- Setiap node yang dibandingkan menambah pencacah langkah; hasil mengembalikan
  posisi (atau -1) beserta jumlah langkah.
- Filter (jenis narkotika, rentang berat, pasal terbukti, rentang vonis) juga
  satu kali penelusuran O(n), hasilnya ditampung di list baru.
- Pada Stack (audit), pencarian bersifat non-destruktif: elemen di-pop ke stack
  bantu, lalu dikembalikan (searchPreserve + restore) sehingga urutan akhir sama.

### Pengurutan
- Merge sort pada linked list berdasarkan vonisBulan (naik/turun).
- Sort dijalankan pada salinan list (node baru yang menunjuk objek Putusan yang
  sama), sehingga list utama tidak berubah dan invariant (head/tail/size) terjaga.
- Stabil: jika vonisBulan sama, urutan mengikuti urutan awal (tie-break caseId).
- Tidak memakai array bantu atau konversi ke koleksi bawaan.

## Alasan
- Binary search tidak cocok: linked list tidak bisa akses tengah dalam O(1),
  jadi biayanya tetap O(n) dan malah lebih rumit.
- Merge sort cocok untuk linked list: hanya mengubah pointer, tanpa random access,
  O(n log n) dengan ruang bantu kecil.
- Analisis O(n log n): rekurensi T(n) = 2T(n/2) + O(n). Pembagian list
    membutuhkan satu traversal O(n) (addLast O(1) karena ada tail), dan penggabungan
    memindahkan tiap elemen tepat satu kali, O(n). Ada log2(n) tingkat rekursi dan
    tiap tingkat total kerja O(n), sehingga hasilnya O(n log n) pada best, average,
    maupun worst case. Ruang: O(n) untuk list hasil dan O(log n) untuk stack rekursi.
- Sort pada salinan menjaga urutan arsip asli dan menghindari risiko merusak pointer.


## Konsekuensi
- Search/filter: O(n), ruang tambahan O(1) (O(k) untuk hasil filter).
- Sort: O(n log n) waktu, O(n) ruang untuk salinan list.
- Tampilan terurut harus dibuat ulang jika data berubah.
- Pengujian: cek hasil terurut, urutan list utama tidak berubah, validateInvariant()
  lolos, dan pencacah langkah sesuai trace manual.

## Alternatif yang dipertimbangkan
- Binary search: butuh akses indeks O(1), tidak tersedia pada linked list.
- Insertion sort: sederhana, tetapi O(n^2).
- Sort in-place pada list utama: hemat ruang, tetapi merusak urutan arsip asli.
- Salin ke array lalu sort: melanggar semangat batasan (konversi ke struktur lain
  untuk menyamarkan operasi inti).