package training.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import training.ecommerce.model.Order;
import training.ecommerce.model.User;
import training.ecommerce.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieve orders for a specific user.
     * 
     * @param user the user whose orders need to be fetched
     * @return a list of orders associated with the user
     * @throws IllegalArgumentException if the user is null
     */
    public List<Order> index(User user) {
        return orderRepository.findByUser(user);
    }
}