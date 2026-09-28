package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository;

import com.sliit.ayushada_server.Entity.Category;
import com.sliit.ayushada_server.Entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  }