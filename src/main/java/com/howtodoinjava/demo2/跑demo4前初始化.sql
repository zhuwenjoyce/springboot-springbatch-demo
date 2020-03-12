-- use Oracle 11g database

CREATE TABLE customer (
    id number(8,2) NOT NULL,
    firstName VARCHAR(255) NULL,
    lastName VARCHAR(255) NULL,
    birthdate VARCHAR(255) NULL,
PRIMARY KEY (id)
) ;

INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('1', 'John', 'Doe', '1952-10-10 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('2', 'Amy', 'Eugene', '1985-07-05 17:10:00');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('3', 'Laverne', 'Mann', '1988-12-11 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('4', 'Janice', 'Preston', '1960-02-19 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('5', 'Pauline', 'Rios', '1977-08-29 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('6', 'Perry', 'Burnside', '1981-03-10 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('7', 'Todd', 'Kinsey', '1998-12-14 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('8', 'Jacqueline', 'Hyde', '1983-03-20 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('9', 'Rico', 'Hale', '2000-10-10 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('10', 'Samuel', 'Lamm', '1999-11-11 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('11', 'Robert', 'Coster', '1972-10-10 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('12', 'Tamara', 'Soler', '1978-01-02 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('13', 'Justin', 'Kramer', '1951-11-19 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('14', 'Andrea', 'Law', '1959-10-14 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('15', 'Laura', 'Porter', '2010-12-12 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('16', 'Michael', 'Cantu', '1999-04-11 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('17', 'Andrew', 'Thomas', '1967-05-04 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('18', 'Jose', 'Hannah', '1950-09-16 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('19', 'Valerie', 'Hilbert', '1966-06-13 10:10:10');
INSERT INTO customer (id, firstName, lastName, birthdate) VALUES ('20', 'Patrick', 'Durham', '1978-10-12 10:10:10');