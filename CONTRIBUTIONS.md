# CONTRIBUTIONS

Kelompok 11, Struktur Data (Semester Ganjil 2026/2027).
https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11.git

## Ringkasan pembagian
| Area | Adjie | Adinda |
|---|---|---|
| Ekstraksi data | P001-P025 (PR #36) | P026-P050 (PR #40) |
| Implementasi | Interface, Node, SinglyLinkedList, PutusanSearch, PutusanSorter, PutusanAggregator, InputHelper, Menu CLI | Putusan, Stack, AuditLog, PutusanDisplay, PutusanFilter, Exporter |
| Bersama | CsvImporter, ScenarioTest (7 skenario) | CsvImporter, ScenarioTest (7 skenario) |
| Pengujian | Test SinglyLinkedList, Search, Sorter, Aggregator, InputHelper | Test Putusan, Stack, AuditLog, Display, Filter, Exporter |
| Dokumentasi | ADR-001, ADR-002, ADR-003, trace manual, bukti demo, CONTRIBUTIONS, AI_USAGE | ADR pemetaan method Stack, AI_USAGE |
| Review silang | Mereview semua PR Adinda (#26-#41) | Mereview semua PR Adjie (#7-#45) |

Catatan pembagian: PR #38, #40, dan #41 dibuka dan di-merge dari akun Adinda, sedangkan commit di dalamnya tercatat dari akun Adjie. Ekstraksi P026-P050 dikerjakan Adinda (kolom `extractor` pada provenance), sedangkan CsvImporter dikerjakan bersama. Commit dilakukan dari akun Adjie.

## Adjie

### Issue yang dikerjakan
| Issue | Judul | Diselesaikan lewat |
|---|---|---|
| [#1](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/1) | Interface `StrukturData<T>` | PR #7 |
| [#3](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/3) | Ekstraksi putusan 1-25 | PR #36 |
| [#5](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/5) | SinglyLinkedList | PR #12 |
| [#17](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/17) | Cari + hitung langkah | PR #28 |
| [#18](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/18) | Urutan berdasarkan vonis | PR #29 |
| [#19](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/19) | Agregasi | PR #30 |
| [#20](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/20) | Menu CLI + integrasi | PR #42 |
| [#24](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/24) | Test 7 skenario + trace (bersama Adinda) | PR #43, #44 |

### Pull Request yang dibuat
| PR | Judul / isi | Jenis | Status |
|---|---|---|---|
| #7 | feat: add StrukturData<T> interface | Kode | Merged |
| #8 | docs: add ADR-001 data structure selection | Dokumentasi | Merged |
| #9 | data: add csv headers | Data | Merged |
| #10 | feat/singly-linked-list: Node, SinglyLinkedList, get, insert, remove, test (Part of #5) | Kode | Merged |
| #11 | feat/linked-list-search: search dengan penghitung langkah, contains (Part of #5) | Kode | Merged |
| #12 | feat/linked-list-invariant: clear, validateInvariant, implements StrukturData<T>, add (Closes #5) | Kode | Merged |
| #13 | docs: update AI usage log | Dokumentasi | Merged |
| #14 | docs: add CONTRIBUTIONS | Dokumentasi | Merged |
| #15 | docs: add ADR-002 search and sorting strategy | Dokumentasi | Merged |
| #16 | docs: add ADR-003 Data Extraction Rules and Dataset Formats | Dokumentasi | Merged |
| #25 | docs: add AI usage log (Adjie) 2026-11-06 | Dokumentasi | Merged |
| #28 | Feature/putusan search: PutusanSearch, cariByCaseId, cariByNomorPerkara | Kode | Merged |
| #29 | Feature/putusan-sorter: merge sort stabil berdasarkan vonisBulan | Kode | Merged |
| #30 | Feature/putusan-aggregator: JenisCount, Ringkasan, distribusi jenis narkotika | Kode | Merged |
| #31 | docs: add AI usage log (Adjie) issues 17-19 (2026-11-11) | Dokumentasi | Merged |
| #36 | Ekstraksi putusan P001-P025 PN Batam beserta provenance | Data | Merged |
| #37 | fix(data): ubah format tanggalPutusan ke yyyy-MM-dd sesuai ADR-003 | Data | Merged |
| #42 | Feature/menu cli integrasi: InputHelper dan menu CLI | Kode | Merged |
| #43 | test(scenario): kerangka ScenarioTest, fixture CSV, dan helper | Pengujian | Merged |
| #44 | Feature/test 7 skenario | Pengujian | Merged |
| #45 | docs: Update AI_USAGE.md | Dokumentasi | Merged |

### Review yang diberikan
Seluruh PR Adinda direview dan diberi status Approved.

| PR yang direview | Isi PR | Status |
|---|---|---|
| [#26](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/26) | Class Putusan | Approved |
| [#27](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/27) | AI usage log | Approved |
| [#32](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/32) | Stack generik untuk audit | Approved |
| [#33](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/33) | PutusanDisplay dan PutusanFilter | Approved |
| [#34](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/34) | AuditLog dan Exporter | Approved |
| [#35](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/35) | AI usage 2026-11-12 | Approved |
| [#38](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/38) | CsvImporter, perubahan data di CsvImporterTest | Approved |
| [#39](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/39) | Perbaikan baris di AI usage | Approved |
| [#40](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/40) | Ekstraksi P026-P050, penyesuaian format tanggal di putusan-50 | Approved |
| [#41](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/41) | CsvImporterTest: ukuran data 25 menjadi 50 | Approved |

Komentar utama pada review PR #33 dan #34 (paling banyak diskusi):
- #33: menanyakan apakah nilai berat dan vonis 0 di `cocok()` sebaiknya langsung dikecualikan, karena menurut ADR-003 nilai 0 berarti data tidak ditemukan. Adinda setuju dan merevisi `PutusanFilter` dan `PutusanFilterTest` (`9d870b5`, `268c8c2`). Setelah dicek ulang, perbaikan dinyatakan sesuai ADR-003 dan unit test lulus.
- #34: menanyakan apakah `SecurityException` pada penulisan file perlu ditangani, karena `tulis()` hanya menangkap `IOException`. Adinda merevisi `Exporter.tulis()` (`c2b35cf`). Setelah dicek ulang, alur utama, pemeliharaan urutan Stack, format CSV, dan pengujian dinyatakan sesuai.

### Commit utama
Interface dan dokumentasi:
- `02bccaf` feat: add StrukturData<T> interface
- `7288e82` docs: clarify tail pointer complexity in ADR-001
- `f557737` docs: update AI usage log
- `278e473` docs: add ADR-002 search and sorting strategy
- `595d3bd` docs: add ADR-003 Data Extraction Rules and Dataset Formats
- `4e107fb` docs: explain O(n log n) derivation for merge sort in ADR-002
- `e4a45da` docs(trace): tambah trace manual pointer hapus awal, tengah, dan akhir
- `463007a` docs(trace): tambah tabel pencacah langkah pencarian untuk analisis kompleksitas
- `3736afc` docs(demo): tambah bukti keluaran skenario 2, 3, dan 4
- `04b5c6f` docs(demo): tambah bukti keluaran skenario 5

Struktur data (`SinglyLinkedList`):
- `1d9b1e1` feat: implement generic Node<T> with data and next
- `d4a0988` feat: add SinglyLinkedList fields, size, isEmpty, addFirst, addLast
- `6e665b4` feat: add get with boundary check
- `65889b3` feat: add insert with head, tail, and middle cases
- `c4ef992` feat: add remove for first element
- `9807cb8` feat: add remove for middle and last element
- `18b1e94` feat: add search with step counter
- `7d0bdef` feat: add contains based on search
- `9e40b05` feat: add clear
- `fbd0136` feat: add validateInvariant
- `a1e2df6` feat: implement StrukturData interface and add
- `50e23ea` feat: add package-private getHead accessor to SinglyLinkedList

Pencarian, pengurutan, dan agregasi:
- `3513609` feat: add PutusanSearch with Hasil holding putusan, posisi, and langkah
- `7162d5d` feat: add cariByCaseId with step counting on SinglyLinkedList
- `640947a` feat: add cariByNomorPerkara with step counting
- `d671492` feat: add PutusanSorter with stable merge sort by vonisBulan
- `a60f872` feat: add PutusanAggregator with JenisCount for narcotics distribution
- `700a589` feat: add Ringkasan with jumlah perkara, rata-rata vonis, and total denda
- `f56d8ba` feat: add narcotics distribution using custom SinglyLinkedList

Input dan menu CLI:
- `b0b3ba7` feat(input): tambah kerangka InputHelper dan bacaBaris
- `700dabc` feat(input): tambah pembacaan angka, pilihan, teks, dan y/n yang aman
- `6eb937c` feat(input): tambah pembacaan opsional tanggal dan angka sesuai ADR-003
- `e71835d` feat(main): tambah kerangka menu CLI dan loop utama yang tahan input salah
- `4eb5735` feat(main): tambah menu impor CSV dan tampil
- `e27be9b` feat(main): tambah menu tambah dengan insert asli SinglyLinkedList
- `47fdc7b` feat(main): tambah menu hapus dengan konfirmasi dan validasi invariant
- `4dbc9ae` feat(main): tambah menu cari caseId dan nomorPerkara
- `48d4a24` feat(main): tambah menu filter dan agregasi
- `0974b10` feat(main): tambah menu urutan berdasarkan vonis
- `b2f7934` feat(main): tambah menu audit dengan pencarian riwayat
- `3061ead` feat(main): tambah menu ekspor CSV, laporan, dan hasil cari

CsvImporter (dikerjakan bersama, di-commit dari akun Adjie):
- `3ecb7c9` feat(CsvImporter): kerangka class, penampung error, dan pembukaan file
- `d569c60` feat(CsvImporter): baca file baris demi baris dan lewati header
- `ad8b9a8` feat(CsvImporter): parser CSV manual untuk kutip dan koma di dalam kutip
- `5107fa9` feat(CsvImporter): ubah baris CSV menjadi Putusan dengan validasi field
- `1e829d9` feat(CsvImporter): tolak record tidak valid dengan nomor baris dan alasan
- `5a79f33` feat(CsvImporter): validasi invariant list setelah impor

Pengujian:
- `03552ae`, `3358534`, `d9d4f4b`, `53ebe43`, `00f969f`: test addFirst/addLast, get, insert, remove
- `d1185e1`, `494ff5c`, `3a8c19c`: test search, contains, clear
- `3b11abc` test: add invariant checks after every operation
- `7d445bd` test: add test for StrukturData contract
- `1cd1ad1`, `750bfb9`, `83bd4d3`, `60b4b84`: test getHead, cariByCaseId, cariByNomorPerkara, kunci null
- `581a2aa`, `ef2bbe0`, `55cf29e`: test sorter (kosong, tunggal, duplikat, urutan terbalik, stabil, list asli tidak berubah)
- `d6c58e5`, `47daa57`, `911123d`, `bd15976`: test JenisCount, agregasi, distribusi, sumber tidak berubah
- `f206a5b` test(input): uji input tidak valid dan input berakhir pada InputHelper
- `598a59e`, `9cdbdec`, `cdce48f`, `345169f`, `acb6bf7`, `97e2718`: test CsvImporter
- `a328765` test(scenario): kerangka ScenarioTest, fixture CSV, dan helper bersama
- `7071af7`, `2edfc6d`, `951e3ff`: skenario 2 (cari), 3 (hapus + bukti pointer), 4 (tambah kembali)
- `ac92ca7`, `f55a388`, `1880601`: skenario 1 (muat 50 record), 5 (filter gabungan, pembanding independen, provenance manual)
- `30fa125`, `a7039f9`: skenario 6 (pencarian Stack tidak mengubah urutan), 7 (record tidak valid)

Build dan perbaikan:
- `66099cf` build: add surefire plugin for JUnit 5
- `4cda8ea` chore: remove accidental duplicate classes from src/test
- `e121850` fix(data): ubah format tanggalPutusan ke yyyy-MM-dd sesuai ADR-003
- `3a98ab5`, `9f7673a`, `8d29ee5`: tiga perbaikan menu CLI (lihat bagian bug)

### Data (provenance)
- P001-P025 (25 putusan PN Batam), masuk lewat PR #36 (`f68aa59`).
- Berkas provenance: 12 field per putusan, jadi 300 baris. Tiap baris memuat `source_file`, `source_page`, `extractor` (Adjie), `reviewer` (Adinda), dan `status`.
- Status: 5 baris `not_found` (denda P003, P018, P020; keadaan meringankan P018, P020) dan 6 baris `mixed_types` (jenis narkotika dan berat pada P012, P019, P020). Sisanya `extracted`.
- Kasus khusus: pidana mati dan seumur hidup dicatat `0` pada kolom bulan dengan keterangan. Selisih angka dan terbilang pada denda (P023, P025) memakai terbilang.
- Format `tanggalPutusan` pada CSV diseragamkan ke yyyy-MM-dd lewat PR #37.
- Hasil pemeriksaan provenance manual untuk skenario 5 tercatat di commit `1880601`.

### Bug yang ditemukan dan diperbaiki
- Commit awal `Node` dan `SinglyLinkedList` terkirim kosong, dan ada salinan class nyasar di `src/test`. Ditemukan lewat `git status`/`git log`, diperbaiki lewat commit baru `4cda8ea` tanpa rebase.
- Perubahan `pom.xml` yang rusak (tag `</version` kehilangan `>`) dibatalkan dengan `git restore`, lalu plugin surefire ditambahkan dengan benar di `66099cf`.
- Format `tanggalPutusan` pada data tidak sesuai ADR-003. Diseragamkan ke yyyy-MM-dd di `e121850` (PR #37).
- Pencarian riwayat pada riwayat kosong tidak tercatat di audit. Diperbaiki di `3a98ab5`.
- Hasil filter dan pencarian terakhir tidak direset setelah tambah dan hapus, sehingga bisa menampilkan data usang. Diperbaiki di `9f7673a`.
- Pesan error kosong tidak informatif. Sekarang menampilkan nama exception, diperbaiki di `8d29ee5`.

## Adinda

### Issue yang dikerjakan
| Issue | Judul | Diselesaikan lewat |
|---|---|---|
| [#2](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/2) | Putusan | PR #26 |
| [#4](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/4) | Ekstraksi putusan 26-50 | PR #40 |
| [#6](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/6) | Stack generik untuk audit | PR #32 |
| [#21](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/21) | Impor CSV | PR #38, #41 |
| [#22](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/22) | Tampil + Filter | PR #33 |
| [#23](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/23) | Audit + Ekspor | PR #34 |
| [#24](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/24) | Test 7 skenario + trace (bersama Adjie) | PR #43, #44 |

### Pull Request yang dibuat
| PR | Judul / isi | Jenis | Status |
|---|---|---|---|
| #26 | Feature/putusan class: Putusan dengan 16 field, getter, toString | Kode | Merged |
| #27 | docs: update AI usage log | Dokumentasi | Merged |
| #32 | Feature/stack audit: Stack generik, searchPreserve, validateInvariant, ADR pemetaan method | Kode | Merged |
| #33 | Feature/putusan display filter: PutusanDisplay dan PutusanFilter | Kode | Merged |
| #34 | Feature/audit exporter: AuditLog dan Exporter (CSV, laporan, riwayat) | Kode | Merged |
| #35 | docs: add AI usage entries for 2026-11-12 (Adinda) | Dokumentasi | Merged |
| #38 | Feature/csv importer: CsvImporter dan test (commit dari akun Adjie) | Kode | Merged |
| #39 | docs(AI_USAGE): catat penggunaan AI 2026-11-14 | Dokumentasi | Merged |
| #40 | Ekstraksi putusan P026-P050 PN Batam beserta provenance; penyesuaian format tanggal di putusan-50 | Data | Merged |
| #41 | test(CsvImporterTest): sesuaikan jumlah record dari 25 menjadi 50 | Pengujian | Merged |

Adinda juga me-merge PR #43 dan #45 milik Adjie.

### Review yang diberikan
Seluruh PR Adjie direview dan diberi status Approved: #7-#16, #25, #28-#31, #36, #37, #42-#45.

| PR yang direview | Isi PR | Status |
|---|---|---|
| [#28](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/28) | PutusanSearch | Approved |
| [#29](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/29) | PutusanSorter | Approved |
| [#30](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/30) | PutusanAggregator | Approved |
| [#36](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/36) | Ekstraksi P001-P025 | Approved |
| [#37](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/37) | Perbaikan format tanggal | Approved |
| [#42](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/42) | Menu CLI + InputHelper | Approved |
| [#44](https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/pull/44) | Test 7 skenario | Approved |

Komentar utama pada review PR #42 dan #44 (paling banyak diskusi):
- #42: menanyakan mengapa `catat()` di menu memakai `switch`, bukan langsung `audit.catat(...)`. Jawaban Adjie: `AuditLog` hanya punya method publik untuk TAMBAH, HAPUS, CARI, dan FILTER, sedangkan `catatOperasi` bersifat private, dan `AuditLog` sengaja tidak diubah agar tidak menyentuh class dari issue lain. Enam jenis sisanya dicatat lewat `audit.catat(...)` dengan format yang sama. Kekurangannya, formatter waktu terduplikasi di `Main`.
- #44: menanyakan mengapa tes memakai `data/putusan-50.csv` asli, bukan data tiruan. Jawaban Adjie: disengaja agar skenario 1 benar-benar membuktikan 50 record asli termuat dan ikut gagal kalau dataset tidak lengkap. Konsekuensinya tes bergantung pada isi CSV, jadi perubahan dataset menuntut penyesuaian tes.

### Commit utama
`Putusan` (PR #26):
- `fe7dd75` feat: add Putusan with 16 private final fields and full constructor
- `fe3fddc` feat: add getters for caseId, nomorPerkara, pengadilan, tanggalPutusan, terdakwaKode
- `7b24ec2` feat: add getters for jenisNarkotika, beratBarangBuktiGram, and pasal
- `fa386ac` feat: add getters for tuntutanBulan, vonisBulan, dendaRupiah
- `f3bd41a` feat: add getters for barangBukti, keadaan, and sourcePages
- `bbdc324` feat: add concise toString with caseId, nomorPerkara, vonisBulan

`Stack` (PR #32):
- `b98564a` feat: add generic Stack with array storage and constructor
- `a31c08b` feat: add push with automatic resize
- `ddf9677` feat: add pop and peek with empty stack handling
- `deed58b` feat: add clear and contains
- `6977764` feat: add searchPreserve and restore
- `2d0f099` feat: add validateInvariant
- `a6e4609` docs: add ADR for Stack method mapping

Tampil dan filter (PR #33):
- `9dcfd4f` feat: add PutusanDisplay with summary of all putusan
- `796eadf` feat: add PutusanFilter skeleton and narkotika criterion
- `3c5cb45` feat: add berat range criterion
- `115005d` feat: add pasal terbukti criterion
- `d18d139` feat: add vonis range criterion
- `70fd375` feat: add combined filter summary with active criteria count

Audit dan ekspor (PR #34):
- `4322329` feat: add AuditLog skeleton with record using Stack
- `c3b35cf` feat: add timestamped entries for tambah, hapus, cari, filter
- `7fb7153` feat: add riwayat display that preserves stack order
- `8fbbc3b` feat: add Exporter with CSV escaping for putusan list
- `530b42b` feat: add Exporter for text report and audit history
- `da7461c` feat: handle file write failure without crash

Pengujian (satu commit `test:` untuk tiap fitur di atas):
- `6f92c08`, `8e035bb`, `e4026d8`, `68c4bd8`, `a2cbb5b`, `d85f50c`: test Putusan (16 field, getter, nilai null, denda long, toString)
- `4517ecd`, `26c2e42`, `4d37e1a`, `db77fac`, `5507df7`, `c3e38d3`: test Stack (ukuran, resize, penolakan null, LIFO, batas kosong, urutan tidak berubah, invariant)
- `aef2828`, `54b6ea5`, `17618a5`, `f0d3b28`, `922b79f`, `ad47684`: test Display dan Filter (urutan tampil, list asli tidak berubah, batas nilai, hasil kosong, invariant)
- `4065879`, `a8a1a5e`, `5ddb4b5`, `30a7c94`, `e34a7b3`, `6261515`: test AuditLog dan Exporter (format entri, urutan Stack tidak berubah setelah riwayat, karakter khusus CSV, path tidak valid)

Perbaikan:
- `9d870b5` fix: revize PutusanFilter
- `268c8c2` fix: revize PutusanFilterTest
- `c2b35cf` fix: Update exporter.java in method tulis()

Dokumentasi dan merge:
- `55370b8` docs: update AI usage log
- `0a7f5c4` docs: add AI usage entries for 2026-11-12 (Adinda)
- Commit merge PR #38 (`ab10ed9`), #40 (`a91410d`), #41 (`334fe17`), dan #43 (`e6796e7`) dilakukan dari akun Adinda.

### Data (provenance)
- P026-P050 (25 putusan PN Batam), masuk lewat PR #40, extractor Adinda, reviewer Adjie.
- Berkas provenance: 13 field per putusan (ditambah `pengadilan`), jadi 325 baris. Digabung dengan P001-P025 menjadi 50 putusan di `data/putusan-50.csv`.
- Status: 2 baris `mixed_types` (P026), 1 baris `not_found` (berat P042, jenis "Happy Water" tanpa berat netto), 2 baris `bruto_only` (berat P044 dan P045, hanya bruto 495,9 g yang tidak dipakai sebagai netto). Sisanya `extracted`.
- Kasus khusus: P038 (etomidate) dan P042 (Happy Water) bukan jenis narkotika umum. P033, P034, dan P050 tidak menjatuhkan denda sehingga dicatat 0. P050 dijatuhi pidana seumur hidup sehingga tidak dikonversi ke bulan.
- Setelah digabung, jumlah record pada CsvImporterTest disesuaikan dari 25 menjadi 50 (PR #41).

### Bug yang ditemukan dan diperbaiki
- Filter berat dan vonis meloloskan putusan yang datanya tidak ditemukan. Menurut ADR-003 nilai 0 berarti data tidak ditemukan, tetapi `cocok()` memperlakukannya sebagai angka biasa, sehingga rentang yang mencakup 0 ikut meloloskan putusan tanpa data. Ditemukan lewat review Adjie di PR #33. Diperbaiki di `9d870b5` dengan syarat `berat <= 0` dan `vonis <= 0` langsung dikecualikan, ditambah penyempurnaan dokumentasi dan format kode. Test disesuaikan di `268c8c2`.
- `Exporter.tulis()` hanya menangkap `IOException`, sehingga kegagalan akses file berupa `SecurityException` lolos dan bisa menghentikan program. Ditemukan lewat review Adjie di PR #34. Diperbaiki di `c2b35cf` dengan menangkap `IOException | SecurityException`, mengembalikan `false`, dan menyimpan pesan di `getLastError()`.
- Jumlah record pada CsvImporterTest masih 25 setelah data bertambah menjadi 50, diperbaiki di PR #41.

## Catatan
- Disusun dari riwayat Git dan GitHub (issue, PR, commit, review) serta berkas provenance, lalu diverifikasi oleh kedua anggota.
- PR #38, #40, dan #41 dibuka dari akun Adinda, sedangkan commit di dalamnya tercatat dari akun Adjie. Pekerjaannya dibagi sesuai tabel di atas.
- Penggunaan AI tercatat di AI_USAGE.md.
