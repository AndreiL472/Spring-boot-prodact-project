package sda.academy.restdemo.repository;

import sda.academy.restdemo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


}
