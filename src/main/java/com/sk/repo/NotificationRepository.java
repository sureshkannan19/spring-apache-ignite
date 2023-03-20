package com.sk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	List<Notification> findByContentLike(String keyword);

}
