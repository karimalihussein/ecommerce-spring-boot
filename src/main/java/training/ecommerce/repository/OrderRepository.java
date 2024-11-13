package training.ecommerce.repository;


import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import training.ecommerce.model.Order;
import training.ecommerce.model.User;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, Long> {
    List<Order> findByUser(User user);
}