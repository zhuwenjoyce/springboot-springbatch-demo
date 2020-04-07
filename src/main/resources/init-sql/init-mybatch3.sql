-- Oracle DB SQL
CREATE TABLE cat (
  id NUMBER(4) not null,
  catname varchar2(50) DEFAULT NULL,
  catage varchar2(3) DEFAULT NULL,
  cataddress varchar2(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO cat(id, catname, catage, cataddress) VALUES (1, '陈赫', '1', '上丰一路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (2, '邓超', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (3, '沈腾', '4', '上丰三路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (4, '贾玲', '1', '上丰四路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (5, '马伊琍', '1', '上丰一路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (6, '岳云鹏', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (7, '靳东', '4', '上丰三路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (8, '陆毅', '1', '上丰四路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (9, '鹿晗', '1', '上丰一路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (10, '辛芷蕾', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (11, '咖啡猫', '4', '上丰三路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (12, '折耳猫', '1', '上丰四路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (13, '大黄', '1', '上丰一路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (14, '小黄', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (15, '张曼玉', '32', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (16, '张国荣', '29', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (17, '张家辉', '42', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (18, '阮玲玉', '22', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (19, '郭富城', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (20, '柳岩', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (21, '孙俪', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (22, '赵薇', '2', '上丰二路');
INSERT INTO cat(id, catname, catage, cataddress) VALUES (23, '林心如', '2', '上丰二路');