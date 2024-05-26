package com.github.shopping_mall_be.service;

import com.github.shopping_mall_be.domain.TokenEntity;
import com.github.shopping_mall_be.repository.Jwt.TokenJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    private final TokenJpaRepository tokenRepository;

    public TokenService(TokenJpaRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<TokenEntity> findAllTokens() {
        return tokenRepository.findAll();
    }
}