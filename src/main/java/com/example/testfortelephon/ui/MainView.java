package com.example.testfortelephon.ui;

import com.example.testfortelephon.model.Contact;
import com.example.testfortelephon.service.ContactService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import elemental.json.Json;
import elemental.json.JsonObject;

@Route("contacts")
public class MainView extends VerticalLayout {
    private final ContactService contactService;
    private final Grid<Contact> grid;

    public MainView(ContactService contactService) {
        this.contactService = contactService;
        this.grid = new Grid<>(Contact.class);

        grid.setColumns("name", "phone", "email");

        grid.addComponentColumn(contact -> {
            Button editButton = new Button("Редактировать", e -> {
                getUI().ifPresent(ui -> ui.navigate("edit-contact/" + contact.getId()));
            });
            return editButton;
        });

        Button addContactButton = new Button("Добавить контакт", e ->
                getUI().ifPresent(ui -> ui.navigate("edit-contact/new"))
        );

        // Кнопка для создания отчета
        Button generateReportButton = new Button("Сгенерировать отчет", e -> generateReport());

        add(
                new RouterLink("Список контактов", MainView.class),
                grid,
                addContactButton,
                generateReportButton
        );

        updateGrid();
    }

    private void updateGrid() {
        grid.setItems(contactService.getAllContacts());
    }
    private void generateReport() {
        try {
            // URL для получения сгенерированного отчета
            String reportUrl = "/api/contacts/report";

            // Открываем отчет в новом окне
            getUI().ifPresent(ui ->
                    ui.getPage().executeJs("window.open($0, '_blank')", reportUrl)
            );
        } catch (Exception e) {
            Notification.show("Ошибка при генерации отчета: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
