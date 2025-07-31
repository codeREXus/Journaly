package com.codeREXus.journalApp.Controller;


import com.codeREXus.journalApp.Entity.User;
import com.codeREXus.journalApp.repository.UserRepository;
import com.codeREXus.journalApp.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getALL(){
        return userService.getAll();
    }



    @PutMapping
    public ResponseEntity<?> updateUser( @RequestBody  User user){

        String userName= SecurityContextHolder.getContext().getAuthentication().getName();

        User userInDb=userService.findByUserName(userName);
        if(userInDb!=null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.saveNewUser(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
