package test.service.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
          User findUserById(int id);
          User findUserByName(String name);
}
