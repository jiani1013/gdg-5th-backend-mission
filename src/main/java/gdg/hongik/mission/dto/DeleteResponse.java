package gdg.hongik.mission.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "물품 삭제 결과")
public class DeleteResponse {
    @Schema(description = "삭제 후 남아있는 물품 목록")
    private List<RemainingItemDto> remainItems;
    //------------------------------------------------------------------
    @Setter
    @Getter
    @Schema(description = "삭제 후 남아있는 개별 물품 정보")
    public static class RemainingItemDto {

        @Schema(description = "물품 ID", example = "10")
        private Integer id;
        @Schema(description = "물품 이름", example = "strawberry")
        private String name;
        @Schema(description = "잔여 재고 수량", example = "70")
        private Integer stock;
    }
}