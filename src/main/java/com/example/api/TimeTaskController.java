package com.example.api;

import com.example.service.TimeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TimeTaskController {

    @Autowired
    private TimeTaskService timeTaskService;

    @GetMapping("/timetask")
    public ResponseEntity<String> timetaskAction() {
        try {
            timeTaskService.timetask();
            return ResponseEntity.ok("Time task executed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error executing time task: " + e.getMessage());
        }
    }

    @GetMapping("/resetSql")
    public ResponseEntity<String> resetSqlAction() {
        try {
            timeTaskService.resetSql();
            return ResponseEntity.ok("Reset SQL task executed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error executing reset SQL task: " + e.getMessage());
        }
    }
}
