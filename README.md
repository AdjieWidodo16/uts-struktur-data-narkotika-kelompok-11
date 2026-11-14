# UTS Struktur Data – Putusan Pengadilan Narkotika (Kelompok 11)

Aplikasi Java berorientasi objek untuk menyimpan dan mengolah 50 putusan pengadilan
narkotika PN Batam menggunakan struktur data buatan sendiri (tanpa koleksi `java.util`).

Mata kuliah : Struktur Data D Semester Ganjil 2026/2027
Dosen pengampu: 	Ir. Galih Wasis Wicaksono, S.kom. M.Cs.

## Identitas Kelompok

| Nama | NIM | Akun GitHub | Peran |
|------|-----|-------------|-------|
| Dzikri Ngesti Adjie Widodo | 202510370110102 | https://github.com/AdjieWidodo16 | Interface, Node, SinglyLinkedList, PutusanSearch, PutusanSorter, PutusanAggregator, InputHelper, Menu CLI |
| Adinda Mayditia Susanti | 202510370110071 | https://github.com/adindasusan | Putusan, Stack, AuditLog, PutusanDisplay, PutusanFilter, Exporter|

## Struktur Data yang Dipilih

| Peran | Struktur | Class | Keterangan |
|-------|----------|-------|------------|
| Utama | Singly Linked List generik | `SinglyLinkedList<T>` | Menyimpan seluruh objek `Putusan`; memakai pointer `head` dan `tail` |
| Sekunder | Stack generik berbasis array | `Stack<T>` (dibungkus `AuditLog`) | Merekam riwayat operasi pengguna (audit) |

`SinglyLinkedList<T>` mengimplementasikan interface `StrukturData<T>` (`add`, `get`, `remove`, `size`,
`isEmpty`, `contains`, `clear`). `Stack<T>` tidak mengimplementasikan interface tersebut, tetapi
menyediakan operasi yang setara: `push`, `pop`, `peek`, `contains`, `size`, `isEmpty`, `clear`.

Alasan pemilihan: lihat [ADR-001-pemilihan-struktur.md](docs/adr/ADR-001-pemilihan-struktur.md)
Strategi pencarian, filter, dan pengurutan: lihat [ADR-002-strategi-pencarian-pengurutan.md](docs/adr/ADR-002-strategi-pencarian-pengurutan.md)
Aturan ekstraksi dan format dataset: lihat [ADR-003-aturan-ekstraksi-dan-format-dataset.md](docs/adr/ADR-003-aturan-ekstraksi-dan-format-dataset.md)
Pemetaan method Stack audit: lihat [ADR-004-stack-audit-method-mapping.md](docs/adr/ADR-004-stack-audit-method-mapping.md)

### Kompleksitas operasi struktur utama (`SinglyLinkedList<T>`)

| Operasi | Kompleksitas | Keterangan |
|---------|--------------|------------|
| `addFirst` | O(1) | Hanya mengubah `head` |
| `addLast` / `add` | O(1) | Memakai pointer `tail` |
| `get(i)` | O(n) | Menelusuri dari `head` |
| `insert(i, x)` | O(1) di posisi 0 dan akhir, O(n) di tengah | Posisi 0 → `addFirst`, posisi `size` → `addLast`, selain itu mencari node sebelum posisi i |
| `remove(i)` | O(1) di posisi 0, selain itu O(n) | Mencari node sebelum posisi i; `tail` diperbarui bila node terakhir dihapus |
| `search` / `contains` | O(n) | Sequential search dengan pencacah langkah (`getLastSearchSteps`) |
| `clear` | O(1) | `head`, `tail` di-null-kan |
| `validateInvariant` | O(n) | Memeriksa `size`, `head`, `tail`, `tail.next`, dan jumlah node |

### Kompleksitas operasi layanan

| Fitur | Kompleksitas | Keterangan |
|-------|--------------|------------|
| Tampil (menu 2) | O(n) | Menelusuri node dari `head` langsung (`PutusanDisplay`), tidak memakai `get(i)` |
| Cari (menu 5) | O(n) | Menelusuri node dengan pencacah langkah (`PutusanSearch`) |
| Filter (menu 6) | O(n) | Satu kali penelusuran, hasil ke list baru; kriteria digabung dengan logika AND |
| Agregasi (menu 7) | O(n·k) | k = jumlah jenis narkotika berbeda; tiap putusan mencari jenisnya di list distribusi |
| Urutan (menu 8) | O(n log n) | Merge sort buatan sendiri, stabil, menghasilkan list baru |
| Ekspor CSV (menu 10) | O(n) | Satu kali penelusuran |

