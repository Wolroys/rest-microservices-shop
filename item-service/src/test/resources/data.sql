INSERT INTO Category (id, name) VALUES
     (1, 'Электроника'),
     (2, 'Одежда'),
     (3, 'Бытовая техника');

INSERT INTO Product (id, name, description, price, category_id, seller_id) VALUES
    (1, 'Смартфон Samsung Galaxy S21', 'Современный смартфон от Samsung', 799.99, 1, 1),
    (2, 'Ноутбук HP Envy 13', 'Мощный ноутбук для работы и развлечений', 1099.99, 1, 2),
    (3, 'Платье с цветочным узором', 'Летнее платье для женщин', 49.99, 2, 3),
    (4, 'Футболка с логотипом', 'Спортивная футболка для мужчин', 19.99, 2, 4),
    (5, 'Холодильник LG Inverter', 'Просторный холодильник для дома', 699.99, 3, 5),
    (6, 'Микроволновая печь Panasonic', 'Быстро нагревает пищу', 129.99, 3, 6);