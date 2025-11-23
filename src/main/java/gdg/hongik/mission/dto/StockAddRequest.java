package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "기존 물품 재고 추가 요청")
public class StockAddRequest {
    @Schema(description = "추가할 재고 수", example = "20")
    private Integer addStock;
}
