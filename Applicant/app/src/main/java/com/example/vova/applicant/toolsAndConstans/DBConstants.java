package com.example.vova.applicant.toolsAndConstans;

public class DBConstants {

    public static final String DB_NAME = "note_db";
    public static final int DB_VERSION = 1;

    public static final class CitiesTable {
        public static final String TABLE_NAME = "CitiesInfo";

        public static final class Cols {
            public static final String CITIES_INFO_FIELD_ID = "_id";
            public static final String CITIES_INFO_FIELD_YEAR_ID = "_year_id";
            public static final String CITIES_INFO_FIELD_NAME = "_name";
            public static final String CITIES_INFO_FIELD_LINK = "_link";
            public static final String CITIES_INFO_FIELD_DATE_UPDATE = "_date_update";
        }
    }

    public static final class CategoryUniversTable {
        public static final String TABLE_NAME = "CategoryUniversTable";

        public static final class Cols {
            public static final String CATEGORY_UNIVERS_INFO_FIELD_ID = "_id";
            public static final String CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID = "_city_id";
            public static final String CATEGORY_UNIVERS_INFO_FIELD_NAME = "_name";
            public static final String CATEGORY_UNIVERS_INFO_FIELD_DATE_UPDATE = "_date_update";
        }
    }

    public static final class UniversityTable {
        public static final String TABLE_NAME = "UniversityInfo";

        public static final class Cols {
            public static final String UNIVERSITY_INFO_FIELD_ID = "_id";
            public static final String UNIVERSITY_INFO_FIELD_CITIES_ID = "_city_id";
            public static final String UNIVERSITY_INFO_FIELD_CATEGORY_NAME = "_category_name";
            public static final String UNIVERSITY_INFO_FIELD_CATEGORY_LINK = "_category_link";
            public static final String UNIVERSITY_INFO_FIELD_NAME = "_name";
            public static final String UNIVERSITY_INFO_FIELD_LINK = "_link";
        }
    }

    public static final class UniversityDetailTable {
        public static final String TABLE_NAME = "UniversityDetailInfo";

        public static final class Cols {
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_ID = "_id";
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID = "_university_id";
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_NAME = "_name";
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_LINK = "_link";
            public static final String UNIVERSITY_DETAIL_INFO_FIELD_DATE_UPDATE = "_date_update";
        }
    }

    public static final class AboutUniversityTable {
        public static final String TABLE_NAME = "AboutUniversityTable";

        public static final class Cols {
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_ID = "_id";
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_DETAIL_UNV_ID = "_detail_unv_id";
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_TYPE = "_type";
            public static final String ABOUT_UNIVERSITY_INFO_FIELD_DATA = "_data";
        }
    }

    public static final class TimeFormTable {
        public static final String TABLE_NAME = "TimeFormTable";

        public static final class Cols {
            public static final String TIME_FORM_INFO_FIELD_ID = "_id";
            public static final String TIME_FORM_INFO_FIELD_DETAIL_UNV_ID = "_detail_unv_id";
            public static final String TIME_FORM_INFO_FIELD_NAME = "_name";
            public static final String TIME_FORM_INFO_FIELD_LINK = "_link";
            public static final String TIME_FORM_INFO_FIELD_DATE_UPDATE = "_date_update";
        }
    }

    public static final class SpecialitiesTable {
        public static final String TABLE_NAME = "SpecialitiesTable";

        public static final class Cols {
            public static final String SPECIALITIES_INFO_FIELD_ID = "_id";
            public static final String SPECIALITIES_INFO_FIELD_TIME_FORM_ID = "_time_form_id";
            public static final String SPECIALITIES_INFO_FIELD_DEGREE = "_degree";
            public static final String SPECIALITIES_INFO_FIELD_SPECIALITY = "_name";
            public static final String SPECIALITIES_INFO_FIELD_APPLICATION = "_application";
            public static final String SPECIALITIES_INFO_FIELD_ACCEPTED = "_accepted";
            public static final String SPECIALITIES_INFO_FIELD_RECOMMENDED = "_recommended";
            public static final String SPECIALITIES_INFO_FIELD_LICENSED_ORDERS = "_licensed_orders";
            public static final String SPECIALITIES_INFO_FIELD_VOLUME_ORDERS = "_volume_orders";
            public static final String SPECIALITIES_INFO_FIELD_EXAMS = "_exams";
            public static final String SPECIALITIES_INFO_FIELD_LINK = "_link";
            public static final String SPECIALITIES_INFO_FIELD_DATE_UPDATE = "_date_update";
        }
    }

