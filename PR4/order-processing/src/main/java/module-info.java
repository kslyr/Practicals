module order.processing {
    requires transitive order.model;
    requires transitive order.storage;   // OrderStorage видно в конструкторі OrderProcessor

    exports org.example.processing;
    // org.example.processing.internal НЕ експортується
}
