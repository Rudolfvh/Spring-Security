package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.database.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.example.database.entity.User;
import org.example.database.repository.QPredicates;
import org.example.database.repository.UserRepository;
import org.example.dto.UserCreateEditDto;
import org.example.dto.UserFilter;
import org.example.dto.UserReadDto;
import org.example.mapper.UserCreateEditMapper;
import org.example.mapper.UserReadMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final ImageService imageService;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::map).collect(Collectors.toList());
    }

    public Optional<UserReadDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userReadMapper::map);
    }

    //для динамической фильтрации запросов
    public List<UserReadDto> findAll(UserFilter filter) {
        return userRepository.findAllByFilter(filter).stream().map(userReadMapper::map).collect(Collectors.toList());
    }

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), QUser.user.firstname::containsIgnoreCase)
                .add(filter.lastname(), QUser.user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), QUser.user.birthDate::before)
                .build();
        return userRepository.findAll(predicate, pageable).map(userReadMapper::map); // метод QuerydslPredicateExecutor
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) { // всегда бросает исключение!
        return Optional.of(userDto)
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return userCreateEditMapper.map(dto);
                })
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) { // exception, зависит от разработчика
        return userRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDto.getImage());
                    return userCreateEditMapper.map(userDto, entity);
                })
                .map(userRepository::saveAndFlush) // чтобы запрос сразу полетел в базу
                .map(userReadMapper::map);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    public Optional<byte[]> findAvatar(Long id) {
        return  userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush(); // потому что, тесты rollback автоматически, а delete летит в базу последним, из-за этого он его даже не исполняет
                    return true;
                })
                .orElse(false);
    }

    //SECURITY
    @Override // вызываемтся спринг секурити
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole()) //отправляем список ролей
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrive user: "+ username));
        // если совпадут, то заходим
    }


}
