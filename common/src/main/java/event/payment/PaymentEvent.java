package event.payment;

import dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private PaymentDto payment;
    private PaymentStatus paymentStatus;
}
