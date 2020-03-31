CREATE TABLE CUSTOMER (
	ID NUMBER(38)  NOT NULL PRIMARY KEY ,
	VERSION NUMBER(38) ,
	NAME VARCHAR2(45) ,
	CREDIT DECIMAL(10,2)
) ;

INSERT INTO CUSTOMER (ID, VERSION, NAME, CREDIT) VALUES (1, 0, 'customer1', 100000);
INSERT INTO CUSTOMER (ID, VERSION, NAME, CREDIT) VALUES (2, 0, 'customer2', 100000);
INSERT INTO CUSTOMER (ID, VERSION, NAME, CREDIT) VALUES (3, 0, 'customer3', 100000);
INSERT INTO CUSTOMER (ID, VERSION, NAME, CREDIT) VALUES (4, 0, 'customer4', 100000);