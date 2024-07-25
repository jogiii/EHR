package com.electronichealth.repository;

import com.electronichealth.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p JOIN FETCH p.medicalRecords WHERE p.id = :id")
    Optional<Patient> findByIdWithMedicalRecords(Long id);

    // Fetch all patients with their medical records
    @Query("SELECT p FROM Patient p JOIN FETCH p.medicalRecords")
    List<Patient> findAllWithMedicalRecords();
}

