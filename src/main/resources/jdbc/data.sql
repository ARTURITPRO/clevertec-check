
insert into product ( name, price, is_promotional ) values
('milk', 1.0, true),
('brot', 1.5, false),
('icecream', 2.0, true),
('chocolate', 2.5, false),
('chicken', 3.0, true),
('pizza', 3.5, false),
('pudding', 4.0, true),
('popcorn', 4.5, false),
('pie', 5.0, true);

insert into discount_card (name, discount, number) values
( 'mastercard',10, 11111),
( 'mastercard',20, 22222),
( 'mastercard',30, 33333),
( 'mastercard',40, 44444),
( 'mastercard',50, 55555),
( 'mastercard',60, 66666),
( 'mastercard',70, 77777),
( 'mastercard',80, 88888),
( 'mastercard',90, 99999);








-- drop table product;
-- drop table discount_card;
--
-- SELECT nextval('product_id_seq'::regclass);
-- select setval('product_id_seq',9);



