module order.storage {
    // transitive: у публічному API сховища є Order і Product, тож їх побачать і ті, хто підключив storage
    requires transitive order.model;

    exports org.example.storage;
    // org.example.storage.internal НЕ експортується: IdGenerator ззовні недоступний
}
