DROP TABLE IF EXISTS "Customer" CASCADE;

CREATE TABLE "Customer" (
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

INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Carla','ECY16XSW7PO','Dora','Page','sapien.molestie.orci@elementumduiquis.net','P.O. Box 602, 840 Ultricies Ave','4716855879364','10/15/17','366','04/13/17'),('Suki','ABM89EGZ9SM','Ava','Gonzalez','ac.turpis.egestas@ac.co.uk','Ap #136-1258 Lorem, St.','4532874588625803','09/30/16','356','08/01/16'),('Michelle','NAU18OOK2VQ','Amy','Odom','gravida.nunc@gravidasitamet.co.uk','624-5412 Turpis St.','4556923911460460','12/20/16','182','08/11/16'),('Jael','ZAK50BXP3QK','Tamekah','Leach','interdum@Aliquam.ca','P.O. Box 484, 7208 In St.','4556542190563','06/21/17','425','06/16/16'),('Kadeem','JNT58VUM4UU','Nola','Mclaughlin','quis@nisi.net','7851 Fames Rd.','4532190755262','01/28/17','114','12/30/17'),('Hu','KHR48YZE5FR','Fatima','Savage','fringilla@sit.edu','Ap #385-5581 Suspendisse Av.','4399192478803667','11/07/17','262','12/07/16'),('Ulysses','ASC93PCO1QL','Addison','Tyson','Fusce.aliquam@Crassed.org','Ap #686-413 Lectus Rd.','4929824998229','01/25/18','776','11/09/17'),('Arsenio','KNR46YPJ9FY','Branden','Lane','libero@nisiAeneaneget.org','P.O. Box 623, 7050 Metus. Ave','4716943872195316','02/02/17','375','06/27/16'),('Wang','PPC26ONX8KS','Fuller','Hayes','Donec@ipsumSuspendisse.co.uk','5986 Cursus, Road','4716862511307','02/04/18','296','04/26/17'),('Len','MXK49VYF6EQ','Damon','Ortiz','Mauris.molestie@egestasurnajusto.net','P.O. Box 915, 7507 Sit Road','4929851510819861','03/12/17','703','05/17/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Isabelle','AHV42GVM5WE','Harrison','Dean','accumsan.neque.et@eratin.edu','8816 Sem, Road','4024007193025532','03/30/18','758','05/26/16'),('Ian','NZD80KMB7EB','Keiko','Hamilton','Nullam.nisl.Maecenas@ligulaAliquam.net','5097 Aliquet Rd.','4539639868853','09/24/16','609','12/07/16'),('Wynter','FCU21VGM3XH','Kylie','Wiley','eu@mollisIntegertincidunt.org','Ap #599-631 Proin Av.','4929983942364','04/14/16','389','09/16/16'),('Yuri','NVX87FNP8DX','Yen','Herring','in@liberoMorbi.edu','Ap #666-3722 Mi Street','4556484898868','04/28/17','801','02/27/17'),('Colin','LMK88JND2AC','Leah','Potts','enim@enim.co.uk','6274 At Av.','4532586825489','07/13/16','414','08/25/16'),('Violet','RXO53LQH4JL','Xena','Martin','consequat@ipsumportaelit.net','7417 Malesuada Rd.','4539878538722647','02/01/18','118','02/09/18'),('Mara','ABC77HVM3MA','Iona','Chen','auctor.velit@Donecdignissim.org','1755 Non Rd.','4716170808874786','06/05/16','731','11/05/17'),('Mohammad','LCZ14ZBZ1IS','Grace','Greer','sed.consequat@metussit.org','Ap #878-5064 In Rd.','4716605132494','06/09/16','651','07/25/16'),('Chaney','IQP42AVG4VS','Jade','Stafford','eget.ipsum@sed.edu','9392 Nunc Rd.','4539701549365','01/25/18','484','01/30/17'),('Azalia','WLM92VOW9TH','Gillian','Sears','auctor.velit.Aliquam@mienim.com','654-6371 Quam Av.','4024007124635','03/14/17','902','07/29/16');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Helen','WOT59BBD1VY','Carolyn','Cole','nisi.Cum.sociis@montes.org','P.O. Box 218, 9514 Tristique St.','4916914102358','04/25/16','333','09/12/17'),('Savannah','SUZ94FTU1DN','Leslie','Blankenship','Cras@Ut.edu','P.O. Box 551, 1113 Nulla Avenue','4485671507090029','07/17/17','388','08/05/16'),('Tarik','OAQ61OBB2LH','Ciara','Blankenship','quam.vel@nonummy.org','P.O. Box 788, 4073 Adipiscing Ave','4716167767598000','12/25/17','545','10/05/17'),('Brenna','LCT06VOF0KV','Tatiana','Carney','ultricies@leo.co.uk','P.O. Box 271, 8205 Sem. Avenue','4024007106211252','07/05/17','355','08/03/17'),('Liberty','KVG84ZSK3KQ','Baker','Oneal','ac@aliquam.net','510-4277 Etiam Road','4716317478128','10/08/16','926','02/01/18'),('Hector','ZXV80POI3YG','Natalie','Walters','neque@pedeCumsociis.net','230-6137 Lorem Road','4716726527193079','01/15/18','120','10/07/17'),('Stephen','PWJ60LWS7LQ','Mariko','Stephenson','Vivamus.nibh@acfeugiatnon.org','P.O. Box 425, 3620 Donec Road','4929411422344','12/14/16','589','06/02/16'),('Baker','OVM44LLI4ES','Brett','Woods','sed.sapien@gravidasagittisDuis.co.uk','P.O. Box 870, 2765 Id Rd.','4539301722320334','01/16/17','789','06/16/16'),('Eric','HSZ00WUS3CQ','Yardley','Howe','Nulla.facilisi.Sed@malesuada.com','5386 Vel Av.','4916376376672','10/03/16','438','04/18/17'),('Kyra','MDD37CTI9EL','Micah','Moon','nibh.enim.gravida@semper.com','523-9599 Morbi Rd.','4539748477828','03/14/17','779','08/03/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Dominique','JJX33FAD4FL','Emery','Hanson','vitae.odio@fermentumvel.net','3944 Vivamus St.','4716027642229','05/07/17','321','01/24/17'),('Regan','CSA03VSV9JE','Axel','Rich','risus.Donec@utnullaCras.com','Ap #267-2517 Integer Av.','4485233203503','07/12/17','925','11/26/17'),('Timothy','WTO49KBU0QC','Cassady','Hayes','elit.Etiam.laoreet@leoelementumsem.org','168-5108 Luctus Ave','4762734455142673','04/12/16','797','03/13/18'),('Jerome','ZXJ02LSM3LM','Veda','Calhoun','tempor@ridiculusmusProin.edu','321 Magna. Rd.','4556048390418944','01/24/17','443','03/04/18'),('Margaret','YLZ90HVE4DA','Fay','Soto','nisi.sem@condimentumDonec.ca','Ap #335-1214 Sapien Street','4716664000715','01/02/18','230','12/14/17'),('Gail','ZFL89MNV4DQ','Theodore','Vang','Aliquam.tincidunt@malesuadavel.net','Ap #727-6866 Rutrum. Street','4430575903701723','01/22/17','362','03/16/17'),('April','PCA27NQS6YD','Ian','Pitts','non.lobortis.quis@odioEtiamligula.ca','Ap #829-4846 Cras St.','4539573776291914','09/09/16','132','04/15/16'),('Dale','AYZ31CPR1BY','Kirsten','Nolan','Nulla@odiotristique.net','2066 Ante St.','4916638086010','11/19/16','163','04/17/17'),('Cheryl','FPH56MAL1EQ','Tatiana','Townsend','dapibus.quam.quis@dignissimmagnaa.edu','907-2436 Nec St.','4485792683211138','01/01/18','727','08/20/16'),('Brady','RYU81HZB2YD','Allen','Chapman','lobortis.augue@magnis.co.uk','1197 Aliquet Ave','4485771492708','07/16/16','559','11/02/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Conan','PBW23TCX8WW','Gisela','Rasmussen','a.nunc@risus.com','P.O. Box 670, 8463 Eu Ave','4485672583986171','11/03/17','987','12/31/16'),('Clark','XXG53PAX8XT','Devin','Conner','amet@pellentesqueeget.edu','417-3566 Odio, Ave','4539327498908143','03/13/18','124','12/21/17'),('Cameron','SSN75MMN9II','Felix','Hodges','adipiscing@Suspendissealiquetmolestie.edu','8760 Lacus Street','4929737442950','11/22/16','691','07/06/17'),('Martha','DFG41HDV1KR','Igor','Marsh','cursus.luctus@euelit.org','721-531 Diam Rd.','4532564073967527','11/10/16','265','09/16/16'),('Mariko','WBF12ZCF3CT','Abdul','Donovan','nisl.sem.consequat@SedmolestieSed.org','P.O. Box 193, 6330 Dolor, Street','4631375905559','09/12/16','145','03/19/18'),('Tobias','XIK43VNX7RR','Meghan','Knapp','et@et.com','6573 Sapien Rd.','4539559454407336','04/05/18','392','01/05/17'),('Marny','HDK94UMA0PP','Harrison','Gilliam','condimentum@facilisisSuspendisse.com','574-356 Eros. Road','4485835229617','09/26/17','768','06/16/17'),('Jessamine','NLW87TPK9VL','Willow','Short','Nullam.feugiat@lobortis.co.uk','2953 Amet Rd.','4716836610755','08/14/17','910','08/03/16'),('Dante','NES49OUC4YO','Hall','Barlow','lacus.Quisque.imperdiet@egestas.com','Ap #652-5696 Vel, Road','4532429645342','12/17/16','554','01/21/17'),('Kasimir','FMG94QYT0IN','Velma','Madden','lorem.ipsum@Proin.org','P.O. Box 985, 1888 Nunc Street','4024007136209','05/04/16','665','08/07/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Jessica','RTH17RRP6FB','Sonya','Poole','Etiam.vestibulum@purus.net','972-2792 Imperdiet Rd.','4556502467019','04/23/16','584','11/13/16'),('Jasper','ODN19TOE1DB','Vielka','Villarreal','nonummy@cursuspurus.org','P.O. Box 420, 7049 Cras Rd.','4916656510765351','05/25/16','309','09/06/16'),('Xenos','TQL68SHV4PW','Jordan','Bryan','Maecenas.malesuada@vitaeodiosagittis.edu','511-8252 Ligula. Rd.','4916730731885','11/23/17','704','08/01/16'),('Kaden','AFW41SMB1DP','Elmo','Orr','velit.Quisque@cubiliaCuraePhasellus.ca','822-2279 Lorem Ave','4485957474724850','08/04/17','803','10/04/17'),('Garrison','AES10IYW9WH','Leah','Quinn','volutpat.Nulla.facilisis@pretiumaliquetmetus.com','1523 Donec Ave','4916164787999','10/06/16','755','09/24/17'),('Marah','URA54QHC6XA','Ivana','Stanley','hendrerit@eget.edu','P.O. Box 624, 6757 Et Ave','4564467827557','12/04/16','710','10/25/16'),('Donna','QET43FII6FO','Yuli','Morse','justo.nec@Nunclaoreetlectus.ca','P.O. Box 287, 6012 Etiam Road','4716117432227030','06/09/17','628','03/16/17'),('Felix','XVB19MFR0AC','Cathleen','Rosa','eget.massa.Suspendisse@aliquetlobortis.edu','7148 Accumsan St.','4728572689799980','07/27/17','763','03/20/18'),('Jaden','JTZ81KVP3UA','Lucius','Kidd','vel.arcu@ametlorem.co.uk','Ap #863-3568 Molestie Avenue','4532784956130908','03/01/18','196','12/02/17'),('Hayes','FUT66PDX2CM','Bell','West','nec.urna.et@congue.com','P.O. Box 933, 5373 Lectus. Ave','4485157808434637','02/14/18','691','04/08/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Ursa','RKS75OWT7AS','Castor','Hunter','metus@velitin.com','8201 Vulputate Ave','4024007182807','06/04/16','588','01/15/17'),('Charde','QZS36ZEB3MP','Axel','Bell','Nam.nulla@et.org','532-5747 Faucibus Av.','4716080564724','07/29/17','660','06/17/16'),('Yen','QDA14AIM4SD','Maite','Newman','ornare@non.ca','5324 Phasellus Rd.','4539963900991','05/19/16','715','08/28/16'),('Xandra','XYB30KCI6KX','Orli','Bender','est.tempor@dictum.ca','Ap #782-9891 Scelerisque Street','4929944135066','10/12/17','333','01/21/18'),('Ursa','PGS14IUK5YO','Trevor','Lott','et@lectusquis.co.uk','Ap #954-3818 Nullam Av.','4292664072444179','04/22/17','245','02/19/18'),('Farrah','XPA34HRZ3VQ','Orla','Estes','elementum.at@lorem.co.uk','Ap #947-1515 Nulla Road','4716661721298632','11/16/17','883','07/27/16'),('Marny','BHS79UXA4SO','Ila','Alvarado','amet.diam.eu@dignissimpharetra.net','P.O. Box 477, 3210 Orci. Street','4716565108831','08/26/17','860','12/19/16'),('Cameron','VFM51NSJ1RX','Amanda','Hogan','dignissim.lacus.Aliquam@tortor.edu','831-798 Eget Street','4929037437999719','04/20/17','776','07/12/17'),('Ali','VYY02SDJ6AE','Violet','Osborn','senectus@imperdietornare.net','Ap #619-5050 Lobortis Av.','4929337884098','08/07/16','327','01/07/17'),('Craig','QDA01SFW6DL','Damon','Velez','hendrerit@magnaet.co.uk','P.O. Box 972, 6830 Lectus Rd.','4916928652687','07/24/17','404','07/09/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Phelan','ZCQ28OSX7BF','Isabelle','White','pellentesque@Donec.com','551-4964 Pede Rd.','4929510742196','12/13/16','628','12/31/17'),('Hedley','WDF33VDR2WJ','Hanae','Avila','dolor@semper.co.uk','514-2672 Lacus. Street','4485745796557326','07/28/16','839','03/25/17'),('Upton','RMG04EQO7TE','Jorden','Stark','magna.Phasellus.dolor@tellusPhasellus.edu','837-4195 Et, Rd.','4532668762115148','07/13/16','769','11/13/16'),('Ciaran','PSA37HVT5KY','Noah','Odom','interdum.Nunc@faucibusorciluctus.edu','9041 At Rd.','4456055296166023','08/18/17','401','12/04/16'),('Mara','TMT60MVJ7YV','Walter','Hurst','lorem@nectempusscelerisque.org','P.O. Box 591, 8509 Donec Road','4024007105139','11/06/16','857','10/16/17'),('Price','MUQ42UTE0JY','Lael','Salinas','nisi.dictum@et.ca','8866 Rutrum Road','4485935382694423','06/13/16','610','01/07/18'),('Wesley','QOP17XBI7VK','Aline','Rios','magnis.dis.parturient@perinceptos.com','Ap #192-9004 Id, Ave','4532423850948','03/08/18','156','05/31/16'),('Zephr','PBM90YJU9TL','Serena','Newton','congue.a@nonantebibendum.com','6808 Euismod Street','4916268152660','12/28/17','893','01/13/18'),('Melvin','CXX80ZVX3MJ','Lucius','Gibson','enim@aliquetmagna.com','802-9612 Imperdiet, St.','4916989049716','04/26/17','780','05/04/16'),('Addison','UGX55OOX3GW','Abbot','Bender','cursus.vestibulum.Mauris@disparturientmontes.org','Ap #320-9399 Cras Avenue','4929650701132401','03/08/18','923','11/27/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Xena','GJB26TBD4SD','Kristen','Blair','orci@nunc.net','Ap #417-5781 Vitae Avenue','4716786362571','08/01/17','358','10/15/16'),('Lana','JYX74GLF4MC','Hannah','Rocha','sed.orci@lacusMauris.co.uk','478-9354 Ligula Rd.','4916803571283','08/03/16','513','10/04/17'),('Octavius','LCG04LMJ2AM','Coby','Estes','tellus@semmollisdui.net','P.O. Box 309, 6091 Donec Av.','4485311377714602','07/05/16','237','09/21/16'),('Channing','MKU12YKA1NO','Ryder','Rowe','iaculis.aliquet.diam@nequeSedeget.ca','Ap #633-8451 Quam St.','4539005083715','03/04/17','388','03/11/18'),('John','ZNX22JYZ5JQ','Brandon','White','nunc@etultricesposuere.net','Ap #172-8003 Pede Avenue','4539067136088','10/01/17','396','02/15/17'),('Macaulay','TYA73YWK2JO','Armand','Herrera','augue@fermentumarcuVestibulum.org','Ap #443-551 Amet Ave','4024007175959','01/26/18','230','12/25/17'),('Nicholas','OXD75AGI9ZI','Whoopi','Oconnor','mollis.lectus@adipiscing.com','5374 Enim. Street','4801537172459061','08/02/16','280','06/09/16'),('Sybill','HUB41ZRG7IS','Magee','Hughes','enim@Sedeunibh.com','P.O. Box 882, 1452 Nunc Avenue','4916417680728','08/29/16','834','02/15/18'),('Keiko','GEG91LUJ2VN','Tate','Torres','lacus.Quisque@eratnequenon.org','662-4532 Molestie Road','4916004222379205','08/20/17','903','05/01/16'),('Ignatius','NPI36QQY4UF','Tana','Lowery','bibendum.ullamcorper.Duis@nonummy.com','1268 Elit Rd.','4716533606064','03/02/17','980','07/27/17');
INSERT INTO "Customer" (login,password,first_name,last_name,email,postal_address,credit_card_number,expiration_date,cvv_code,created_date) VALUES ('Desirae','SAS47ABX3DR','Lyle','Mosley','lobortis.nisi.nibh@Mauris.co.uk','2745 Hendrerit Av.','4532775894687884','02/08/17','226','08/31/16'),('Kai','SWU24UYV6ME','Hashim','Valdez','eget@fringillacursus.ca','9863 Sed Street','4532982834360214','02/28/18','832','06/18/16'),('Christine','DBD28EST3RF','Chandler','Velez','ipsum@ac.com','Ap #797-7988 Lectus St.','4137362513851','10/20/16','773','09/01/16'),('Kaye','BCC49AEB5BF','Ryder','Little','Sed.auctor.odio@eget.org','2238 Purus, Ave','4929728763703','04/14/16','161','10/04/16'),('Oleg','EOC48RLR1BJ','Aiko','Sherman','Praesent.interdum@blanditviverraDonec.co.uk','P.O. Box 649, 7659 Lectus Road','4916178371517','11/30/16','614','12/13/16'),('Castor','ILQ72FEH5XU','Nichole','Alexander','a.aliquet.vel@lorem.co.uk','Ap #889-8911 Adipiscing St.','4287626659114407','03/11/18','580','12/26/17'),('Baxter','CQD41UZK4GJ','Aurelia','Wells','sodales.Mauris.blandit@utipsum.org','Ap #401-6318 Tristique Street','4485529555632','03/31/17','344','05/03/17'),('Olivia','UZQ59LAN2ZE','Sylvia','Ayala','tempus.mauris@bibendumDonecfelis.co.uk','Ap #490-7062 Eget Ave','4994750000459123','06/19/16','611','11/12/17'),('Audrey','FDK37JHA2TD','Sawyer','Cotton','sapien@pretiumaliquet.ca','407-2005 Velit Road','4485960231255','10/08/16','655','06/17/17'),('Neville','OHV14WLI0AQ','Zachary','Hall','lacus.Mauris.non@consectetuer.com','Ap #520-4304 Dictum Rd.','4716749778814656','01/30/18','979','09/20/16');

DROP TABLE IF EXISTS "Room" CASCADE;

CREATE TABLE "Room" (
    id             serial PRIMARY KEY,
    view           varchar(40) NOT NULL,
    type           varchar(40) NOT NULL,
    room_number    varchar(100) NOT NULL
);

DROP TABLE IF EXISTS "Reservation" CASCADE;

CREATE TABLE "Reservation" (
    id             serial PRIMARY KEY,
    customer_id    integer NOT NULL,
    room_id        integer NOT NULL,
    start_date     date NOT NULL,
    end_date       date NOT NULL
);

ALTER TABLE "Reservation" 
   ADD CONSTRAINT fk_customer
   FOREIGN KEY (customer_id) 
   REFERENCES Customer(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "Reservation" 
   ADD CONSTRAINT fk_room
   FOREIGN KEY (room_id) 
   REFERENCES Room(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS "RoomPrice" CASCADE;

CREATE TABLE "RoomPrice" (
    id             serial PRIMARY KEY,
    room_id        integer NOT NULL,
    price          float NOT NULL,
    start_date     date NOT NULL,
    end_date       date NOT NULL
);

ALTER TABLE "RoomPrice" 
   ADD CONSTRAINT fk_room
   FOREIGN KEY (room_id) 
   REFERENCES Room(id)
   ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE IF EXISTS "Employee" CASCADE;

CREATE TABLE "Employee" (
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

DROP TABLE IF EXISTS "Reward" CASCADE;

CREATE TABLE "Reward" (
    id            serial PRIMARY KEY,
    customer_id   integer NOT NULL,
    type          varchar(100) NOT NULL,
    points        integer NOT NULL
);

ALTER TABLE "Reward" 
   ADD CONSTRAINT fk_customer
   FOREIGN KEY (customer_id) 
   REFERENCES Customer(id)
   ON DELETE CASCADE ON UPDATE CASCADE;
