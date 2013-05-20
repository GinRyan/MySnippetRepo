package com.gaode;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    /**
     * Authority
     */
    final static String AUTHORITY = "com.debby.googlemap.SuggestionProvider";
    /**
     * Mode
     */
    final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }
}