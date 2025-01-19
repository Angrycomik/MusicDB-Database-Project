package projekt.bd;

public class Review {
    private int userID;
    private String username;
    private String reviewText;

    public Review(int userID, String username, String reviewText) {
        this.userID = userID;
        this.username = username;
        this.reviewText = reviewText;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getReviewText() {
        return reviewText;
    }
}

