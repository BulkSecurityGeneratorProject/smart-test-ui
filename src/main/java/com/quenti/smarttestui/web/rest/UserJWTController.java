package com.quenti.smarttestui.web.rest;
import com.quenti.smarttestui.components.LoginQuentiComponent;
import com.quenti.smarttestui.security.UserDetailsService;
import com.quenti.smarttestui.security.jwt.JWTConfigurer;
import com.quenti.smarttestui.security.jwt.TokenProvider;
import com.quenti.smarttestui.service.SeguridadService;
import com.quenti.smarttestui.service.UserService;
import com.quenti.smarttestui.service.dto.SeguridadDTO;
import com.quenti.smarttestui.service.dto.UserQuentiDTO;
import com.quenti.smarttestui.web.rest.vm.LoginVM;
import com.quenti.smarttestui.domain.User;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;
import com.quenti.smarttestui.web.rest.vm.ManagedUserVM;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserJWTController {
    @Inject
    private LoginQuentiComponent loginQuentiComponent;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserService userService;

    @Inject
    private SeguridadService seguridadService;


    /**
     * @param loginVM the login object, response is the response from the servlet
     * @return the ResponseEntity
     */
    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * POST : This authenticate uses the API service login from Quenti, and if the user is correct on quenti it logs in in our application, if it doesnt exist it creates it
     * @param loginVM the login object, response is the response from the servlet
     * @return the ResponseEntity
     */
    @PostMapping("/authenticate/quenti")
    @Timed
    public ResponseEntity<?> authorizeQuenti(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UserQuentiDTO uqDTO = loginQuentiComponent.init(loginVM);
        Long userId = null;

        if (!uqDTO.getObjTokenDTO().equals("null")) { //si quenti devuelve token:
            Boolean band = loginQuentiComponent.setOrganizationsCode(uqDTO.getObjTokenDTO());
            if(band){
                Optional<User> usuarioReal = userService.getUserWithAuthoritiesByLogin(loginVM.getUsername());
                if (usuarioReal.toString().equals("Optional.empty")) { //si no existe en jhipster, se crea:
                    String customEmail = loginVM.getUsername().toLowerCase() + "@localhost";
                    User newUser = userService.createUser(loginVM.getUsername(),loginVM.getPassword(),uqDTO.getFirstName(),
                        uqDTO.getLastName(),customEmail,"en");
                    userId = newUser.getId();
                } else {
                    userId = usuarioReal.get().getId();
                }

                //después de encontrar el usuario o de crearlo, se autentica:
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

                try {
                    Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
                    String jwt = tokenProvider.createToken(authentication, rememberMe);
                    response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);

                    SeguridadDTO seguridadDTO = new SeguridadDTO();
                    SeguridadDTO seguridadDTO2 = seguridadService.findBySeguridadId(userId);

                    if (seguridadDTO2 == null){
                        seguridadDTO.setToken(uqDTO.getObjTokenDTO());
                        seguridadDTO.setFecha(LocalDate.now());
                        seguridadDTO.setJhUserId(userId);
                        seguridadService.save(seguridadDTO);
                    }else {
                        if(!seguridadDTO2.getToken().equals(uqDTO.getObjTokenDTO())){
                            seguridadDTO2.setToken(uqDTO.getObjTokenDTO());
                            seguridadService.save(seguridadDTO2);
                        }
                    }

                    return ResponseEntity.ok(new JWTToken(jwt));
                } catch (AuthenticationException exception) {
                    return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
                }

            } // Step 2 SEGU.
            else { //credenciales invalidos de quenti
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else { //credenciales invalidos de quenti
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
