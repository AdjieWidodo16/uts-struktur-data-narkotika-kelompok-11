# CONTRIBUTIONS

Kelompok 11, Struktur Data (Semester Ganjil 2026/2027).
https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11.git

## Ringkasan pembagian
| Area | Adjie | Adinda |
|---|---|---|
| Ekstraksi data | P001-P025 (dalam proses) | P026-P050 |
| Implementasi | Interface, Node, SinglyLinkedList (search, contains, clear, validateInvariant), pencarian, urutan | Putusan, Stack, CsvImporter, filter, agregasi, audit, ekspor, menu |
| Pengujian | SinglyLinkedListTest (JUnit 5, 22 test) | Test Stack, importer, filter |
| Dokumentasi | ADR-001, ADR-002, AI_USAGE, CONTRIBUTIONS | README, trace, demo |                                                |

## Adjie

### Issue yang dikerjakan
- #1 Interface `StrukturData<T>`: (diselesaikan lewat PR #7): https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/1
- #3 Ekstraksi putusan 1-25 (dalam proses) : https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/3
  #5 `SinglyLinkedList` (diselesaikan lewat PR #12) : https://github.com/AdjieWidodo16/uts-struktur-data-narkotika-kelompok-11/issues/5

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

### Review yang diberikan
| PR yang direview | Komentar utama | Link |
|------------------|----------------|------|
| -                | -              | -    |

### Commit utama
Interface dan dokumentasi:
- `02bccaf` feat: add StrukturData<T> interface
- `7288e82` docs: clarify tail pointer complexity in ADR-001
- `f557737` docs: update AI usage log

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

Pengujian:
- `03552ae` test: add tests for addFirst and addLast
- `3358534` test: add tests for get order and boundary
- `d9d4f4b` test: add tests for insert at head, middle, tail, and bounds
- `53ebe43` test: add tests for remove first and single element
- `00f969f` test: add tests for remove middle, last, and bounds
- `d1185e1` test: add tests for search result and step count
- `494ff5c` test: add test for contains
- `3a8c19c` test: add test for clear
- `3b11abc` test: add invariant checks after every operation
- `7d445bd` test: add test for StrukturData contract

Build dan perbaikan:
- `66099cf` build: add surefire plugin for JUnit 5
- `4cda8ea` chore: remove accidental duplicate classes from src/test

### Data (provenance)
- Belum ada baris data. Ekstraksi P001-P025 sedang berjalan. (Akan perbarui setelah PR data pertama di-merge.)

### Bug yang ditemukan dan diperbaiki
- Commit awal `Node` dan `SinglyLinkedList` terkirim kosong, dan ada salinan class nyasar di `src/test`. Ditemukan lewat `git status`/`git log`, diperbaiki lewat commit baru `4cda8ea` tanpa rebase.
- Perubahan `pom.xml` yang rusak (tag `</version` kehilangan `>`) dibatalkan dengan `git restore`, lalu plugin surefire ditambahkan dengan benar di `66099cf`.


## Adinda
(struktur sama: issue, PR, review, commit utama, data, bug)

## Catatan
- Tiap anggota menulis bagiannya sendiri.
- Penggunaan AI tercatat di AI_USAGE.md.