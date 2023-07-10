package org.example.database.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.database.entity.QUser;
import org.example.database.entity.User;
import org.example.dto.UserFilter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), QUser.user.firstname::containsIgnoreCase)
                .add(filter.lastname(), QUser.user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), QUser.user.birthDate::before)
                .build();
//
//        return new JPAQuery<User>(entityManager)
//                .select(user)
//                .from(user)
//                .where(predicate)
//                .fetch();
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);

        var user = criteria.from(User.class);
        criteria.select(user);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.firstname() != null && !filter.firstname().isBlank()) {
            predicates.add(cb.like(user.get("firstname"), filter.firstname()));
        }
        if (filter.lastname() != null && !filter.lastname().isBlank()) {
            predicates.add(cb.like(user.get("lastname"), filter.lastname()));
        }
        if (filter.birthDate() != null) {
            predicates.add(cb.lessThan(user.get("birthDate"), filter.birthDate()));
        }
        criteria.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(criteria).getResultList();
    }


}
