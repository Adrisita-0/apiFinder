package grupoExpo.API.Repositories.User;

import grupoExpo.API.Entities.User.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Page<UserEntity> findAll(Pageable pageable);
    boolean existsByCorreo(String  correoUsuario);
    Optional<UserEntity>findBycorreoUsuario(String correoUsuario);
}
