package com.codeREXus.journalApp.Controller;

import com.codeREXus.journalApp.Entity.JournalEntry;
import com.codeREXus.journalApp.Entity.User;
import com.codeREXus.journalApp.service.JournalEntryService;
import com.codeREXus.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllByUserName(){

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!= null && !all.isEmpty())
            return new ResponseEntity<>(all, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<> ( myEntry,HttpStatus.CREATED);
        }
        catch(Exception e ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect= user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<> (journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String userName = auth.getName();
         boolean removed = journalEntryService.deleteById(myId, userName);
         if(removed)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
         else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = userService.findByUserName(userName);
        List<JournalEntry> collect= user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            Optional JornalEntry = journalEntryService.getById(myId);
            if(JornalEntry.isPresent()){
                JournalEntry oldEntry = journalEntryService.getById(myId).orElse(null);
                //updates can be only title and content, so we make a check and update those
                if(oldEntry !=null)
                {
                    oldEntry.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                    oldEntry.setContent(newEntry.getContent()!=null &&
                            !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                    oldEntry.setDate(LocalDateTime.now());
                    journalEntryService.saveEntry(oldEntry); // Assuming the user is not needed for update
                    return new ResponseEntity<>(oldEntry, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
