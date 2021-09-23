package dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequestDto {
    private UUID userId;
    private UUID productId;
    private UUID orderId;
}