### Kompleksitas operasi struktur sekunder (`Stack<T>`)

| Operasi | Kompleksitas | Keterangan |
|---------|--------------|------------|
| `push` | O(1) rata-rata | O(n) saat `resize()` menyalin array ke kapasitas 2× (kapasitas awal 10) |
| `pop`, `peek` | O(1) | Akses langsung ke `data[size - 1]` |
| `contains` | O(n) | Menelusuri dari puncak ke dasar |
| `searchPreserve` | O(n) | Pop ke array penampung `holding`, berhenti saat ditemukan, lalu `restore()` |
| `clear` | O(n) | Mengosongkan semua slot |
| `validateInvariant` | O(kapasitas) | Memeriksa ukuran, elemen aktif non-null, slot di atas puncak null, dan tidak ada elemen tertahan |

`AuditLog` menampilkan riwayat dengan memindahkan entri ke stack sementara lalu mengembalikannya,
sehingga urutan akhir stack tidak berubah.

## Struktur Repository

```
uts-struktur-data-narkotika-kelompok-11/
|-- src/main/java/id/ac/umm/strukturdata/
|   |-- Main.java                (menu konsol)
|   |-- InputHelper.java         (pembacaan dan validasi input)
|   |-- Putusan.java             (model data)
|   |-- Node.java
|   |-- StrukturData.java        (interface kontrak struktur data)
|   |-- SinglyLinkedList.java
|   |-- Stack.java
|   |-- AuditLog.java            (riwayat operasi di atas Stack)
|   |-- CsvImporter.java         (impor CSV dan validasi)
|   |-- PutusanSearch.java
|   |-- PutusanFilter.java
|   |-- PutusanSorter.java
|   |-- PutusanAggregator.java
|   |-- PutusanDisplay.java
|   `-- Exporter.java
|-- src/test/java/id/ac/umm/strukturdata/   (unit test JUnit 5)
|-- data/putusan-50.csv
|-- docs/
|   |-- adr/                     (ADR-001 s.d. ADR-004, keputusan desain)
|   |-- demo/                    (bukti demo: Scenario-1..7.png, keluaran teks skenario 2-5)
|   |-- trace/                   (trace manual: trace-hapus.md, pencacah-langkah.md, keluaran-skenario-3.txt)
|   `-- data-provenance.csv
|-- AI_USAGE.md
|-- CONTRIBUTIONS.md
|-- README.md
`-- pom.xml
```

## Cara Build, Test, dan Menjalankan

Kebutuhan: JDK 25 (`maven.compiler.release` = 25 di `pom.xml`), Maven 3.9+ (atau Maven bawaan IntelliJ IDEA). Dependensi hanya JUnit Jupiter 5.10.2 (scope test); `maven-compiler-plugin` 3.13.0 dan `maven-surefire-plugin` 3.5.2.

```bash
# build dan jalankan seluruh test
mvn clean test

# kompilasi lalu jalankan aplikasi dari folder root project
mvn clean compile
java -cp target/classes id.ac.umm.strukturdata.Main
```

Lewat IntelliJ: panel Maven → Lifecycle → `clean` lalu `test`; jalankan `Main.java` dengan tombol Run.

Aplikasi sebaiknya dijalankan dari folder root project karena path CSV bawaan relatif (`data/putusan-50.csv`).

### Menu aplikasi

| Menu | Fungsi | Operasi struktur |
|------|--------|------------------|
| 1 | Impor CSV (path bawaan `data/putusan-50.csv`, bisa diganti); menampilkan jumlah dibaca, valid, ditolak, dan daftar error per baris | `addLast` |
| 2 | Tampil ringkasan semua putusan sesuai urutan list | traversal `head` → `tail` |
| 3 | Tambah putusan: `caseId` wajib dan duplikat ditolak, kolom lain boleh dikosongkan; lalu pilih posisi sisip (0 = awal, size = akhir) | `insert` |
| 4 | Hapus berdasarkan posisi, dengan konfirmasi `y/n` | `remove` |
| 5 | Cari berdasarkan 1) `caseId` atau 2) `nomorPerkara`; menampilkan posisi dan jumlah langkah | sequential search |
| 6 | Filter gabungan: jenis narkotika, rentang berat, pasal terbukti, rentang vonis | `PutusanFilter` |
| 7 | Agregasi: jumlah perkara, rata-rata vonis, total denda, distribusi jenis narkotika | `PutusanAggregator` |
| 8 | Urutkan berdasarkan vonis (naik/turun) ke list baru; list utama tidak berubah | `PutusanSorter` (merge sort) |
| 9 | 1) Tampilkan riwayat audit (terbaru di atas) atau 2) cari entri di riwayat tanpa mengubah urutan stack | `AuditLog`, `searchPreserve` |
| 10 | Ekspor: 1) CSV data saat ini, 2) CSV hasil filter terakhir, 3) laporan (agregasi + riwayat audit), 4) hasil pencarian terakhir | `Exporter` |
| 0 | Keluar | |

Perilaku penting:

- Hasil filter dan hasil cari terakhir direset setelah impor atau tambah, agar ekspor tidak memakai data lama.
- Ekspor mode 2 dan 4 hanya bisa dipakai setelah menu Filter / Cari dijalankan.
- Operasi yang gagal (termasuk `RuntimeException`) tidak menghentikan program; pesan ditampilkan dan dicatat ke audit dengan awalan `GAGAL:`.
- Program berhenti dengan rapi jika input berakhir (`InputBerakhirException`).

### Contoh penggunaan

```
=== MENU PUTUSAN NARKOTIKA ===
 1. Impor CSV
 2. Tampil
 3. Tambah
 4. Hapus
 5. Cari
 6. Filter
 7. Agregasi
 8. Urutan (berdasarkan vonis)
 9. Audit
