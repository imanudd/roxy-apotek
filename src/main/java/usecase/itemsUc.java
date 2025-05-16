package usecase;

import model.items;
import repository.itemsRepo;

import java.util.List;

public class itemsUc {
    private final itemsRepo itemRepo;

    public itemsUc(itemsRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    public List<items> getAllItems() {
        return itemRepo.getAllItems();
    }

    public boolean createItem(items itm) {
        if (itm.getItemName() == null || itm.getItemName().isEmpty()) {
            System.out.println("Nama item tidak boleh kosong");
            return false;
        }

        if (itm.getPrice() <= 0) {
            System.out.println("Harga item tidak boleh nol atau negatif");
            return false;
        }

        return itemRepo.createItem(itm);
    }

    public boolean updateItem(items itm) {
        if (itm.getId() <= 0) {
            System.out.println("ID item tidak valid");
            return false;
        }

        return itemRepo.updateItem(itm);
    }

//     public boolean deleteItem(int id) {
//         if (id <= 0) {
//             System.out.println("ID item tidak valid");
//             return false;
//         }

//         items itm = new items(null, id, id, null, id, null, id);
//         itm.setId(id);
//         return itemRepo.deleteItem(id);
//     }
}
