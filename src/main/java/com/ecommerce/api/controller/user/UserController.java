package com.ecommerce.api.controller.user;

import com.ecommerce.api.model.DataChange;
import com.ecommerce.model.Address;
import com.ecommerce.model.Dao.AddressDao;
import com.ecommerce.model.localuser;
import com.ecommerce.services.userService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private AddressDao addressDao;
    private SimpMessagingTemplate simpMessagingTemplate;
    private userService usve;
    @GetMapping("/{userId}/address")
     public ResponseEntity<List<Address>> getAddress(
            @AuthenticationPrincipal localuser user, @PathVariable Long userId){
        if(!usve.userHasPermissionToUser(user,userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return   ResponseEntity.ok(addressDao.findByUser_Id(userId));

    }
    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(@AuthenticationPrincipal localuser user,@PathVariable Long userId
            ,@RequestBody Address address) {
        if (!usve.userHasPermissionToUser(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        address.setId(null);
        localuser reuser = new localuser();
        reuser.setId(userId);
        address.setUser(reuser);
        Address saveaddress = addressDao.save(address);
        simpMessagingTemplate.convertAndSend("/topic/user/"+userId+"/address",
                new DataChange<>(DataChange.ChangeType.INSERT,address));
        return ResponseEntity.ok(saveaddress);

    }
    @PatchMapping("/{userId}/address/{addressId}")
     public  ResponseEntity<Address> patchaddress(@AuthenticationPrincipal localuser user,@PathVariable Long userId,@PathVariable Long addressId,@RequestBody Address address){
        if (!usve.userHasPermissionToUser(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        if(Objects.equals(address.getId(), addressId)) {
            Optional<Address> optionalAddress = addressDao.findById(addressId);
            if (optionalAddress.isPresent()){
                localuser originaluser = optionalAddress.get().getUser();
                if(Objects.equals(originaluser.getId(), userId)){
                    address.setUser(originaluser);
                    Address saveaddress = addressDao.save(address);
                    simpMessagingTemplate.convertAndSend("/topic/user/"+userId+"/address",
                            new DataChange<>(DataChange.ChangeType.UPDATE,address));
                    return ResponseEntity.ok(saveaddress);
                }
            }
        }
        return  ResponseEntity.badRequest().build();
    }





}
