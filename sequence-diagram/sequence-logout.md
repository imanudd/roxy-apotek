# Sequence Diagram - Logout

```mermaid
sequenceDiagram
    participant Admin
    participant HalamanMenu
    participant Sistem

    Admin->>HalamanMenu: Klik tombol logout
    HalamanMenu->>Sistem: Cek login
    
    alt Sudah login
        HalamanMenu->>Sistem: Hapus session user
        Sistem->>Sistem: Hapus data session
        Sistem-->>HalamanMenu: Session dihapus
        HalamanMenu->>HalamanMenu: Tutup menu
        HalamanMenu->>HalamanMenu: Tampilkan halaman login
        HalamanMenu-->>Admin: Menampilkan halaman login
    else Belum login
        HalamanMenu-->>Admin: Tidak ada aksi
    end
```
