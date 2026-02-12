-- API Cache Table for Live Score API responses
-- This table stores cached API responses to reduce API calls and improve performance

CREATE TABLE IF NOT EXISTS `api_cache` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(500) NOT NULL,
  `json_response` LONGTEXT NOT NULL,
  `cached_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cache_url` (`url`),
  INDEX `idx_cache_url` (`url`),
  INDEX `idx_cache_time` (`cached_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
