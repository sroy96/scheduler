package com.voltmoney.scheduler.repository;

import com.voltmoney.scheduler.dao.ServiceOperator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOperatorRepository extends JpaRepository<ServiceOperator,Long> {
}
