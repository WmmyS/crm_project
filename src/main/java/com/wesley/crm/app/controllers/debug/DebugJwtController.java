package com.wesley.crm.app.controllers.debug;

import com.wesley.crm.app.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugJwtController {

  @Autowired
  private JwtService jwtService;

  @GetMapping("/debug/decode-token")
  public String decodeToken(@RequestParam String token) {
    try {
      String username = jwtService.getUsernameFromToken(token);
      Long userId = jwtService.getUserIdFromToken(token);
      String role = jwtService.getRoleFromToken(token);

      return String.format("Username: %s, UserId: %d, Role: %s", username, userId, role);
    } catch (Exception e) {
      return "Erro: " + e.getMessage();
    }
  }
}