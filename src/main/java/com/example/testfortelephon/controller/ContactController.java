package com.example.testfortelephon.controller;

import com.example.testfortelephon.model.Contact;
import com.example.testfortelephon.service.ContactService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return service.getAllContacts();
    }

    @PostMapping
    public Contact addContact(@RequestBody Contact contact) {
        return service.saveContact(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable String id, @RequestBody Contact contact) {
        Contact existingContact = service.findContactById(id);
        if (existingContact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Контакт не найден");
        }

        contact.setId(id);
        return service.saveContact(contact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable String id) {
        service.deleteContact(id);
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport() {
        try {
            // Путь к сгенерированному PDF-файлу
            String filePath = "contacts_report.pdf";
            service.generatePdfReport(filePath);

            // Чтение сгенерированного PDF в байтовый массив
            byte[] pdfContent = Files.readAllBytes(Paths.get(filePath));

            // Установка заголовков для правильного отображения PDF в браузере
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=contacts_report.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при генерации отчета");
        }
    }
}
