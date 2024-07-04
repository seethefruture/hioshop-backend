package com.example.api;

import com.example.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/index")
    public ResponseEntity<?> index() {
        try {
            return ResponseEntity.ok(noticeService.getAllNotices());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/updateContent")
    public ResponseEntity<?> updateContent(@RequestParam Long id, @RequestParam String content) {
        try {
            return ResponseEntity.ok(noticeService.updateContent(id, content));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam String content, @RequestParam String time) {
        try {
            return ResponseEntity.ok(noticeService.add(content, time));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam String id, @RequestParam String content, @RequestParam String time) {
        try {
            return ResponseEntity.ok(noticeService.update(id, content, time));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/destroy")
    public ResponseEntity<?> destroy(@RequestParam Long id) {
        try {
            noticeService.destroy(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
