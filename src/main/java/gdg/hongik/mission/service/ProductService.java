package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller에서 받은 요청을 처리하고 다시 돌려줍니다
 * 캡슐화
 */
@Service
public class ProductService {

    // 메모리에 상품 저장 -> 임의의 값이 들어와도 작동하도록,,,<<
    private final Map<String, ProductDto> products = new HashMap<>();

    // 생성자 - 초기 데이터 설정
    public ProductService() {
        ProductDto apple = new ProductDto();
        apple.setId(1);
        apple.setName("apple");
        apple.setPrice(1000);
        apple.setStock(100);
        products.put("apple", apple);

        ProductDto banana = new ProductDto();
        banana.setId(2);
        banana.setName("banana");
        banana.setPrice(2000);
        banana.setStock(50);
        products.put("banana", banana);
    }

    /**
     * 상품명으로 재고 검색
     * @param name 검색할 상품의 이름
     * @return 검색된 상품의 정보, 없으면 null 반환
     */
    public ProductDto searchStock(String name) {
        System.out.println("name: " + name);

        // 실제라면 DB에서 상품 찾아서 반환
        ProductDto product = products.get(name);
        return product;
    }

    /**
     *여러상품을 구매할 수 있음
     * 각 상품의 가격과 수량을 계산 후 총 구매액과 개별 비용을 반환
     * @param request 구매할 상품 목록이 담긴 요청
     * @return 총 구매액과 상품별 구매 내역이 담긴 응답
     */
    public PurchaseResponse buyItems(PurchaseOrderRequest request) {
        System.out.println("구매 요청 목록 크기: " + request.getItems().size() + "개");

        // 응답 객체 생성
        PurchaseResponse response = new PurchaseResponse();
        List<PurchasedItemResponse> purchasedItems = new ArrayList<>();
        int totalCost = 0;

        // 각 아이템별로 처리
        for (PurchaseOrderItem item : request.getItems()) {
            // 상품 정보 가져오기 (미리 저장된 products에서)
            ProductDto product = products.get(item.getName());

            // 개별 상품 비용 계산 (가격 × 수량)
            int itemCost = product.getPrice() * item.getQuantity();

            // 응답 아이템 생성
            PurchasedItemResponse purchaseItem = new PurchasedItemResponse();
            purchaseItem.setName(item.getName());
            purchaseItem.setQuantity(item.getQuantity());
            purchaseItem.setCost(itemCost);

            // 리스트에 추가
            purchasedItems.add(purchaseItem);
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
        // 🟢 실제 등록 로직 수행
        System.out.println("새 물품 등록 요청: "
                + request.getName() + ", 가격: " + request.getPrice() + ", 재고: " + request.getStock());

        // 새 상품 객체 생성
        ProductDto newProduct = new ProductDto();
        newProduct.setId(products.size() + 1);  // 간단하게 ID 생성
        newProduct.setName(request.getName());
        newProduct.setPrice(request.getPrice());
        newProduct.setStock(request.getStock());

        // products Map에 저장
        products.put(newProduct.getName(), newProduct);
    }

    /**
     * 기존 상품의 재고를 추가
     * 상퓸 ID로 상품을 찾아서 재고 수량을 증가시킴
     * @param id 재고를 추가할 상품의 고유 ID
     * @param request 추가할 재고 수량이 담긴 요청 객체
     * @return 재고 추가 후 업데이트된 상품 정보
     */
    public StockAddResponse addStock(Integer id, StockAddRequest request) {
        // 🟢 실제 재고 추가 로직 수행
        System.out.println("ID " + id + "번 물품에 재고 " + request.getAddStock() + " 추가 요청");

        // ID로 상품 찾기
        ProductDto product = null;
        for (ProductDto p : products.values()) {
            if (p.getId().equals(id)) {
                product = p;
                break;
            }
        }

        // 재고 추가
        product.setStock(product.getStock() + request.getAddStock());

        // 응답 생성
        StockAddResponse response = new StockAddResponse();
        response.setItem(product);

        return response;
    }

    /**
     * 여러 상품을 삭제
     * 삭제할 상품 이름 목록을 받아 상품들을 삭제, 남은 상품목록 반환
     * @param request 삭제할 상품 이름 목록이 담긴 요청
     * @return 삭제 후 남아있는 상품 목록
     */
    public DeleteResponse deleteProducts(DeleteRequest request) {
        // 🟢 실제 삭제 로직 수행
        System.out.println("삭제 요청된 물품 목록: " + request.getNames());

        // 요청된 이름들을 products Map에서 삭제
        for (String name : request.getNames()) {
            products.remove(name);
        }

        // 삭제 후 남은 상품 목록 생성
        List<DeleteResponse.RemainingItemDto> remainingItems = new ArrayList<>();
        for (ProductDto product : products.values()) {
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

