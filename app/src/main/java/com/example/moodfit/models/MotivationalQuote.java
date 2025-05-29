package com.example.moodfit.models;

public class MotivationalQuote {
    private String quoteId;
    private String text;
    private String author;
    private String category;
    private boolean isDaily;
    private long dateShown;

    // Constructors
    public MotivationalQuote() {
        this.quoteId = generateQuoteId();
    }

    public MotivationalQuote(String text, String author) {
        this();
        this.text = text;
        this.author = author;
    }

    public MotivationalQuote(String text, String author, String category) {
        this(text, author);
        this.category = category;
    }

    // Utility Methods
    private String generateQuoteId() {
        return "quote_" + System.currentTimeMillis();
    }

    public boolean wasShownToday() {
        long today = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;
        return (today - dateShown) < dayInMillis;
    }

    public void markAsShown() {
        this.dateShown = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getQuoteId() { return quoteId; }
    public void setQuoteId(String quoteId) { this.quoteId = quoteId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isDaily() { return isDaily; }
    public void setDaily(boolean daily) { isDaily = daily; }

    public long getDateShown() { return dateShown; }
    public void setDateShown(long dateShown) { this.dateShown = dateShown; }
}
