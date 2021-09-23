package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OrderDto {
    private UUID orderId;
    private UUID productId;
    private UUID userId;
    private Integer price;
}
