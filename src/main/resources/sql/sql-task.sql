--Вывести к каждому самолету класс обслуживания и количество мест этого класса

SELECT model, fare_conditions, count(*)
FROM aircrafts
         JOIN seats s
              on aircrafts.aircraft_code = s.aircraft_code
GROUP BY model, fare_conditions
ORDER BY model, fare_conditions;

--Найти 3 самых вместительных самолета (модель + кол-во мест)

select model, count(fare_conditions) as number_of_seats
from aircrafts
         join seats s
              on aircrafts.aircraft_code = s.aircraft_code
group by model
order by number_of_seats desc
limit 3;

--Вывести код,модель самолета и места не эконом класса для самолета 'Аэробус A321-200' с сортировкой по местам

select aircrafts.aircraft_code, model, seat_no
from aircrafts
         join seats s on aircrafts.aircraft_code = s.aircraft_code
where fare_conditions
    NOT LIKE 'Economy'
  and model
    LIKE 'Аэробус A321-200'
order by seat_no;

--Вывести города в которых больше 1 аэропорта ( код аэропорта, аэропорт, город)

select airport_code, airport_name, city
from airports ou
where (select count(*)
       from airports inr
       where inr.city = ou.city) > 1;

-- Найти ближайший вылетающий рейс из Екатеринбурга в Москву, на который еще не завершилась регистрация

SELECT flights_v.*
FROM flights_v
WHERE (status = 'Scheduled' or status = 'Delayed')
  and departure_city = 'Екатеринбург'
  and arrival_city = 'Москва'
ORDER BY scheduled_departure
LIMIT 1;

--Вывести самый дешевый и дорогой билет и стоимость ( в одном результирующем ответе)

select ticket_no, amount
from (select *
      from ticket_flights
      order by amount desc
      limit 1) as "max_amount"
union
(select ticket_no, amount
 from ticket_flights
 order by amount
 limit 1);

-- Написать DDL таблицы Customers , должны быть поля id , firstName, LastName, email , phone. Добавить ограничения на поля ( constraints).

CREATE TABLE customers
(
    id        SERIAL PRIMARY KEY,
    firstName varchar(25) NOT NULL,
    lastName  varchar(30) NOT NULL,
    email     varchar(40) NOT NULL UNIQUE
);

-- Написать DDL таблицы Orders , должен быть id, customerId, quantity. Должен быть внешний ключ на таблицу customers + ограничения

CREATE TABLE orders
(
    id         SERIAL PRIMARY KEY,
    customerId int NOT NULL,
    quantity   int NOT NULL CHECK (quantity >= 1 AND quantity <= 100000) DEFAULT 1,
    FOREIGN KEY (customerId)
        REFERENCES customers (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
-- Написать 5 insert в эти таблицы

insert into customers (firstName, lastName, email)
values ('Jo', 'Cros', 'Cros@mail.com'),
       ('Jek', 'Cr', 'Cr@mail.com'),
       ('Jim', 'Crot', 'Crot@mail.com');

insert into orders (customerId, quantity)
values (1, 200),
       (2, 200),
       (3, 200),
       (2, 200);

-- удалить таблицы

drop table customers, orders;

-- Написать свой кастомный запрос ( rus + sql)
--Найти все самолеты Аэробус A319-100 бизнес  класса которые в данный момент находятся в полёте и вывести полную информацию о них
-- отсортировать по актуальному ближайшему  времени прибытия в аэропорты
select a.*, s.*, f.*, a.aircraft_code as ac
from aircrafts a
    inner join seats s
        on a.aircraft_code = s.aircraft_code
inner join flights f on  a.aircraft_code = f.aircraft_code
where a.model like 'Аэробус A319-100'
and s.fare_conditions like 'Business'
and f.status like 'Arrived'
order by f.actual_arrival;






