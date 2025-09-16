package sda.academy.restdemo.repository;

import sda.academy.restdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Product p set p.category = null where p.category.id = :catId ")
    int clearCategoryByCategoryID(@Param("catId") Integer catId);
}
