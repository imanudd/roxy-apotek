# Sequence Diagram - Dashboard

```mermaid
sequenceDiagram
    participant Admin
    participant HalamanDashboard
    participant Sistem

    Admin->>HalamanDashboard: Akses dashboard
    HalamanDashboard->>Sistem: Cek login
    
    alt Sudah login
        HalamanDashboard->>Sistem: Minta data stok
        Sistem->>Sistem: Ambil data stok
        Sistem-->>HalamanDashboard: Kirim data stok
        
        HalamanDashboard->>Sistem: Minta data transaksi
        Sistem->>Sistem: Ambil data transaksi
        Sistem-->>HalamanDashboard: Kirim data transaksi
        
        HalamanDashboard->>HalamanDashboard: Buat grafik stok bulanan
        HalamanDashboard->>HalamanDashboard: Buat grafik penjualan
        HalamanDashboard->>HalamanDashboard: Buat grafik jenis produk
        HalamanDashboard->>HalamanDashboard: Tampilkan tabel data
        HalamanDashboard-->>Admin: Menampilkan dashboard
    else Belum login
        HalamanDashboard-->>Admin: Kembali ke halaman login
    end
```

