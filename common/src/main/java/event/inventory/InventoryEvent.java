package event.inventory;

import dto.InventoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private InventoryDto inventory;
    private InventoryStatus status;
}
