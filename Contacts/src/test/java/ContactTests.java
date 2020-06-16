import Application.ContactApplication;
import Application.Domain.Address;
import Application.Domain.Contact;
import Application.Domain.Name;
import Application.Domain.Phone;
import Application.Service.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(
        classes = {
                ContactApplication.class,
                ContactService.class
        })
public class ContactTests {
    ContactService service = new ContactService("jdbc:sqlite:C:/Contacts/testcontactsDB");

    Contact contact = new Contact();
    Name name = new Name();
    List<Phone> phoneList = new ArrayList<>();
    Address address = new Address();

    public Contact createContact(){
        name.setFirst("John");
        name.setMiddle("Wayne");
        name.setLast("Smith");
        contact.setName(name);

        address.setStreet("1 Something Street");
        address.setCity("Newburg");
        address.setState("Virginia");
        address.setZip("1");
        contact.setAddress(address);

        Phone phone1 = new Phone();
        phone1.setNumber("111-111-1111");
        phone1.setType("home");
        phoneList.add(phone1);
        Phone phone2 = new Phone();
        phone2.setNumber("222-222-2222");
        phone2.setType("work");
        phoneList.add(phone2);
        contact.setPhone(phoneList);

        contact.setEmail("a.b@email.com");
        return contact;
    }

    @Test
    public void insertContactTest() throws Exception{
        Contact testContact = createContact();
        String result = service.insertContact(testContact);
        assertEquals(result, "Contact inserted with ID of 1.");
    }

    @Test
    public void getContactListTest() throws Exception{
        List<Contact> testContactList = service.getFullContactList();
        assertEquals(1, testContactList.size());
        assertEquals("Wayne", testContactList.get(0).getName().getMiddle());
    }

    @Test
    public void updateContactTest() throws Exception{
        Contact testContact = createContact();
        testContact.getName().setFirst("Jim");
        String result = service.updateContact(testContact,1);
        assertEquals(result, "Contact updated with ID of 1.");
        testContact = service.getContact(1);
        assertEquals("Jim", testContact.getName().getFirst());
    }

    @Test
    public void getContactTest() throws Exception{
        Contact testContact = service.getContact(1);
        assertEquals("a.b@email.com", testContact.getEmail());
    }

    @Test
    public void deleteContactTest() throws Exception{
        String result = service.deleteContact(1);
        assertEquals("Contact deleted with ID of 1.",result);
    }

    @Test
    public void invalidIdTest() throws Exception{
        assertThrows(IllegalArgumentException.class, () -> {service.deleteContact(100);});
        assertThrows(IllegalArgumentException.class, () -> {service.updateContact(contact,100);});
        assertThrows(IllegalArgumentException.class, () -> {service.getContact(100);});
    }

    @Test
    public void pathwayTest() throws Exception{
        Contact testContact = createContact();
        String result = service.insertContact(testContact);
        assertEquals(result, "Contact inserted with ID of 1.");
        List<Contact> contactList = service.getFullContactList();
        assertEquals(1, contactList.size());
        testContact.setEmail("b.c@email.com");
        assertThrows(IllegalArgumentException.class, () -> {service.updateContact(contact,100);});
        result = service.updateContact(contact, 1);
        assertEquals("Contact updated with ID of 1.", result);
        Contact newContact = service.getContact(1);
        assertEquals("b.c@email.com", newContact.getEmail());
        result = service.deleteContact(1);
        assertEquals("Contact deleted with ID of 1.", result);
        contactList = service.getFullContactList();
        assertEquals(0, contactList.size());
    }
}
