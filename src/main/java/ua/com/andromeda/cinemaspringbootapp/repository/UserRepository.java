package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    @Query(value = "select t1.id, t1.email, t1.login, t1.password " +
            "from " +
            "(select u.id, u.email, u.login, u.password, count(*) as roles_count " +
            "from users u " +
            "join users_roles ur on u.id = ur.user_id " +
            "join roles r on r.id = ur.role_id " +
            "group by u.id, u.email, u.login, u.password) as t1 " +
            "where t1.roles_count <= :roleSize " +
            "order by login",

            countQuery = "select count(*) " +
                    "from " +
                    "(select u.id, u.email, u.login, u.password, count(*) as roles_count " +
                    "from users u " +
                    "join users_roles ur on u.id = ur.user_id " +
                    "join roles r on r.id = ur.role_id " +
                    "group by u.id, u.email, u.login, u.password) as t1 " +
                    " where t1.roles_count <= :roleSize",
            nativeQuery = true)
    Page<User> findAllWhereUsersHaveSameOrLessRoles(@Param("roleSize") int roleSize, Pageable pageable);
}
