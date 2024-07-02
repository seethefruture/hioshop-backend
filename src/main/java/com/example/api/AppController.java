package com.example.api;

import com.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping("/appInfo")
    public ResponseEntity<?> getAppInfo() {
        Map<String, Object> data = appService.getAppInfo();
        return ResponseEntity.ok(data);
    }
}
