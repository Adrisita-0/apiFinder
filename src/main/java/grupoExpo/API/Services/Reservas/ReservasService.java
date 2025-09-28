package grupoExpo.API.Services.Reservas;


import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Models.DTO.ReservasDto;
import grupoExpo.API.Repositories.Reservas.ReservasRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservasService {

    @Autowired
    private ReservasRepo repo;

    public List<ReservasDto> getAllReservas(){
        List <ReservasEntity> reservas = repo.findAll();
        return reservas.stream()
                .map(this :: convertirReservasADTO)
                .collect(Collectors.toList());
    }

    public ReservasDto convertirReservasADTO(ReservasEntity reservas){
        ReservasDto Rdto = new ReservasDto();
        Rdto.setIdReserva(reservas.getIdReserva());
        Rdto.setIdCliente(reservas.getIdCliente());
        Rdto.setIdEstadoReserva(reservas.getIdEstadoReserva());
        Rdto.setIdMetodoPago(reservas.getIdMetodoPago());
        Rdto.setFechaReserva(reservas.getFechaReserva());
        Rdto.setPrecioTotalReserva(reservas.getPrecioTotalReserva());
        return Rdto;
    }
}
