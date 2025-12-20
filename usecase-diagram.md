# Use Case Diagram - Sistem Inventory Roxy

```mermaid
flowchart LR
    %% Actor di kiri - Actor UML (stick figure)
    Admin((Admin))
    
    %% Use Cases berbentuk oval tersusun vertikal di kanan
    UC_Login(["Login"])
    UC_Dashboard(["Dashboard"])
    UC_DataSupplier(["Data Supplier"])
    UC_DataBrand(["Data Brand"])
    UC_DataBarang(["Data Barang"])
    UC_DataStock(["Data Stock"])
    UC_DataTransaksi(["Data Transaksi Penjualan"])
    UC_ManagementUser(["Management User"])
    UC_ReportData(["Report Data"])
    UC_Logout(["Logout"])
    
    %% Common Use Cases untuk Include
    UC_CheckAuth(["Check Authentication"])
    
    %% Actor Connections - Admin ke semua use case utama
    Admin --> UC_Login
    Admin --> UC_Dashboard
    Admin --> UC_DataSupplier
    Admin --> UC_DataBrand
    Admin --> UC_DataBarang
    Admin --> UC_DataStock
    Admin --> UC_DataTransaksi
    Admin --> UC_ManagementUser
    Admin --> UC_ReportData
    Admin --> UC_Logout
    
    %% Include: Semua use case (kecuali Login) harus check authentication
    UC_Dashboard -.->|<<include>>| UC_CheckAuth
    UC_DataSupplier -.->|<<include>>| UC_CheckAuth
    UC_DataBrand -.->|<<include>>| UC_CheckAuth
    UC_DataBarang -.->|<<include>>| UC_CheckAuth
    UC_DataStock -.->|<<include>>| UC_CheckAuth
    UC_DataTransaksi -.->|<<include>>| UC_CheckAuth
    UC_ManagementUser -.->|<<include>>| UC_CheckAuth
    UC_ReportData -.->|<<include>>| UC_CheckAuth
    UC_Logout -.->|<<include>>| UC_CheckAuth
    
    %% Extend: Relasi opsional
    UC_DataBarang -.->|<<extend>>| UC_DataStock
    UC_DataTransaksi -.->|<<extend>>| UC_DataStock
    UC_Dashboard -.->|<<extend>>| UC_DataStock
    UC_Dashboard -.->|<<extend>>| UC_DataTransaksi
    UC_ReportData -.->|<<extend>>| UC_DataSupplier
    UC_ReportData -.->|<<extend>>| UC_DataBrand
    UC_ReportData -.->|<<extend>>| UC_DataBarang
    UC_ReportData -.->|<<extend>>| UC_DataStock
    UC_ReportData -.->|<<extend>>| UC_DataTransaksi
```

## Penjelasan Singkat

### Include (<<include>>)
- **Check Authentication** wajib dilakukan sebelum akses semua fitur (kecuali Login)
- Semua use case di atas Login memerlukan autentikasi terlebih dahulu

### Extend (<<extend>>)
- **Data Barang** → **Data Stock**: Membuat barang baru otomatis membuat stock
- **Data Transaksi** → **Data Stock**: Transaksi mengurangi stock
- **Dashboard** → Menampilkan data Stock dan Transaksi
- **Report Data** → Dapat membuat laporan dari berbagai data

