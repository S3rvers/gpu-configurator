package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.cart.DeleteCartItemResponse;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CartRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ConfigurationRepository configuredProductRepository;
    private final ConfigurationService configuredProductService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserSessionRepository userSessionRepository;

    @Override
    public CartItemsCountResponse getCartItemsCount(Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> optionalCart = getUserOrGuestCart(user, userSession);

        long cartItemsCount = optionalCart
                .map(cart -> configuredProductRepository.countByCartId(cart.getId()))
                .orElse(0L);

        return CartItemsCountResponse.builder()
                .count(cartItemsCount)
                .build();
    }

    @Override
    public AddCartItemResponse addCartItem(UUID productId, Boolean warranty, String components, Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> cartOptional = getUserOrGuestCart(user, userSession);
        Cart cart = cartOptional.orElseGet(() -> cartRepository.save(new Cart()));

        if (user == null) {
            userSession.setCart(cart);
            userSessionRepository.save(userSession);
        } else if (user.getCart() == null) {
            user.setCart(cart);
            userRepository.save(user);
        }

        ConfigurationResponseDto configuredProductResponse = configuredProductService.saveConfiguration(productId.toString(), warranty, components, cart);

        return AddCartItemResponse.builder()
                .message("Configured product added to cart successfully")
                .configuration(configuredProductResponse)
                .build();
    }

    @Override
    public DeleteCartItemResponse deleteCartItem(UUID configuredProductId, Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> optionalCart = getUserOrGuestCart(user, userSession);

        if (optionalCart.isEmpty()) {
            return DeleteCartItemResponse.builder().message("Configured product deleted successfully").build();
        }

        configuredProductRepository
                .findByIdAndCartId(configuredProductId, optionalCart.get().getId())
                .ifPresent(configuredProductRepository::delete);

        return DeleteCartItemResponse.builder().message("Configured product deleted successfully").build();
    }

    @Override
    public CartItemsResponse getCartItems(Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> optionalCart = this.getUserOrGuestCart(user, userSession);

        if (optionalCart.isEmpty()) {
            return new CartItemsResponse(new ArrayList<>(), 0);
        }

        List<ConfigurationResponseDto> configuredProducts = configuredProductRepository.findByCartId(optionalCart.get().getId())
                .stream()
                .map(this::mapToConfigurationResponseDto)
                .toList();

        return new CartItemsResponse(configuredProducts, configuredProducts.size());
    }

    private Optional<Cart> getUserOrGuestCart(User user, UserSession userSession) {
        if (user == null) {
            return Optional.ofNullable(userSession.getCart());
        }
        return Optional.ofNullable(user.getCart());
    }

    private User getUser(Principal principal) {
        if (principal != null) {
            return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        return null;
    }

    private ConfigurationResponseDto mapToConfigurationResponseDto(Configuration configuredProduct) {
        Product product = configuredProduct.getProduct();

        return ConfigurationResponseDto.builder()
                .Id(String.valueOf(configuredProduct.getId()))
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getTotalProductPrice())
                .productDescription(product.getProductDescription())
                .productCoverImage(product.getProductCase().getCoverImageUrl())
                .totalPrice(configuredProduct.getTotalPrice())
                .warranty(null)
                .vat(null)
                .configuredPrice(null)
                .configured(configuredProduct.getConfiguredOptions())
                .build();
    }
}