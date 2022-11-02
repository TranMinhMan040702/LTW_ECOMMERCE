use ecommercewebsite;
create table address
(
    _id bigint auto_increment,
    name       nvarchar(200),

    constraint pk_address primary key (_id)
);
create table user
(
    _id bigint      auto_increment,
    firstname       nvarchar(32) not null,
    lastname        nvarchar(32) not null,
    slug            nvarchar(65) unique,
    id_card         varchar(9) unique,
    email           varchar(50) unique,
    phone           varchar(12) unique,
    isEmailActive   boolean   default false,
    isPhoneActive   boolean   default false,
    salt            varchar(50),
    hashed_password varchar(50) not null,
    role            enum('user','admin') default 'user',
    avatar          varchar(100),
    cover           varchar(100),
    point           int       default 0,
    e_wallet        decimal   default 0,
    createdAt       timestamp default '0000-00-00 00:00:00',
    updateAt        timestamp default now() on update now(),
    constraint pk_user primary key (_id)
);
create table user_address
(
    addressId bigint,
    userId    bigint,

    constraint pk_user_address primary key (addressId, userId),
    constraint fk_user foreign key (userId) references user (_id),
    constraint fk_address foreign key (addressId) references address (_id)
);
create table userlevel
(
    _id bigint auto_increment,
    name       nvarchar(32) not null unique,
    minPoint   int not null unique,
    discount   decimal unique,
    isDeleted  boolean   default false,
    createdAt  timestamp default '0000-00-00 00:00:00',
    updateAt   timestamp default now() on update now(),
    CONSTRAINT pk_userlevel PRIMARY KEY (_id)
);
create table commission
(
    _id bigint  auto_increment,
    name        nvarchar(32) not null unique,
    cost        decimal not null unique,
    description nvarchar(3000) not null,
    isDeleted   boolean   default false,
    createdAt   timestamp default '0000-00-00 00:00:00',
    updateAt    timestamp default now() on update now(),
    constraint pk_commission primary key (_id),
    constraint check_commission_cost check (0 <= cost)
);
create table store
(
    _id bigint   auto_increment,
    name         nvarchar(100) not null unique,
    bio          nvarchar(100) not null,
    slug         nvarchar(110) unique,
    ownerId      bigint not null,
    isActive     boolean   default false,
    isOpen       boolean   default false,
    avatar       varchar(100),
    cover        varchar(100),
    commissionId bigint,
    point        int       default 0,
    rating       int       default 3,
    e_wallet     decimal   default 0,
    createdAt    timestamp default '0000-00-00 00:00:00',
    updateAt     timestamp default now() on update now(),
    constraint pk_store primary key (_id),
    constraint fk_owner foreign key (_id) references user (_id),
    constraint fk_commission foreign key (commissionId) references commission (_id),
    constraint check_store_rating check (0 <= rating <= 5)
);
create table store_staffIds
(
    storeId bigint,
    staffId bigint,
    constraint fk_store foreign key (storeId) references store (_id),
    constraint fk_staff foreign key (staffId) references user (_id)

);
create table store_featured_images
(
    storeId         bigint,
    featured_images varchar(100) not null,
    constraint fk_images_store foreign key (storeId) references store (_id)
);

create table storelevel
(
    _id bigint auto_increment,
    name       nvarchar(32) not null unique,
    minPoint   int     not null unique,
    discout    decimal not null,
    isDeleted  boolean   default false,
    createdAt  timestamp default '0000-00-00 00:00:00',
    updateAt   timestamp default now() on update now(),
    constraint pk_storelevel primary key (_id)
);

create table category
(
    _id bigint auto_increment,
    name       nvarchar(32) not null unique,
    slug       nvarchar(42) unique,
    categoryId bigint,
    image      varchar(100),
    isDeleted  boolean   default false,
    createdAt  timestamp default '0000-00-00 00:00:00',
    updateAt   timestamp default now() on update now(),
    constraint pk_category primary key (_id),
    constraint fk_category foreign key (categoryId) references category (_id)
);
###########################
create table style
(
    _id bigint auto_increment,
    name       nvarchar(32) not null unique,
    idDeleted  boolean default false,
    createdAt  timestamp null,
    updatedAt  timestamp null,

    constraint pk_style primary key (_id),
    constraint check_style_name check (length(name) < 32)
);

