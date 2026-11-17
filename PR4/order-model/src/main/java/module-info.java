module order.model {
    requires static lombok;   // Lombok потрібен лише на етапі компіляції

    exports org.example.model;
    exports org.example.model.exception;
}
