package io.github.judeosbert.stories;

/**
 * Created by judeosbert on 2/9/18.
 */

public class Story {
    public String getPrompt() {
        return prompt;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getPermalink() {
        return permalink;
    }

    String prompt;
    String author;
    String content;
    String permalink;

    public Story(String prompt,String author,String content,String permalink)
    {
        this.prompt = prompt;
        this.author = author;
        this.content = content;
        this.permalink = permalink;
    }
}
