package com.project.network.service.implementation;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import com.project.network.controller.security.AuthenticationUtils;
import com.project.network.exception.ResponseErrorException;
import com.project.network.service.UserService;
import com.project.network.dao.UserDao;
import com.project.network.dao.implementation.DefaultUserDao;
import com.project.network.dto.JwtTokenDto;
import com.project.network.dto.UserDto;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;

public class DefaultUserService implements UserService {

    private static final int JWT_EXPIRATION_HOURS = 4;

    UserDao userDao = new DefaultUserDao();

    @Override
    public JwtTokenDto loginUserAndReturnJwt(UserDto credentials) {
        try {
            Optional<UserDto> userData = userDao.getUserByLogin(credentials.getLogin());
            if (userData.isPresent()) {
                String originalPassword = credentials.getPassword();
                String storedPassword = userData.get().getPassword();
                if (AuthenticationUtils.validatePassword(originalPassword, storedPassword)) {
                    SecretKey key = AuthenticationUtils.getKey();
                    String jwt = Jwts.builder()
                            .claim("login", credentials.getLogin())
                            .setIssuedAt(new Date())
                            .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(JWT_EXPIRATION_HOURS)))
                            .signWith(key)
                            .compact();
                    return new JwtTokenDto(jwt);
                } else {
                    throw new ResponseErrorException(401, "Invalid credentials");
                }
            } else {
                throw new ResponseErrorException(401, "No such user");
            }
        } catch (SQLException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

    @Override
    public UserDto createNewUser(final UserDto userDto) {
        try {
            if (userDao.getUserByLogin(userDto.getLogin()).isPresent()) {
                throw new ResponseErrorException(409, "User with such login already exists");
            } else {
                userDto.setPassword(AuthenticationUtils.generatePasswordHash(userDto.getPassword(), 10));
                int rowCount = userDao.insertNewUser(userDto);
                if (rowCount == 1) {
                    return userDto;
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

    @Override
    public boolean validateUserJwt(final JwtTokenDto jwtTokenDto) {
        try {
            SecretKey key = AuthenticationUtils.getKey();
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtTokenDto.getToken());
            return true;
        } catch (JwtException e) {
            throw new ResponseErrorException(403, "Invalid token");
        }
    }
}
