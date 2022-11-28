package com.mj.payroll.preload;

import com.mj.payroll.model.Employee;
import com.mj.payroll.model.Order;
import com.mj.payroll.model.Product;
import com.mj.payroll.model.constant.Status;
import com.mj.payroll.repository.EmployeeRepository;
import com.mj.payroll.repository.OrderRepository;
import com.mj.payroll.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        return args -> {

            employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
            employeeRepository.save(new Employee("Frogo", "Baggins", "thief"));
            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("MacBool Pro",Status.COMPLETED));
            orderRepository.save(new Order("Iphone",Status.IN_PROGRESS));
            orderRepository.findAll().forEach(order -> log.info("Preloded " + order));

            productRepository.save(new Product("Food",10L,"Bread"));
            productRepository.save(new Product("Drinks",20L,"Water"));
            productRepository.save(new Product("Tech",40L,"Phone"));
            productRepository.save(new Product("Other",100L,"Present"));
        };
    }
}
