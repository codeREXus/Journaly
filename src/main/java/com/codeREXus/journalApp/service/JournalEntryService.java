package com.codeREXus.journalApp.service;

import com.codeREXus.journalApp.Entity.JournalEntry;
import com.codeREXus.journalApp.Entity.User;
import com.codeREXus.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    @Transactional // attains ACID properties
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("Error saving journal entry: " + e.getMessage());
        }
        // here service layer calls the repository layer, which extends the MongoRepository interface
        // The mongoRepository interface provides methods for CRUD
        // such an operation is called to save the entry!
    }
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
        // this method returns all the entries in the database
    }


    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }


    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        // goes to user and gets all the journal entries and checks if the entry with the given id exists
        // if it does, it removes it from user's journal
        user.getJournalEntries().removeIf(x-> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }
}