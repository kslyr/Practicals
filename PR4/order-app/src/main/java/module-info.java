module order.app {
    requires order.model;
    requires order.processing;
    requires javafaker;
    requires order.storage;
    requires java.sql;
}
