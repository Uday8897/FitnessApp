package com.fitness.userService.Service;

import com.fitness.userService.DTO.RegisterRequest;
import com.fitness.userService.DTO.UserResponse;
import com.fitness.userService.Models.User;
import com.fitness.userService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserResponse getUserProfile(String userId) {
        User save=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        return UserResponse.builder().id(save.getId()).emailId(save.getEmailId()).firstName(save.getFirstName())
                .lastName(save.getLastName()).passWord(save.getPassWord()).createdAt(save.getCreatedAt()).updatedAt(save.getUpdatedAt())
                .build();


    }

    public UserResponse register(RegisterRequest req) {
        if(userRepository.existsByEmailId(req.getEmail())){
            User user=userRepository.findByEmailId((req.getEmail()));
            user.setEmailId(req.getEmail());
            user.setLastName(req.getLastName());
            user.setFirstName(req.getFirstName());
            user.setPassWord(req.getPassword());
            user.setKeyCloakId(req.getKeyCloakId());
            User save = userRepository.save(user);
            return UserResponse.builder().id(save.getId()).emailId(save.getEmailId()).firstName(save.getFirstName())
                    .lastName(save.getLastName()).passWord(save.getPassWord()).keyCloakId(save.getKeyCloakId()).createdAt(save.getCreatedAt()).updatedAt(save.getUpdatedAt())
                    .build();

        }
        User user=new User();
        user.setEmailId(req.getEmail());
        user.setLastName(req.getLastName());
        user.setFirstName(req.getFirstName());
        user.setPassWord(req.getPassword());
        user.setKeyCloakId(req.getKeyCloakId());
        User save = userRepository.save(user);
        return UserResponse.builder().id(save.getId()).emailId(save.getEmailId()).firstName(save.getFirstName())
                .lastName(save.getLastName()).passWord(save.getPassWord()).keyCloakId(save.getKeyCloakId()).createdAt(save.getCreatedAt()).updatedAt(save.getUpdatedAt())
                .build();

    }

    public Boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

    public Boolean existsByKeyCloakId(String userId) {
        return userRepository.existsByKeyCloakId(userId);
    }
}
