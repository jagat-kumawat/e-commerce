package com.ecommerce.api.security;

import com.ecommerce.model.Dao.LocalUserDao;
import com.ecommerce.model.localuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Primary
public class JunitUserDetailsService implements UserDetailsService {
    @Autowired
    private LocalUserDao localUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<localuser> ouser = localUserDao.findByUsernameIgnoreCase(username);
        if(ouser.isPresent())
            return ouser.get();
        return null;
    }
}
