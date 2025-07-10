package com.codeREXus.journalApp.repository;

import com.codeREXus.journalApp.Entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository <JournalEntry, ObjectId> {
    // This interface will automatically provide CRUD operations for JournalEntry entities
}

