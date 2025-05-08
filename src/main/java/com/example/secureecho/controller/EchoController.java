package com.example.secureecho.controller;

import com.example.secureecho.model.EchoRequest;
import com.example.secureecho.model.EchoResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/echo")
public class EchoController {

    @PostMapping("/user/standard")
    public EchoResponse standardEcho(@RequestBody EchoRequest request, Authentication authentication) {
        return new EchoResponse(request.getMessage(), "User Echo Service");
    }
    
    @PostMapping("/user/uppercase")
    public EchoResponse uppercaseEcho(@RequestBody EchoRequest request, Authentication authentication) {
        String uppercaseMessage = request.getMessage().toUpperCase();
        return new EchoResponse(uppercaseMessage, "User Echo Service (Uppercase)");
    }
    
    @PostMapping("/admin/reverse")
    public EchoResponse reverseEcho(@RequestBody EchoRequest request, Authentication authentication) {
        String reversedMessage = new StringBuilder(request.getMessage()).reverse().toString();
        return new EchoResponse(reversedMessage, "Admin Echo Service (Reverse)");
    }
    
    @GetMapping("/user/info")
    public EchoResponse userInfo(Authentication authentication) {
        return new EchoResponse("Authenticated as: " + authentication.getName(), "User Info Service");
    }
    
    @GetMapping("/admin/info")
    public EchoResponse adminInfo(Authentication authentication) {
        return new EchoResponse("Authenticated as admin: " + authentication.getName(), "Admin Info Service");
    }
}
