DROP TABLE IF EXISTS customer CASCADE;

CREATE TABLE customer (
    id                  serial PRIMARY KEY,
    login               varchar(40) NOT NULL UNIQUE,
    password            varchar(40) NOT NULL,
    first_name          varchar(100) NOT NULL,
    last_name           varchar(100) NOT NULL,
    email               varchar(100) NOT NULL,
    postal_address      varchar(100) NOT NULL,
    credit_card_number  bigint NOT NULL,
    expiration_date     date NOT NULL,
    cvv_code            integer NOT NULL,
    created_date        date NOT NULL
);

INSERT INTO customer (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Carla','ECY16XSW7PO','Dora','Page','sapien.molestie.orci@elementumduiquis.net','P.O. Box 602, 840 Ultricies Ave','4716855879364','10/15/17','366','04/13/17'),('Suki','ABM89EGZ9SM','Ava','Gonzalez','ac.turpis.egestas@ac.co.uk','Ap #136-1258 Lorem, St.','4532874588625803','09/30/16','356','08/01/16'),('Michelle','NAU18OOK2VQ','Amy','Odom','gravida.nunc@gravidasitamet.co.uk','624-5412 Turpis St.','4556923911460460','12/20/16','182','08/11/16'),('Jael','ZAK50BXP3QK','Tamekah','Leach','interdum@Aliquam.ca','P.O. Box 484, 7208 In St.','4556542190563','06/21/17','425','06/16/16'),('Kadeem','JNT58VUM4UU','Nola','Mclaughlin','quis@nisi.net','7851 Fames Rd.','4532190755262','01/28/17','114','12/30/17'),('Hu','KHR48YZE5FR','Fatima','Savage','fringilla@sit.edu','Ap #385-5581 Suspendisse Av.','4399192478803667','11/07/17','262','12/07/16'),('Ulysses','ASC93PCO1QL','Addison','Tyson','Fusce.aliquam@Crassed.org','Ap #686-413 Lectus Rd.','4929824998229','01/25/18','776','11/09/17'),('Arsenio','KNR46YPJ9FY','Branden','Lane','libero@nisiAeneaneget.org','P.O. Box 623, 7050 Metus. Ave','4716943872195316','02/02/17','375','06/27/16'),('Wang','PPC26ONX8KS','Fuller','Hayes','Donec@ipsumSuspendisse.co.uk','5986 Cursus, Road','4716862511307','02/04/18','296','04/26/17'),('Len','MXK49VYF6EQ','Damon','Ortiz','Mauris.molestie@egestasurnajusto.net','P.O. Box 915, 7507 Sit Road','4929851510819861','03/12/17','703','05/17/17');
INSERT INTO customer(login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date)
VALUES('nkahal', 'password', 'Neeraj', 'Kahal', 'nkahal@yahoo.com', '123 Main Street', '4716855879364','10/15/17','366','04/15/17');

DROP TABLE IF EXISTS room CASCADE;

CREATE TABLE room (
    room_number    integer NOT NULL PRIMARY KEY,
    view           varchar(40) NOT NULL,
    type           varchar(40) NOT NULL
);

DROP TABLE IF EXISTS defaultroomprice CASCADE;

/* This table holds only default room charges. This is what a room
 * charge defaults to if the reservation is made outside of a changed
 * price window or no change has been made.
 */

CREATE TABLE defaultroomprice (
    room_number    integer NOT NULL PRIMARY KEY,
    amount         float NOT NULL
);

ALTER TABLE defaultroomprice 
   ADD CONSTRAINT fk_room
   FOREIGN KEY (room_number) 
   REFERENCES room(room_number)
   ON DELETE CASCADE ON UPDATE CASCADE;


DROP TABLE IF EXISTS roomprice CASCADE;

CREATE TABLE roomprice (
    id             serial PRIMARY KEY,
    room_number    integer NOT NULL,
    amount         float NOT NULL,
    day            date NOT NULL
);

ALTER TABLE roomprice 
   ADD CONSTRAINT fk_room
   FOREIGN KEY (room_number) 
   REFERENCES room(room_number)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS reservation CASCADE;

CREATE TABLE reservation (
    id             serial PRIMARY KEY,
    customer_id    integer NOT NULL,
    room_number    integer NOT NULL,
    start_date     date NOT NULL,
    end_date       date NOT NULL
);

ALTER TABLE reservation 
   ADD CONSTRAINT fk_customer
   FOREIGN KEY (customer_id) 
   REFERENCES customer(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE reservation
   ADD CONSTRAINT fk_room
   FOREIGN KEY (room_number) 
   REFERENCES room(room_number)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS roombill CASCADE;

CREATE TABLE roombill (
    id             serial PRIMARY KEY,
    reservation_id integer NOT NULL,
    day            date NOT NULL,
    amount         float NOT NULL
); 

ALTER TABLE roombill
   ADD CONSTRAINT fk_reservation
   FOREIGN KEY (reservation_id) 
   REFERENCES reservation(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS chargebill CASCADE;

/* This table holds the bill information mapping charges
 * to the respective reservation. The bill_date is an
 * important field that allows original charges to be
 * preserved. There is a separate tuple for each charge
 * to a reservation. From the reservation_id we can get
 * the room_id. And from the room_id we can get the price.
 */

CREATE TABLE chargebill (
    id                serial PRIMARY KEY,
    reservation_id    integer NOT NULL,
    chargetype        varchar(100) NOT NULL,
    day               date NOT NULL,
    amount            float NOT NULL
);

ALTER TABLE chargebill
   ADD CONSTRAINT fk_reservation
   FOREIGN KEY (reservation_id) 
   REFERENCES reservation(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS defaultcharge CASCADE;

/* This table holds all types of charges (room, wifi, etc.)
 * and the default price for each charge. This is what a charge
 * defaults to if the reservation is made outside of a changed
 * price window.
 */

CREATE TABLE defaultcharge (
    id              serial PRIMARY KEY,
    type            varchar(100) NOT NULL UNIQUE,
    amount          float NOT NULL
);

DROP TABLE IF EXISTS charge CASCADE;

/* This table holds all types of charges (room, wifi, etc.)
 * and the range of time that price holds before going back
 * to the default price. This table can be altered by admins
 * when prices change.
 */

CREATE TABLE charge (
    id               serial PRIMARY KEY,
    type             varchar(100) NOT NULL,
    amount           float NOT NULL,
    day              date NOT NULL
);

DROP TABLE IF EXISTS employee CASCADE;

CREATE TABLE employee (
    id                  serial PRIMARY KEY,
    is_admin            integer NOT NULL,
    login               varchar(40) NOT NULL,
    password            varchar(40) NOT NULL,
    first_name          varchar(100) NOT NULL,
    last_name           varchar(100) NOT NULL,
    email               varchar(100) NOT NULL,
    postal_address      varchar(100) NOT NULL,
    created_date        date NOT NULL
);

INSERT INTO employee(is_admin, login, password, first_name, last_name, email, postal_address, created_date)
VALUES(1, 'skahal', 'password', 'Shubham', 'Kahal', 'shubhamkahal@acme.com', '123 Main Street', '04/20/2017');

INSERT INTO employee(is_admin, login, password, first_name, last_name, email, postal_address, created_date)
VALUES(0, 'siddkahal', 'password', 'Siddhant', 'Kahal', 'siddkahal@acme.com', '123 Main Street', '04/21/2017');

DROP TABLE IF EXISTS checkin CASCADE;

CREATE TABLE checkin (
    id serial PRIMARY KEY,
    reservation_id integer NOT NULL UNIQUE,
    checkin_date date NOT NULL
);

ALTER TABLE checkin
   ADD CONSTRAINT fk_reservation
   FOREIGN KEY (reservation_id) 
   REFERENCES reservation(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS reward CASCADE;

CREATE TABLE reward (
    id            serial PRIMARY KEY,
    customer_id   integer NOT NULL,
    type          varchar(100) NOT NULL,
    points        integer NOT NULL
);

ALTER TABLE reward 
   ADD CONSTRAINT fk_customer
   FOREIGN KEY (customer_id) 
   REFERENCES customer(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS roomtransactionhistory CASCADE;

CREATE TABLE roomtransactionhistory (
  id serial PRIMARY KEY,
  /* create copy of ever field in a room trasaction */
  reservation_id integer NOT NULL,
  customer_id    integer NOT NULL,
  room_number integer NOT NULL,
  view        varchar(100) NOT NULL,
  type        varchar(100) NOT NULL,
  day         date NOT NULL,
  amount      float NOT NULL
);

DROP TABLE IF EXISTS chargetransactionhistory CASCADE;

CREATE TABLE chargetransactionhistory (
  id serial PRIMARY KEY,
  /* create copy of ever field in a charge trasaction */
  reservation_id integer NOT NULL,
  customer_id    integer NOT NULL,
  chargetype     varchar(100) NOT NULL,
  day            date NOT NULL,
  amount         float NOT NULL
);