package com.hostelhub.view;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.util.Duration;

public class RoleSelection {

    public static void show() {

        BorderPane root = new BorderPane();

        root.setStyle("""
                -fx-background-color: linear-gradient(
                    to bottom right,
                    #08050F,
                    #120A20,
                    #21103D,
                    #10091D
                );
                """);

        Label logo = new Label("⌂  HOSTEL HUB");

        logo.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 25px;
                -fx-font-weight: bold;
                """);

        BorderPane.setMargin(
                logo,
                new Insets(28, 45, 15, 45)
        );

        root.setTop(logo);

        Label title = new Label("Choose Your Role");

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 38px;
                -fx-font-weight: bold;
                """);

        Label subtitle = new Label(
                "Select how you want to continue"
        );

        subtitle.setStyle("""
                -fx-text-fill: #BDB1D0;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 16px;
                """);

        VBox header = new VBox(
                8,
                title,
                subtitle
        );

        header.setAlignment(Pos.CENTER);

        Label studentIcon = new Label("👨‍🎓");

        studentIcon.setStyle("""
                -fx-font-size: 48px;
                """);

        Label studentTitle = new Label("STUDENT");

        studentTitle.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                """);

        Label studentDescription = new Label(
                "Manage your hostel account"
        );

        studentDescription.setStyle("""
                -fx-text-fill: #AFA3BF;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 14px;
                """);

        Button studentButton =
                new Button("CONTINUE  →");

        styleButton(studentButton);

        studentButton.setOnAction(e -> {
            StudentLogin.show();
        });

        VBox studentCard = new VBox(
                15,
                studentIcon,
                studentTitle,
                studentDescription,
                studentButton
        );

        styleCard(studentCard);

        Label wardenIcon = new Label("👨‍💼");

        wardenIcon.setStyle("""
                -fx-font-size: 48px;
                """);

        Label wardenTitle = new Label("WARDEN");

        wardenTitle.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                """);

        Label wardenDescription = new Label(
                "Manage hostel operations"
        );

        wardenDescription.setStyle("""
                -fx-text-fill: #AFA3BF;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 14px;
                """);

        Button wardenButton =
                new Button("CONTINUE  →");

        styleButton(wardenButton);

        wardenButton.setOnAction(e -> {
            System.out.println("Warden Login");
        });

        VBox wardenCard = new VBox(
                15,
                wardenIcon,
                wardenTitle,
                wardenDescription,
                wardenButton
        );

        styleCard(wardenCard);

        HBox cards = new HBox(
                35,
                studentCard,
                wardenCard
        );

        cards.setAlignment(Pos.CENTER);

        VBox content = new VBox(
                40,
                header,
                cards
        );

        content.setAlignment(Pos.CENTER);

        StackPane center =
                new StackPane(content);

        root.setCenter(center);

        Label back =
                new Label("← Back to Welcome");

        back.setStyle("""
                -fx-text-fill: #9183A5;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 13px;
                -fx-cursor: hand;
                """);

        back.setOnMouseClicked(e -> {
            new Welcome().start(Welcome.stage);
        });

        BorderPane.setAlignment(
                back,
                Pos.CENTER
        );

        BorderPane.setMargin(
                back,
                new Insets(10, 0, 20, 0)
        );

        root.setBottom(back);

        Scene scene =
                new Scene(
                        root,
                        1200,
                        700
                );

        Welcome.stage.setScene(scene);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(600),
                        content
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        fade.play();
    }

    private static void styleCard(
            VBox card
    ) {

        card.setAlignment(Pos.CENTER);

        card.setPrefWidth(320);
        card.setPrefHeight(330);

        card.setStyle("""
                -fx-background-color: #151022;
                -fx-background-radius: 25px;
                -fx-border-color: #352451;
                -fx-border-radius: 25px;
                """);

        DropShadow shadow =
                new DropShadow();

        shadow.setRadius(25);

        shadow.setColor(
                Color.rgb(
                        100,
                        50,
                        190,
                        0.30
                )
        );

        card.setEffect(shadow);

        card.setOnMouseEntered(e -> {

            ScaleTransition animation =
                    new ScaleTransition(
                            Duration.millis(180),
                            card
                    );

            animation.setToX(1.04);
            animation.setToY(1.04);

            animation.play();
        });

        card.setOnMouseExited(e -> {

            ScaleTransition animation =
                    new ScaleTransition(
                            Duration.millis(180),
                            card
                    );

            animation.setToX(1);
            animation.setToY(1);

            animation.play();
        });
    }

    private static void styleButton(
            Button button
    ) {

        button.setPrefWidth(220);
        button.setPrefHeight(45);

        button.setStyle("""
                -fx-background-color: linear-gradient(
                    to right,
                    #7437E8,
                    #A064FF
                );
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-background-radius: 25px;
                -fx-cursor: hand;
                """);

        DropShadow glow =
                new DropShadow();

        glow.setRadius(18);

        glow.setColor(
                Color.rgb(
                        145,
                        85,
                        255,
                        0.45
                )
        );

        button.setEffect(glow);
    }
}