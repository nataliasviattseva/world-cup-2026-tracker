package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.ApiCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ApiCacheRepository extends JpaRepository<ApiCache, Long> {

    Optional<ApiCache> findByUrl(String url);

    @Modifying
    @Query("DELETE FROM ApiCache a WHERE a.expiresAt < :now")
    void deleteExpiredCache(LocalDateTime now);

    @Query("SELECT a FROM ApiCache a WHERE a.url = :url AND a.expiresAt > :now")
    Optional<ApiCache> findValidCacheByUrl(String url, LocalDateTime now);
}
