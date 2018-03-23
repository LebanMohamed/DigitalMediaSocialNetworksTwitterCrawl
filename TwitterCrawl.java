import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import java.util.ArrayList;
import java.util.List;

public class TwitterCrawl {

	private static final String KEY = "Enter",
			SECRET = "Enter",
			TOKEN = "Enter",
			TOKENSECRET = "Enter";

	public static void main(String[] args) throws TwitterException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(KEY);
		cb.setOAuthConsumerSecret(SECRET);
		cb.setOAuthAccessToken(TOKEN);
		cb.setOAuthAccessTokenSecret(TOKENSECRET);

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		User user = twitter.verifyCredentials();

		getProfile(user);
		getFollowers(user, twitter);
		getFollowing(args, twitter);
		getTimeline(user, twitter);
	}

	private static void getFollowing(String[] args, Twitter twitter) throws TwitterException {
		print("------------------------------------------");
		print("Following:");

		List<Long> followingID = new ArrayList<Long>();
		IDs ids;

		if (0 < args.length)
			ids = twitter.getFriendsIDs(args[0], -1);
		else
			ids = twitter.getFriendsIDs(-1);

		for (long id : ids.getIDs()) {
			followingID.add(id);
		}

		for (int i = 0; i < followingID.size(); i++) {
			print("@" + twitter.createFriendship(followingID.get(i)).getName());
		}
	}

	private static void getFollowers(User user, Twitter twitter) throws TwitterException {
		print("------------------------------------------");
		print("Followers:");

		IDs followerID = twitter.getFollowersIDs(user.getScreenName(), -1);
		long[] followerIDs = followerID.getIDs();
		for (long s : followerIDs) {
			print("@" + twitter.showUser(s).getScreenName());
		}
	}

	private static void getProfile(User user) {
		print("Profile:");

		print("Date: " + user.getCreatedAt());
		print("UserName: " + "@" + user.getScreenName());
		print("Name: " + user.getName());
		print("ID: " + user.getId());
		print("Profile Image: " + user.getProfileImageURL());
		print("Following: " + user.getFriendsCount());
		print("Followers: " + user.getFollowersCount());
		print("Language: " + user.getLang());
		print("AccessLevel: " + user.getAccessLevel());
		print("Favourites: " + user.getFavouritesCount());
	}

	private static void getTimeline(User user, Twitter twitter) throws TwitterException {
		print("------------------------------------------");
		print("Timeline:");

		List<twitter4j.Status> statuses = twitter.getHomeTimeline();
		for (twitter4j.Status status : statuses) {
			print("@" + status.getUser().getScreenName() + " - " + status.getText());
		}
	}

	private static void print(String Message) {
		System.out.println(Message);
	}
}
