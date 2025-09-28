package grupoExpo.API.Entities.Reservas;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Entity
@Table(name = "TBRESERVAS")
@Getter @Setter @ToString @EqualsAndHashCode
public class ReservasEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Reservas")
    @SequenceGenerator(name = "seq_Reservas", sequenceName = "seq_Reservas", allocationSize = 1)
    @Column(name = "IDRESERVA")
    private Long idReserva;
    @Column(name = "IDCLIENTE")
    private Long idCliente;
    @Column(name = "IDESTADORESERVA")
    private Long idEstadoReserva;
    @Column(name = "IDMETODOPAGO")
    private Long idMetodoPago;
    @Column(name = "FECHARESERVA")
    private Date fechaReserva;
    @Column(name = "PRECIOTOTALRESERVA")
    private double precioTotalReserva;
}
