package org.shopping.models.buyer;

import lombok.RequiredArgsConstructor;
import org.shopping.repositories.CartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartSaveService {

    private final CartRepository repository;


}
