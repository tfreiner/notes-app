package edu.umsl.notes.database;

/**
 * Created by taylorfreiner on 4/15/17.
 */

public class FinalSchema {

    public static final class UserTable{
        public static final String NAME = "user";

        public static final class Cols{
            public static final String USER = "username";
            public static final String EMAIL = "email";
            public static final String FIRST = "first";
            public static final String LAST = "last";
            public static final String PASS = "password";
            public static final String ACTIVE_USER = "activeUser";
        }
    }

    public static final class NotesTable{
        public static final String NAME = "notes";

        public static final class Cols{
            public static final String USER_ID = "userId";
            public static final String TITLE = "title";
            public static final String TEXT = "text";
            public static final String ACTIVE_NOTE = "activeNote";
        }
    }
}
