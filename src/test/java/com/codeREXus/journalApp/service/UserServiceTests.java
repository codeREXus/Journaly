package com.codeREXus.journalApp.service;


import com.codeREXus.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTests {

    @Autowired
    private UserRepository userRepostory;

    public void testFindbyUsername(){
        assertNotNull(userRepostory.findByUserName("testUser"));
    }



}
