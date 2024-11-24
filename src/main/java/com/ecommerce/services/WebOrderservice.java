package com.ecommerce.services;

import com.ecommerce.model.Dao.WebOrderDao;
import com.ecommerce.model.WebOrder;
import com.ecommerce.model.localuser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WebOrderservice {
    public WebOrderDao webOrderDao;

public List<WebOrder> getuser(localuser user){
    return webOrderDao.findByUser(user);
}
}
