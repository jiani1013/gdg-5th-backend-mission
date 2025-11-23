package gdg.hongik.mission.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "물품 구매 요청 전체",
        example = """
        {
          "items": [
            { "name": "apple", "quantity": 3 },
            { "name": "banana", "quantity": 2 }
          ]
        }
    """
)
public class PurchaseOrderRequest {
    @Schema(description = "구매할 물품 목록")
    private List<PurchaseOrderItem> items;
}