package com.sk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByContentContaining(String keyword);

}
