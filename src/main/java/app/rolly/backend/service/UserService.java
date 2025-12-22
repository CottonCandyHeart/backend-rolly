package app.rolly.backend.service;

import app.rolly.backend.dto.*;
import app.rolly.backend.exception.GlobalExceptionHandler;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.exception.WrongPasswordException;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EventService eventService;

    public UserResponseDto getUserProfile(String username){
        User user = userRepository.findByUsername(username).get();

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setBirthday(user.getDateOfBirth());
        userResponseDto.setRole(user.getRole().getName());
        userResponseDto.setLevel(user.getUserLevel());


        List<EventDto> organizedEventsDtos = user.getOrganizedEvents().stream()
                        .map(EventDto::new)
                        .toList();

        Set<EventDto> attendedEvents = user.getAttendedEvents().stream()
                        .map(EventDto::new)
                                .collect(Collectors.toSet());

        Set<AchievementDto> achievements = user.getAchievements().stream()
                        .map(AchievementDto::new)
                                .collect(Collectors.toSet());

        List<RouteDto> routesCreated = user.getRoutesCreated().stream()
                        .map(RouteDto::new)
                                .toList();

        userResponseDto.setOrganizedEvents(organizedEventsDtos);
        userResponseDto.setAttendedEvents(attendedEvents);
        userResponseDto.setAchievements(achievements);
        userResponseDto.setRoutesCreated(routesCreated);

        if (user.getUserProgress() != null){
            userResponseDto.setUserProgress(new UserProgressDto(user.getUserProgress()));
        }


        return userResponseDto;
    }

    public void updateUserProfile(UpdateUserDto updateUserDto, String username){
        Optional<User> user = userRepository.findByUsername(username);

        user.get().setEmail(updateUserDto.getEmail());

        if (!user.get().getRole().getName().equals(updateUserDto.getRole())) {
            Optional<Role> role = roleRepository.findByName(updateUserDto.getRole());
            user.get().setRole(role.get());
        }
        userRepository.save(user.get());
    }

    public void changePassword(ChangePasswordRequest request, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!passwordEncoder.matches(request.getCurrentPasswd(), user.get().getHashedPasswd())) {
            throw new WrongPasswordException();
        }

        user.get().setHashedPasswd(passwordEncoder.encode(request.getNewPasswd()));
        userRepository.save(user.get());
    }

    @Transactional
    public boolean removeUser(String username){
        username = username.trim();
        System.out.println(">" + username + "<");
        System.out.println("length = " + username.length());
        Optional<User> user = userRepository.findByUsername(username);

        System.out.println(user.isEmpty());

        if (user.isEmpty()) return false;

        List<Event> organizedEvents = new ArrayList<>(user.get().getOrganizedEvents());

        for (Event e : organizedEvents) {
            eventService.deleteEvent(e.getName(), username);
        }

        userRepository.removeUserById(user.get().getId());
        return true;
    }

    public boolean updateMeasurements(UserMeasurementsDto userMeasurementsDto, String username){
        Optional<User> user = userRepository.findByUsername(username);
        user.get().setWeight(userMeasurementsDto.getWeight());
        user.get().setHeight(userMeasurementsDto.getHeight());

        userRepository.save(user.get());

        return true;
    }

    public UserMeasurementsDto getMeasurements(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return new UserMeasurementsDto(user.get().getWeight(), user.get().getHeight());
    }
}