10. Ekspor
 0. Keluar
Data saat ini: 0 putusan
Pilih menu: 1
Path CSV (Enter = data/putusan-50.csv):
Dibaca : 50 baris
Valid  : 50 putusan (size = 50)
Ditolak: 0

Pilih menu: 5
Cari berdasarkan 1) caseId  2) nomorPerkara: 1
Kata kunci: P002
HASIL PENCARIAN
Ditemukan: ya
Posisi: 1
Langkah: 2
Putusan: Putusan{caseId=P002, nomorPerkara=1123/Pid.Sus/2025/PN Btm, vonisBulan=72}

Pilih menu: 9
1) Tampilkan riwayat  2) Cari entri di riwayat: 1
Riwayat audit: 2 entri (terbaru dulu)
1. [2026-11-15 03:13:34] CARI: caseId=P002, ditemukan=true, posisi=1, langkah=2
2. [2026-11-15 03:13:20] IMPOR: path=data/putusan-50.csv, dibaca=50, valid=50, ditolak=0
```

### Riwayat audit

Setiap operasi dicatat ke `AuditLog` dengan format:

```
[yyyy-MM-dd HH:mm:ss] JENIS: detail
```

Jenis yang dicatat: `IMPOR`, `TAMPIL`, `TAMBAH`, `HAPUS`, `CARI`, `FILTER`, `AGREGASI`, `URUTAN`,
`AUDIT`, `EKSPOR`. Operasi gagal atau ditolak dicatat dengan detail berawalan `GAGAL:` atau `DITOLAK`.

### Ekspor

- Path bawaan: `docs/demo/data.csv`, `docs/demo/hasil-filter.csv`, `docs/demo/laporan.txt`, `docs/demo/hasil-cari.txt`; path bisa diganti saat menu berjalan. Folder `docs/demo/` sudah disediakan lewat `.gitkeep`.
- CSV hasil ekspor memakai header 16 kolom dan pemisah koma yang sama dengan `data/putusan-50.csv`, sehingga dapat diimpor ulang.
- Nilai yang mengandung koma, tanda kutip, atau baris baru dibungkus tanda kutip (`"` menjadi `""`); nilai `null` ditulis kosong.
- Berat ditulis tanpa notasi ilmiah (mis. `10000000`, bukan `1.0E7`).
- Laporan teks berisi ringkasan putusan, agregasi, dan riwayat audit.

## Dataset

### Sumber dan kriteria

- Sumber: Direktori Putusan Mahkamah Agung, https://putusan3.mahkamahagung.go.id
- Pengadilan: **PN Batam** (`PN Btm`) untuk seluruh 50 putusan
- Kriteria: putusan tingkat pertama perkara narkotika, satu terdakwa per putusan, memuat dakwaan, tuntutan, pertimbangan, dan amar
- Berkas PDF sumber tidak diunggah ke repository (`*.pdf` ada di `.gitignore`). Identitas sumber dicatat melalui nomor perkara dan `docs/data-provenance.csv`
- Nama terdakwa disamarkan dengan kode `T001`–`T050`, termasuk di dalam teks `keadaanMemberatkan` dan `keadaanMeringankan`
- Pembagian ekstraksi: P001–P025 dan P026–P050 oleh dua anggota yang berbeda, dengan aturan bersama di [ADR-003](docs/adr/ADR-003-aturan-ekstraksi-dan-format-dataset.md)

