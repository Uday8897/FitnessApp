package com.fitness.userService.Controllers;


import com.fitness.userService.DTO.RegisterRequest;
import com.fitness.userService.DTO.UserResponse;
import com.fitness.userService.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable String user_id){
        return ResponseEntity.ok(userService.getUserProfile(user_id));

    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterRequest req){
        return ResponseEntity.ok(userService.register(req));
    }
    @GetMapping("/{user_id}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String user_id){
        log.info("Request Recieved: {}",user_id);
        ResponseEntity<Boolean> result= ResponseEntity.ok(userService.existsByKeyCloakId(user_id));
            log.info("Result {}",result);
            return result;

    }
}
