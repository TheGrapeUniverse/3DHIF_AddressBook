package at.dalex.addressbook.repository;

import at.dalex.addressbook.repository.model.Contact;
import at.dalex.repolib.CSVRepository;

public class ContactCSVRepository extends CSVRepository<Contact> {

    public ContactCSVRepository() {
        super(Contact.class);
    }

    @Override
    protected String getHeader() {
        return "Id;Name;PhoneNumber;Address";
    }

    @Override
    protected String convertModelToCSV(Contact model) {
        return String.format("%d;%s;%s;%s", model.getId(), model.getName(), model.getPhoneNumber(), model.getAddress());
    }

    @Override
    protected Contact convertCSVToModel(String csvLine) {
        String[] columns = csvLine.split(";");

        Contact model = createModel();
        int modelId = Integer.parseInt(columns[0]);

        model.setId(modelId);
        model.setName(columns[1]);
        model.setPhoneNumber(columns[2]);
        model.setAddress(columns[3]);

        return model;
    }
}
