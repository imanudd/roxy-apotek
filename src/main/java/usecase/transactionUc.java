package usecase;

// import dto.trx;
// import dto.trxDetail;
// import helper.currentUser;
// import java.sql.SQLException;
// import model.transaction;
// import model.TransactionDetail;
// import model.items;
// import model.stock;
// import model.suppliers;
// import repository.itemsRepo;
// import repository.supplierRepo;
// import repository.transactionsRepo;
// import repository.transactionsDetailRepo;
// import repository.stocksRepo;

// public class transactionUc {
//     private final itemsRepo itemsRepo;
//     private final transactionsRepo transactionsRepo;
//     private final transactionsDetailRepo transactionsDetailRepo;
//     private final supplierRepo supplierRepo;
//     private final stocksRepo stocksRepo;

//     public transactionUc(itemsRepo itemsRepo, transactionsRepo transactionsRepo, transactionsDetailRepo transactionsDetailRepo, supplierRepo supplierRepo, stocksRepo stocksRepo) {
//         this.itemsRepo = itemsRepo;
//         this.transactionsRepo = transactionsRepo;
//         this.transactionsDetailRepo = transactionsDetailRepo;
//         this.supplierRepo = supplierRepo;
//         this .stocksRepo = stocksRepo;
//     }

//     public boolean createTrx (trx trx) {
//         model.transaction transaction = new model.transaction();

//         if (trx.getDetails() == null) {
//             System.out.println("Detail is required.");
//             return false;
//         }

//         try {
//             return transactionsRepo.createTrx(trx, currentUser.getId());
//         } catch (SQLException e) {
//             System.err.println("Create transaction error: " + e.getMessage());
//             return false;
//         }
//     }

// }
