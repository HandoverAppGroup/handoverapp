package com.example.handoverapp.repository;


import com.example.handoverapp.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t order by t.dateCreated desc")
    Page<Task> findAllOrderByDate(Pageable pageable);

    @Query("select t from Task t where t.patient.mrn = ?1 order by t.dateCreated desc")
    Page<Task> findByPatientMrn(String mrn, Pageable pageable);

    @Query("select t from Task t where t.completed = ?1 order by t.dateCreated desc")
    Page<Task> findByCompleted(boolean isCompleted, Pageable pageable);

    @Query("select t from Task t where t.dateCreated >= ?1 and t.dateCreated <= ?2 order by t.dateCreated asc")
    Page<Task> findTasksBetween(Date date1, Date date2, Pageable pageable);

}