create table style_category
(
    categoryId bigint,
    styleId    bigint,

    constraint pk_style_category primary key (categoryId),
    constraint fk_style_category_category foreign key (categoryId) references category (_id),
    constraint fk_style_category_style foreign key (styleId) references style (_id)
);

create table stylevalue
(
    _id bigint auto_increment,
    name       nvarchar(32) not null unique,
    styleId    bigint not null,
    idDeleted  boolean default false,
    createdAt  timestamp null,
    updatedAt  timestamp null,

    constraint pk_stylevalue primary key (_id),
    constraint fk_stylevalue_style foreign key (styleId) references style (_id),
    constraint check_stylevalue_name check (length(name) < 32)
);

create table image
(
    _id bigint auto_increment,
    url        varchar(255),

    constraint pk_image primary key (_id)
);

create table product
(
    _id bigint       auto_increment,
    name             nvarchar(100) not null unique,
    slug             varchar(100) unique,
    description      nvarchar(1000) not null,
    price            decimal not null,
    promotionalPrice decimal not null,
    quantity         int     not null,
    sold             int     not null,
    isActive         boolean default true,
    isSelling        boolean default true,
    categoryId       bigint  not null,
    storeId          bigint  not null,
    rating           int     default 3,
    createdAt        timestamp null,
    updatedAt        timestamp null,

    constraint pk_product primary key (_id),
    constraint fk_product_store foreign key (storeId) references store (_id),
    constraint fk_product_category foreign key (categoryId) references category (_id),
    constraint check_product_rating check (0 <= rating <= 5),
    constraint check_product_sold check (sold > 0),
    constraint check_product_quantity check (quantity > 0),
    constraint check_product_promotionalPrice check (promotionalPrice > 0),
    constraint check_product_price check (price > 0),
    constraint check_product_name check (length(name) <= 100)
);

create table image_product
(
    productID bigint,
    imageId   bigint,

    constraint pk_image_product primary key (productId, imageId),
    constraint fk_image_product_product foreign key (productId) references product (_id),
    constraint fk_image_product_image foreign key (imageId) references image (_id)
);

create table styleValue_product
(
    styleValueId bigint,
    productId    bigint,

    constraint pk_styleValue_product primary key (styleValueId),
    constraint fk_styleValue_product_product foreign key (productId) references product (_id),
    constraint fk_styleValue_product_styleValue foreign key (styleValueId) references styleValue (_id)
);

create table delivery
(
    _id bigint  auto_increment,
    name        nvarchar(100) not null unique,
    description nvarchar(1000) not null,
    price       decimal not null,
    idDeleted   boolean default false,
    createdAt   timestamp null,
    updatedAt   timestamp null,

    constraint pk_delivery primary key (_id),
    constraint check_delivery_price check (price > 0),
    constraint check_delivery_name check (length(name) <= 100)
);

create table userfollowstore
(
    _id bigint auto_increment,
    userId     bigint not null,
    storeId    bigint not null,
    createdAt  timestamp null,
    updatedAt  timestamp null,

    constraint pk_userfollowstore primary key (_id),
    constraint fk_userfollowstore_store foreign key (storeId) references store (_id),
    constraint fk_userfollowstore_user foreign key (userId) references user (_id)
);

create table userfollowproduct
(
    _id bigint auto_increment,
    userId     bigint not null,
    productId  bigint not null,
    createdAt  timestamp null,
    updatedAt  timestamp null,

    constraint pk_userfollowproduct primary key (_id),
    constraint fk_userfollowproduct_product foreign key (productId) references product (_id),
    constraint fk_userfollowproduct_user foreign key (userId) references user (_id)
);

