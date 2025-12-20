# Sequence Diagram - Login

```mermaid
sequenceDiagram
    participant Admin
    participant HalamanLogin
    participant Sistem

    Admin->>HalamanLogin: Akses aplikasi
    HalamanLogin-->>Admin: Menampilkan halaman login
    
    Admin->>HalamanLogin: Input email dan password
    HalamanLogin->>Sistem: Kirim email dan password
    
    Sistem->>Sistem: Cari data user
    Sistem->>Sistem: Cek email dan password
    
    alt Email dan password sesuai
        Sistem->>Sistem: Simpan session user
        Sistem-->>HalamanLogin: Login berhasil
        HalamanLogin->>HalamanLogin: Tampilkan pesan sukses
        HalamanLogin-->>Admin: Menampilkan halaman dashboard
    else Email atau password tidak sesuai
        Sistem-->>HalamanLogin: Login gagal
        HalamanLogin->>HalamanLogin: Tampilkan pesan error
        HalamanLogin-->>Admin: Tampilkan halaman login kembali
    end
```

