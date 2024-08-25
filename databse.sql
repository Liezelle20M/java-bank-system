Create database Banking_system;

use Banking_system;


CREATE TABLE Bank (
     FirstName VARCHAR(255) NOT NULL,
     LastName VARCHAR(255) NOT NULL,
     Password VARCHAR(255) NOT NULL,
     AccountNumber INT NOT NULL,
     Balance DECIMAL(10, 2) DEFAULT 0.00,
     Type VARCHAR(50) NULL,
     PRIMARY KEY (AccountNumber)
 );
 
 select * from Bank;
 
 SHOW GRANTS FOR 'root'@'localhost';
ALTER TABLE banking_system MODIFY COLUMN Type VARCHAR(255) NULL;
ALTER TABLE bank MODIFY COLUMN Type VARCHAR(255) NULL;

select * from bank;

ALTER TABLE bank MODIFY COLUMN Password VARCHAR(255) NULL;
ALTER TABLE bank MODIFY COLUMN AccountNumber INT Default 0 ;
