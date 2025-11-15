package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// DTO 에서 예시 보여주고 싶으면 getter setter 다 설정해줘야함
// Schema 섹션: DTO에서 정의한 내용들이 들어가 있음

@Setter
@Getter
@Schema(description = "개별 물품의 상세 정보")
public class ProductDto {

    @Schema(description = "물건별 고유 ID", example = "1")
    private Integer id;
    @Schema(description = "물건 이름", example = "apple")
    private String name;
    @Schema(description = "물건 가격", example = "1000")
    private Integer price;
    @Schema(description = "물건 재고수", example = "100")
    private Integer stock;

}
