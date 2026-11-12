# ADR-004: Stack generik untuk Audit dan pemetaan method ke StrukturData<T>

## Status
Diterima

## Konteks
Fitur Audit merekam operasi pengguna pada struktur sekunder. Stack dipilih karena
riwayat paling baru harus bisa dilihat/dibatalkan lebih dulu (LIFO).
Kontrak umum `StrukturData<T>` memiliki `get(int)` dan `remove(int)` yang mengakses
posisi sembarang, sehingga bertentangan dengan LIFO.

## Keputusan
1. `Stack<T>` dibuat dari nol dengan array `T[]` (kapasitas awal 10, digandakan saat penuh)
   dan tidak meng-`implements StrukturData<T>`.
2. Pemetaan operasi:

| StrukturData<T> | Stack<T>  | Alasan penyimpangan                          |
|-----------------|-----------|----------------------------------------------|
| add(T)          | push(T)   | penamaan standar stack; selalu ke puncak     |
| remove(int)     | pop()     | hanya elemen puncak yang boleh dihapus       |
| get(int)        | peek()    | hanya elemen puncak yang boleh dilihat       |
| size/isEmpty/clear/contains | sama | tidak ada perubahan                  |

3. Operasi tambahan: `searchPreserve(T)` dan `restore()` (pencarian non-destruktif),
   `validateInvariant()`.
4. `pop`/`peek` pada stack kosong melempar `IllegalStateException` dengan pesan jelas.
5. Elemen `null` ditolak agar invariant dapat diperiksa.

## Konsekuensi
- push amortized O(1), pop/peek O(1), searchPreserve O(n) waktu dan O(n) ruang tambahan.
- Stack tidak bisa dipakai di tempat yang membutuhkan tipe `StrukturData<T>`.