### Format file

- `data/putusan-50.csv` memakai pemisah **koma (`,`)**; sel yang berisi koma dibungkus tanda kutip dua
- Beberapa nilai dalam satu sel dipisah titik koma (`;`), mis. `sabu;ganja`
- Encoding UTF-8, baris pertama adalah header, 16 kolom:
  `caseId, nomorPerkara, pengadilan, tanggalPutusan, terdakwaKode, jenisNarkotika, beratBarangBuktiGram,
  pasalDakwaan, pasalTerbukti, tuntutanBulan, vonisBulan, dendaRupiah, barangBukti, keadaanMemberatkan,
  keadaanMeringankan, sourcePages`

### Aturan validasi impor

| Kondisi | Perlakuan |
|---------|-----------|
| Baris kosong | Ditolak |
| Jumlah kolom bukan 16 | Ditolak |
| Tanda kutip tidak tertutup | Ditolak |
| `caseId` kosong | Ditolak |
| `tanggalPutusan` bukan `yyyy-MM-dd` | Ditolak |
| Angka (`berat`, `tuntutan`, `vonis`, `denda`) bukan angka atau negatif | Ditolak |
| Sel kosong atau berisi `null` | Teks → `null`, angka → `0`, tanggal → `null` (data tidak ditemukan, tidak diperkirakan) |

Baris yang ditolak dicatat sebagai `Baris N: alasan` dan impor tetap berlanjut ke baris berikutnya.

### Aturan pengisian atribut

| Atribut | Aturan |
|---------|--------|
| `tanggalPutusan` | `yyyy-MM-dd`, tanggal pengucapan putusan di sidang terbuka (bukan tanggal musyawarah) |
| `tuntutanBulan`, `vonisBulan` | Total dalam bulan (5 tahun 6 bulan = 66). Pidana mati atau seumur hidup tidak bisa dikonversi, ditulis `0` |
| `dendaRupiah` | Angka polos tanpa `Rp` dan pemisah ribuan; tanpa denda = `0` |
| `beratBarangBuktiGram` | Berat netto narkotika saja (HP, timbangan, uang diabaikan). Prioritas: netto hasil laboratorium, lalu netto penimbangan penyidik. Beberapa barang bukti dijumlahkan; angka mengikuti putusan (maks. 2 desimal) tanpa pembulatan sendiri |
| `jenisNarkotika` | Jenis lebih dari satu digabung dengan `;` |
| Nilai tidak ditemukan | Angka = `0`, teks = `null`; tidak diperkirakan |
| `sourcePages` | Nomor halaman footer "Hal. X dari Y" per kelompok atribut |
| `terdakwaKode` | `T001` dst. |

Kasus khusus berat barang bukti: hanya bruto yang tertulis → `0` dengan status `bruto_only`;
butir/tablet tanpa berat gram → `0`; berat tidak ditemukan → `0` dengan status `not_found`.

### Catatan khusus data

- Putusan dengan lebih dari satu jenis narkotika, berat dijumlahkan dan status provenance `mixed_types`: P012, P019, P020, P026
- Berat `0` karena hanya bruto yang tertulis (`bruto_only`): P044, P045
- Berat `0` karena tidak ditemukan (`not_found`): P042 (putusan hanya mencantumkan berat tanpa menyebut netto)
- Pidana mati atau seumur hidup tidak dikonversi ke bulan, sehingga `tuntutanBulan` = `0` pada P018, P020, P021–P025, P050 dan `vonisBulan` = `0` pada P018, P020, P050
- Denda: P023 dan P025 memuat angka di amar yang tidak sesuai dengan terbilangnya; nilai yang dipakai adalah terbilang
- Denda tidak ditemukan atau tidak dijatuhkan: `0` (mis. P003, P018, P020, P033, P034, P050)
- `keadaanMeringankan` tidak ditemukan di P018 dan P020 (`null`)
- P021–P025 memiliki berat barang bukti yang sama (20593.53 g)

Keputusan perilaku aplikasi yang berkaitan dengan data:

