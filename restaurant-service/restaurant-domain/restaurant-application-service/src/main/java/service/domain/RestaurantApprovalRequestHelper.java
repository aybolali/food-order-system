package service.domain;

import domain.RestaurantDomainService;
import domain.entity.Restaurant;
import domain.event.OrderApprovalEvent;
import domain.exception.RestaurantNotFoundException;
import domain.valueObject.OrderId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import service.domain.dto.RestaurantApprovalRequest;
import service.domain.mapper.RestaurantMapper;
import service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import service.domain.ports.output.repository.OrderApprovalRepository;
import service.domain.ports.output.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class RestaurantApprovalRequestHelper {
    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantMapper restaurantDataMapper,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository,
                                           OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                           OrderRejectedMessagePublisher orderRejectedMessagePublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
        this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
    }

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent =
                restaurantDomainService.validateOrder(
                        restaurant,
                        failureMessages,
                        orderApprovedMessagePublisher,
                        orderRejectedMessagePublisher);
        orderApprovalRepository.save(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper
                .restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantResult = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantResult.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() +
                    " not found!");
        }

        Restaurant restaurantEntity = restaurantResult.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                    if (p.getId().equals(product.getId())) {
                        product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));

        return restaurant;
    }
}
