# Activity Diagram - Data Brand

```mermaid
flowchart TD
    subgraph Admin["👤 Admin"]
        Start([Mulai]) --> PilihMenu[Pilih menu Data Brand]
        PilihMenu --> PilihAksi{Pilih aksi}
        PilihAksi -->|Tambah| InputData[Masukkan data brand]
        PilihAksi -->|Ubah| PilihUbah[Pilih brand yang akan diubah]
        PilihAksi -->|Hapus| PilihHapus[Pilih brand yang akan dihapus]
        PilihAksi -->|Lihat| LihatData[Lihat daftar brand]
        PilihAksi -->|Export| ExportData[Export data brand]
        InputData --> TerimaHasil[Menerima hasil]
        PilihUbah --> InputUbah[Masukkan data baru]
        InputUbah --> TerimaHasil
        PilihHapus --> TerimaHasil
        LihatData --> TerimaHasil
        ExportData --> TerimaHasil
        TerimaHasil --> End([Selesai])
    end
    
    subgraph System["⚙️ Sistem"]
        TampilMenu[Menampilkan menu Data Brand]
        CekData{Validasi data}
        SimpanData[Simpan data brand]
        AmbilData[Ambil data brand]
        CekAda{Brand ditemukan?}
        CekStatus{Status aktif?}
        UpdateData[Update data brand]
        CekItem{Ada item terkait?}
        HapusItem[Hapus item terkait]
        HapusData[Hapus brand]
        TampilTabel[Menampilkan tabel brand]
        BuatExcel[Buat file Excel]
        TampilHasil[Menampilkan hasil]
    end
    
    PilihMenu --> TampilMenu
    TampilMenu --> PilihAksi
    InputData --> CekData
    CekData -->|Tidak valid| TampilHasil
    CekData -->|Valid| SimpanData
    SimpanData --> TampilHasil
    PilihUbah --> AmbilData
    AmbilData --> CekAda
    CekAda -->|Tidak| TampilHasil
    CekAda -->|Ya| CekStatus
    CekStatus -->|Tidak aktif| TampilHasil
    CekStatus -->|Aktif| InputUbah
    InputUbah --> CekData
    CekData --> UpdateData
    UpdateData --> TampilHasil
    PilihHapus --> AmbilData
    AmbilData --> CekAda
    CekAda --> CekItem
    CekItem -->|Ya| HapusItem
    HapusItem --> HapusData
    CekItem -->|Tidak| HapusData
    HapusData --> TampilHasil
    LihatData --> AmbilData
    AmbilData --> TampilTabel
    TampilTabel --> TerimaHasil
    ExportData --> AmbilData
    AmbilData --> BuatExcel
    BuatExcel --> TampilHasil
    TampilHasil --> TerimaHasil
```
