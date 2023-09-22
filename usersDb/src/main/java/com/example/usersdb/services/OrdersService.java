package com.example.usersdb.services;

import com.example.usersdb.dto.OrderDTO;
import com.example.usersdb.entities.Order;
import com.example.usersdb.entities.Product;
import com.example.usersdb.entities.ProductInOrder;
import com.example.usersdb.entities.User;
import com.example.usersdb.repositories.OrderRepository;
import com.example.usersdb.repositories.ProductInOrdersRepository;
import com.example.usersdb.repositories.ProductsRepository;
import com.example.usersdb.repositories.UsersRepository;
import com.example.usersdb.responseObjects.FilteringResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class OrdersService {
    private final OrderRepository orderRepository;
    private final ProductInOrdersRepository productInOrdersRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public OrdersService(OrderRepository orderRepository,
                         ProductInOrdersRepository productInOrdersRepository,
                         ProductsRepository productsRepository, UsersRepository usersRepository) {
        this.orderRepository = orderRepository;
        this.productInOrdersRepository = productInOrdersRepository;
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
    }

    //TODO Ошбики при сохранении в базу order и productInOrder-ов.
    // 1. Первым должен сохраняться Order
    // 2. Проверить петливые зависимости между классами
    // 3. Проверить инициализацию по умолчанию для связующих классов
    // 4. Если не помогло пройтись по решению ChatGPT
    // 5. В крайнем случае переопределить hash() в классах, исключив из него связующие классы
    // 6. В самом крайнем случае удалить проект и забыть о нем
    @Transactional
    public void addOrder(OrderDTO orderDTO){
        Order order = new Order(orderDTO);
        Optional<User> user = usersRepository.findById(orderDTO.getUserId());
        if (user.isPresent()){
            order.setOrderUser(user.get());
            for (Map<String, Long> p : orderDTO.getProductsInOrder()){
                Optional<Product> product = productsRepository.findById(p.get("productId"));
                if (product.isPresent()){
                    ProductInOrder productInOrder = new ProductInOrder(p.get("productCount"),
                            product.get().getId(), product.get().getCost());
                    productInOrder.setProduct(product.get());
                    order.addProductInOrder(productInOrder);
                    productInOrdersRepository.save(productInOrder);
                }
                else
                    throw new RuntimeException("Add order exception: Product with id "
                            + p.get("productId") + " not found in database");
            }
            orderRepository.save(order);
        }
        else
            throw new RuntimeException("Add order exception: User with id "
                    + orderDTO.getUserId() + " not found in database");
    }

    @Transactional
    public void delOrder(Long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()){
            orderRepository.delete(order.get());
        }
        else
            throw new RuntimeException("Delete order exception: Order with id " + orderId + " not found in database");
    }

    @Transactional
    public void updateOrder(OrderDTO orderDTO){
        Order currentOrder = new Order(orderDTO);
        Optional<Order> order = orderRepository.findById(currentOrder.getId());
        if (order.isPresent()){
            if (order.get().getOrderUser().getId().equals(orderDTO.getUserId())){
                currentOrder.setOrderUser(order.get().getOrderUser());
                for (Map<String, Long> p : orderDTO.getProductsInOrder()){
                    Optional<Product> product = productsRepository.findById(p.get("productId"));
                    if (product.isPresent()){
                        ProductInOrder productInOrder = new ProductInOrder(p.get("productCount"),
                                product.get().getId(), product.get().getCost());
                        productInOrder.setProduct(product.get());
                        currentOrder.addProductInOrder(productInOrder);
                        productInOrdersRepository.save(productInOrder);
                    }
                    else
                        throw new RuntimeException("Update order exception: Product with id "
                                + p.get("productId") + " not found in database");
                }
                orderRepository.save(currentOrder);
            }
            else
                throw new RuntimeException("Update order exception: User id " + orderDTO.getUserId() +
                        " not equal with user id in existed order");
        }
        else
            throw new RuntimeException("Update order exception: Order with id " + currentOrder.getId() +
                    " not found in database");
    }

    public FilteringResponseObj findOrders(){
        FilteringResponseObj filteringResponseObj = new FilteringResponseObj();
        filteringResponseObj.setDataList(orderRepository.findAll());
        return filteringResponseObj;
    }
}
