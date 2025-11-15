package gdg.hongik.mission.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "물품 삭제 요청 (INPUT)")
public class DeleteRequest {
    @Schema(description = "삭제할 물품 이름 목록 (1개 이상)", example = "[\"apple\", \"banana\"]")
    private List<String> names;
}