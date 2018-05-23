package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.annotation.Data;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;

public class Book extends Item {

    @Data
    private DataObject bookData;

    public Book(DataObjectCollection collection) {
        super(collection);
        bookData = collection.getDataObjectByTable("book");
        if (bookData == null) {
            throw new IllegalArgumentException("No book data found in data collection");
        }
    }

    public void printAll() {
        for (Object o : bookData.getAll()) {
            System.out.println(o);
        }
        for (Object o : dataObject.getAll()) {
            System.out.println(o);
        }
    }
}
