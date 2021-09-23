package event.order;

import dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private OrderDto order;
    private OrderStatus orderStatus;
}
