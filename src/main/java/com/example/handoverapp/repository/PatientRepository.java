package com.example.handoverapp.repository;

import com.example.handoverapp.entity.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    // @Query annotation used to specify custom SQL query using special JPA syntax
    @Query("select p from Patient p where p.mrn = ?1")
    Optional<Patient> findByMrn(String mrn);

}