package org.test.h2o.repository;

import org.test.h2o.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с коллекцией продуктов в MongoDB.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Проверяет существование продукта с указанным серийным номером.
     *
     * @param serialNumber серийный номер для проверки
     * @return true если продукт существует, false в противном случае
     */
    boolean existsBySerialNumber(String serialNumber);

    /**
     * Находит все продукты указанного типа.
     *
     * @param type тип продукта (DESKTOP, LAPTOP, MONITOR, HDD)
     * @return список продуктов
     */
    List<Product> findByType(String type);
}