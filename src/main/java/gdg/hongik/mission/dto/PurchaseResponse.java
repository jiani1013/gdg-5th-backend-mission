package gdg.hongik.mission.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "최종 물품 구매 결과 (OUTPUT)",
        example = """
        {
          "totalCost": 7000,
          "items": [
            {
              "name": "apple",
              "quantity": 3,
              "cost": 3000
            },
            {
              "name": "banana",
              "quantity": 2,
              "cost": 4000
            }
          ]
        }
    """
)
public class PurchaseResponse {
    @Schema(description = "전체 주문의 최종 총 구매액", example = "7000")
    private Integer totalCost;

    @Schema(description = "물건별 소비 금액 목록")
    private List<PurchasedItemResponse> items;
}