package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "재고 추가 최종 결과",
        example = """
        {
          "item":{
            "id": 10,
            "name": "strawberry",
            "price": 3000,
            "stock": 70
          }
        }
    """
)
public class StockAddResponse {
    @Schema(description = "재고 추가 후 물품 정보")
    private ProductDto item;
}