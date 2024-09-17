CREATE
DATABASE IF NOT EXISTS inventory_receipts;

USE
inventory_receipts;

CREATE TABLE users
(
    id               VARCHAR(10) NOT NULL PRIMARY KEY,                                     -- Khóa chính của bảng, bắt đầu bằng "USR" và 5 ký tự số
    full_name        VARCHAR(50),                                                          -- Tên đầy đủ của người dùng
    phone            CHAR(10),                                                             -- Số điện thoại của người dùng
    email            CHAR(50),                                                             -- Địa chỉ email của người dùng
    password         TEXT,                                                                 -- Mật khẩu của người dùng
    address          TEXT,                                                                 -- Địa chỉ của người dùng
    avatar           TEXT,                                                                 -- Ảnh đại diện của người dùng
    is_active        BOOLEAN,                                                              -- Trạng thái hoạt động của người dùng
    gender           SMALLINT,                                                             -- Giới tính của người dùng
    role             ENUM('COORDINATOR', 'WAREHOUSE_STAFF', 'WAREHOUSE_MANAGER') NOT NULL, -- Vai trò của người dùng
    last_change_pass TIMESTAMP,                                                            -- Thời gian thay đổi mật khẩu cuối cùng
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE supplier_groups
(
    id         VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng, bắt đầu bằng "SUPGR" và 5 ký tự số
    name       VARCHAR(50),                      -- Tên nhóm nhà cung cấp
    note       TEXT,                             -- Ghi chú
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE suppliers
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY,                                -- Khóa chính của bảng, bắt đầu bằng "SUP" và 5 ký tự số
    name              VARCHAR(50),                                                     -- Tên nhà cung cấp
    phone             CHAR(10),                                                        -- Số điện thoại của nhà cung cấp
    email             CHAR(50),                                                        -- Địa chỉ email của nhà cung cấp
    address           TEXT,                                                            -- Địa chỉ của nhà cung cấp
    status            ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE', -- Trạng thái hoạt động của nhà cung cấp
    supplier_group_id VARCHAR(10),                                                     -- Nhóm nhà cung cấp
    current_debt      DECIMAL(10, 2),                                                  -- Nợ hiện tại
    total_refund      DECIMAL(10, 2),                                                  -- Tổng số tiền đã hoàn trả\
    tags              TEXT,                                                            -- Tags
    note              TEXT,                                                            -- Ghi chú
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_group_id) REFERENCES supplier_groups (id)
);

