package Application.Service;

import Application.DAO.ContactDAO;
import Application.Domain.Address;
import Application.Domain.Contact;
import Application.Domain.Name;
import Application.Domain.Phone;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    String connectionString = "";

    public ContactService(){

    }

    public ContactService(String connectionString){
        this.connectionString = connectionString;
    }

    Connection conn = null;

    /**
     * Initializes the connection to the database. In this case it is using a local file which SQLite creates
     * and edits.
     */
    public void initialize(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Creates and returns the full list of contact objects.
     */
    public List<Contact> getFullContactList() throws Exception {
        initialize();
        ContactDAO contactDao = new ContactDAO(conn);
        ResultSet rs = null;
        List<Contact> contactList = new ArrayList<>();
        try {
            rs = contactDao.getContactList();
            //Loops through the result set created from the DAO creating new contact objects.
            while(rs.next()){
                Contact contact = createContact(rs);
                contactList.add(contact);
            }
            return contactList;
        }
        catch(Exception e){
            System.err.println("Error getting contact List " + e);
            throw e;
        }
        finally{
            if(conn!=null){
                conn.close();
            }
        }
    }

    /**
     * Inserts a new contact
     */
    public String insertContact(Contact contact) throws Exception {
        initialize();
        ContactDAO contactDao = new ContactDAO(conn);

        try {
            return contactDao.insertContactInfo(contact);
        }
        catch(SQLException e){
            System.err.println("SQL Error in inserting a contact " +e);
            throw e;
        }
        catch(Exception e){
            System.err.println("Error in inserting a contact "+e);
            throw e;
        }
        finally{
            if(conn!=null){
                conn.close();
            }
        }
    }

    /**
     * Updates an existing contact based on id parameter
     */
    public String updateContact(Contact contact, int id) throws Exception {
        initialize();
        ContactDAO contactDao = new ContactDAO(conn);

        try{
            return contactDao.updateContactInfo(contact, id);
        }
        catch(IllegalArgumentException e){
            System.err.println("Argument Error updating specific contact "+e);
            throw e;
        }
        catch(SQLException e){
            System.err.println("SQL Error in updating a contact "+e);
            throw e;
        }
        catch(Exception e){
            System.err.println("Error in updating a contact "+e);
            throw e;
        }
        finally{
            if(conn!=null){
                conn.close();
            }
        }
    }

    /**
     * Creates and returns the single contact that has the id provided.
     */
    public Contact getContact(int id) throws Exception {
        initialize();
        ContactDAO contactDao = new ContactDAO(conn);

        ResultSet rs = null;

        try {
            rs = contactDao.getContactInfo(id);
            return createContact(rs);
        }
        catch(IllegalArgumentException e){
            System.err.println("Argument Error getting specific contact "+e);
            throw e;
        }
        catch(SQLException e){
            System.err.println("SQL Error getting specific contact "+e);
            throw e;
        }
        catch(Exception e){
            System.err.println("Error getting specific contact " + e);
            throw e;
        }
        finally{
            if(conn!=null){
                conn.close();
            }
        }
    }

    /**
     * Deletes the contact that has the id provided.
     */
    public String deleteContact(int id) throws Exception {
        initialize();
        try {
            ContactDAO contactDao = new ContactDAO(conn);
            return contactDao.deleteContactInfo(id);
        }
        catch(IllegalArgumentException e){
            System.err.println("Argument Error deleting a contact "+e);
            throw e;
        }
        catch(SQLException e){
            System.err.println("SQL Error deleting a contact "+e);
            throw e;
        }
        catch(Exception e){
            System.err.println("Error deleting a contact " + e);
            throw e;
        }
        finally{
            if(conn!=null){
                conn.close();
            }
        }
    }

    public Contact createContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        Name name = new Name();
        Phone phone = new Phone();
        Address address = new Address();
        if(rs == null){
            return null;
        }
        List<Phone> phoneList = new ArrayList<>();
        contact.setId(rs.getInt("id"));

        //Creates the name object and adds it to the contact.
        name.setFirst(rs.getString("fname"));
        name.setMiddle(rs.getString("mname"));
        name.setLast(rs.getString("lname"));
        if(name!=null) {
            contact.setName(name);
        }

        //Creates the address object and adds it to the contact.
        address.setStreet(rs.getString("street"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setZip(rs.getString("zip"));
        if(address!=null) {
            contact.setAddress(address);
        }

        //Setting the data for the phone object. Only sets the objects for the type of phone number
        //that we have present in the table. If a certain phone number type is not present then nothing
        //is added to the contact object.
        if (rs.getString("home") != null && !rs.getString("home").equals("")) {
            phone = new Phone();
            phone.setType("home");
            phone.setNumber(rs.getString("home"));
            phoneList.add(phone);
        }

        if (rs.getString("worknum") != null && !rs.getString("worknum").equals("")) {
            phone = new Phone();
            phone.setType("work");
            phone.setNumber(rs.getString("worknum"));
            phoneList.add(phone);
        }

        if (rs.getString("mobile") != null && !rs.getString("mobile").equals("")) {
            phone = new Phone();
            phone.setType("mobile");
            phone.setNumber(rs.getString("mobile"));
            phoneList.add(phone);
        }

        if(phoneList.size()!=0) {
            contact.setPhone(phoneList);
        }
        contact.setEmail(rs.getString("email"));
        return contact;
    }
}
