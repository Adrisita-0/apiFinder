package grupoExpo.API.Repositories.Reservas;

import grupoExpo.API.Entities.Reservas.ReservasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasRepo extends JpaRepository<ReservasEntity, Long> {
}
