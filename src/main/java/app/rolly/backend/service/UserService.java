package app.rolly.backend.service;

import app.rolly.backend.dto.*;
import app.rolly.backend.exception.GlobalExceptionHandler;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.exception.WrongPasswordException;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    public UserResponseDto getUserProfile(User user){
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

    public void updateUserProfile(UpdateUserDto updateUserDto, User user){
        user.setEmail(updateUserDto.getEmail());

        if (!user.getRole().getName().equals(updateUserDto.getRole())) {
            Role role = roleRepository.findByName(updateUserDto.getRole());
            user.setRole(role);
        }
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest request, User user) {
        if (!passwordEncoder.matches(request.getCurrentPasswd(), user.getHashedPasswd())) {
            throw new WrongPasswordException();
        }

        user.setHashedPasswd(passwordEncoder.encode(request.getNewPasswd()));
        userRepository.save(user);
    }

    public boolean removeUser(Long id){
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) return false;

        userRepository.removeUserById(id);
        return true;
    }

    public boolean updateMeasurements(UserMeasurementsDto userMeasurementsDto, User user){
        user.setWeight(userMeasurementsDto.getWeight());
        user.setHeight(userMeasurementsDto.getHeight());

        return true;
    }

    public UserMeasurementsDto getMeasurements(User user){
        return new UserMeasurementsDto(user.getWeight(), user.getHeight());
    }
}
