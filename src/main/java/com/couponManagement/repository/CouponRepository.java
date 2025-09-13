package com.couponManagement.repository;

import com.couponManagement.entity.Coupon;
import com.couponManagement.constants.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c LEFT JOIN FETCH c.cartWiseDetails LEFT JOIN FETCH c.productWiseDetails LEFT JOIN FETCH c.bxGyDetails WHERE c.couponCode = :couponCode")
    Optional<Coupon> findByCouponCodeWithDetails(@Param("couponCode") String couponCode);
    
    @Query("SELECT c FROM Coupon c LEFT JOIN FETCH c.cartWiseDetails LEFT JOIN FETCH c.productWiseDetails LEFT JOIN FETCH c.bxGyDetails WHERE c.id = :id")
    Optional<Coupon> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT c FROM Coupon c LEFT JOIN FETCH c.cartWiseDetails LEFT JOIN FETCH c.productWiseDetails LEFT JOIN FETCH c.bxGyDetails")
    List<Coupon> findAllWithDetails();
    
    @Query("SELECT c FROM Coupon c LEFT JOIN FETCH c.cartWiseDetails LEFT JOIN FETCH c.productWiseDetails LEFT JOIN FETCH c.bxGyDetails WHERE c.isActive = true")
    List<Coupon> findByIsActiveTrueWithDetails();
    
    Optional<Coupon> findByCouponCode(String couponCode);

    List<Coupon> findByIsActiveTrue();

    List<Coupon> findByCouponTypeAndIsActiveTrue(CouponType couponType);

    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.couponCode = :couponCode")
    Optional<Coupon> findActiveByCouponCode(@Param("couponCode") String couponCode);

    boolean existsByCouponCode(String couponCode);

    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.isActive = true AND c.couponCode = :couponCode")
    long countActiveByCouponCode(@Param("couponCode") String couponCode);
}
