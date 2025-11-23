package gdg.hongik.mission.controller;

import gdg.hongik.mission.service.ProductService;
import gdg.hongik.mission.dto.*; // 모든 DTO를 import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;



 // 공통

/**
 * 쇼핑몰 재고 관련 API 요청을 처리하는 컨트롤러
 * 재고 검색, 구매, 등록, 추가, 물품 삭제 기능을 제공한다
 */
@Tag(name = "쇼핑몰 API", description = "재고 검색, 구매, 등록, 추가, 물품삭제를 지원")
@RestController
@RequestMapping("/products")
public class ProductController{
    //--------------------------------------
    // 1. 재고검색, GET /product?name=string
    // --------------------------------------
    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }
    // 의존성 주입

    /**
     * 재고를 검색하는 매서드
     * @param name 검색할 물건의 이름
     * @return 메소드 정상 수행 성공시 상품반환
     */
    @Operation( // 각 API에 대한 설명을 추가할 수 있음
            summary = "재고 검색",
            description = "이름 기준으로 물품 재고 검색",
            responses = { // response = HTTP 상태코드에 대해서 우리가 반환해주는 값
                    @ApiResponse(responseCode = "200", description = "검색성공",
                            // 200 OK가 되었을떄 어떤 응답을 보여줄 것인지
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                            // content = 무엇을 정의 하는가  -> ApiResonse나 @RequestBody 같은 어노테이션 내부에 위치함
                            // schema = @Schema -> content 가 따르는 데이터 모델 정의
                            // @Schema = 데이터 객체의 구조(필드이름, 타입등 설명)
                            // implement = 스키마가 자바의 어떤 클래스의 구조를 따르는지
                    )
            }
    )
    // GetMapping = 메소드위에 붙어서 메소드를 실행하는역할
    // Opration = 기능 설명 제공
    // parameter = 변수

    @GetMapping
    public ProductDto searchStock(
            @Parameter(description = "검색할 물건의 이름", example = "apple") // swagger용
            @RequestParam String name
    ){
        return service.searchStock(name);
        // service계층에서 searchStock(name)호출

    }


    //--------------------------------------
    // 2. 재고구매 POST /products
    //--------------------------------------

    /**
     * 재고를 구매하는 메서드
     * @param request 구매할 물품의 목록과 수량
     * @return 구매하는 물품의 총금액, 이름, 구매개수, 해당물건의 총가격 반환
     */
    @Operation(
        summary = "재고구매",
        description = "물품을 구매하고 총 금액과 개별 사용 금액을 나타냄",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( // 💡 @RequestBody 어노테이션 사용
                description = "구매할 물품 목록과 수량",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = PurchaseOrderRequest.class)
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "구매 성공 및 결과 반환",
                        content = @Content(
                                schema = @Schema(implementation = PurchaseResponse.class))
                    )
            }
    )
    @PostMapping
    public PurchaseResponse buyItems(
        @RequestBody PurchaseOrderRequest request
        // 요청 본분 Body의 Json을 객체로 받음
    ) {
        return service.buyItems(request);
        // service 계층에서 구매과정을 처리함 -> 총 금액, 개별 금액 사용량 반환, 재고 차감
    }

    //--------------------------------------
    // 3.재고등록 POST /products
    //--------------------------------------

    /**
     * 재고를 등록하는 매서드
     * @param request 등록할 재고물품의 이름, 가격, 수량
     */
    @Operation(
        summary = "새 물품 재고 등록",
        description = "새로운 물품의 이름, 가격, 초기 재고를 등록합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "등록할 물품의 상세 정보",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = ProductRegistrationRequest.class)
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "등록 성공 (응답 본문 없음)"
                )
            }
    )
    @PostMapping("/register") // 공통 URL외에 다르면 구분해줘야함
    public void registerProduct(
        @RequestBody ProductRegistrationRequest request
    ) {
        service.registerProduct(request);
    }

    //--------------------------------------
    // 4. 재고 추가 PATCH /products/{id}
    //--------------------------------------

    /**
     * 재고를 추가하는 매서드
     * @param id 재고를 추가하고자 하는 물품의 id(고유코드)
     * @param request 추가하기를 원하는 수량
     * @return 재고를 추가한 물품의 id, 이름, 가격, 추가 후 총 재고수
     */
    @Operation(
        summary = "기존 물품 재고 추가",
        description = "특정 ID를 가진 물품의 재고를 지정된 수량만큼 늘립니다.",
        // URL 경로에 들어가는 파라미터 정의
        parameters = {
                @Parameter(name = "id", description = "재고를 추가할 물품의 고유 ID", example = "20")
        },
        // INPUT 정의
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "추가할 재고 수량",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = StockAddRequest.class)
                )
        ),
        // OUTPUT 정의
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "재고 추가 성공 및 결과 반환",
                        content = @Content(
                                schema = @Schema(implementation = StockAddResponse.class) // 응답 DTO 사용
                            )
                    )
            }
    )
    @PatchMapping("/{id}") // id를 받아올거임
    public StockAddResponse addStock(
        @PathVariable Integer id, // URL 경로에서 ID를 받음 {id}자리에 들어오는 값!
        @RequestBody StockAddRequest request
        // ex -> 요청 PATCH/produxt/1 + body{addStock:50} 2개를 같이 처리할거임
    ) {
        return service.addStock(id, request);
    }

    //--------------------------------------
    // 5. 물품 삭제 (DELETE /prodcts)
    //--------------------------------------

    /**
     * 물품을 삭제하는 매서드
     * @param request 삭제를 원하는 물품의 이름목록 (1개 이상일 수 있음)
     * @return 남아있는 물품들의 id, 이름, 재고수 반환
     */
    @Operation(
        summary = "물품 삭제",
        description = "요청 본문의 이름 목록을 받아 해당 물품들을 시스템에서 삭제합니다.",
        // INPUT 정의
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "삭제할 물품 이름 목록",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = DeleteRequest.class) // 입력 DTO 사용
                )
        ),
        // OUTPUT 정의
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공 및 잔여 물품 정보 반환",
                        content = @Content(
                                schema = @Schema(implementation = DeleteResponse.class) // 응답 DTO 사용
                        )
                )
        }
    )
    @DeleteMapping
    public DeleteResponse deleteProducts(
        @RequestBody DeleteRequest request
        // 여러 상품을 한 번에 삭제하기 위해서 Body로 받아야함
    ) {
        return service.deleteProducts(request);
    }

}// 전체 닫는 괄호