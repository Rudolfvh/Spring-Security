package org.example.database.repository;

import org.example.database.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query(value = "select c from Company c " +
            "join fetch c.locales c1" +
            " where c.name = :name2")
//    ,nativeQuery = true) // native query - обычный запрос SQL
    Optional<Company> findByName(@Param("name2")/* устанавливает имя переменной в HQL */ String name);

    List<Company> findAllByNameContainingIgnoreCase(String name);

}
