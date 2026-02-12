package com.worldcup.tracker.controller;

import com.worldcup.tracker.service.LiveScoreDataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/sync")
public class DataSyncController {

    private final LiveScoreDataSyncService syncService;

    public DataSyncController(LiveScoreDataSyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/all")
    public ResponseEntity<Map<String, String>> syncAll() {
        log.info("Manual sync triggered via API");
        
        try {
            syncService.forceSyncAll();
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Data synchronization completed successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during manual sync: {}", e.getMessage(), e);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error during synchronization: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/live")
    public ResponseEntity<Map<String, String>> syncLiveMatches() {
        log.info("Live matches sync triggered via API");
        
        try {
            syncService.syncLiveMatches();
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Live matches synchronized successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during live matches sync: {}", e.getMessage(), e);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error during synchronization: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getSyncStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "active");
        response.put("message", "Data sync service is running. Automatic sync every 5 minutes for live matches.");
        
        return ResponseEntity.ok(response);
    }
}
