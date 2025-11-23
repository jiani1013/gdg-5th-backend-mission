package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.*;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * controller에서 받은 요청을 처리하고 다시 돌려줍니다
 * 캡슐화
 */
@Service // 서비스 계층임을 스프링에 알려줌
@Transactional // 클래스 전체에 트랜잭션을 적용함 -> 중간에 오류나면 다시 돌아옴
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // ProductRepository라는 의존성을 주입받음
    // 그니까 Service는 Repository통해 DB에 접근할 수 있게됨

    /**
     * 상품명으로 재고 검색
     * @param name 검색할 상품의 이름
     * @return 검색된 상품의 정보, 없으면 null 반환
     */
    @Transactional(readOnly = true)
    // 찾는거니까 읽기만 데이터 변경없이 읽기만 하면됨
    public ProductDto searchStock(String name) {
        // controller에서 이야기한 searchStock의 내부 구조 구현
        System.out.println("name: " + name);

        // DB에서 상품 찾아서 반환
        Product product = productRepository.findByName(name).orElse(null);
        // Repository의 메서드를 호출하는데 DB에서 name으로 상품을 검색
        // 상품이 있으면 Product 객체를 반환, 없으면 null반환함

        if (product == null) {
            return null;
        }

        // Entity를 DTO로 변환
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());

        return productDto;
    }

    /**
     *여러상품을 구매할 수 있음
     * 각 상품의 가격과 수량을 계산 후 총 구매액과 개별 비용을 반환
     * @param request 구매할 상품 목록이 담긴 요청
     * @return 총 구매액과 상품별 구매 내역이 담긴 응답
     */
    public PurchaseResponse buyItems(PurchaseOrderRequest request) {
        System.out.println("구매 요청 목록 크기: " + request.getItems().size() + "개");
        // 몇가지의 물건을 구매할것인지

        // 응답 객체 생성
        PurchaseResponse response = new PurchaseResponse();
        // 동적으로 일단 빈 응답 객체를 생성해서 나중에 결과를 담아서 controller한테 보냄
        List<PurchasedItemResponse> purchasedItems = new ArrayList<>();
        // 동적으로 아이템들의 정보를 담을 리스트를 만듦 (상품명, 수량, 금액)

        int totalCost = 0;

        // 각 아이템별로 처리
        for (PurchaseOrderItem item : request.getItems()) {
            // 구매 요청 목록을 가져오는데 리스트에서 하나씩 꺼내서 item에 담음
            Product product = productRepository.findByName(item.getName()).orElse(null);
            // Repository통해서 DB에서 상품찾기

            if (product == null) {
                throw new RuntimeException("상품을 찾을 수 없습니다: " + item.getName());
            }

            // 재고 확인 및 차감
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("재고가 부족합니다: " + item.getName());
                // 재고 부족하면 back
            }
            product.setStock(product.getStock() - item.getQuantity());
            // product.getStock() = 현재 재고 가져오기
            // getQuantity() = 구매할 수량
            productRepository.save(product);
            // product의 재고를 변경 후 저장

            // 개별 상품 비용 계산 (가격 × 수량)
            int itemCost = product.getPrice() * item.getQuantity();

            // 응답 아이템 생성
            PurchasedItemResponse purchaseItem = new PurchasedItemResponse();
            purchaseItem.setName(item.getName()); // 이름저장
            purchaseItem.setQuantity(item.getQuantity()); // 구매수량설정
            purchaseItem.setCost(itemCost); // 개별금액 설정

            // 리스트에 추가
            purchasedItems.add(purchaseItem); // 아까 만든 빈 리스트에 저장
            totalCost += itemCost;
        }

        // 최종 응답 설정
        response.setTotalCost(totalCost);
        response.setItems(purchasedItems);

        return response;
    }


    /**
     * 새로운 상품을 등록
     * 상품명, 가격, 초기 재고 정보를 받아 시스템에 저장
     * @param request 등록할 상품의 정보가 담긴 요청 객체
     */
    public void registerProduct(ProductRegistrationRequest request) {
        System.out.println("새 물품 등록 요청: "
                + request.getName() + ", 가격: " + request.getPrice() + ", 재고: " + request.getStock());

        // 중복 체크
        if (productRepository.existsByName(request.getName())) {
            // request.getName = 같은 이름의 상품이 이미 존재하는지 확인하기 위해서
            throw new RuntimeException("이미 존재하는 상품입니다: " + request.getName());
        }

        // 새 상품 객체 생성
        Product newProduct = new Product();
        // DB에 저장하기위해서 새로운 Entity객체 생성
        // JPA에서는 Entity만 DB에 저장할 수 있음
        newProduct.setName(request.getName());
        newProduct.setPrice(request.getPrice());
        newProduct.setStock(request.getStock());
        // id는 DB가 자동으로 생성

        // DB에 저장
        productRepository.save(newProduct);
    }

    /**
     * 기존 상품의 재고를 추가
     * 상퓸 ID로 상품을 찾아서 재고 수량을 증가시킴
     * @param id 재고를 추가할 상품의 고유 ID
     * @param request 추가할 재고 수량이 담긴 요청 객체
     * @return 재고 추가 후 업데이트된 상품 정보
     */
    public StockAddResponse addStock(Integer id, StockAddRequest request) {
        // id와 얼마나 추가할지를 받을 거임(입력파라미터)
        System.out.println("ID " + id + "번 물품에 재고 " + request.getAddStock() + " 추가 요청");

        // ID로 DB에서 상품 찾기
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        // 재고 추가(상품이 존재한다면)
        product.setStock(product.getStock() + request.getAddStock());
        // product -> 현재 DB에 존재하는 값, request -> 추가하고 싶은 수량 받아오기
        productRepository.save(product);
        // 변경된 Product 객체를 DB에 저장함

        // Entity를 DTO로 변환
        ProductDto productDto = new ProductDto();
        // DTO객체를 생성 새로만들어서 위에서 만든 Entity값을 넣고 다시 클라이언트한테 반환해줌
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());

        // 응답 생성
        StockAddResponse response = new StockAddResponse();
        response.setItem(productDto);

        return response;
    }

    /**
     * 여러 상품을 삭제
     * 삭제할 상품 이름 목록을 받아 상품들을 삭제, 남은 상품목록 반환
     * @param request 삭제할 상품 이름 목록이 담긴 요청
     * @return 삭제 후 남아있는 상품 목록
     */
    public DeleteResponse deleteProducts(DeleteRequest request) {
        System.out.println("삭제 요청된 물품 목록: " + request.getNames());

        // 요청된 이름들을 DB에서 삭제
        for (String name : request.getNames()) {
            Product product = productRepository.findByName(name).orElse(null);
            if (product != null) {
                productRepository.delete(product);
            }
        }

        // 삭제 후 남은 상품 목록 조회
        List<Product> remainingProducts = productRepository.findAll();
        List<DeleteResponse.RemainingItemDto> remainingItems = new ArrayList<>();

        for (Product product : remainingProducts) {
            DeleteResponse.RemainingItemDto item = new DeleteResponse.RemainingItemDto();
            item.setId(product.getId());
            item.setName(product.getName());
            item.setStock(product.getStock());
            remainingItems.add(item);
        }

        // 응답 생성
        DeleteResponse response = new DeleteResponse();
        response.setRemainItems(remainingItems);

        return response;
    }
}