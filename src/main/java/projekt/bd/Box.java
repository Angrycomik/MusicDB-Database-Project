package projekt.bd;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class has different constructors for HBox, used for displaying elements dynamically.
 */
public class Box extends HBox {
    /**
     * This constructor is used for displaying song name, album name and an image.
     * @param image image
     * @param name name of song or album
     * @param artist name of artist
     * @param top_id id of top item
     * @param bot_id id of artist
     * @param mode 's' for song or 'a' for album
     */
    public Box(Image image, String name, String artist, Integer top_id, Integer bot_id, char mode) {
        this.setSpacing(10);
        this.setPadding(new Insets(5));

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        VBox textBox = new VBox();
        textBox.setSpacing(2);

        Label nameLabel = new Label(name);
        if (mode == 's') {
            Utilities.turnLabelClickable(nameLabel, top_id, "songpage", 'd');
            this.setPrefSize(347.5, 25);

        } else if (mode == 'a') {
            Utilities.turnLabelClickable(nameLabel, top_id, "albumpage", 'd');
            this.setPrefSize(347.5, 50);
        }

        Label subtitleLabel = new Label(artist);
        Utilities.turnLabelClickable(subtitleLabel, bot_id, "artistpage", 'd');

        textBox.getChildren().addAll(nameLabel, subtitleLabel);
        this.getChildren().addAll(imageView, textBox);
        this.getStyleClass().add("box");

    }

    /**
     * This constructor is used for creating a box with a single line.
     * @param name item name
     * @param id item id
     * @param mode 'a' for albums, 's' for songs, 'v' for artists, 'p' for playlists and 'f' for follow list.
     */
    public Box(String name, Integer id, char mode) {
        Label label = new Label(name);

        if (mode == 'v') {
            Utilities.turnLabelClickable(label, id, "artistpage", 'd');
            this.setPrefSize(347.5, 75);
        } else if (mode == 's') {
            Utilities.turnLabelClickable(label, id, "songpage", 'd');
            this.setPrefSize(347.5, 25);
        } else if (mode == 'a') {
            Utilities.turnLabelClickable(label, id, "albumpage", 'd');
            this.setMinSize(347.5, 50);
        } else if (mode == 'p') {
            this.setPrefSize(347.5, 50);
            label.getStyleClass().add("labelMainPage");
        } else if (mode == 'f') {
            Utilities.turnLabelClickable(label, id, "profile", 'p');
            this.setMinSize(347.5, 50);
        }
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(label);

        this.getStyleClass().add("box");
    }

    /**
     * This constructor is used for creating a box that is used for reviews.
     * @param username username
     * @param userID user id
     * @param reviewText text of the review
     */
    public Box(String username, Integer userID, String reviewText) {
        Label user = new Label(username + " wrote:");
        Utilities.turnLabelClickable(user, userID, "profile", 'p');

        Label reviewLabel = new Label(reviewText);
        reviewLabel.setWrapText(true);

        this.setPrefWidth(412.5);
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(5);

        this.getChildren().addAll(user, reviewLabel);

        this.getStyleClass().add("box");
    }

}
