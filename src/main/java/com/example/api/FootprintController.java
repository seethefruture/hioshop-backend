package com.example.api;

import com.example.service.FootprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/footprint")
public class FootprintController {

    @Autowired
    private FootprintService footprintService;

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFootprint(@RequestBody Map<String, String> payload) {
        String footprintId = payload.get("footprintId");
        footprintService.deleteFootprint(footprintId);
        return ResponseEntity.ok("删除成功");
    }

    @GetMapping("/list")
    public ResponseEntity<?> listFootprints(@RequestParam int page, @RequestParam int size) {
        String userId = getUserIdFromSession(); // Implement your session handling logic here
        return ResponseEntity.ok(footprintService.listFootprints(userId, page, size));
    }

    // Dummy method to simulate getting user ID from session (replace with actual logic)
    private String getUserIdFromSession() {
        return ""; // Replace with your session handling logic
    }
}
