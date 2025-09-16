package tech.eebase.directories.annotations;

public @interface Directory {
    DirectoryType type();
    String key();
    String singularName();
    String pluralName();
    String createTitleName() default "Create";
    String editTitleName() default "Edit";
    String treeRootName() default "Root level";
    double weight();
    boolean disabled() default false;
    boolean doTopMenuItem() default false;
    boolean showInDirRegister() default true;
    String icon() default "";
    String groupsOrder() default "";
    String defaultSortField() default "key";
    String defaultSortOrder() default "DESC";
    String[] compositeUniqueness() default {};
    
    boolean doIndexInFullTextSearchEngine() default true;
    String customPageUrl() default "";
    
    boolean editInWindow() default false;
    int editWindowWidth() default 300;
    int editWindowHeight() default 400;
}
