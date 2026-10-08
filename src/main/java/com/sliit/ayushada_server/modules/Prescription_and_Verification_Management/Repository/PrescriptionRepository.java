package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Repository;

import com.sliit.ayushada_server.Entity.Prescription;
import com.sliit.ayushada_server.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByOrderByUploadAtDesc();
    List<Prescription> findByCustomer_UserIdOrderByUploadAtDesc(String userId);
    boolean existsByCustomer(User user);
    void deleteByCustomer(User user);
}