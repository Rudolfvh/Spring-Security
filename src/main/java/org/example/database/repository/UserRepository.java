package org.example.database.repository;

import lombok.*;
import org.example.database.entity.Role;
import org.example.database.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository,
        QuerydslPredicateExecutor<User> {

    //% - между процентами какой-либо символ должен присутствовать(типо regex)
    @Query("select u from User u" +
            " where u.firstname like %:firstName% " +
            "and u.lastname like %:lastName%")
    List<User> findAllBy(String firstName, String lastName);

    @Modifying // нужна там, где будет обновляться БД
            (clearAutomatically = true) // чистит кэш перед применением запроса,(но может возникнуть LazyException
    //т.к чистится весь кэш и он не понимает за какой сущностью идти в базу )
    @Query("update User u set u.role = :role " +
            "where u.id in (:ids)")
    int updateRole(Role role, Long... ids);

    Optional<User> findFirstByOrderByIdDesc();

    //для динамической сортировки
    List<User> findFirst3By(Sort sort);

    //для разбиениея результатов по страницам
    List<User> findAllBy(Pageable pageable);

    @Query("select u from User u")
    Slice<User> sliceFindAll(Pageable pageable);

    //отличается от slize тем, что считает страницы
    @Query("select u from User u")
    Page<User> pageFindAll(Pageable pageable);

    List<User> findFirst4By(Sort sort);

    Optional<User> findByUsername(String username);
}
