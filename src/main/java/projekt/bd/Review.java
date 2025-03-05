package projekt.bd;
/**
 * This class represents Review with main information about it.
 */
public class Review {
    private final int userID;
    private final String username;
    private final String reviewText;

    /**
     * Constructor for a review class.
     * @param userID user id
     * @param username username
     * @param reviewText review text
     */
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

