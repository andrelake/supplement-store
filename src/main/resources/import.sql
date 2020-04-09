insert into supplement_store (name, national_id, registration_date) values ('SupStore Ltda', '99.999.999/0001-99', utc_timestamp);

insert into tb_payment (description, sup_store_id) values ('Dinheiro', 1), ('Cartão de crédito', 1), ('Cartão de débito', 1);

insert into tb_customer (email, name, password, register_date, sup_store_id) values ('joao@gmail.com', 'João Silva', 'senha123', utc_timestamp, 1);
insert into tb_customer (email, name, password, register_date, sup_store_id) values ('pedro@gmail.com', 'Pedro Martins', 'senha444', utc_timestamp, 1);
insert into tb_customer (email, name, password, register_date, sup_store_id) values ('mario@gmail.com', 'Mario Vargas', 'senha777', utc_timestamp, 1);
