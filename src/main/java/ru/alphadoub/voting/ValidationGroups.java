package ru.alphadoub.voting;

import javax.validation.groups.Default;

/**
 * Created by User on 23.12.17.
 */
public class ValidationGroups {
    public interface Rest extends Default {}

    public interface Persist extends Default {}
}
