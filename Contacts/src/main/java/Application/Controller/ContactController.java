package Application.Controller;

import Application.Domain.Contact;
import Application.Service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * This is the controller that handles all of the get, post, put, delete requests.
 */
@RestController
public class ContactController {

    ContactService contactService = new ContactService("jdbc:sqlite:C:/Contacts/contactsDB");

    /**
     * This returns the entire list of contacts that the table contains. A get request for the contacts endpoint.
     */
    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public ResponseEntity<List<Contact>> getContactList() {
        try{
            return ResponseEntity.ok(contactService.getFullContactList());
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This inserts a new contact into the table. A post request for the contacts endpoint.
     */
    @RequestMapping(value = "/contacts", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<String> insertContact(@RequestBody Contact contact) throws SQLException {
        try {
            return ResponseEntity.ok(contactService.insertContact(contact));
        }
        catch(SQLException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This updates an existing contact in the table based on the id passed. A put request
     * for the contacts/{id} endpoint.
     */
    @RequestMapping(value = "/contacts/{id}", method = RequestMethod.PUT, consumes = {"application/json"})
    public ResponseEntity<String> updateContact(@PathVariable int id, @RequestBody Contact contact) throws Exception {
        try {
            return ResponseEntity.ok(contactService.updateContact(contact, id));
        }
        catch(IllegalArgumentException e){
            System.err.println("Argument Error updating specific contact "+e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        catch(SQLException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This gets a specific contact in the list based on the id that is passed. A get request
     * for the contacts/{id} endpoint.
     */
    @RequestMapping(value = "/contacts/{id}", method = RequestMethod.GET)
    public ResponseEntity<Contact> getContact(@PathVariable int id) throws Exception {
        try {
            return ResponseEntity.ok(contactService.getContact(id));
        }
        catch(IllegalArgumentException e){
            System.err.println("Argument Error updating specific contact "+e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        catch(SQLException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This deletes one of the contacts in the table based on the id that is passed. A delete
     * request for the contacts/{id} endpoint.
     */
    @RequestMapping(value = "/contacts/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteContact(@PathVariable int id) throws Exception {
        try {
            return ResponseEntity.ok(contactService.deleteContact(id));
        }
        catch(IllegalArgumentException e){
            System.err.println("Argument Error updating specific contact "+e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        catch(SQLException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}