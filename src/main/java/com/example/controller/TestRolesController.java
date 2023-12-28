package com.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {

    @GetMapping("/accessadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin(){
        return "Hola has accedido con rol admin";
    }

    @GetMapping("/accessuser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String accessUser(){
        return "Hola has accedido con rol user";
    }

    @GetMapping("/accessinvited")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'INVITED')")
    public String accessInvited(){
        return "Hola has accedido con rol invited";
    }
}
