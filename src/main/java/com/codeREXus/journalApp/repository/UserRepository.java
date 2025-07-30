package com.codeREXus.journalApp.repository;

import com.codeREXus.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository <User, ObjectId> {
    // This interface will automatically provide CRUD operations for JournalEntry entities

    User findByUserName(String userName);
    void deleteByUserName(String userName);
}

