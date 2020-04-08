package at.dalex.addressbook;

import at.dalex.addressbook.repository.ContactCSVRepository;
import at.dalex.addressbook.repository.model.Contact;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import java.util.Iterator;

public class ViewController implements EventHandler<WindowEvent> {

    //TextFields
    @FXML private TextField field_id;
    @FXML private TextField field_name;
    @FXML private TextField field_phone;
    @FXML private TextField field_address;
    @FXML private TextField field_search;

    //Buttons
    @FXML private Button button_search;
    @FXML private Button button_edit;
    @FXML private Button button_new;
    @FXML private Button button_delete;
    @FXML private Button button_save;
    @FXML private Button button_cancel;

    //ListView
    @FXML private ListView<String> list_contacts;

    private ContactCSVRepository csvRepository;
    private Contact shownContact;

    //Path to our csv file, as we don't have any dialog where we can set the output path.
    private final String CSV_FILE_PATH = "D:\\temp\\contacts.csv";

    public ViewController() {
         this.csvRepository = new ContactCSVRepository();
    }

    /**
     * Enables/disables the input fields.
     *
     * @param accessible Whether or not to enable the fields
     */
    private void changeFieldAccess(boolean accessible) {
        field_name.setDisable(!accessible);
        field_address.setDisable(!accessible);
        field_phone.setDisable(!accessible);
    }

    /**
     * Applies the contact's data to the view.
     * NOTE: This method ignores the accessibility of the text fields!
     *
     * @param contact The contact to show
     */
    private void applyContactToView(Contact contact) {
        if (contact == null)
            return;

        field_id.setText("" + contact.getId());
        field_name.setText(contact.getName());
        field_phone.setText(contact.getPhoneNumber());
        field_address.setText(contact.getAddress());
    }

    /**
     * Updates the contact list according to the data contained
     * in the repository.
     */
    private void updateContactList() {
        //Clear list before adding items
        list_contacts.getItems().clear();

        for (Iterator<Contact> it = csvRepository.getAll(); it.hasNext(); ) {
            Contact contact = it.next();
            list_contacts.getItems().add(contact.getName());
        }
    }

    /**
     * This method will be called when the window finishes it's initialisation.
     * @param event The window event invoking the method.
     */
    @Override
    public void handle(WindowEvent event) {
        changeFieldAccess(false);
    }

    /* --- Button Listeners below --- */

    @FXML
    public void onClicked_buttonEdit() {
        if (shownContact != null) {
            //Enable fields
            changeFieldAccess(true);
        }
    }

    @FXML
    public void onClicked_buttonNew() {
        Contact newContact = csvRepository.createModel();
        newContact.setId(csvRepository.getMaxId() + 1);

        //Enable fields and apply data
        changeFieldAccess(true);
        applyContactToView(newContact);
        this.shownContact = newContact;
    }

    @FXML
    public void onClicked_buttonDelete() {

    }

    @FXML
    public void onClicked_buttonSave() {
        //Store values from fields back into contact
        shownContact.setName(field_name.getText());
        shownContact.setPhoneNumber(field_phone.getText());
        shownContact.setAddress(field_address.getText());

        //Update if contact already exists in repository
        if (csvRepository.getById(shownContact.getId()) != null) {
            csvRepository.update(shownContact);
        }
        //Else insert the contact into the repository
        else {
            csvRepository.insert(shownContact);
        }

        //Write repository to csv file
        csvRepository.writeCSVFile(CSV_FILE_PATH);
        updateContactList();
    }

    @FXML
    public void onClicked_buttonCancel() {

    }
}
