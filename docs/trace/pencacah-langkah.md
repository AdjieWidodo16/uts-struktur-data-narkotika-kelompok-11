# Pencacah langkah pencarian (n = 50)

| Kasus | Posisi | Langkah |
|---|---|---|
| Awal (terbaik) | 0 | 1 |
| Seperempat | 12 | 13 |
| Tengah | 25 | 26 |
| Tigaperempat | 37 | 38 |
| Akhir (terburuk jika ada) | 49 | 50 |
| Tidak ada | -1 | 50 |

Kesimpulan: langkah = posisi + 1 untuk data yang ada dan = n untuk data yang tidak ada,
jadi pencarian linear pada linked list berkompleksitas **O(n)** (O(1) di kasus terbaik).