package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObjectCollection;

public class Book extends Item {

    public Book(DataObjectCollection collection) {
        super(collection, "book");
    }

    public String getISBN() {
        return (String) getAttributeValue("isbn");
    }

    public String getAuthor() {
        return (String) getAttributeValue("author");
    }
}
