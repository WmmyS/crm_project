package com.wesley.crm.app.controllers.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/debug/hash")
  public String generateHash(@RequestParam String password) {
    return passwordEncoder.encode(password);
  }
}