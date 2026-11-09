package id.ac.umm.strukturdata;

import java.time.LocalDate;

/**
 * Satu putusan narkotika (skema minimum Panduan UTS 2.2).
 * Nilai tidak ditemukan: angka = 0, teks/tanggal = null (tidak diperkirakan).
 */
public class Putusan {
    private final String caseId;
    private final String nomorPerkara;
    private final String pengadilan;
    private final LocalDate tanggalPutusan;
    private final String terdakwaKode;          // kode anonim, bukan nama asli
    private final String jenisNarkotika;
    private final double beratBarangBuktiGram;  // berat netto
    private final String pasalDakwaan;
    private final String pasalTerbukti;
    private final int tuntutanBulan;
    private final int vonisBulan;
    private final long dendaRupiah;
    private final String barangBukti;
    private final String keadaanMemberatkan;
    private final String keadaanMeringankan;
    private final String sourcePages;

    public Putusan(String caseId, String nomorPerkara, String pengadilan,
                   LocalDate tanggalPutusan, String terdakwaKode,
                   String jenisNarkotika, double beratBarangBuktiGram,
                   String pasalDakwaan, String pasalTerbukti,
                   int tuntutanBulan, int vonisBulan, long dendaRupiah,
                   String barangBukti, String keadaanMemberatkan,
                   String keadaanMeringankan, String sourcePages) {
        this.caseId = caseId;
        this.nomorPerkara = nomorPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.terdakwaKode = terdakwaKode;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.pasalDakwaan = pasalDakwaan;
        this.pasalTerbukti = pasalTerbukti;
        this.tuntutanBulan = tuntutanBulan;
        this.vonisBulan = vonisBulan;
        this.dendaRupiah = dendaRupiah;
        this.barangBukti = barangBukti;
        this.keadaanMemberatkan = keadaanMemberatkan;
        this.keadaanMeringankan = keadaanMeringankan;
        this.sourcePages = sourcePages;
    }

    public String getCaseId() { return caseId; }
    public String getNomorPerkara() { return nomorPerkara; }
    public String getPengadilan() { return pengadilan; }
    public LocalDate getTanggalPutusan() { return tanggalPutusan; }
    public String getTerdakwaKode() { return terdakwaKode; }


    public String getJenisNarkotika() { return jenisNarkotika; }
    public double getBeratBarangBuktiGram() { return beratBarangBuktiGram; }
    public String getPasalDakwaan() { return pasalDakwaan; }
    public String getPasalTerbukti() { return pasalTerbukti; }


    public int getTuntutanBulan() { return tuntutanBulan; }
    public int getVonisBulan() { return vonisBulan; }
    public long getDendaRupiah() { return dendaRupiah; }
}