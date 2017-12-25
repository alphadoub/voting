package ru.alphadoub.voting;

import javax.validation.groups.Default;

public class ValidationGroups {
    public interface Rest extends Default {}

    public interface Persist extends Default {}
}
