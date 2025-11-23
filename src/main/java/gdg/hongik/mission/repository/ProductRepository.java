package gdg.hongik.mission.repository;

import gdg.hongik.mission.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Product 엔티티에 대한 데이터베이스 접근을 담당하는 리포지토리
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * 상품명으로 상품을 조회
     */
    Optional<Product> findByName(String name);

    /**
     * 상품명으로 상품의 존재 여부를 확인
     */
    boolean existsByName(String name);
}