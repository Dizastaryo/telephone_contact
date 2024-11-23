package com.example.testfortelephon.service;
import com.example.testfortelephon.model.Contact;
import com.example.testfortelephon.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> getAllContacts() {
        return repository.findAll();
    }
    public Contact saveContact(Contact contact) {
    return repository.save(contact);
}
    public Contact findContactById(String id) {
        return repository.findById(id).orElse(null);
    }
    public void deleteContact(String id) {
    repository.deleteById(id);
}
    public void generatePdfReport(String filePath) {
        try (var writer = new com.itextpdf.kernel.pdf.PdfWriter(filePath);
             var pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
             var document = new com.itextpdf.layout.Document(pdfDocument)) {

            // Добавляем информацию о каждом контакте в PDF
            for (Contact contact : getAllContacts()) {
                document.add(new com.itextpdf.layout.element.Paragraph(
                        contact.getName() + " - " + contact.getPhone() + " - " + contact.getEmail()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
