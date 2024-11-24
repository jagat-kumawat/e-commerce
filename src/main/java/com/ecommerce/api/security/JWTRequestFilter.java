package com.ecommerce.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ecommerce.model.Dao.LocalUserDao;
import com.ecommerce.model.localuser;
import com.ecommerce.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter implements ChannelInterceptor {
    private JWTService jwtService;
    private LocalUserDao localUserDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
String tokenheader = request.getHeader("Authorization");
UsernamePasswordAuthenticationToken token = checkToken(tokenheader);

if(token !=null){
    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));



}
filterChain.doFilter(request,response);
    }
    private UsernamePasswordAuthenticationToken checkToken(String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtService.getusername(token);
                Optional<localuser> opuser = localUserDao.findByUsernameIgnoreCase(username);
                if (opuser.isPresent()) {
                    localuser user = opuser.get();
                    if (user.getEmailVerified()) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return authentication;
                    }
                }
            } catch (JWTDecodeException ex) {

            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return null;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageType messageType = (SimpMessageType) message.getHeaders().get("simpMessageType");
        if(messageType.equals(SimpMessageType.SUBSCRIBE)|| messageType.equals(SimpMessageType.MESSAGE)) {



            Map nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");

            if (nativeHeaders != null) {
                List authTokenList = (List) nativeHeaders.get("Authorization");
                if (authTokenList != null) {
                    String tokenHeader = (String) authTokenList.get(0);
                    checkToken(tokenHeader);
                }
            }
        }
        return message;
    }
}
