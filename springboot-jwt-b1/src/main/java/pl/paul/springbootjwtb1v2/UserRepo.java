package pl.paul.springbootjwtb1v2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepo extends CrudRepository<User, Long> {

   User findByLogin(String login);
}
