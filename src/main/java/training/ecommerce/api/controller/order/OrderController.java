package training.ecommerce.api.controller.order;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.ecommerce.model.Order;
import training.ecommerce.model.User;
import training.ecommerce.services.OrderService;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal User user) {
        return orderService.index(user);
    }
}
