package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "구매된 개별 물품 결과 (OUTPUT 목록 항목)")
public class PurchasedItemResponse {
    @Schema(description = "물건 이름", example = "apple")
    private String name;
    @Schema(description = "구매 개수", example = "3")
    private Integer quantity;
    @Schema(description = "해당 물건의 총 가격", example = "3000") // 명세의 'cost'
    private Integer cost;

}