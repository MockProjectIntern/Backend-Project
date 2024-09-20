CREATE
DATABASE IF NOT EXISTS inventory_receipts;

USE
inventory_receipts;

CREATE TABLE users
(
    id               VARCHAR(10) NOT NULL PRIMARY KEY,                                    -- Khóa chính của bảng (tự động tăng)
    sub_id           VARCHAR(10),                                                         -- Mã định danh ban đầu
    full_name        VARCHAR(50),                                                         -- Tên đầy đủ của người dùng
    phone            CHAR(10),                                                            -- Số điện thoại của người dùng
    email            CHAR(50),                                                            -- Địa chỉ email của người dùng
    password         TEXT,                                                                -- Mật khẩu của người dùng
    address          TEXT,                                                                -- Địa chỉ của người dùng
    avatar           TEXT,                                                                -- Ảnh đại diện của người dùng
    is_active        BOOLEAN,                                                             -- Trạng thái hoạt động của người dùng
    gender           SMALLINT,                                                            -- Giới tính của người dùng
    role             ENUM('COORDINATOR', 'WAREHOUSE_STAFF', 'WAREHOUSE_MANAGER') NOT NULL,-- Vai trò của người dùng
    last_change_pass TIMESTAMP,                                                           -- Thời gian thay đổi mật khẩu cuối cùng
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE supplier_groups
(
    id         VARCHAR(10) NOT NULL PRIMARY KEY,                     -- Khóa chính của bảng (tự động tăng)
    sub_id     VARCHAR(10),                                          -- Mã định danh ban đầu
    name       VARCHAR(50),                                          -- Tên nhóm nhà cung cấp
    note       TEXT,                                                 -- Ghi chú
    status     ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE', -- Trạng thái hoạt động
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE suppliers
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY,                                -- Khóa chính của bảng (tự động tăng)
    sub_id            VARCHAR(10),                                                     -- Mã định danh ban đầu
    name              VARCHAR(50),                                                     -- Tên nhà cung cấp
    phone             CHAR(10),                                                        -- Số điện thoại của nhà cung cấp
    email             CHAR(50),                                                        -- Địa chỉ email của nhà cung cấp
    address           TEXT,                                                            -- Địa chỉ của nhà cung cấp
    status            ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE', -- Trạng thái hoạt động
    supplier_group_id VARCHAR(10),                                                     -- Nhóm nhà cung cấp
    current_debt      DECIMAL(10, 2),                                                  -- Nợ hiện tại
    total_refund      DECIMAL(10, 2),                                                  -- Tổng số tiền đã hoàn trả
    tags              TEXT,                                                            -- Tags
    note              TEXT,                                                            -- Ghi chú
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_group_id) REFERENCES supplier_groups (id)
);

CREATE TABLE orders
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY,                                    -- Khóa chính của bảng (tự động tăng)
    sub_id            VARCHAR(10),                                                         -- Mã định danh ban đầu
    status            ENUM('PENDING', 'RECEIVED', 'CANCELLED') NOT NULL DEFAULT 'PENDING', -- Trạng thái đơn hàng
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
    FOREIGN KEY (user_created_id) REFERENCES users (id),
    FOREIGN KEY (user_completed_id) REFERENCES users (id),
    FOREIGN KEY (user_cancelled_id) REFERENCES users (id),
    FOREIGN KEY (user_ended_id) REFERENCES users (id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);

CREATE TABLE brands
(
    id         VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id     VARCHAR(10),                      -- Mã định danh ban đầu
    name       VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE products
(
    id              VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id          VARCHAR(10),                      -- Mã định danh ban đầu
    name            VARCHAR(50),
    images          TEXT,
    types           TEXT,
    quantity        DECIMAL(10, 2),
    sold            DECIMAL(10, 2),
    description     TEXT,
    unit            VARCHAR(50),
    cost_price      DECIMAL(10, 2),
    wholesale_price DECIMAL(10, 2),
    retail_price    DECIMAL(10, 2),
    tags            TEXT,
    status          ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    category_id     VARCHAR(10),
    brand_id        VARCHAR(10),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES brands (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE order_details
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id            VARCHAR(10),                      -- Mã định danh ban đầu
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
    id         VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id     VARCHAR(10),                      -- Mã định danh ban đầu
    name       VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE gins
(
    id                 VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id             VARCHAR(10),                      -- Mã định danh ban đầu
    status             ENUM('PENDING', 'BALANCED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    balanced_at        TIMESTAMP,
    user_created_id    VARCHAR(10),
    user_balanced_id   VARCHAR(10),
    user_inspection_id VARCHAR(10),
    note               TEXT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES users (id),
    FOREIGN KEY (user_balanced_id) REFERENCES users (id),
    FOREIGN KEY (user_inspection_id) REFERENCES users (id)
);

CREATE TABLE gins_products
(
    id                   VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id               VARCHAR(10),                      -- Mã định danh ban đầu
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
    id                VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id            VARCHAR(10),                      -- Mã định danh ban đầu
    status            ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    histories         TEXT,
    user_created_id   VARCHAR(10),
    user_completed_id VARCHAR(10),
    user_cancelled_id VARCHAR(10),
    supplier_id       VARCHAR(10),
    order_id          VARCHAR(10),
    completed_at      TIMESTAMP,
    cancelled_at      TIMESTAMP,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_created_id) REFERENCES users (id),
    FOREIGN KEY (user_completed_id) REFERENCES users (id),
    FOREIGN KEY (user_cancelled_id) REFERENCES users (id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE grn_products
(
    id                VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id            VARCHAR(10),                      -- Mã định danh ban đầu
    quantity          DECIMAL(10, 2),
    imported_quantity DECIMAL(10, 2),
    price             DECIMAL(10, 2),
    discount          DECIMAL(10, 2),
    tax               DECIMAL(10, 2),
    unit              VARCHAR(50),
    note              TEXT,
    grn_id            VARCHAR(10),
    product_id        VARCHAR(10),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (grn_id) REFERENCES grns (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE transaction_categories -- Loại giao dịch
(
    id          VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id      VARCHAR(10),                      -- Mã định danh ban đầu
    name        VARCHAR(50),
    description TEXT,
    type        ENUM("INCOME", "EXPENSE") NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO transaction_categories (id, sub_id, name, description, type)
VALUES ('TSC00001', 'TSC00001', 'Tự động', 'Loại phiếu tự động', 'INCOME'),
       ('TSC00002', 'TSC00002', 'Tự động', 'Loại phiếu tự động', 'EXPENSE');

INSERT INTO transaction_category_sequences (id)
VALUES (1),
       (2);

CREATE TABLE transactions
(
    id                      VARCHAR(10) NOT NULL PRIMARY KEY, -- Khóa chính của bảng (tự động tăng)
    sub_id                  VARCHAR(10),                      -- Mã định danh ban đầu
    amount                  DECIMAL(10, 2),
    payment_method          ENUM("CASH", "BANK_TRANSFER", "CREDIT_CARD") DEFAULT "CASH",
    note                    TEXT,
    tags                    TEXT,
    reference_code          VARCHAR(50),
    reference_id            VARCHAR(10),
    recipient_group         TEXT,
    recipient_id            VARCHAR(10),
    recipient_name          VARCHAR(50),
    type                    ENUM("INCOME", "EXPENSE") NOT NULL,
    status                  ENUM("COMPLETED", "CANCELLED") NOT NULL DEFAULT "COMPLETED",
    transaction_category_id VARCHAR(10),
    user_created_id         VARCHAR(10),
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_category_id) REFERENCES transaction_categories (id),
    FOREIGN KEY (user_created_id) REFERENCES users (id)
);