package shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shortener.services.CodeGenerator;
import shortener.models.ShortLink;
import shortener.dao.ShortLinkDAO;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class UrlShortenerController {
    private CodeGenerator codeGenerator;
    private ShortLinkDAO shortLinkDAO;
    private int shortLinkLength;

    @Autowired
    public UrlShortenerController(ShortLinkDAO shortLinkDAO) {
        codeGenerator = new CodeGenerator();
        this.shortLinkDAO = shortLinkDAO;
        shortLinkLength = 10;
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome!";
    }

    @PostMapping(value = "/new-short-link", consumes = APPLICATION_JSON_VALUE)
    public ShortLink createShortLink(@RequestBody ShortLink shortLink) {
        String hash = codeGenerator.generate(shortLinkLength);
        if (shortLink.getOriginalUrl() != null) {
            String originalUrl = shortLink.getOriginalUrl();
            shortLink = new ShortLink(null, hash, originalUrl, LocalDateTime.now(), 7, 0);
            return shortLinkDAO.save(shortLink);
        } else {
            return null;
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity redirectShortLink (@PathVariable("hash") String hash) {
        ShortLink shortLink = shortLinkDAO.findByHash(hash);
        if (shortLink != null) {
            LocalDateTime finishDate = shortLink.getStartDate().plusDays(shortLink.getLifeSpan());
            if (finishDate.isAfter(LocalDateTime.now())) {
                shortLinkDAO.increaseRedirectsNumber(shortLink);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", shortLink.getOriginalUrl());
                return new ResponseEntity<String>(headers, HttpStatus.FOUND);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/all-short-links", produces = APPLICATION_JSON_VALUE)
    public List<ShortLink> showAll () {
        return shortLinkDAO.showAllShortLinks();
    }

    @DeleteMapping("/{hash}")
    public String delete(@PathVariable("hash") String hash) {
        if (shortLinkDAO.findByHash(hash) != null) {
            shortLinkDAO.delete(hash);
            return "Short link " + hash + " deleted";
        }
        return null;
    }

    @PatchMapping(value = "/{hash}", consumes = APPLICATION_JSON_VALUE)
    public String update(@PathVariable("hash") String hash, @RequestBody ShortLink shortUrl) {
        if (shortLinkDAO.findByHash(hash) != null) {
            if (shortUrl.getLifeSpan() != null) {
                shortLinkDAO.update(hash, shortUrl);
                return "Life span of short link " + hash + " is " + shortUrl.getLifeSpan() + " days";
            }
        }
        return null;
    }

    @GetMapping(value = "/top-10", produces = APPLICATION_JSON_VALUE)
    public List<ShortLink> showTop10() {
        List<ShortLink> all = shortLinkDAO.showAllShortLinks();
        all.sort((s1, s2) -> s2.getRedirectsNumber() - s1.getRedirectsNumber());
        if (all.size() > 10) {
            return all.subList(0, 10);
        } else return all;
    }
}
