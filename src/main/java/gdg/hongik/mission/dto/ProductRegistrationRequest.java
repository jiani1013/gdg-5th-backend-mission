package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "새 물품 재고 등록 요청")
public class ProductRegistrationRequest {
    @Schema(description = "물건 이름", example = "strawberry")
    private String name;
    @Schema(description = "물건 가격", example = "3000")
    private Integer price;
    @Schema(description = "초기 재고수", example = "50")
    private Integer stock;

}

