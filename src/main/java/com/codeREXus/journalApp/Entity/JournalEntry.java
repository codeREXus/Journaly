package com.codeREXus.journalApp.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor // to use deserialization frm JSON to POJO
public class JournalEntry {

    // this is a POJO (Plain Old Java Object) class representing a journal entry
    // this is basically a model class that will be used to represent the DATA STRUCTURE of a journal entry showing
    // the fields required to create an entry which are id, title and content of the entry
    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;



}
