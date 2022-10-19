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
    String SEARCH_FROM = "from " +
            "(select u.id, u.email, u.login, u.password, count(*) as roles_count " +
            "from users u " +
            "join users_roles ur on u.id = ur.user_id " +
            "join roles r on r.id = ur.role_id " +
            "group by u.id, u.email, u.login, u.password) as t1 ";

    String COMPARE_ROLES_COUNT = " where t1.roles_count < :roleSize ";
    String AND_EMAIL_OR_LOGIN_LIKE = " and login LIKE :searchValue or email LIKE :searchValue";

    String SEARCH_USERS_DATA = "select t1.id, t1.email, t1.login, t1.password " + SEARCH_FROM;

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    @Query(value = SEARCH_USERS_DATA + COMPARE_ROLES_COUNT + " order by login",
            countQuery = "select count(*) " + SEARCH_FROM,
            nativeQuery = true)
    Page<User> findAllWhereUsersHaveSameOrLessRolesCount(@Param("roleSize") int roleSize, Pageable pageable);

    @Query(value = SEARCH_USERS_DATA + COMPARE_ROLES_COUNT + AND_EMAIL_OR_LOGIN_LIKE + " order by login",
            countQuery = "select count(*) " + SEARCH_FROM + COMPARE_ROLES_COUNT + AND_EMAIL_OR_LOGIN_LIKE,
            nativeQuery = true)
    Page<User> findAllWhereUsersHaveSameOrLessRolesCountAndEmailLikeOrLoginLike(@Param("roleSize") int roleSize,
                                                                                @Param("searchValue") String searchValue,
                                                                                Pageable pageable);


}
