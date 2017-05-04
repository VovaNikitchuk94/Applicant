package com.example.vova.applicant.toolsAndConstans;

public class DBConstans {

    public static final String DB_NAME = "note_db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "CitiesInfo";

    public static final String CITIES_INFO_FIELD_ID = "_id";
    public static final String CITIES_INFO_FIELD_NAME = "_name";
    public static final String CITIES_INFO_FIELD_LINK = "_link";

    public static final class UniversityTable {
        public static final String TABLE_NAME = "UniversityInfo";

        public static final class Cols {
            public static final String UNIVERSITY_INFO_FIELD_ID = "_id";
            public static final String UNIVERSITY_INFO_FIELD_NAME = "_name";
            public static final String UNIVERSITY_INFO_FIELD_LINK = "_link";
        }
    }

    public static final class UniversityDetailTable {
        public static final String TABLE_NAME = "UniversityDetailInfo";

        public static final class Cols {
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_ID = "_id";
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_NAME = "_name";
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_LINK = "_link";
        }
    }

    public static final class AboutUniversityTable {
        public static final String TABLE_NAME = "AboutUniversityTable";

        public static final class Cols {
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_ID = "_id";
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_TYPE = "_type";
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_DATA = "_data";
        }
    }
}
