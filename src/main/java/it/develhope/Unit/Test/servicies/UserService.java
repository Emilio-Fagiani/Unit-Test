package it.develhope.Unit.Test.servicies;

import it.develhope.Unit.Test.entities.User;
import it.develhope.Unit.Test.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User setStudentIsWorkingStatus(Long id, boolean isActive){
        Optional<User> student = userRepository.findById(id);
        if(!student.isPresent()) return null;
        student.get().setActive(isActive);
        return userRepository.save(student.get());
    }
}
