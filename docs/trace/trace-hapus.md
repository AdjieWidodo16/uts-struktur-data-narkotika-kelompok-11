# Trace manual hapus: awal, tengah, akhir

Struktur: `SinglyLinkedList<Putusan>` dengan field `head`, `tail`, `size`.
Data: 50 putusan hasil impor `data/putusan-50.csv`.
Sumber caseId: `docs/trace/keluaran-skenario-3.txt`.

## Kondisi awal (size = 50)

```
head -> [P001] -> [P002] -> [P003] -> ... -> [P050] -> null
                                                   ^ tail
```

## 1. Hapus pertama: `remove(0)`

Kode: simpan `head.data`, `head = head.next`, `size--` (jika `head == null` maka `tail = null`).

```
Sebelum: head -> [P001] -> [P002] -> [P003] -> ...
Sesudah: head -> [P002] -> [P003] -> ...        (node <id0> tidak terjangkau)
```

- Pointer berubah: `head` pindah dari node `P001` ke node `P002`.
- `tail` tidak berubah. `size` 50 -> 49.
- Langkah traversal: 0. Kompleksitas **O(1)**.

## 2. Hapus tengah: `remove(k)` dengan k = <k> (size 49)

Kode: cari `prev` di posisi k-1 dengan loop, `prev.next = target.next`, `size--`.

```
Sebelum: ... -> [P025] -> [P026] -> [P027] -> ...
Sesudah: ... -> [P025] ---------------> [P027] -> ...   (node <id_target> terlepas)
```

- Pointer berubah: `prev.next` dari `<id_target>` ke `<id_after>`. `head` dan `tail` tidak berubah.
- `size` 49 -> 48.
- Langkah traversal: k-1 = <k-1> kali maju. Kompleksitas **O(n)**.

## 3. Hapus terakhir: `remove(size-1)` (size 48)

Kode: cari `prev` di posisi size-2, `prev.next = target.next` (null), karena `target == tail` maka `tail = prev`, `size--`.

```
Sebelum: ... -> [P049 -> [P050] -> null
                             ^ tail
Sesudah: ... -> [P049] -> null
                  ^ tail                          (node <id_akhir> terlepas)
```

- Pointer berubah: `prev.next` menjadi `null` dan `tail` mundur ke `P049`.
- `size` 48 -> 47.
- Bukti tail benar: `addLast` berikutnya menyambung ke `P049` (diperiksa di tes).
- Langkah traversal: size-2 = 46 kali maju. Kompleksitas **O(n)**.

## Ringkasan

| Operasi | Pointer yang berubah | Langkah traversal | Kompleksitas |
|---|---|---|---|
| remove awal | head | 0 | O(1) |
| remove tengah | prev.next | k-1 | O(n) |
| remove akhir | prev.next, tail | n-2 | O(n) |

Setiap operasi diverifikasi `validateInvariant()`.