- Distribusi jenis pada menu Agregasi menghitung **nilai kolom `jenisNarkotika` apa adanya**; jenis gabungan seperti `sabu;ganja` dihitung sebagai satu kategori tersendiri.
- Rata-rata vonis dihitung dari **seluruh** putusan, termasuk yang `vonisBulan = 0` (pidana mati/seumur hidup atau tidak ditemukan), sehingga rata-rata cenderung lebih rendah dari rata-rata vonis penjara sebenarnya.
- Filter berat dan vonis **tidak meloloskan** nilai `0` (data tidak ditemukan).
- Filter berat pada putusan `mixed_types` memakai total semua zat; perlu dicek manual saat uji skenario 5.
- Filter jenis dan pasal memakai pencarian substring tanpa membedakan huruf besar/kecil.

### Provenance

`docs/data-provenance.csv` memuat satu baris per atribut per putusan dengan kolom
`case_id, field_name, value, source_file, source_page, extractor, reviewer, status`.
Anggota pengekstrak (`extractor`) dan pemeriksa silang (`reviewer`) dicatat per baris.

| Status | Arti |
|--------|------|
| `extracted` | Nilai sudah diekstrak dari PDF |
| `verified` | Nilai sudah dicocokkan pemeriksa dengan PDF |
| `bruto_only` | Hanya berat bruto yang tertulis, berat netto diisi 0 |
| `not_found` | Nilai tidak ditemukan di putusan |
| `mixed_types` | Lebih dari satu jenis narkotika, berat dijumlahkan |

## Skenario Uji

Tujuh skenario wajib ada di `ScenarioTest`. Keluaran terminalnya disimpan sebagai bukti di `docs/demo/`.

| No | Skenario | Test | Bukti |
|----|----------|------|-------|
| 1 | Muat 50 record, size = 50, caseId unik | `ScenarioTest.skenario1_muat50Record_sizeSama50` | `docs/demo/Scenario-1.png` |
| 2 | Cari perkara yang ada, tidak ada, dan di posisi batas | `ScenarioTest.skenario2_cari_ada_tidakAda_dan_posisiBatas` | `Scenario-2.png`, `skenario-2-cari.txt`, [pencacah-langkah.md](docs/trace/pencacah-langkah.md) |
| 3 | Hapus elemen pertama, tengah, terakhir dan tunjukkan perubahan pointer | `ScenarioTest.skenario3_hapus_pertama_tengah_terakhir_dan_perubahanPointer` | `Scenario-3.png`, `skenario-3-hapus.txt`, [trace-hapus.md](docs/trace/trace-hapus.md), `keluaran-skenario-3.txt` |
| 4 | Tambah kembali tiga record, cek invariant dan urutan | `ScenarioTest.skenario4_tambahKembaliTigaRecord_invariantDanUrutan` | `Scenario-4.png`, `skenario-4-tambah-kembali.txt` |
| 5 | Filter gabungan dicocokkan dengan pemeriksaan manual | `ScenarioTest.skenario5_filterGabungan_cocokDenganPemeriksaanManual` | `Scenario-5.png`, [skenario-5-pemeriksaan-manuam.md](docs/demo/skenario-5-pemeriksaan-manuam.md) |
| 6 | Pencarian pada Stack tidak mengubah urutan akhir (`Stack` dan `AuditLog`) | `ScenarioTest.skenario6_pencarianStack_tidakMengubahUrutanAkhir` | `Scenario-6.png` |
| 7 | Record tidak valid ditolak dengan pesan jelas, struktur tetap konsisten | `ScenarioTest.skenario7_recordTidakValid_pesanKesalahanDanStrukturKonsisten` | `Scenario-7.png` |

### Test per komponen

| Class test | Komponen yang diuji |
|------------|---------------------|
| `SinglyLinkedListTest` | Struktur utama |
| `StackTest` | Struktur sekunder |
| `AuditLogTest` | Riwayat audit |
| `CsvImporterTest` | Impor dan validasi CSV |
| `PutusanTest` | Model data |
| `PutusanSearchTest` | Pencarian |
| `PutusanFilterTest` | Filter |
| `PutusanSorterTest` | Pengurutan |
| `PutusanAggregatorTest` | Agregasi |
| `PutusanDisplayTest` | Tampilan ringkasan |
| `ExporterTest` | Ekspor |
| `InputHelperTest` | Pembacaan input |

Hasil `mvn clean test`: Tests run: 196, Failures: 0, Errors: 0, Skipped: 0

## Kolaborasi dan Transparansi

- Alur kerja: Issue → branch → commit kecil → Pull Request → review anggota lain → merge
- Kontribusi tiap anggota: [CONTRIBUTIONS.md](CONTRIBUTIONS.md)
- Penggunaan AI: [AI_USAGE.md](AI_USAGE.md)
- Versi final: tag UTS-1.0
