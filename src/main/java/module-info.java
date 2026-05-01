module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

//    requires org.controlsfx.controls;
//
//    opens drinkshop.ui to javafx.fxml;
//    exports drinkshop.ui;
//
//    opens drinkshop.domain to  javafx.base;
//    exports drinkshop.domain;

    // UI
    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;

    // DOMAIN
    opens drinkshop.domain to javafx.base;
    exports drinkshop.domain;

    // 🔥 ADAUGI ASTEA (IMPORTANT)
    exports drinkshop.repository;
    exports drinkshop.service;
    exports drinkshop.service.validator;

    opens drinkshop.repository to org.mockito;
    opens drinkshop.service to org.mockito;
    opens drinkshop.service.validator to org.mockito;
}