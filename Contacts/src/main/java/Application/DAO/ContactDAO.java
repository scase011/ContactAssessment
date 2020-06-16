package Application.DAO;

import Application.Domain.Contact;
import Application.Domain.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    Connection conn = null;

    public ContactDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Pulls all records from the contacts table and returns that result set.
     */
    public ResultSet getContactList() throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "select * from contacts_db";

        st = conn.prepareStatement(query);
        rs = st.executeQuery();
        return rs;
    }

    /**
     * Inserts a new record into the contacts table.
     */
    public String insertContactInfo(Contact contact) throws SQLException {
        PreparedStatement st = null;
        List<Phone> phoneList = new ArrayList<>();
        String query = "insert into contacts_db values(?,?,?,?,?,?,?,?,?,?,?,?)";
        st = conn.prepareStatement(query);
        //Finds the max id and increments by 1 to create id for the new record.
        int newMax = getMaxId() + 1;
        st.setInt(1, newMax);
        st.setString(2, contact.getName().getFirst());
        st.setString(3, contact.getName().getMiddle());
        st.setString(4, contact.getName().getLast());
        st.setString(5, contact.getAddress().getStreet());
        st.setString(6, contact.getAddress().getCity());
        st.setString(7, contact.getAddress().getState());
        st.setString(8, contact.getAddress().getZip());
        //Goes through the phone list provided in the contact object to insert to the correct number type column.
        if(contact.getPhone()!=null){
            phoneList = contact.getPhone();
        }
        for(int i = 0; i < phoneList.size(); i++){
            if(phoneList.get(i).getType().equals("home")){
                st.setString(9, phoneList.get(i).getNumber());
            }
            if(phoneList.get(i).getType().equals("work")){
                st.setString(10, phoneList.get(i).getNumber());
            }
            if(phoneList.get(i).getType().equals("mobile")){
                st.setString(11, phoneList.get(i).getNumber());
            }
        }
        st.setString(12, contact.getEmail());

        st.executeUpdate();
        return "Contact inserted with ID of "+newMax+".";
    }

    /**
     * Updates entire existing record with the matching id.
     */
    public String updateContactInfo(Contact contact, int id) throws SQLException {
        PreparedStatement st = null;
        List<Phone> phoneList = new ArrayList<>();
        //Checks to see if the ID is present before pulling the data.
        if(!checkForId(id)){
            throw new IllegalArgumentException("No contact present with this ID");
        }
        //Updates entire content except id because the changed data is unknown here.
        String query = "update contacts_db set fname = ?, mname = ?, lname = ?, street = ?, " +
                "city = ?, state = ?, zip = ?, home = ?, worknum = ?, mobile = ?, email = ?" +
                "where id = "+id;
        st = conn.prepareStatement(query);
        st.setString(1, contact.getName().getFirst());
        st.setString(2, contact.getName().getMiddle());
        st.setString(3, contact.getName().getLast());
        st.setString(4, contact.getAddress().getStreet());
        st.setString(5, contact.getAddress().getCity());
        st.setString(6, contact.getAddress().getState());
        st.setString(7, contact.getAddress().getZip());
        //Goes through the phone list provided in the contact object to update to the correct number type column.
        if(contact.getPhone()!=null){
            phoneList = contact.getPhone();
        }
        for(int i = 0; i < phoneList.size(); i++){
            if(phoneList.get(i).getType().equals("home")){
                st.setString(8, phoneList.get(i).getNumber());
            }
            if(phoneList.get(i).getType().equals("work")){
                st.setString(9, phoneList.get(i).getNumber());
            }
            if(phoneList.get(i).getType().equals("mobile")){
                st.setString(10, phoneList.get(i).getNumber());
            }
        }
        st.setString(11, contact.getEmail());

        st.executeUpdate();
        return "Contact updated with ID of "+id+".";
    }

    /**
     * Pulls a single record based on the id.
     */
    public ResultSet getContactInfo(int id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        //Checks to see if the ID is present before pulling the data.
        if(!checkForId(id)){
            throw new IllegalArgumentException("No contact present with this ID");
        }
        String query = "select * from contacts_db where id = ?";
        st = conn.prepareStatement(query);
        st.setInt(1, id);
        rs = st.executeQuery();
        return rs;
    }

    /**
     * Deletes a single record based on the id.
     */
    public String deleteContactInfo(int id) throws SQLException {
        PreparedStatement st = null;
        //Checks to see if the ID is present before pulling the data.
        if(!checkForId(id)){
            throw new IllegalArgumentException("No contact present with this ID");
        }

        String query = "delete from contacts_db where id = ?";
        st = conn.prepareStatement(query);
        st.setInt(1, id);
        st.executeUpdate();
        return "Contact deleted with ID of " + id + ".";
    }

     /**
     * This returns the max id currently in the table.
     */
    public int getMaxId() throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "select max(id) as id from contacts_db";
        st = conn.prepareStatement(query);
        rs = st.executeQuery();
        return rs.getInt("id");
    }

    /**
     * Checks if that ID is present in the table.
     */
    public boolean checkForId(int id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean check = false;
        String query = "select * from contacts_db where id = ?";
        st = conn.prepareStatement(query);
        st.setInt(1,id);
        rs = st.executeQuery();
        //Sets the boolean to true if the email exists somewhere else in the table.
        if(rs.next()){
            check = true;
        }
        return check;
    }
}
