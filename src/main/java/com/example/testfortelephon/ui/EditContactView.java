package com.example.testfortelephon.ui;
import com.example.testfortelephon.model.Contact;
import com.example.testfortelephon.service.ContactService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

@Route("edit-contact/:id")
public class EditContactView extends VerticalLayout implements BeforeEnterObserver {
    private final ContactService contactService;

    private TextField nameField = new TextField("Имя");
    private TextField phoneField = new TextField("Телефон");
    private EmailField emailField = new EmailField("Email");

    private Contact currentContact;

    public EditContactView(ContactService contactService) {
        this.contactService = contactService;

        FormLayout formLayout = new FormLayout(nameField, phoneField, emailField);

        Button saveButton = new Button("Сохранить", e -> saveContact());
        Button cancelButton = new Button("Отмена", e ->
                getUI().ifPresent(ui -> ui.navigate("contacts"))
        );

        add(new RouterLink("Список контактов", MainView.class), formLayout, saveButton, cancelButton);
    }

    private void saveContact() {
        currentContact.setName(nameField.getValue());
        currentContact.setPhone(phoneField.getValue());
        currentContact.setEmail(emailField.getValue());

        contactService.saveContact(currentContact);
        getUI().ifPresent(ui -> ui.navigate("contacts"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String id = event.getRouteParameters().get("id").orElse("new");
        if (id.equals("new")) {
            currentContact = new Contact();
        } else {
            currentContact = contactService.getAllContacts().stream()
                    .filter(contact -> id.equals(contact.getId()))
                    .findFirst()
                    .orElse(new Contact());
        }
        nameField.setValue(currentContact.getName() == null ? "" : currentContact.getName());
        phoneField.setValue(currentContact.getPhone() == null ? "" : currentContact.getPhone());
        emailField.setValue(currentContact.getEmail() == null ? "" : currentContact.getEmail());
    }
}
