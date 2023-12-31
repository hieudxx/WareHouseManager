package hieudx.fpoly.warehousemanager.Supplier.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import hieudx.fpoly.warehousemanager.SQliteDB.DBHelper;
import hieudx.fpoly.warehousemanager.Supplier.Model.Supplier;

public class Supplier_Dao {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public Supplier_Dao(Context context) {
        dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Supplier> getAll() {
        ArrayList<Supplier> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM Supplier", null);
        if (c.getCount() != 0) { // nếu có dữ liệu
            c.moveToFirst(); // chuyển con trỏ về đầu bảng
            do {
//                hoặc c.getInt(c.getColumnIndex("id"))
                list.add(new Supplier(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            } while (c.moveToNext()); // nếu chưa đến cuối bảng thì đi tiếp
        }
        return list;
    }

    public Supplier getSupplierById(int id) {
        Supplier supplier = new Supplier();
        Cursor c = db.rawQuery("SELECT * FROM Supplier WHERE id = ?", new String[]{String.valueOf(id)});
        if (c.getCount() != 0) {
            c.moveToFirst();
            supplier = new Supplier(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
        }
        return supplier;
    }

    public boolean isExistSupplier(String name) {
        Cursor c = db.rawQuery("select * from Delivery WHERE name = ?", new String[]{name});
        if (c.getCount() != 0) {
            return true;
        }
        return false;
    }

    public long insert(Supplier supplier) {
        ContentValues values = new ContentValues();
        values.put("name", supplier.getName());
        values.put("phone", supplier.getPhone());
        values.put("address", supplier.getAddress());
        values.put("tax_code", supplier.getTax_code());
        return db.insert("Supplier", null, values);
    }

    public ArrayList<HashMap<String, Object>> getListSupplier() {
        ArrayList<Supplier> list = getAll();
        ArrayList<HashMap<String, Object>> listHM = new ArrayList<>(); //tạo một arrList có kiểu là hashmap
        for (Supplier supplier : list) { // duyệt qua từng đối tượng trong arrList
            HashMap<String, Object> hs = new HashMap<>(); //mỗi lần lặp tạo ra 1 hashmap mới chứa cặp key-value
            hs.put("id", supplier.getId());
            hs.put("name", supplier.getName());
            listHM.add(hs);
        }
        return listHM;
    }



    public boolean updateSupplier(Supplier supplier) {
        ContentValues values = new ContentValues();

        values.put("name", supplier.getName());
        values.put("phone", supplier.getPhone());
        values.put("address", supplier.getAddress());
        values.put("tax_code", supplier.getTax_code());
        long check = db.update("Supplier", values, "id = ?", new String[]{String.valueOf(supplier.getId())});
        return check > 0;
    }

    public int deleteSupplier(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Product where id_supplier = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() != 0) {
            return -1;
        }
        long check = db.delete("Supplier", "id = ?", new String[]{String.valueOf(id)});
        if (check == -1) {
            return 0;
        } else {
            return 1;
        }
    }


}
