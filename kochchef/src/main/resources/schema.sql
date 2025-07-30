CREATE TABLE IF NOT EXISTS KOCHCHEF_ROLE (
                               ID INT AUTO_INCREMENT PRIMARY KEY,
                               NAME VARCHAR(250) NOT NULL,
                               DESCRIPTION VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS KOCHCHEF_USER (
                               ID INT AUTO_INCREMENT PRIMARY KEY,
                               USERNAME VARCHAR(250) NOT NULL,
                               EMAIL VARCHAR(250) NOT NULL,
                               PASSWORD VARCHAR(250) NOT NULL,
                               ROLE INT,
                               CONSTRAINT FK_ROLE_ID FOREIGN KEY (ROLE) REFERENCES KOCHCHEF_ROLE(ID),
                               BLOCKED BOOLEAN DEFAULT FALSE,
                               LOGIN_TRIES INT DEFAULT 0,
                               BLOCKED_SINCE TIMESTAMP DEFAULT NULL
);
