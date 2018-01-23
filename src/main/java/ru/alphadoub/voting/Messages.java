package ru.alphadoub.voting;

public class Messages {
    public static final String NOT_FOUND = "Not found entity with %s";
    public static final String OLD_DATE = "You can not set old date to dish. %s must not be with date earlier than %s";
    public static final String OLD_DATE_AFTER_11 = "After 11:00 you can not set to dish current date or old date. %s must be with date later than %s";
    public static final String OLD_DISH = "You can not change date of old dish. %s must be with date=%s";
    public static final String OLD_DISH_AFTER_11 = "After 11:00 you can not change date of current day's dish. %s must be with date=%s";
    public static final String WRONG_RESTAURANT_ID = "Wrong restaurant id. This dish belongs to another restaurant. Restaurant id must be=%s, but not %s";

    public static final String NOT_UNIQUE_RESTAURANT = "Restaurant with this name already exists";
    public static final String NOT_UNIQUE_DISH = "Dish with this date and name already exists in this restaurant";
    public static final String NOT_UNIQUE_EMAIL = "User with this email already exists";
    public static final String DATA_ERROR = "Insert or update data results in violation of an integrity constraint";
}
