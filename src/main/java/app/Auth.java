package app;

public class Auth {
    private static final ThreadLocal<User> user = new ThreadLocal<>();

    public static void setUser(String name, String phoneNumber) {
        for (User user : Data.getUserList()) {
            if (user.getName().equals(name.trim()) && user.getPhoneNumber().equals(phoneNumber.trim())) {
                Auth.user.remove();
                Auth.user.set(user);
                return;
            }
        }
    }

    public static User getUser() {
        return user.get();
    }
}