    public static final class ApplicationTable {
        public static final String TABLE_NAME = "ApplicationTable";

        public static final class Cols {
            public static final String APPLICATION_INFO_FIELD_ID = "_id";
            public static final String APPLICATION_INFO_FIELD_SPECIALITY_ID = "_speciality_id";

            public static final String APPLICATION_INFO_FIELD_NUMBER = "_number";
            public static final String APPLICATION_INFO_FIELD_NAME = "_name";
            public static final String APPLICATION_INFO_FIELD_PRIORITY = "_priority";
            public static final String APPLICATION_INFO_FIELD_TOTAL_SCORE = "_total_score";
            public static final String APPLICATION_INFO_FIELD_MARK_DOCUMENT = "_mark_document";
            public static final String APPLICATION_INFO_FIELD_MARK_TEST = "_mark_test";
            public static final String APPLICATION_INFO_FIELD_MARK_EXAM = "_mark_exam";
            public static final String APPLICATION_INFO_FIELD_EXTRA_POINTS = "_extra_points";
            public static final String APPLICATION_INFO_FIELD_ORIGINAL_DOCUMENT = "_original_document";

            public static final String APPLICATION_INFO_FIELD_LINK = "_link";
            public static final String APPLICATION_INFO_FIELD_BACKGROUND = "_background";
            public static final String APPLICATION_INFO_FIELD_DATE_UPDATE = "_date_update";
        }
    }

    public static final class ImportantInfoTable {
        public static final String TABLE_NAME = "ImportantInfoTable";

        public static final class Cols {
            public static final String IMPORTANT_INFO_FIELD_ID = "_id";
            public static final String IMPORTANT_INFO_FIELD_SPECIALITY_ID = "_speciality_id";

            public static final String IMPORTANT_INFO_FIELD_UNIVERSITY_NAME = "_university_name";
            public static final String IMPORTANT_INFO_FIELD_SPECIALITY = "_speciality";
            public static final String IMPORTANT_INFO_FIELD_SPECIALIZATION = "_specialization";
            public static final String IMPORTANT_INFO_FIELD_FACULTY = "_faculty";
            public static final String IMPORTANT_INFO_FIELD_TIME_FORM = "_time_form";
            public static final String IMPORTANT_INFO_FIELD_LAST_TIME_UPDATE = "_last_time_update";

            public static final String IMPORTANT_INFO_FIELD_NUMBER = "_number";
            public static final String IMPORTANT_INFO_FIELD_NAME = "_name";
            public static final String IMPORTANT_INFO_FIELD_PRIORITY = "_priority";
            public static final String IMPORTANT_INFO_FIELD_TOTAL_SCORE = "_total_score";
            public static final String IMPORTANT_INFO_FIELD_MARK_DOCUMENT = "_mark_document";
            public static final String IMPORTANT_INFO_FIELD_MARK_TEST = "_mark_test";
            public static final String IMPORTANT_INFO_FIELD_MARK_EXAM = "_mark_exam";
            public static final String IMPORTANT_INFO_FIELD_EXTRA_POINTS = "_extra_points";
            public static final String IMPORTANT_INFO_FIELD_ORIGINAL_DOCUMENT = "_original_document";
        }
    }

    public static final class LegendInfoTable {
        public static final String TABLE_NAME = "LegendInfoTable";

        public static final class Cols {
            public static final String LEGEND_INFO_FIELD_ID = "_id";
            public static final String LEGEND_INFO_FIELD_YEAR_ID = "_year_id";
            public static final String LEGEND_INFO_FIELD_NAME = "_name";
            public static final String LEGEND_INFO_FIELD_DETAIL = "_detail";
            public static final String LEGEND_INFO_FIELD_BACKGROUND = "_background";
        }
    }

}
