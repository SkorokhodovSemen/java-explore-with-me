package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids.isEmpty()) {
            return userRepository.findAll(pageable)
                    .getContent()
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.getUsersWithIds(ids, pageable)
                    .getContent()
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            log.info("User with id = {} not found", id);
            return new NotFoundException("User not found");
        });
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }
}