CREATE TABLE orders
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY,
    status            ENUM('PENDING', 'RECEIVED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    note              TEXT,
    tags              TEXT,
    expected_at       TIMESTAMP,
    completed_at      TIMESTAMP,
    ended_at          TIMESTAMP,
    cancelled_at      TIMESTAMP,
    user_created_id   VARCHAR(10),
    user_completed_id VARCHAR(10),
    user_cancelled_id VARCHAR(10),
    user_ended_id     VARCHAR(10),
    supplier_id       VARCHAR(10),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES Users (id),
    FOREIGN KEY (user_completed_id) REFERENCES Users (id),
    FOREIGN KEY (user_cancelled_id) REFERENCES Users (id),
    FOREIGN KEY (user_ended_id) REFERENCES Users (id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);

CREATE TABLE brands
(
    id         VARCHAR(10) NOT NULL PRIMARY KEY,
    name       VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE products
(
    id              VARCHAR(10) NOT NULL PRIMARY KEY,
    name            VARCHAR(50),
    images          TEXT,
    type            TEXT,
    quantity        DECIMAL(10, 2),
    sold            DECIMAL(10, 2),
    description     TEXT,
    unit            VARCHAR(50),
    cost_price      DECIMAL(10, 2),
    wholesale_price DECIMAL(10, 2),
    retail_price    DECIMAL(10, 2),
    tags            TEXT,
    status          ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    brand_id        VARCHAR(10),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES brands (id)
);

CREATE TABLE order_details
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY,
    order_id          VARCHAR(10),
    product_id        VARCHAR(10),
    quantity          DECIMAL(10, 2),
    price             DECIMAL(10, 2),
    discount          DECIMAL(10, 2),
    imported_quantity DECIMAL(10, 2),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE categories
(
    id         VARCHAR(10) NOT NULL PRIMARY KEY,
    name       VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE category_products
(
    id          VARCHAR(10) NOT NULL PRIMARY KEY,
    category_id VARCHAR(10),
    product_id  VARCHAR(10),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE price_adjustments
(
    id              VARCHAR(10) NOT NULL PRIMARY KEY,
    status          ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    note            TEXT,
    tags            TEXT,
    new_price       DECIMAL(10, 2),
    user_created_id VARCHAR(10),
    product_id      VARCHAR(10),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES Users (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE gins
(
    id                 VARCHAR(10) NOT NULL PRIMARY KEY,
    status             ENUM('PENDING', 'BALANCED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    balanced_at        TIMESTAMP,
    user_created_id    VARCHAR(10),
    user_balanced_id   VARCHAR(10),
    user_inspection_id VARCHAR(10),
    note               TEXT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES Users (id),
    FOREIGN KEY (user_balanced_id) REFERENCES Users (id),
    FOREIGN KEY (user_inspection_id) REFERENCES Users (id)
);

CREATE TABLE gins_products
(
    id                   VARCHAR(10) NOT NULL PRIMARY KEY,
    actual_stock         DECIMAL(10, 2),
    discrepancy_quantity DECIMAL(10, 2),
    reason               TEXT,
    note                 TEXT,
    gin_id               VARCHAR(10),
    product_id           VARCHAR(10),
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (gin_id) REFERENCES gins (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE grns
(
    id                     VARCHAR(10) NOT NULL PRIMARY KEY,
    status                 ENUM('PENDING', 'RECEIVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    received_status        ENUM('PENDING', 'RECEIVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    expected_delivery_at   TIMESTAMP,
    received_at            TIMESTAMP,
    cancelled_at           TIMESTAMP,
    payment_at             TIMESTAMP,
    user_created_id        VARCHAR(10),
    user_imported_id       VARCHAR(10),
    user_cancelled_id      VARCHAR(10),
    user_ended_id          VARCHAR(10),
    total_receipt_quantity DECIMAL(10, 2),
    discount               DECIMAL(10, 2),
    tax_amount             DECIMAL(10, 2),
    total_value            DECIMAL(10, 2),
    payment_status         ENUM('PENDING', 'PAID', 'UNPAID') NOT NULL DEFAULT 'PENDING',
    return_status          ENUM('PENDING', 'RETURNED', 'UNRETURNED') NOT NULL DEFAULT 'PENDING',
    refund_status          ENUM('PENDING', 'REFUNDED', 'UNREFUNDED') NOT NULL DEFAULT 'PENDING',
    note                   TEXT,
    tags                   TEXT,
    supplier_id            VARCHAR(10),
    order_id               VARCHAR(10),
    import_cost            TEXT,
    payment_method         TEXT,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES Users (id),
    FOREIGN KEY (user_imported_id) REFERENCES Users (id),
    FOREIGN KEY (user_cancelled_id) REFERENCES Users (id),
    FOREIGN KEY (user_ended_id) REFERENCES Users (id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE grn_products
(
    id         VARCHAR(10) NOT NULL PRIMARY KEY,
    quantity   DECIMAL(10, 2),
    discount   DECIMAL(10, 2),
    tax        DECIMAL(10, 2),
    price      DECIMAL(10, 2),
    grn_id     VARCHAR(10),
    product_id VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (grn_id) REFERENCES grns (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE refund_information
(
    id                      VARCHAR(10) NOT NULL PRIMARY KEY,
    total_refunded_quantity DECIMAL(10, 2),
    total_refunded_value    DECIMAL(10, 2),
    total_refunded_cost     DECIMAL(10, 2),
    total_refunded_tax      DECIMAL(10, 2),
    total_refunded_discount DECIMAL(10, 2),
    reason                  TEXT,
    refund_type             TEXT,
    refund_amount           DECIMAL(10, 2),
    refund_status           ENUM('PENDING', 'REFUNDED', 'UNREFUNDED') NOT NULL DEFAULT 'PENDING',
    refund_payment_status   ENUM('PENDING', 'PAID', 'UNPAID') NOT NULL DEFAULT 'PENDING',
    refund_received_at      TIMESTAMP,
    user_created_id         VARCHAR(10),
    grn_id                  VARCHAR(10),
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES Users (id),
    FOREIGN KEY (grn_id) REFERENCES grns (id)
);

CREATE TABLE refund_information_details
(
    id                    VARCHAR(10) NOT NULL PRIMARY KEY,
    quantity              DECIMAL(10, 2),
    refunded_price        DECIMAL(10, 2),
    amount                DECIMAL(10, 2),
    refund_information_id VARCHAR(10),
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (refund_information_id) REFERENCES refund_information (id)
);

CREATE TABLE transaction_categories
(
    id          VARCHAR(10) NOT NULL PRIMARY KEY,
    name        VARCHAR(50),
    description TEXT,
    type        ENUM('INCOME', 'EXPENSE') NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO transaction_categories (id, name, description, type)
VALUES ('TSC00001', 'Tự động', null, 'INCOME');
INSERT INTO transaction_categories (id, name, description, type)
VALUES ('TSC00002', 'Tự động', null, 'EXPENSE');

INSERT INTO transaction_category_sequences (id)
VALUES (1),
       (2);

CREATE TABLE transactions
(
    id                      VARCHAR(10) NOT NULL PRIMARY KEY,
    amount                  DECIMAL(10, 2),
    note                    TEXT,
    tags                    TEXT,
    payment_method          ENUM('CASH', 'BANK_TRANSFER', 'CREDIT_CARD') NOT NULL,
    type                    ENUM('INCOME', 'EXPENSE') NOT NULL,
    status                  ENUM('COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'COMPLETED',
    reference_code          VARCHAR(50),
    reference_id            VARCHAR(10),
    recipient_group         VARCHAR(50),
    recipient_id            VARCHAR(10),
    recipient_name          VARCHAR(50),
    transaction_category_id VARCHAR(10),
    user_created_id         VARCHAR(10),
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_category_id) REFERENCES transaction_categories (id),
    FOREIGN KEY (user_created_id) REFERENCES users (id)
);