package org.test.h2o.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.test.h2o.enam.ProductType;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Документ MongoDB, представляющий товар в компьютерном магазине.
 *
 * <p>Поддерживает четыре типа товаров:
 * <ul>
 *   <li>DESKTOP - настольные компьютеры (десктопы, неттопы, моноблоки)</li>
 *   <li>LAPTOP - ноутбуки (13, 14, 15, 17 дюймов)</li>
 *   <li>MONITOR - мониторы (диагональ в дюймах)</li>
 *   <li>HDD - жесткие диски (объем в ГБ)</li>
 * </ul>
 *
 * <p>Использует стратегию единой коллекции с дискриминатором по полю {@code type}.
 * Для производительности добавлены составные индексы.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 * @see org.test.h2o.dto.ProductRequestDto
 * @see org.test.h2o.repository.ProductRepository
 */
@Getter
@Setter
@Document(collection = "products")
@CompoundIndex(name = "idx_type_price", def = "{'type': 1, 'price': -1}")
@CompoundIndex(name = "idx_type_manufacturer", def = "{'type': 1, 'manufacturer': 1}")
public class Product {

    /**
     * Уникальный идентификатор документа в MongoDB.
     * Генерируется автоматически при вставке.
     */
    @Id
    private String id;

    /**
     * Уникальный серийный номер товара.
     * Обязательное поле. Индексирован для быстрого поиска и проверки уникальности.
     */
    @Indexed(unique = true)
    private String serialNumber;

    /**
     * Тип товара.
     * Допустимые значения: DESKTOP, LAPTOP, MONITOR, HDD.
     * Служит дискриминатором для полиморфных запросов.
     */
    @Indexed
    private ProductType type;

    /**
     * Производитель товара (например, "Apple", "Dell", "Samsung").
     * Индексирован для фильтрации по производителю.
     */
    @Indexed
    private String manufacturer;

    /**
     * Цена товара в рублях.
     * Используется BigDecimal для точного хранения денежных сумм.
     * Индексирован для сортировки и фильтрации по цене.
     */
    @Indexed
    private BigDecimal price;

    /**
     * Количество единиц товара на складе.
     * Не может быть отрицательным (валидация на уровне сервиса).
     */
    private Integer stockQuantity;

    /**
     * Форм-фактор настольного компьютера.
     * Актуально только для типа DESKTOP.
     * Возможные значения: DESKTOP, NETTOP, ALL_IN_ONE.
     */
    private String formFactor;

    /**
     * Размер экрана ноутбука в дюймах.
     * Актуально только для типа LAPTOP.
     * Допустимые значения: 13, 14, 15, 17.
     */
    private Integer screenSize;

    /**
     * Диагональ монитора в дюймах.
     * Актуально только для типа MONITOR.
     * Диапазон: от 10 до 49 дюймов.
     */
    private Integer diagonal;

    /**
     * Объем жесткого диска в гигабайтах.
     * Актуально только для типа HDD.
     * Диапазон: от 120 до 20000 ГБ.
     */
    private Integer volumeGb;

    /**
     * Время создания записи в формате Unix timestamp (миллисекунды с начала эпохи).
     * Устанавливается один раз при первом сохранении.
     */
    private Long createdAt;

    /**
     * Время последнего обновления записи в формате Unix timestamp.
     * Обновляется при каждом изменении товара.
     */
    private Long updatedAt;

    /**
     * Устанавливает текущее время в миллисекундах для полей createdAt и updatedAt.
     * Должен вызываться перед сохранением нового документа.
     */
    public void setCreationTime() {
        long now = Instant.now().toEpochMilli();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Обновляет время последней модификации текущим моментом.
     * Должен вызываться перед обновлением существующего документа.
     */
    public void updateModificationTime() {
        this.updatedAt = Instant.now().toEpochMilli();
    }
}