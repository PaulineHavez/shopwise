CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE MERCHANT(
                         merchant_id UUID DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         phone_number VARCHAR(15) NOT NULL,
                         headquarters_address VARCHAR(255) NOT NULL,
                         siret_number VARCHAR(14) NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         hash_password VARCHAR(255) NOT NULL,
                         automatic_earned_points SMALLINT,
                         PRIMARY KEY(merchant_id),
                         UNIQUE(phone_number),
                         UNIQUE(siret_number),
                         UNIQUE(email)
);

CREATE TABLE SERVICE(
                        service_id UUID DEFAULT gen_random_uuid(),
                        name VARCHAR(50) NOT NULL,
                        merchant_id UUID NOT NULL,
                        PRIMARY KEY(service_id),
                        FOREIGN KEY(merchant_id) REFERENCES MERCHANT(merchant_id)
);

CREATE TABLE CUSTOMER(
                         customer_id UUID DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         phone_number VARCHAR(15) NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         merchant_id UUID NOT NULL,
                         hash_password VARCHAR(255),
                         PRIMARY KEY(customer_id),
                         UNIQUE(phone_number),
                         UNIQUE(email),
                         FOREIGN KEY(merchant_id) REFERENCES MERCHANT(merchant_id)
);

CREATE TABLE APPOINTMENT(
                            appointment_id UUID DEFAULT gen_random_uuid(),
                            start_at TIMESTAMP NOT NULL,
                            end_at TIMESTAMP NOT NULL,
                            status VARCHAR(255) NOT NULL,
                            earned_points SMALLINT,
                            service_id UUID NOT NULL,
                            merchant_id UUID NOT NULL,
                            customer_id UUID NOT NULL,
                            PRIMARY KEY(appointment_id),
                            FOREIGN KEY(service_id) REFERENCES SERVICE(service_id),
                            FOREIGN KEY(merchant_id) REFERENCES MERCHANT(merchant_id),
                            FOREIGN KEY(customer_id) REFERENCES CUSTOMER(customer_id)
);

CREATE TABLE TRANSACTION(
                            transaction_id UUID DEFAULT gen_random_uuid(),
                            transaction_date TIMESTAMP NOT NULL,
                            earned_points SMALLINT,
                            status VARCHAR(255) NOT NULL,
                            service_id UUID NOT NULL,
                            merchant_id UUID NOT NULL,
                            customer_id UUID NOT NULL,
                            PRIMARY KEY(transaction_id),
                            FOREIGN KEY(service_id) REFERENCES SERVICE(service_id),
                            FOREIGN KEY(merchant_id) REFERENCES MERCHANT(merchant_id),
                            FOREIGN KEY(customer_id) REFERENCES CUSTOMER(customer_id)
);

