# ADR-001: Pemilihan Struktur Data Utama

Status: Diterima
Tanggal: 2026-11-02

## Konteks
Aplikasi menyimpan 50 putusan narkotika. Struktur utama harus dibuat dari nol,
generik, dan tidak memakai koleksi java.util.

## Keputusan
Struktur utama: Singly Linked List. Struktur sekunder: Stack (riwayat audit).

## Alasan
- Mencakup dua capaian pembelajaran (Linked List dan Stack).
- Operasi hapus di awal, tengah, dan akhir mudah ditelusuri lewat perubahan pointer.
- Stack cocok untuk riwayat operasi pengguna (LIFO).

## Konsekuensi
- Akses get(i) dan search bernilai O(n).
- addLast O(1) hanya jika menyimpan pointer tail.
- Invariant yang dijaga: head/tail konsisten, node terakhir menunjuk null, size sesuai jumlah node.

## Alternatif yang dipertimbangkan
Array dinamis: akses O(1), tetapi insert/remove di tengah menggeser elemen.