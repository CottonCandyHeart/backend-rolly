package app.rolly.backend.service;

import app.rolly.backend.dto.*;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final TrickRepository trickRepository;

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public boolean addUser(UserDto userDto){
        if (userRepository.findByUsername(userDto.getUsername()).isPresent())
            return false;

        Optional<Role> role = roleRepository.findByName(userDto.getRole());

        User user = new User(
                userDto.getUsername(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPasswd()),
                userDto.getBirthday(),
                role.get()
        );

        userRepository.save(user);
        return true;
    }

    public boolean updateRole(ChangeRoleDto changeRoleDto){
        Optional<Role> role = roleRepository.findByName(changeRoleDto.getRoleName());
        Optional<User> user = userRepository.findByUsername(changeRoleDto.getUsername());

        if (role.isEmpty()) return false;
        if (user.isEmpty()) return false;

        user.get().setRole(role.get());
        userRepository.save(user.get());
        return true;
    }

    @Transactional
    public boolean deleteEvent(String name){
        Optional<Event> eventOpt = eventRepository.findEventsByName(name);
        if (eventOpt.isEmpty()) {
            return false;
        }

        Event event = eventOpt.get();

        Set<User> att = new HashSet<>(event.getAttendee());
        for (User u : att){
            u.getAttendedEvents().remove(event);
            userRepository.save(u);
        }

        event.getAttendee().clear();

        eventRepository.removeEventByName(name);
        return true;
    }

    @Transactional
    public boolean updateTrickCategory(TrickDto trickDto){
        Optional<Category> category = categoryRepository.findByName(trickDto.getCategoryName());

        if (category.isEmpty()) return false;

        Optional<Trick> trick = trickRepository.findByName(trickDto.getTrickName());

        if (trick.isEmpty()) return false;

        trick.get().setCategory(category.get());

        return true;
    }
}
