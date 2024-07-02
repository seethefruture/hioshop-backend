package com.example.api;

import com.example.po.Address;
import com.example.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 查询某用户所有的收货地址
     *
     * @return .
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAddresses() {
        String userId = getLoginUserId();
        List<Address> addressList = addressService.getAddresses(userId);
        return ResponseEntity.ok(addressList);
    }

    /**
     * 用户新增/编辑收货地址
     *
     * @param address 。
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveAddress(@RequestBody Address address) {
        String userId = getLoginUserId();
        Address savedAddress = addressService.saveAddress(userId, address);
        return ResponseEntity.ok(savedAddress);
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<?> deleteAddress(@RequestParam String id) {
        boolean success = addressService.deleteAddress(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.status(500).build();
    }

    /**
     * 地址详情
     *
     * @param id .
     * @return
     */
    @GetMapping("/detail")
    public ResponseEntity<?> addressDetail(@RequestParam String id) {
        String userId = getLoginUserId();
        Address address = addressService.getAddressDetail(userId, id);
        return ResponseEntity.ok(address);
    }

    private String getLoginUserId() {
        // 假设有一个方法可以获取当前登录的用户ID
        return ""; // 示例，实际应从认证上下文中获取
    }
}
