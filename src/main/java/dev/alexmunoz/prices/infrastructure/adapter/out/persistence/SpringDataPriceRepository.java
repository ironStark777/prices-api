package dev.alexmunoz.prices.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query("""
            select p from PriceEntity p
            where p.brandId = :brandId
              and p.productId = :productId
              and :applicationDate between p.startDate and p.endDate
            """)
    List<PriceEntity> findApplicable(@Param("brandId") long brandId,
                                     @Param("productId") long productId,
                                     @Param("applicationDate") LocalDateTime applicationDate);
}
