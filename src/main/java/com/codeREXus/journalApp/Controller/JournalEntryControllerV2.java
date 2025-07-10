package com.codeREXus.journalApp.Controller;

import com.codeREXus.journalApp.Entity.JournalEntry;
import com.codeREXus.journalApp.Entity.User;
import com.codeREXus.journalApp.service.JournalEntryService;
import com.codeREXus.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllByUserName(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!= null && !all.isEmpty())
            return new ResponseEntity<>(all, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        try{
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<> ( myEntry,HttpStatus.CREATED);
        }
        catch(Exception e ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.getById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<> (journalEntry.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId, @PathVariable String userName){
         journalEntryService.deleteById(myId, userName);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }

    @PutMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> updateById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry,
            @PathVariable String userName)
    {
        JournalEntry oldEntry = journalEntryService.getById(myId).orElse(null);
        //updates can be only title and content, so we make a check and update those
        if(oldEntry !=null)
        {
            oldEntry.setTitle(newEntry.getTitle()!=null &&
                    !newEntry.getTitle().equals("")? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent()!=null &&
                    !newEntry.getContent().equals("")? newEntry.getContent() : oldEntry.getContent());

            journalEntryService.saveEntry(oldEntry); // Assuming the user is not needed for update
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
