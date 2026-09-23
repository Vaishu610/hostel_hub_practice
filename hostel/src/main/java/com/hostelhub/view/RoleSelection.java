
package com.hostelhub.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleSelection {

        private static final List<Circle> particles = new ArrayList<>();

        public static void show() {

                BorderPane root = new BorderPane();

                root.setStyle("""
                                -fx-background-color: linear-gradient(
                                    to bottom right,
                                    #06040C,
                                    #10081C,
                                    #1C0D35,
                                    #0A0613
                                );
                                """);

                StackPane center = new StackPane();

                Circle glow1 = createGlow(220, -450, -230, 0.13);
                Circle glow2 = createGlow(260, 450, 220, 0.10);
                Circle glow3 = createGlow(130, 330, -260, 0.08);
                Circle glow4 = createGlow(100, -330, 250, 0.07);

                center.getChildren().addAll(
                                glow1,
                                glow2,
                                glow3,
                                glow4);

                particles.clear();
                createParticles(center);

                Label logo = new Label("⌂  HOSTEL HUB");

                logo.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 25px;
                                -fx-font-weight: bold;
                                -fx-letter-spacing: 1px;
                                """);

                DropShadow logoGlow = new DropShadow();
                logoGlow.setRadius(18);
                logoGlow.setSpread(0.18);
                logoGlow.setColor(
                                Color.rgb(150, 85, 255, 0.55));

                logo.setEffect(logoGlow);

                BorderPane.setMargin(
                                logo,
                                new Insets(28, 45, 15, 45));

                root.setTop(logo);

                Label title = new Label("Choose Your Role");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 40px;
                                -fx-font-weight: bold;
                                """);

                DropShadow titleGlow = new DropShadow();
                titleGlow.setRadius(25);
                titleGlow.setSpread(0.10);
                titleGlow.setColor(
                                Color.rgb(132, 70, 255, 0.35));

                title.setEffect(titleGlow);

                Label subtitle = new Label(
                                "Select how you want to continue");

                subtitle.setStyle("""
                                -fx-text-fill: #BDB1D0;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 16px;
                                """);

                VBox header = new VBox(
                                8,
                                title,
                                subtitle);

                header.setAlignment(Pos.CENTER);

                Label studentIcon = new Label("👨‍🎓");

                studentIcon.setStyle("""
                                -fx-font-size: 52px;
                                """);

                Label studentTitle = new Label("STUDENT");

                studentTitle.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 24px;
                                -fx-font-weight: bold;
                                """);

                Label studentDescription = new Label(
                                "Manage your hostel account");

                studentDescription.setStyle("""
                                -fx-text-fill: #AFA3BF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                Button studentButton = new Button("CONTINUE  →");

                styleButton(studentButton);

                studentButton.setOnAction(e -> {
                        StudentLogin.show();
                });

                VBox studentCard = new VBox(
                                15,
                                studentIcon,
                                studentTitle,
                                studentDescription,
                                studentButton);

                styleCard(studentCard);

                Label wardenIcon = new Label("👨‍💼");

                wardenIcon.setStyle("""
                                -fx-font-size: 52px;
                                """);

                Label wardenTitle = new Label("WARDEN");

                wardenTitle.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 24px;
                                -fx-font-weight: bold;
                                """);

                Label wardenDescription = new Label(
                                "Manage hostel operations");

                wardenDescription.setStyle("""
                                -fx-text-fill: #AFA3BF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                Button wardenButton = new Button("CONTINUE  →");

                styleButton(wardenButton);

                wardenButton.setOnAction(e -> {
                        WardenSignup.show();
                });

                VBox wardenCard = new VBox(
                                15,
                                wardenIcon,
                                wardenTitle,
                                wardenDescription,
                                wardenButton);

                styleCard(wardenCard);

                Label parentIcon = new Label("👨‍👩‍👧");

                parentIcon.setStyle("""
                                -fx-font-size: 52px;
                                """);

                Label parentTitle = new Label("PARENT");

                parentTitle.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 24px;
                                -fx-font-weight: bold;
                                """);

                Label parentDescription = new Label(
                                "Stay connected with your child");

                parentDescription.setStyle("""
                                -fx-text-fill: #AFA3BF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                Button parentButton = new Button("CONTINUE  →");

                styleButton(parentButton);

                parentButton.setOnAction(e -> {
                        ParentLogin.show();
                });

                VBox parentCard = new VBox(
                                15,
                                parentIcon,
                                parentTitle,
                                parentDescription,
                                parentButton);

                styleCard(parentCard);

                HBox cards = new HBox(
                                30,
                                studentCard,
                                wardenCard,
                                parentCard);

                cards.setAlignment(Pos.CENTER);

                VBox content = new VBox(
                                38,
                                header,
                                cards);

                content.setAlignment(Pos.CENTER);

                center.getChildren().add(content);

                root.setCenter(center);

                Label back = new Label("← Back to Welcome");

                back.setStyle("""
                                -fx-text-fill: #9183A5;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                -fx-cursor: hand;
                                """);

                back.setOnMouseEntered(e -> {

                        back.setTextFill(
                                        Color.web("#BFA3FF"));

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(160),
                                        back);

                        scale.setToX(1.05);
                        scale.setToY(1.05);
                        scale.play();
                });

                back.setOnMouseExited(e -> {

                        back.setTextFill(
                                        Color.web("#9183A5"));

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(160),
                                        back);

                        scale.setToX(1);
                        scale.setToY(1);
                        scale.play();
                });

                back.setOnMouseClicked(e -> {

                        FadeTransition fade = new FadeTransition(
                                        Duration.millis(350),
                                        root);

                        fade.setFromValue(1);
                        fade.setToValue(0);

                        fade.setOnFinished(event -> {
                                new Welcome().start(Welcome.stage);
                        });

                        fade.play();
                });

                BorderPane.setAlignment(
                                back,
                                Pos.CENTER);

                BorderPane.setMargin(
                                back,
                                new Insets(10, 0, 20, 0));

                root.setBottom(back);

                Scene scene = new Scene(
                                root,
                                1550,
                                800);

                Welcome.stage.setScene(scene);

                prepareAnimations(
                                logo,
                                title,
                                subtitle,
                                studentCard,
                                wardenCard,
                                parentCard,
                                studentIcon,
                                wardenIcon,
                                parentIcon,
                                studentButton,
                                wardenButton,
                                parentButton,
                                back);

                Welcome.stage.show();

                startAnimations(
                                logo,
                                title,
                                subtitle,
                                studentCard,
                                wardenCard,
                                parentCard,
                                studentIcon,
                                wardenIcon,
                                parentIcon,
                                studentButton,
                                wardenButton,
                                parentButton,
                                back);
        }

        private static void prepareAnimations(
                        Label logo,
                        Label title,
                        Label subtitle,
                        VBox studentCard,
                        VBox wardenCard,
                        VBox parentCard,
                        Label studentIcon,
                        Label wardenIcon,
                        Label parentIcon,
                        Button studentButton,
                        Button wardenButton,
                        Button parentButton,
                        Label back) {

                logo.setOpacity(0);
                logo.setScaleX(0.85);
                logo.setScaleY(0.85);

                title.setOpacity(0);
                title.setScaleX(0.85);
                title.setScaleY(0.85);

                subtitle.setOpacity(0);

                studentCard.setOpacity(0);
                studentCard.setTranslateY(45);
                studentCard.setScaleX(0.88);
                studentCard.setScaleY(0.88);

                wardenCard.setOpacity(0);
                wardenCard.setTranslateY(45);
                wardenCard.setScaleX(0.88);
                wardenCard.setScaleY(0.88);

                parentCard.setOpacity(0);
                parentCard.setTranslateY(45);
                parentCard.setScaleX(0.88);
                parentCard.setScaleY(0.88);

                studentIcon.setScaleX(0.5);
                studentIcon.setScaleY(0.5);

                wardenIcon.setScaleX(0.5);
                wardenIcon.setScaleY(0.5);

                parentIcon.setScaleX(0.5);
                parentIcon.setScaleY(0.5);

                studentButton.setOpacity(0);
                wardenButton.setOpacity(0);
                parentButton.setOpacity(0);

                back.setOpacity(0);
        }

        private static void startAnimations(
                        Label logo,
                        Label title,
                        Label subtitle,
                        VBox studentCard,
                        VBox wardenCard,
                        VBox parentCard,
                        Label studentIcon,
                        Label wardenIcon,
                        Label parentIcon,
                        Button studentButton,
                        Button wardenButton,
                        Button parentButton,
                        Label back) {

                FadeTransition logoFade = fade(
                                logo,
                                0,
                                1,
                                650,
                                0);

                ScaleTransition logoScale = scale(
                                logo,
                                0.85,
                                1,
                                650,
                                0);

                new ParallelTransition(
                                logoFade,
                                logoScale).play();

                FadeTransition titleFade = fade(
                                title,
                                0,
                                1,
                                700,
                                250);

                ScaleTransition titleScale = scale(
                                title,
                                0.85,
                                1,
                                700,
                                250);

                new ParallelTransition(
                                titleFade,
                                titleScale).play();

                playFade(
                                subtitle,
                                450);

                animateCard(
                                studentCard,
                                studentIcon,
                                studentButton,
                                550);

                animateCard(
                                wardenCard,
                                wardenIcon,
                                wardenButton,
                                700);

                animateCard(
                                parentCard,
                                parentIcon,
                                parentButton,
                                850);

                playFade(
                                back,
                                1200);
        }

        private static void animateCard(
                        VBox card,
                        Label icon,
                        Button button,
                        double delay) {

                FadeTransition fade = fade(
                                card,
                                0,
                                1,
                                650,
                                delay);

                TranslateTransition translate = new TranslateTransition(
                                Duration.millis(650),
                                card);

                translate.setFromY(45);
                translate.setToY(0);
                translate.setDelay(
                                Duration.millis(delay));

                ScaleTransition scale = scale(
                                card,
                                0.88,
                                1,
                                650,
                                delay);

                new ParallelTransition(
                                fade,
                                translate,
                                scale).play();

                ScaleTransition iconScale = new ScaleTransition(
                                Duration.millis(450),
                                icon);

                iconScale.setFromX(0.5);
                iconScale.setFromY(0.5);
                iconScale.setToX(1);
                iconScale.setToY(1);
                iconScale.setDelay(
                                Duration.millis(delay + 350));

                iconScale.play();

                FadeTransition buttonFade = fade(
                                button,
                                0,
                                1,
                                450,
                                delay + 450);

                buttonFade.play();
        }

        private static void styleCard(
                        VBox card) {

                card.setAlignment(Pos.CENTER);

                card.setPrefWidth(320);
                card.setPrefHeight(330);

                card.setStyle("""
                                -fx-background-color: linear-gradient(
                                    to bottom right,
                                    #171125,
                                    #120D1E
                                );
                                -fx-background-radius: 25px;
                                -fx-border-color: #352451;
                                -fx-border-width: 1px;
                                -fx-border-radius: 25px;
                                """);

                DropShadow shadow = new DropShadow();

                shadow.setRadius(25);
                shadow.setSpread(0.18);

                shadow.setColor(
                                Color.rgb(
                                                100,
                                                50,
                                                190,
                                                0.30));

                card.setEffect(shadow);

                card.setOnMouseEntered(e -> {

                        ScaleTransition animation = new ScaleTransition(
                                        Duration.millis(180),
                                        card);

                        animation.setToX(1.05);
                        animation.setToY(1.05);
                        animation.play();

                        shadow.setRadius(38);
                        shadow.setSpread(0.30);

                        shadow.setColor(
                                        Color.rgb(
                                                        145,
                                                        85,
                                                        255,
                                                        0.55));
                });

                card.setOnMouseExited(e -> {

                        ScaleTransition animation = new ScaleTransition(
                                        Duration.millis(180),
                                        card);

                        animation.setToX(1);
                        animation.setToY(1);
                        animation.play();

                        shadow.setRadius(25);
                        shadow.setSpread(0.18);

                        shadow.setColor(
                                        Color.rgb(
                                                        100,
                                                        50,
                                                        190,
                                                        0.30));
                });
        }

        private static void styleButton(
                        Button button) {

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

                DropShadow glow = new DropShadow();

                glow.setRadius(18);
                glow.setSpread(0.15);

                glow.setColor(
                                Color.rgb(
                                                145,
                                                85,
                                                255,
                                                0.45));

                button.setEffect(glow);

                button.setOnMouseEntered(e -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(160),
                                        button);

                        scale.setToX(1.06);
                        scale.setToY(1.06);
                        scale.play();

                        glow.setRadius(28);
                        glow.setSpread(0.25);
                });

                button.setOnMouseExited(e -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(160),
                                        button);

                        scale.setToX(1);
                        scale.setToY(1);
                        scale.play();

                        glow.setRadius(18);
                        glow.setSpread(0.15);
                });
        }

        private static void createParticles(
                        StackPane pane) {

                Random random = new Random();

                for (int i = 0; i < 24; i++) {

                        Circle particle = new Circle(
                                        1.5 +
                                                        random.nextDouble() * 2.5);

                        particle.setFill(
                                        Color.rgb(
                                                        180,
                                                        130,
                                                        255,
                                                        0.20 +
                                                                        random.nextDouble() * 0.35));

                        particle.setTranslateX(
                                        -600 +
                                                        random.nextDouble() * 1200);

                        particle.setTranslateY(
                                        -350 +
                                                        random.nextDouble() * 700);

                        pane.getChildren().add(
                                        particle);

                        particles.add(particle);

                        animateParticle(
                                        particle,
                                        2.5 +
                                                        random.nextDouble() * 4);
                }
        }

        private static void animateParticle(
                        Circle particle,
                        double seconds) {

                TranslateTransition movement = new TranslateTransition(
                                Duration.seconds(seconds),
                                particle);

                movement.setByY(-25);
                movement.setAutoReverse(true);
                movement.setCycleCount(
                                TranslateTransition.INDEFINITE);

                movement.play();

                FadeTransition fade = new FadeTransition(
                                Duration.seconds(seconds),
                                particle);

                fade.setFromValue(0.20);
                fade.setToValue(0.80);
                fade.setAutoReverse(true);
                fade.setCycleCount(
                                FadeTransition.INDEFINITE);

                fade.play();
        }

        private static Circle createGlow(
                        double radius,
                        double x,
                        double y,
                        double opacity) {

                Circle glow = new Circle(radius);

                glow.setFill(
                                Color.rgb(
                                                125,
                                                55,
                                                235,
                                                opacity));

                glow.setTranslateX(x);
                glow.setTranslateY(y);

                DropShadow shadow = new DropShadow();

                shadow.setRadius(90);
                shadow.setSpread(0.05);

                shadow.setColor(
                                Color.rgb(
                                                145,
                                                75,
                                                255,
                                                opacity));

                glow.setEffect(shadow);

                ScaleTransition pulse = new ScaleTransition(
                                Duration.seconds(5),
                                glow);

                pulse.setFromX(0.90);
                pulse.setFromY(0.90);

                pulse.setToX(1.10);
                pulse.setToY(1.10);

                pulse.setAutoReverse(true);
                pulse.setCycleCount(
                                ScaleTransition.INDEFINITE);

                pulse.play();

                return glow;
        }

        private static FadeTransition fade(
                        javafx.scene.Node node,
                        double from,
                        double to,
                        double duration,
                        double delay) {

                FadeTransition transition = new FadeTransition(
                                Duration.millis(duration),
                                node);

                transition.setFromValue(from);
                transition.setToValue(to);

                transition.setDelay(
                                Duration.millis(delay));

                return transition;
        }

        private static ScaleTransition scale(
                        javafx.scene.Node node,
                        double from,
                        double to,
                        double duration,
                        double delay) {

                ScaleTransition transition = new ScaleTransition(
                                Duration.millis(duration),
                                node);

                transition.setFromX(from);
                transition.setFromY(from);

                transition.setToX(to);
                transition.setToY(to);

                transition.setDelay(
                                Duration.millis(delay));

                return transition;
        }

        private static void playFade(
                        javafx.scene.Node node,
                        double delay) {

                fade(
                                node,
                                0,
                                1,
                                600,
                                delay).play();
        }
}