create table orders
(
    _id bigint      auto_increment,
    userId          bigint  not null,
    storeId         bigint  not null,
    deliveryId      bigint  not null,
    commissionId    bigint  not null,
    address         nvarchar(255) not null,
    phone           int not null,
    status          enum('not processed', 'processing', 'shipped', 'delivered', 'cancelled') default 'not processed',
    isPaidBefore    boolean   default false,
    amountFromUser  decimal not null,
    amountFromStore decimal not null,
    amountToStore   decimal not null,
    amountToGD      decimal not null,
    createdAt       timestamp default '0000-00-00 00:00:00',
    updatedAt       timestamp default now() on update now(),

    constraint pk_orders primary key (_id),
    constraint fk_orders_user foreign key (userId) references user (_id),
    constraint fk_orders_store foreign key (storeId) references store (_id),
    constraint fk_oder_delivery foreign key (deliveryId) references delivery (_id),
    constraint fk_orders_commission foreign key (commissionId) references commission (_id),
    constraint check_orders_amountFromUser check (amountFromUser >= 0),
    constraint check_orders_amountFromStore check (amountFromStore >= 0),
    constraint check_orders_amountToStore check (amountToStore >= 0),
    constraint check_orders_amountToGD check (amountToGD >= 0)
);

create table review
(
    _id bigint        not null auto_increment,
    userId    bigint  not null,
    productId bigint  not null,
    storeId   bigint  not null,
    ordersId  bigint  not null,
    content   nvarchar(1001) not null,
    stars     int not null,
    createdAt timestamp default '0000-00-00 00:00:00',
    updatedAt timestamp default now() on update now(),

    constraint pk_review primary key (_id),
    constraint fk_review_user foreign key (userId) references user (_id),
    constraint fk_review_product foreign key (productId) references product (_id),
    constraint fk_review_store foreign key (storeId) references store (_id),
    constraint fk_review_orders foreign key (ordersId) references orders (_id),
    constraint check_review_content check (length(content) <= 1000),
    constraint check_review_stars check (0 <= stars <= 5)
);

create table ordersItem
(
    _id bigint        not null auto_increment,
    ordersId  bigint  not null,
    productId bigint  not null,
    count     int not null,
    createdAt timestamp default '0000-00-00 00:00:00',
    updatedAt timestamp default now() on update now(),

    constraint pk_ordersItem primary key (_id),
    constraint fk_ordersItem_orders foreign key (ordersId) references orders (_id),
    constraint fk_ordersItem_product foreign key (productId) references product (_id),
    constraint check_ordersItem_count check (count >= 1)
);

create table ordersItem_styleValueIds
(
    styleValueId bigint not null,
    idOrdersItem bigint not null,
    constraint pk_ordersItem_styleValueIds primary key (styleValueId),
    constraint fk_ordersItem_styleValueIds foreign key (idOrdersItem) references ordersItem (_id)
);

create table cart
(
    _id bigint       not null auto_increment,
    userId    bigint not null,
    storeId   bigint not null,
    createdAt timestamp default '0000-00-00 00:00:00',
    updatedAt timestamp default now() on update now(),

    constraint pk_cart primary key (_id),
    constraint fk_cart_user foreign key (userId) references user (_id),
    constraint fk_cart_store foreign key (storeId) references store (_id)
);

create table cartItem
(
    _id bigint       not null auto_increment,
    cartId    bigint not null,
    productId bigint not null,
    count     bigint not null,
    createdAt timestamp default '0000-00-00 00:00:00',
    updatedAt timestamp default now() on update now(),

    constraint pk_cartItem primary key (_id),
    constraint fk_cartItem_card foreign key (cartId) references cart (_id),
    constraint fk_cartItem_product foreign key (productId) references product (_id),
    constraint check_cartItem_count check (count >= 1)
);

create table cartItem_styleValueIds
(
    styleValueId bigint not null,
    idCartItem   bigint not null,
    constraint pk_cartItem_styleValueIds primary key (styleValueId),
    constraint fk_cartItem_styleValueIds foreign key (idCartItem) references cartItem (_id)
);

create table transaction
(
    _id bigint        not null auto_increment,
    userId    bigint  not null,
    storeId   bigint  not null,
    isUp      boolean not null,
    amount    decimal not null,
    createdAt timestamp default '0000-00-00 00:00:00',
    updatedAt timestamp default now() on update now(),

    constraint pk_transaction primary key (_id),
    constraint fk_transaction_user foreign key (userId) references user (_id),
    constraint fk_transaction_store foreign key (storeId) references store (_id)
);

