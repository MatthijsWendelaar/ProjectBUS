package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.annotations.Column;
import name.wendelaar.snowdb.annotations.Foreign;
import name.wendelaar.snowdb.annotations.Primary;
import name.wendelaar.snowdb.annotations.Table;
import name.wendelaar.snowdb.model.Model;

import java.sql.Date;

@Table(name = "user_data_personal")
public class UserData extends Model {

    @Primary(name = "user_id")
    @Foreign(name = "user_id", foreignModel = User.class)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "home_number")
    private String homeNumber;
}
