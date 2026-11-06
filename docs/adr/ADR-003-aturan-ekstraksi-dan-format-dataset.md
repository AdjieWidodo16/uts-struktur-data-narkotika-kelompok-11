# ADR-003: Aturan Ekstraksi dan Format Dataset Putusan

Status: Diterima
Tanggal: 2026-11-06

## Konteks
Dataset 50 putusan narkotika diekstrak oleh dua orang (P001-P025 dan P026-P050).
Tanpa aturan bersama, nilai yang sama bisa ditulis berbeda sehingga filter dan
agregasi tidak konsisten. Panduan UTS mewajibkan nilai yang tidak ditemukan
ditulis 0/null, bukan diperkirakan.

## Keputusan

### Format data/putusan-50.csv
- tanggalPutusan: yyyy-MM-dd (LocalDate), diambil dari tanggal pengucapan putusan
  di sidang terbuka, bukan tanggal musyawarah.
- tuntutanBulan dan vonisBulan: total dalam bulan (5 tahun 6 bulan = 66).
- dendaRupiah: angka polos tanpa "Rp" dan titik (contoh: 1000000000).
- Beberapa nilai dalam satu sel dipisah ";". Sel berisi koma dibungkus tanda kutip.
- Tidak ditemukan: angka = 0, teks = null.
- terdakwaKode: T001 dst. Nama asli diganti kode di semua kolom, termasuk teks
  keadaanMemberatkan dan keadaanMeringankan.
- sourcePages: nomor footer "Hal. X dari Y" per kelompok atribut, contoh:
  identitas:1; barangBukti:6; tuntutan:3; pertimbangan:25-26; amar:30

### Agregasi beratBarangBuktiGram
- Satuan gram, berat netto narkotika saja; HP, timbangan, uang diabaikan.
- Prioritas sumber: netto hasil laboratorium (Labfor/BPOM) yang dikutip putusan,
  lalu netto hasil penimbangan penyidik.
- Beberapa barang bukti dijumlahkan menjadi satu total. Jenis berbeda tetap
  dijumlahkan; jenisNarkotika digabung dengan ";" dan provenance diberi status
  mixed_types.
- Hanya bruto tertulis: isi 0, status bruto_only.
- Butir/tablet tanpa berat gram: isi 0, jumlah dicatat di provenance.
- Berat tidak ditemukan: isi 0, status not_found.
- Angka mengikuti putusan (maks. 2 desimal), tanpa pembulatan sendiri.

### Provenance (docs/data-provenance.csv)
- Satu baris per atribut penting per putusan.
- extractor diisi saat ekstraksi, reviewer saat cross-check oleh partner.
- status: extracted, verified, bruto_only, not_found, mixed_types.
- File PDF sumber tidak di-commit (*.pdf di .gitignore).

## Alasan
- Satu aturan untuk dua extractor menjamin hasil filter dan agregasi konsisten.
- Prioritas hasil lab menghindari perbedaan pilihan antara timbangan polisi dan lab.
- Kolom status membedakan "memang 0" dari "tidak ditemukan".

## Konsekuensi
- Filter berat pada kasus mixed_types perlu dicek manual saat uji skenario 5.
- Nilai 0 pada berat tidak selalu berarti tanpa barang bukti; baca status di provenance.
- Perubahan aturan setelah ekstraksi dimulai berarti ekstraksi ulang oleh kedua anggota.

## Alternatif yang dipertimbangkan
- Memakai berat bruto: lebih mudah ditemukan, tetapi bukan berat zat sebenarnya.
- Mengisi perkiraan saat berat tidak ada: dilarang panduan UTS.