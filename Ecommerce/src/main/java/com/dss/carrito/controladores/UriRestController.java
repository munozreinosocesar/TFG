package com.dss.carrito.controladores;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class UriRestController {
    private final String expectedToken = Base64.getEncoder().encodeToString("admin:admin".getBytes(StandardCharsets.UTF_8));
    @GetMapping("/login")
    @ResponseBody
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        String token = username + ":" + password;
        String encodedToken = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodedToken);
        Map<String, String> response = new HashMap<>();
        response.put("token", encodedToken);
        return response;
    }

    @GetMapping("/checkPrivileges")
    @ResponseBody
    public Boolean checkPrivileges  (@RequestParam String token) {
        System.out.println("Checking " + token + " == " + expectedToken + " ");
        return token.equals(expectedToken);
    }
}
