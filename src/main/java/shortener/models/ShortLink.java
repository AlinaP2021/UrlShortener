package shortener.models;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShortLink {
    private Long id;
    private String hash;
    private String originalUrl;
    private LocalDateTime startDate;
    private Integer lifeSpan;
    private int redirectsNumber;

    public ShortLink() {

    }

    public ShortLink(Long id, String hash, String originalUrl, LocalDateTime startDate, int lifeSpan,
                     int redirectsNumber) {
        this.id = id;
        this.hash = hash;
        this.originalUrl = originalUrl;
        this.startDate = startDate;
        this.lifeSpan = lifeSpan;
        this.redirectsNumber = redirectsNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(Integer lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public int getRedirectsNumber() {
        return redirectsNumber;
    }

    public void setRedirectsNumber(Integer redirectsNumber) {
        this.redirectsNumber = redirectsNumber;
    }
}
