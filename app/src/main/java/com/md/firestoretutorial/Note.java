package com.md.firestoretutorial;

//Todo step 7: Create Note class
public class Note {
    private String title;
    private String description;

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
