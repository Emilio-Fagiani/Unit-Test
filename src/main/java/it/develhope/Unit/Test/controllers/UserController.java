package it.develhope.Unit.Test.controllers;

import it.develhope.Unit.Test.entities.User;
import it.develhope.Unit.Test.repositories.UserRepository;
import it.develhope.Unit.Test.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public @ResponseBody User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping("")
    public @ResponseBody List<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody User getId(@PathVariable Long id){
        Optional<User> user= userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else
            return null;
    }

    @PutMapping("/{id}")
    public @ResponseBody User update(@PathVariable Long id,@RequestBody User user){
        user.setId(id);
        return userRepository.save(user);
    }

    @PutMapping("/{id}/active")
    public @ResponseBody User update(@PathVariable Long id, @RequestParam("active") boolean active){
        return userService.setStudentIsWorkingStatus(id, active);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userRepository.deleteById(id);
    }

    @DeleteMapping("")
    public void deleteAll(){
        userRepository.deleteAll();
    }
}
